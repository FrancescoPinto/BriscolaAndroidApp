package it.ma.polimi.briscola.model.deck;

import java.util.List;

/**
 * Interface representing a deck. Extends CardListWrapper interface.
 *
 * @author Francesco Pinto
 */
public interface Deck<CARD extends Card> extends CardListWrapper<CARD> {

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
    CARD drawCardFromTop();

    /**
     * Put a card to the deck's bottom.
     *
     * @param card The card that should be put at the end of the deck
     */
    void putCardToBottom(CARD card);

    /**
     * Draw card from the deck's bottom, as a side effect the card is removed from the deck.
     *
     * @return The card at the deck's bottom
     */
    CARD drawCardFromBottom();

}
