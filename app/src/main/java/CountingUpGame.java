// CountingUpGame.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the main game logic for the CountingUpGame.
 * This class handles the initialization, gameplay, and scoring of the game.
 */
@SuppressWarnings("serial")
public class CountingUpGame extends CardGame implements GGKeyListener {
    static private final int SEED = 30008;
    static private final Random RANDOM = new Random(SEED);
    private Properties properties;
    private StringBuilder logResult = new StringBuilder();
    private List<List<String>> playerAutoMovements = new ArrayList<>();
    private final String VERSION = "1.0";
    private final int NB_PLAYERS = 4;
    private final int NB_START_CARDS = 13;
    private final int HAND_WIDTH = 400;
    private final int TRICK_WIDTH = 40;
    private final Deck DECK = new Deck(Suit.values(), Rank.values(), "cover");
    private final Location[] HAND_LOCATIONS = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };
    private final Location[] SCORE_LOCATIONS = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            new Location(575, 575)
    };
    private Actor[] scoreActors = {null, null, null, null};
    private final Location TRICK_LOCATION = new Location(350, 350);
    private final Location TEXT_LOCATION = new Location(350, 450);
    private int thinkingTime = 2000;
    private int delayTime = 600;
    private Player[] players;
    private final Location HIDE_LOCATION = new Location(-500, -500);
    private Card selected;
    private int[] scores = new int[NB_PLAYERS];
    private boolean isWaitingForPass = false;
    private boolean passSelected = false;
    private int[] autoIndexHands = new int [NB_PLAYERS];
    private boolean isAuto = false;
    private Font bigFont = new Font("Arial", Font.BOLD, 36);

    /**
     * Sets the status message for the game.
     *
     * @param string The status message to display.
     */
    private void setStatus(String string) {
        setStatusText(string);
    }

    private void initScore() {
        for (int i = 0; i < NB_PLAYERS; i++) {
            scores[i] = 0;
            String text = "[" + String.valueOf(scores[i]) + "]";
            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], SCORE_LOCATIONS[i]);
        }
    }

    private void calculateScoreEndOfRound(int player, List<Card> cardsPlayed) {
        int totalScorePlayed = 0;
        for (Card card: cardsPlayed) {
            Rank rank = (Rank) card.getRank();
            totalScorePlayed += rank.getScoreCardValue();
        }
        scores[player] += totalScorePlayed;
    }

    private void calculateNegativeScoreEndOfGame(int player, List<Card> cardsInHand) {
        int totalScorePlayed = 0;
        for (Card card: cardsInHand) {
            Rank rank = (Rank) card.getRank();
            totalScorePlayed -= rank.getScoreCardValue();
        }
        scores[player] += totalScorePlayed;
    }

    private void updateScore(int player) {
        removeActor(scoreActors[player]);
        int displayScore = scores[player] >= 0 ? scores[player] : 0;
        String text = "P" + player + "[" + String.valueOf(displayScore) + "]";
        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], SCORE_LOCATIONS[player]);
    }



    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (isWaitingForPass && keyEvent.getKeyChar() == '\n') {
            passSelected = true;
        }
        return false;
    }

    @Override
    public boolean keyReleased(KeyEvent keyEvent) {
        return false;
    }

    /**
     * Method used to create players based on player types
     */
    private void createPlayers() {
        players = new Player[NB_PLAYERS];
        for (int i = 0; i < NB_PLAYERS; i++) {
            String playerKey = "players." + i;
            String playerType = properties.getProperty(playerKey);
            players[i] = PlayerFactory.getInstance().getPlayer(playerType ,new Hand(DECK));
        }
    }
    /**
     * Initializes the game by setting up the players, dealing cards, and preparing the game graphics.
     */
    private void initGame() {
        // Create players and assign storing hand responsibility to players
        createPlayers();
        dealingOut();
        for (int i = 0; i < NB_PLAYERS; i++) {
            players[i].getHand().sort(Hand.SortType.SUITPRIORITY, false);
        }
        // Set up all human players for interaction, in case more than one human player in this game
        for (int i = 0; i < NB_PLAYERS; i++) {
            if (players[i] instanceof HumanPlayer){
                CardListener cardListener = new CardAdapter()  // Human Player plays card
                {
                    public void leftDoubleClicked(Card card) {
                        selected = card;
                        // move setTouchEnabled(false) to playGame() method after player's turn is end
                        // because interaction should stop when human player's turn is over, instead of a card has been selected
                    }
                };
                players[i].getHand().addCardListener(cardListener);
            }
        }

        // graphics
        RowLayout[] layouts = new RowLayout[NB_PLAYERS];
        for (int i = 0; i < NB_PLAYERS; i++) {
            layouts[i] = new RowLayout(HAND_LOCATIONS[i], HAND_WIDTH);
            layouts[i].setRotationAngle(90 * i);
            // layouts[i].setStepDelay(10);
            players[i].getHand().setView(this, layouts[i]);
            players[i].getHand().setTargetArea(new TargetArea(TRICK_LOCATION));
            players[i].getHand().draw();
        }
    }

    // return random Card from ArrayList
    private Card randomCard(ArrayList<Card> list) {
        int x = RANDOM.nextInt(list.size());
        return list.get(x);
    }

    private Rank getRankFromString(String cardName) {
        String rankString = cardName.substring(0, cardName.length() - 1);
        Integer rankValue = Integer.parseInt(rankString);

        for (Rank rank : Rank.values()) {
            if (rank.getRankCardValue() == rankValue) {
                return rank;
            }
        }

        return Rank.ACE;
    }

    private Suit getSuitFromString(String cardName) {
        String suitString = cardName.substring(cardName.length() - 1);

        for (Suit suit : Suit.values()) {
            if (suit.getSuitShortHand().equals(suitString)) {
                return suit;
            }
        }
        return Suit.CLUBS;
    }

    private Card getCardFromList(List<Card> cards, String cardName) {
        Rank cardRank = getRankFromString(cardName);
        Suit cardSuit = getSuitFromString(cardName);
        for (Card card: cards) {
            if (card.getSuit() == cardSuit
                    && card.getRank() == cardRank) {
                return card;
            }
        }

        return null;
    }

    private void dealingOut() {
        Hand pack = DECK.toHand(false);

        for (int i = 0; i < NB_PLAYERS; i++) {
            String initialCardsKey = "players." + i + ".initialcards";
            String initialCardsValue = properties.getProperty(initialCardsKey);
            if (initialCardsValue == null) {
                continue;
            }
            String[] initialCards = initialCardsValue.split(",");
            for (String initialCard: initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }
                Card card = getCardFromList(pack.getCardList(), initialCard);
                if (card != null) {
                    card.removeFromHand(false);
                    players[i].getHand().insert(card, false);
                }
            }
        }

        for (int i = 0; i < NB_PLAYERS; i++) {
            int cardsToDealt = NB_START_CARDS - players[i].getHand().getNumberOfCards();
            for (int j = 0; j < cardsToDealt; j++) {
                if (pack.isEmpty()) return;
                Card dealt = randomCard(pack.getCardList());
                dealt.removeFromHand(false);
                players[i].getHand().insert(dealt, false);
            }
        }
    }

    /**
     * Method used to find the first player in every round
     * @return player index
     */
    private int playerIndexWithAceClub() {
        for (int i = 0; i < NB_PLAYERS; i++) {
            Hand hand = players[i].getHand();
            List<Card> cards = hand.getCardsWithRank(Rank.ACE);
            if (cards.size() == 0) {
                continue;
            }
            for (Card card: cards) {
                if (card.getSuit() == Suit.CLUBS) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void addCardPlayedToLog(int player, Card selectedCard) {
        if (selectedCard == null) {
            logResult.append("P" + player + "-SKIP,");
        } else {
            Rank cardRank = (Rank) selectedCard.getRank();
            Suit cardSuit = (Suit) selectedCard.getSuit();
            logResult.append("P" + player + "-" + cardRank.getRankCardLog() + cardSuit.getSuitShortHand() + ",");
        }
    }

    private void addRoundInfoToLog(int roundNumber) {
        logResult.append("Round" + roundNumber + ":");
    }

    private void addEndOfRoundToLog() {
        logResult.append("Score:");
        for (int i = 0; i < scores.length; i++) {
            logResult.append(scores[i] + ",");
        }
        logResult.append("\n");
    }

    private void addEndOfGameToLog(List<Integer> winners) {
        logResult.append("EndGame:");
        for (int i = 0; i < scores.length; i++) {
            logResult.append(scores[i] + ",");
        }
        logResult.append("\n");
        logResult.append("Winners:" + String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList())));
    }
    /**
     * Plays the game by handling each player's turn, updating scores, and determining the winner.
     */
    private void playGame() {
        // End trump suit
        Hand playingArea = null;
        int winner = 0;
        int roundNumber = 1;
        for (int i = 0; i < NB_PLAYERS; i++) updateScore(i);
        boolean isContinue = true;
        int skipCount = 0;
        List<Card> cardsPlayed = new ArrayList<>();
        playingArea = new Hand(DECK);
        addRoundInfoToLog(roundNumber);

        int nextPlayer = playerIndexWithAceClub();
        while(isContinue) {
            selected = null;
            boolean finishedAuto = false;
            if (isAuto) {
                int nextPlayerAutoIndex = autoIndexHands[nextPlayer]; // decide who to move next
                List<String> nextPlayerMovement = playerAutoMovements.get(nextPlayer);
                String nextMovement = "";

                if (nextPlayerMovement.size() > nextPlayerAutoIndex) {
                    nextMovement = nextPlayerMovement.get(nextPlayerAutoIndex); //get next movement string
                    nextPlayerAutoIndex++;

                    autoIndexHands[nextPlayer] = nextPlayerAutoIndex;
                    Hand nextHand = players[nextPlayer].getHand();

                    if (nextMovement.equals("SKIP")) {
                        setStatusText("Player " + nextPlayer + " skipping...");
                        delay(thinkingTime);
                        selected = null;
                    } else {
                        setStatusText("Player " + nextPlayer + " thinking...");
                        delay(thinkingTime);
                        selected = getCardFromList(nextHand.getCardList(), nextMovement);
                    }
                } else {
                    finishedAuto = true;
                }
            }

            if (!isAuto || finishedAuto){
                // find the current player
                Player curPlayer = players[nextPlayer];
                if (curPlayer instanceof HumanPlayer) {
                    // if current player is a human player
                    // get valid cards that human player can play
                    ArrayList<Card> validCards = ((HumanPlayer)curPlayer).getValidCards(curPlayer.getHand(), playingArea.getCardList());
                    curPlayer.getHand().setTouchEnabled(true);
                    isWaitingForPass = true;
                    passSelected = false;
                    setStatus("Player " + nextPlayer +" double-click on card to follow or press Enter to pass");
                    while (null == selected && !passSelected) delay(delayTime);
                    // if the card player selected is not valid, he can select again until a valid card is selected
                    while (!validCards.contains(selected) && !passSelected) {
                        setStatusText("Player " + nextPlayer + " chose invalid card. Please choose again...");
                        delay(delayTime);
                    }
                    // if the player chooses to pass this turn
                    if (passSelected) {
                        selected = null;
                        setStatusText("Player " + nextPlayer + " skipping...");
                        delay(thinkingTime);
                    }
                    // set enabled touch false when turn is over
                    curPlayer.getHand().setTouchEnabled(false);
                    isWaitingForPass = false;
                } else {
                    // if current player is a computer player
                    setStatusText("Player " + nextPlayer + " thinking...");
                    delay(thinkingTime);
                    // computer player selects card based on their type
                    selected = ((ComputerPlayer)curPlayer).playCard(curPlayer.getHand(), playingArea.getCardList());
                    if (selected == null) {
                        setStatusText("Player " + nextPlayer + " skipping...");
                        delay(thinkingTime);
                    }
                }
            }

            // Follow with selected card

            playingArea.setView(this, new RowLayout(TRICK_LOCATION, (playingArea.getNumberOfCards() + 2) * TRICK_WIDTH));
            playingArea.draw();
            addCardPlayedToLog(nextPlayer, selected);
            if (selected != null) {
                skipCount = 0;
                cardsPlayed.add(selected);
                CleverStrategy.updateDeckTracker(selected);
                selected.setVerso(false);  // In case it is upside down
                // Check: Following card must follow suit if possible

                // End Check
                selected.transfer(playingArea, true); // transfer to trick (includes graphic effect)
                delay(delayTime);
                // End Follow
            } else {
                skipCount++;
            }

            if (skipCount == NB_PLAYERS - 1) {
                playingArea.setView(this, new RowLayout(HIDE_LOCATION, 0));
                playingArea.draw();
                winner = (nextPlayer + 1) % NB_PLAYERS;
                skipCount = 0;
                calculateScoreEndOfRound(winner, cardsPlayed);
                updateScore(winner);
                addEndOfRoundToLog();
                roundNumber++;
                addRoundInfoToLog(roundNumber);
                cardsPlayed = new ArrayList<>();
                delay(delayTime);
                playingArea = new Hand(DECK);
            }

            isContinue = players[0].getHand().getNumberOfCards() > 0 && players[1].getHand().getNumberOfCards() > 0 &&
                    players[2].getHand().getNumberOfCards() > 0 && players[3].getHand().getNumberOfCards() > 0;
            if (!isContinue) {
                winner = nextPlayer;
                calculateScoreEndOfRound(winner, cardsPlayed);
                addEndOfRoundToLog();
            } else {
                nextPlayer = (nextPlayer + 1) % NB_PLAYERS;
            }
            delay(delayTime);
        }

        for (int i = 0; i < NB_PLAYERS; i++) {
            calculateNegativeScoreEndOfGame(i, players[i].getHand().getCardList());
            updateScore(i);
        }
    }

    private void setupPlayerAutoMovements() {
        String player0AutoMovement = properties.getProperty("players.0.cardsPlayed");
        String player1AutoMovement = properties.getProperty("players.1.cardsPlayed");
        String player2AutoMovement = properties.getProperty("players.2.cardsPlayed");
        String player3AutoMovement = properties.getProperty("players.3.cardsPlayed");

        String[] playerMovements = new String[] {"", "", "", ""};
        if (player0AutoMovement != null) {
            playerMovements[0] = player0AutoMovement;
        }

        if (player1AutoMovement != null) {
            playerMovements[1] = player1AutoMovement;
        }

        if (player2AutoMovement != null) {
            playerMovements[2] = player2AutoMovement;
        }

        if (player3AutoMovement != null) {
            playerMovements[3] = player3AutoMovement;
        }

        for (int i = 0; i < playerMovements.length; i++) {
            String movementString = playerMovements[i];
            List<String> movements = Arrays.asList(movementString.split(","));
            playerAutoMovements.add(movements);
        }
    }
    /**
     * Runs the game application, displaying the game interface and handling user interactions.
     *
     * @return A string representation of the game log.
     */
    public String runApp() {
        setTitle("CountingUpGame (V" + VERSION + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        initScore();
        addKeyListener(this);
        setupPlayerAutoMovements();
        initGame();
        playGame();

        for (int i = 0; i < NB_PLAYERS; i++) updateScore(i);
        int maxScore = 0;
        for (int i = 0; i < NB_PLAYERS; i++) if (scores[i] > maxScore) maxScore = scores[i];
        List<Integer> winners = new ArrayList<Integer>();
        for (int i = 0; i < NB_PLAYERS; i++) if (scores[i] == maxScore) winners.add(i);
        String winText;
        if (winners.size() == 1) {
            winText = "Game over. Winner is player: " +
                    winners.iterator().next();
        } else {
            winText = "Game Over. Drawn winners are players: " +
                    String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList()));
        }
        addActor(new Actor("sprites/gameover.gif"), TEXT_LOCATION);
        setStatusText(winText);
        refresh();
        addEndOfGameToLog(winners);

        return logResult.toString();
    }
    /**
     * Constructs a new CountingUpGame with the specified game properties.
     *
     * @param properties The properties to configure the game.
     */
    public CountingUpGame(Properties properties) {
        super(700, 700, 30);
        this.properties = properties;
        isAuto = Boolean.parseBoolean(properties.getProperty("isAuto"));
        if (isAuto) {
            thinkingTime = 50;
            delayTime = 10;
        }
    }

}
