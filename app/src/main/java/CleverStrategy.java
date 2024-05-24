import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Represents a clever strategy for playing cards.
 * This strategy takes into account the remaining points in the deck and
 * adjusts the card selection based on the points in the current round.
 */
public class CleverStrategy extends CompositePlayStrategy {

    // Define numbers as words
    private static final int ZERO_INITIAL = 0;
    private static final int VALUE = 4;
    private static final int ONE = 1;
    private static final int DIVIDE_HALF = 2;


    // Tracker for the remaining cards in the deck
    private static HashMap<Rank, Integer> deckTracker = null;

    /**
     * Initializes the deck tracker with all ranks.
     */
    public CleverStrategy() {
        deckTracker = new HashMap<>();
        for (Rank rank : Rank.values()) {
            deckTracker.put(rank, VALUE);

        }
    }

    /**
     * Updates the deck tracker when a card is played.
     *
     * @param card The card that was played.
     */
    public static void updateDeckTracker(Card card) {
        if (deckTracker != null) {
            Rank rank = Rank.valueOf(card.getRank().name());
            deckTracker.put(rank, deckTracker.get(rank) - ONE);
        }
    }

    /**
     * Calculates the total remaining points in the deck.
     *
     * @return The total remaining points.
     */
    private int calculateTotalRemainingPoints() {
        int totalPoints = ZERO_INITIAL;
        for (HashMap.Entry<Rank, Integer> entry : deckTracker.entrySet()) {
            totalPoints += entry.getKey().getRankCardValue() * entry.getValue();
        }
        return totalPoints;
    }
    /**
     * Determines the best card(s) to play based on the current state of the game and the strategy.
     *
     * <p>The method first retrieves a list of valid cards that can be played using the game's strategy.
     * Depending on the total remaining points and the points in the current round, the method selects
     * the best card to play. The selection is based on various thresholds defined in the game.</p>
     *
     * <p>If the total remaining points are below a certain high threshold, the method selects a card based
     * on the points in the current round. If the round points are below a second low threshold, the lowest
     * card is selected. If the round points are within a range, the middle card is selected. Otherwise, the
     * highest card is selected.</p>
     *
     * <p>If the total remaining points are above the high threshold, the method checks if any cards have been
     * played in the current round. If no cards have been played, the lowest card is selected. If cards have
     * been played, the method selects a card based on the points in the current round, similar to the logic
     * described above.</p>
     *
     * @param hand The current hand of the player.
     * @param playedCards The cards that have been played in the current round.
     * @return A list containing the selected card(s) to play. If no valid card can be played, the list is empty.
     */
    @Override
    public ArrayList<Card> getCardsToPlay(Hand hand, ArrayList<Card> playedCards) {
        ArrayList<Card> validCardsToPlay = strategies.get(0).getCardsToPlay(hand, playedCards);

        if (validCardsToPlay.isEmpty()) {
            return validCardsToPlay;
        }

        Card selectedCard;
        int totalRemainingPoints = calculateTotalRemainingPoints();


        int pointsInRound = calculatePointsInRound(playedCards);

        if (pointsInRound < totalRemainingPoints * 0.1) {
            selectedCard = selectLowestCard(validCardsToPlay);
        } else if (pointsInRound <= totalRemainingPoints * 0.2) {
            selectedCard = selectMiddleCard(validCardsToPlay);
        } else {
            selectedCard = selectHighestCard(validCardsToPlay, hand);
        }


        ArrayList<Card> selectedCards = new ArrayList<>();
        if (selectedCard != null) selectedCards.add(selectedCard);
        return selectedCards;
    }
    /**
     * Calculates the total points of the cards played in the current round.
     *
     * @param playedCards The cards played in the current round.
     * @return The total points of the played cards.
     */
    private int calculatePointsInRound(ArrayList<Card> playedCards) {
        int points = ZERO_INITIAL;
        for (Card card : playedCards) {
            points += Rank.valueOf(card.getRank().name()).getRankCardValue();
        }
        return points;
    }
    /**
     * Selects the card with the lowest rank from the given list of cards.
     *
     * @param validCardsToPlay The list of cards to choose from.
     * @return The card with the lowest rank.
     */
    private Card selectLowestCard(ArrayList<Card> validCardsToPlay) {
        Card lowestCard = validCardsToPlay.get(ZERO_INITIAL);
        for (Card card : validCardsToPlay) {
            if (Rank.valueOf(card.getRank().name()).getRankCardValue() < Rank.valueOf(lowestCard.getRank().name()).getRankCardValue()) {
                lowestCard = card;
            }
        }
        return lowestCard;
    }
    /**
     * Selects the card with the highest rank from the given list of cards.
     * If the highest card's rank matches the highest rank in the deck and the hand has no more than half of that rank,
     * the second highest card is selected.
     *
     * @param validCardsToPlay The list of cards to choose from.
     * @param hand The player's current hand.
     * @return The card with the highest rank or the second highest rank based on the conditions.
     */
    private Card selectHighestCard(ArrayList<Card> validCardsToPlay, Hand hand) {
        Card highestCard = validCardsToPlay.get(ZERO_INITIAL);
        Rank highestRank = findHighestRank();
        for (Card card : validCardsToPlay) {
            if (Rank.valueOf(card.getRank().name()).getRankCardValue() > Rank.valueOf(highestCard.getRank().name()).getRankCardValue()) {
                highestCard = card;
            }
        }
        if(Rank.valueOf(highestCard.getRank().name()).equals(highestRank) && hand.getCardsWithRank(highestRank).size()*2 <=
                deckTracker.get(highestRank)){
            Card secondHighestCard = null;
            for (Card card : validCardsToPlay) {
                if (!Rank.valueOf(card.getRank().name()).equals(highestRank) && (secondHighestCard == null ||
                        Rank.valueOf(card.getRank().name()).getRankCardValue() > Rank.valueOf(secondHighestCard.getRank().name()).getRankCardValue())) {
                    secondHighestCard = card;
                }
            }
            return secondHighestCard;
        }
        return highestCard;
    }
    /**
     * Selects the card with the middle rank from the given list of cards.
     * If no exact middle is found, the nearest middle card is selected.
     * If the card selected in this level is the highest rank, then select to skip to save the card for more score
     *
     * @param validCardsToPlay The list of cards to choose from.
     * @return The card with the middle rank or the nearest middle rank.
     */
    private Card selectMiddleCard(ArrayList<Card> validCardsToPlay) {
        int minRankValue = Integer.MAX_VALUE;
        int maxRankValue = Integer.MIN_VALUE;

        int highestRankValue = findHighestRank().getRankCardValue();

        // Find the minimum and maximum rank values
        for (Card card : validCardsToPlay) {
            int rankValue = Rank.valueOf(card.getRank().name()).getRankCardValue();
            if (rankValue < minRankValue) minRankValue = rankValue;
            if (rankValue > maxRankValue) maxRankValue = rankValue;
        }


        // Calculate the middle rank value
        int middleRankValue = (minRankValue + maxRankValue) / DIVIDE_HALF;

        // Find the card with the middle rank value
        for (Card card : validCardsToPlay) {
            if (Rank.valueOf(card.getRank().name()).getRankCardValue() == middleRankValue) {
                return card;
            }
        }

        // If no exact middle is found (due to even number of cards), return the nearest middle card
        Card nearestMiddleCard = validCardsToPlay.get(ZERO_INITIAL);
        int nearestDifference = Math.abs(Rank.valueOf(nearestMiddleCard.getRank().name()).getRankCardValue() - middleRankValue);
        for (Card card : validCardsToPlay) {
            int difference = Math.abs(Rank.valueOf(card.getRank().name()).getRankCardValue() - middleRankValue);
            if (difference < nearestDifference) {
                nearestDifference = difference;
                nearestMiddleCard = card;
            }
        }

        // If the card selected in this level is the highest rank, then select to skip to save the card for more score
        if(Rank.valueOf(nearestMiddleCard.getRank().name()).getRankCardValue() == highestRankValue) return null;

        return nearestMiddleCard;
    }
    /**
     * Finds the highest rank that is still available in the deck.
     *
     * @return The highest available rank.
     */
    private Rank findHighestRank(){
        int i = 1;
        Rank[] ranks = Rank.values();
        while(i < ranks.length){
            if(deckTracker.get(ranks[i]) != ZERO_INITIAL){
                return ranks[i];
            }
            i++;
        }
        return Rank.ACE;
    }
}
