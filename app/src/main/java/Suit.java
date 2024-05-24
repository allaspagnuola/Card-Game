/**
 * Represents the suit of a playing card.
 * Each suit has an associated shorthand notation.
 */
public enum Suit {
    SPADES ("S"),
    HEARTS ("H"),
    DIAMONDS ("D"),
    CLUBS ("C");

    /** The shorthand notation for the suit. */
    private String suitShortHand = "";

    /**
     * Constructs a Suit with the specified shorthand notation.
     *
     * @param shortHand The shorthand notation for the suit.
     */
    Suit(String shortHand) {
        this.suitShortHand = shortHand;
    }

    /**
     * Retrieves the shorthand notation associated with the suit.
     *
     * @return The shorthand notation of the suit.
     */
    public String getSuitShortHand() {
        return suitShortHand;
    }
}
