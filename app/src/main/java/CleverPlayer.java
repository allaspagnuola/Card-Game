import ch.aplu.jcardgame.Hand;

/**
 * Represents a player that employs a clever strategy for playing cards.
 * The clever strategy is determined by the PlayStrategyFactory.
 */
public class CleverPlayer extends ComputerPlayer {

    /**
     * Constructs a CleverPlayer with the given hand of cards.
     * The player uses a clever strategy for playing cards.
     *
     * @param hand The hand of cards assigned to the player.
     */
    public CleverPlayer(Hand hand) {
        super(hand, PlayStrategyFactory.getInstance().getStrategy("Clever"));
    }
}
