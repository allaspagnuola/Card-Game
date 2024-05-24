import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

/**
 * Represents a generic player in the game.
 * This abstract class provides a foundation for different types of players,
 * both human and computer-controlled, that can participate in the game.
 * Each player has a hand of cards and follows a specific play strategy.
 */
public abstract class Player{

    /** The hand of cards held by the player. */
    protected Hand hand;

    /** The strategy the player uses to decide which card(s) to play. */
    protected IPlayStrategy strategy;

    /**
     * Constructs a new player with the given hand of cards and play strategy.
     *
     * @param hand The hand of cards for the player.
     * @param strategy The play strategy for the player.
     */
    public Player(Hand hand, IPlayStrategy strategy) {
        this.hand = hand;
        this.strategy = strategy;
    }

    /**
     * Retrieves the hand of cards held by the player.
     *
     * @return The hand of cards.
     */
    public Hand getHand() {
        return hand;
    }
}
