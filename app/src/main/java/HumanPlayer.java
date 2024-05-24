import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

/**
 * Represents a human player in the game.
 * This player uses the fundamental strategy for playing cards.
 */
public class HumanPlayer extends Player {

    /**
     * Constructs a HumanPlayer with the given hand.
     * The player is initialized with the fundamental strategy.
     *
     * @param hand The hand of cards assigned to the player.
     */
    public HumanPlayer(Hand hand) {
        super(hand, PlayStrategyFactory.getInstance().getStrategy("Fundamental"));
    }

    /**
     * Find a list of valid cards that human player can play.
     *
     * @param hand The current hand of the player.
     * @param playedCard The cards that have been played in the current round.
     * @return A list of card(s) that human player can play.
     */
    public ArrayList<Card> getValidCards(Hand hand, ArrayList<Card> playedCard){
        return strategy.getCardsToPlay(hand, playedCard);
    }
}
