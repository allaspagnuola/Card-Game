import ch.aplu.jcardgame.Hand;

/**
 * Represents a player that uses a random strategy to play cards.
 * This player type makes decisions based on a random strategy provided by the {@link PlayStrategyFactory}.
 */
public class RandomPlayer extends ComputerPlayer {

    /**
     * Constructs a new RandomPlayer with the specified hand.
     * Initializes the player with a random strategy obtained from the {@link PlayStrategyFactory}.
     *
     * @param hand The hand of cards assigned to the player.
     */
    public RandomPlayer(Hand hand) {
        super(hand, PlayStrategyFactory.getInstance().getStrategy("Random"));
    }
}
