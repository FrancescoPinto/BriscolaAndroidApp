package it.ma.polimi.briscola.model.deck;

import java.io.Serializable;

/**
 * Interface representing a card.
 * @author Francesco Pinto
 */
public interface Card  extends Serializable {

    /**
     * Gets card suit.
     *
     * @return String representing the card suit
     */
    public String getCardSuit();

    /**
     * Gets card number.
     *
     * @return String representing the card number
     */
    public String getCardNumber();

    /**
     * Method that determines whether the argument c is equal to card on which this method is invoked
     * @param c The card to be used for the comparison
     * @return True if the cards are of the same class, number and suit, false otherwise
     */
    public boolean equalTo(Card c);
}
