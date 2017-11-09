package it.ma.polimi.briscola.model.deck;

import java.util.List;

/**
 * Interface representing a deck.
 *
 * @author Francesco Pinto
 */
public interface Deck extends CardListWrapper {

    /**
     * Gets cards contained in the deck.
     *
     * @return List of objects implementing the Card interface representing the cards contained in the deck
     */
    List<? extends Card> getCards();

    /**
     * Sets cards contained in the deck (the previous deck cards are discarded, the argument cards are the only new content of the deck)
     *
     * @param cards The list containing all and only the objects implementing the Card interface that will be contained in the deck after the method is called
     */
    void setCards(List<? extends Card> cards);

    /**
     * Shuffle deck.
     *
     * @param shuffler An object implementing the Shuffler interface, so as that any shuffling algorithm can be passed
     * @return The shuffled deck
     */
    Deck shuffleDeck(Shuffler shuffler);

    /**
     * Draw card from top of the deck, as a side effect the card is removed from the deck
     *
     * @return The top most card of the deck
     */
    Card drawCardFromTop();

    /**
     * Check whether the deck is empty.
     *
     * @return True if the deck contains no card, false otherwise
     */
    boolean isEmpty();

    /**
     * Gets the current number of cards contained in the deck
     *
     * @return The current number of cards contained in the deck
     */
    int getCurrentDeckSize();

    /**
     * Put a card to the deck's bottom.
     *
     * @param card The card that should be put at the end of the deck
     */
    void putCardToBottom(Card card);

    /**
     * Draw card from the deck's bottom,  as a side effect the card is removed from the deck.
     *
     * @return The card at the deck's bottom
     */
    Card drawCardFromBottom();

    /**
     * Check whether the deck contains a certain card (only checks whether a card exists such that it has the same number, suit and class of the argument card)
     *
     * @param card The card one wants to know whether it's contained in the deck
     * @return True if a card with the same number, suit and class of argument card is found in the deck, false otherwise
     */
    boolean containsCard(Card card);

    /**
     * Checks whether the two decks contain the same cards in the same order
     *
     * @param deck The deck to be used for the comparison
     * @return True if the decks contain the same cards in the same order
     */
    boolean equalTo(Deck deck);

}
