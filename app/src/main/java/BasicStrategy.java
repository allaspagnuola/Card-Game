import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a basic playing strategy where the player selects the lowest rank card(s) from their hand.
 * If there are multiple cards with the same lowest rank, one is chosen randomly.
 */
public class BasicStrategy extends CompositePlayStrategy {

    // Random generator for selecting among multiple lowest rank cards.
    private final Random RANDOM = new Random();

    /**
     * Finds all the cards with the lowest rank from the given list of cards.
     *
     * @param cards List of cards to search.
     * @return A list of cards with the lowest rank.
     */
    private ArrayList<Card> findLowestCardsList(ArrayList<Card> cards) {
        ArrayList<Card> lowest = new ArrayList<>();
        Rank lowestRank = null;
        for (Card c : cards) {
            if (lowestRank == null || Rank.valueOf(c.getRank().name()).getRankCardValue() < lowestRank.getRankCardValue()) {
                lowestRank = Rank.valueOf(c.getRank().name());
            }
        }
        for (Card c : cards) {
            if (Rank.valueOf(c.getRank().name()).equals(lowestRank)) {
                lowest.add(c);
            }
        }
        return lowest;
    }

    /**
     * Determines the card to play based on the basic strategy.
     * If there are valid cards to play, selects the lowest rank card(s).
     * If there are multiple cards with the same lowest rank, one is chosen randomly.
     *
     * @param hand The player's current hand.
     * @param playedCards The cards that have already been played in the current round.
     * @return A list containing the selected card to play.
     */
    @Override
    public ArrayList<Card> getCardsToPlay(Hand hand, ArrayList<Card> playedCards) {
        ArrayList<Card> validCardsToPlay = strategies.get(0).getCardsToPlay(hand, playedCards);

        // If there are valid cards, select the lowest rank or randomly select among lowest ranks
        if (!validCardsToPlay.isEmpty()) {
            List<Card> lowestRankCards = findLowestCardsList(validCardsToPlay);
            Card cardToPlay = lowestRankCards.get(RANDOM.nextInt(lowestRankCards.size()));
            ArrayList<Card> selectedCard = new ArrayList<>();
            selectedCard.add(cardToPlay);
            return selectedCard;
        }

        // If no valid cards, return an empty list (player must pass)
        return new ArrayList<>();
    }
}
