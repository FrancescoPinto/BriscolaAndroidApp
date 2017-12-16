package it.ma.polimi.briscola.model.deck;

import java.io.Serializable;
import java.util.List;

/**
 * Interface representing a wrapper to a card list, checking the(optionally specified) maximum number of cards is never esceeded. It  mainly provides convenience methods in order to hide the internal implementation (avoids that client classes need to ask for the internal card list in order to perform on it trivial operations)
 *
 * @param <CARD> the type parameter, extends Card
 * @author Francesco Pinto
 */
public interface CardListWrapper<CARD extends Card> extends Serializable {
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
     * @throws IllegalArgumentException if null argument is passed
     * @throws IllegalStateException if maximum size (if defined) of the list is exceeded
     */
    void appendCard(CARD card);

    /**
     * Gets card.
     *
     * @param i Index of the card to be read.
     * @return The card corresponding to the index.
     * @throws IllegalArgumentException if the list is empty
     * @throws IndexOutOfBoundsException if the list size or maximum size is exceeded
     */
    CARD getCard(int i);

    /**
     * Remove card corresponding to the i index.
     *
     * @param i Index of the card to be removed
     * @return The removed card
     * @throws IllegalArgumentException if the list is empty
     * @throws IndexOutOfBoundsException if the list size or maximum size is exceeded
     */
    CARD removeCard(int i);

    /**
     * Append the cards in the list passed as an argument.
     *
     * @param cards The list of cards to be appended
     * @throws IllegalArgumentException if adding the cards to the list would make it exceed the maximum size
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
     * @throws IllegalArgumentException if setting the cards to the list would make it exceed the maximum size
     */
    void setCardList(List<CARD> cards);

    /**
     * Clear card list, cards are discarded and returned.
     *
     * @return The cards previously contained in the list
     */
    List<CARD> clearCardList();

    /**
     * Gets max num cards allowed in list.
     *
     * @return the max num cards allowed in list (if undefined, returns null)
     */
    Integer getMaxNumCardsAllowedInList();


}
