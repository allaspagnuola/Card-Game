import ch.aplu.jcardgame.*;
import java.util.ArrayList;

/**
 * Represents a strategy for playing cards in the game.
 * Implementing classes should provide a specific strategy for determining
 * which card(s) to play based on the current state of the game.
 */
public interface IPlayStrategy {

    /**
     * Determines which card(s) to play based on the current state of the game.
     *
     * @param hand The current hand of the player.
     * @param playedCard The cards that have been played in the current round.
     * @return A list of valid card(s) that the player can play.
     */
    ArrayList<Card> getCardsToPlay(Hand hand, ArrayList<Card> playedCard);
}
