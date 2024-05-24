import ch.aplu.jcardgame.Hand;

/**
 * Represents a basic computer player that follows a basic playing strategy.
 * This player uses the "Basic" strategy defined in the PlayStrategyFactory.
 */
public class BasicPlayer extends ComputerPlayer {

    /**
     * Constructs a new BasicPlayer with the given hand of cards.
     *
     * @param hand The hand of cards assigned to this player.
     */
    public BasicPlayer(Hand hand) {
        // Initialize the player with the "Basic" strategy from the PlayStrategyFactory.
        super(hand, PlayStrategyFactory.getInstance().getStrategy("Basic"));
    }
}
