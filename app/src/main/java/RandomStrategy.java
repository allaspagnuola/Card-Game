import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a random strategy for playing cards.
 * This strategy randomly selects a card from the valid cards to play.
 */
public class RandomStrategy extends CompositePlayStrategy {

    /** Random number generator for selecting cards. */
    private final Random RANDOM = new Random();

    /**
     * Plays a card from the hand based on the random strategy.
     * If there are valid cards to play, one is selected at random.
     * If there are no valid cards, an empty list is returned indicating the player must pass.
     *
     * @param hand The current hand of the player.
     * @param playedCards The list of cards that have been played in the current round.
     * @return A list containing the selected card to play or an empty list if no valid cards.
     */
    @Override
    public ArrayList<Card> getCardsToPlay(Hand hand, ArrayList<Card> playedCards) {
        ArrayList<Card> validCardsToPlay = strategies.get(0).getCardsToPlay(hand, playedCards);
        ArrayList<Card> selectedCard = new ArrayList<>();
        // If there are valid cards, select one at random
        if (!validCardsToPlay.isEmpty()) {
            Card cardToPlay = validCardsToPlay.get(RANDOM.nextInt(validCardsToPlay.size()));
            selectedCard.add(cardToPlay);
        }

        // If no valid cards, return an empty list (player must pass)
        return selectedCard;
    }
}
