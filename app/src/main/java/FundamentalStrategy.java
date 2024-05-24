import ch.aplu.jcardgame.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the fundamental strategy for playing cards.
 * This strategy determines the valid cards a player can play based on the game's rules.
 */
public class FundamentalStrategy implements IPlayStrategy {
    /**
     * Checks if a selected card is valid to play based on the leading card.
     *
     * @param selected     The card the player wants to play.
     * @param leadingCard  The leading card of the current round.
     * @return true if the selected card is valid to play, false otherwise.
     */
    private boolean isValidCard(Card selected, Card leadingCard) {
//        if (leadingCard == null) {
//            return true;
//        }
        int selected_rank = Rank.valueOf(selected.getRank().name()).getRankCardValue();
        int leading_rank = Rank.valueOf(leadingCard.getRank().name()).getRankCardValue();

        if (selected.getSuitId() == leadingCard.getSuitId() && selected_rank > leading_rank ||
                selected_rank == leading_rank && selected.getSuitId() != leadingCard.getSuitId()) {
            return true;
        }

        return false;
    }

    /**
     * Determines the valid cards a player can play based on the game's rules.
     *
     * @param hand         The player's current hand.
     * @param playedCards  The cards that have been played in the current round.
     * @return A list of valid cards the player can play.
     */
    @Override
    public ArrayList<Card> getCardsToPlay(Hand hand, ArrayList<Card> playedCards) {
        ArrayList<Card> validCardsToPlay = new ArrayList<>();
        // if there are no cards played in this round
        if (playedCards.isEmpty()) {
            // if this is the first round, player only plays the ace of clubs
            for (Card card : hand.getCardsWithRank(Rank.ACE)) {
                if (card.getSuit() == Suit.CLUBS) {
                    validCardsToPlay.add(card);
                    return validCardsToPlay;
                }
            }
            //otherwise, the player can play all cards
            validCardsToPlay = hand.getCardList();
        } else {
            // find all valid cards the player can play
            for (Card card : hand.getCardList()) {
                if (isValidCard(card, playedCards.get(playedCards.size() - 1))) {
                    validCardsToPlay.add(card);
                }
            }
        }
        return validCardsToPlay;
    }
}
