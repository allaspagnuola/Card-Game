import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import java.util.ArrayList;

/**
 * Represents a computer-controlled player in the game.
 * This abstract class extends the Player class and provides
 * a foundation for different types of computer player strategies.
 */
public abstract class ComputerPlayer extends Player {

    /**
     * Constructs a new ComputerPlayer with the specified hand and play strategy.
     *
     * @param hand     The hand of cards assigned to the computer player.
     * @param strategy The play strategy the computer player will use.
     */
    public ComputerPlayer(Hand hand, IPlayStrategy strategy) {
        super(hand, strategy);
    }

    /**
     * Determines which card(s) the player should play based on their strategy.
     *
     * @param hand The current hand of the player.
     * @param playedCards The cards that have been played in the current round.
     * @return A valid card if the player decides to play based on trategy, null if he doesn't play
     */
    public Card playCard(Hand hand, ArrayList<Card> playedCards){
        ArrayList<Card> cardsToPlay = strategy.getCardsToPlay(hand, playedCards);
        return cardsToPlay.isEmpty() ? null : cardsToPlay.get(0);
    }
}
