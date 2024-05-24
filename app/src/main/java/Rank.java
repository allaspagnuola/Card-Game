/**
 * Represents the rank of a playing card.
 * Each rank has an associated card value and a score value.
 */
public enum Rank {
    // Reverse order of rank importance (see rankGreater() below)
    ACE (1, 10), KING (13, 10), QUEEN (12, 10),
    JACK (11, 10), TEN (10, 10), NINE (9, 9),
    EIGHT (8, 8), SEVEN (7, 7), SIX (6, 6),
    FIVE (5, 5), FOUR (4, 4), THREE (3, 3),
    TWO (2, 2);

    /** The card value associated with the rank. */
    private int rankCardValue = 1;

    /** The score value associated with the rank. */
    private int scoreCardValue = 1;

    /**
     * Constructs a Rank with the specified card and score values.
     *
     * @param rankCardValue The card value associated with the rank.
     * @param scoreCardValue The score value associated with the rank.
     */
    Rank(int rankCardValue, int scoreCardValue) {
        this.rankCardValue = rankCardValue;
        this.scoreCardValue = scoreCardValue;
    }

    /**
     * Retrieves the card value associated with the rank.
     *
     * @return The card value of the rank.
     */
    public int getRankCardValue() {
        return rankCardValue;
    }

    /**
     * Retrieves the score value associated with the rank.
     *
     * @return The score value of the rank.
     */
    public int getScoreCardValue() {
        return scoreCardValue;
    }

    /**
     * Retrieves the card value of the rank as a string for logging purposes.
     *
     * @return The card value of the rank as a string.
     */
    public String getRankCardLog() {
        return String.format("%d", rankCardValue);
    }
}
