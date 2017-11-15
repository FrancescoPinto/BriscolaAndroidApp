package it.ma.polimi.briscola.model.deck;

import java.util.List;

/**
 * Interface representing a wrapper to a card list. It  mainly provides convenience methods in order to hide the internal implementation (avoids that client classes need to ask for the internal card list in order to perform on it trivial operations)
 *
 * @param <CARD> the type parameter, extends Card
 * @author Francesco Pinto
 */
public interface CardListWrapper<CARD extends Card> {
    /**
     * Establishes whether the two cards lists contain the same cards in the same order.
     *
     * @param clw The card list wrapper (clw) to be used for the comparison
     * @return True if both contain the same cards in the same order, false otherwise.
     */
    boolean equalTo(CardListWrapper<CARD> clw);

    /**
     * Returns the current number of cards contained in the list (i.e. the list's size).
     *
     * @return The current number of cards in the list.
     */
    int size();

    /**
     * Establishes whether the list is empty.
     *
     * @return True if the list is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Check whether the list contains the card.
     *
     * @param card The card to be searched in the list
     * @return True if the list contains a card with the same class, number and suit of the parameter card.
     */
    boolean containsCard(CARD card);

    /**
     * Append card at the end of the list.
     *
     * @param card The card to be appended.
     */
    void appendCard(CARD card);

    /**
     * Gets card.
     *
     * @param i Index of the card to be appended.
     * @return The card corresponding to the index.
     */
    CARD getCard(int i);

    /**
     * Remove card corresponding to the i index.
     *
     * @param i Index of the card to be removed
     * @return The removed card
     */
    CARD removeCard(int i);

    /**
     * Append the cards in the list passed as an argument.
     *
     * @param cards The list of cards to be appended
     */
    void appendAll(List<CARD> cards);

    /**
     * Gets card list.
     *
     * @return The card list
     */
    List<CARD> getCardList();

    /**
     * Sets card list.
     *
     * @param cards The list of cards that will be contained by the wrapper.
     */
    void setCardList(List<CARD> cards);

    /**
     * Clear card list, cards are discarded and returned.
     *
     * @return The cards previously contained in the list
     */
    List<CARD> clearCardList();
}
