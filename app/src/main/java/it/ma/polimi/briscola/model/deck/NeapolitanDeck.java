package it.ma.polimi.briscola.model.deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a NeapolitanDeck, extends AbstractDeck<NeapolitanCard> and thus inherits the implementation of common deck operations
 *
 * @author Francesco Pinto
 *
 */
public class NeapolitanDeck extends AbstractDeck<NeapolitanCard>{

    private static final int maxNumAllowedCards = 40;

    /**
     * Instantiates a new 40-cards Neapolitan deck (shuffling is not included, should be performed explicitly invoking the superclass shuffleDeck method)
     */
    public NeapolitanDeck(){
        NeapolitanCardNumbers[] numberValues = NeapolitanCardNumbers.values();
        NeapolitanCardSuit[] suitValues = NeapolitanCardSuit.values();

        for(NeapolitanCardNumbers cn : numberValues){ //for each possible card number
            for(NeapolitanCardSuit cs : suitValues){ //and card suit
                super.getCardList().add(new NeapolitanCard(cn,cs)); //generate a card, and add it to the deck
            }
        }
    }


    /**
     * Instantiates a Neapolitan deck containing the cards contained in the argument cards list
     *
     * @param cards The cards contained in the deck
     */
    public NeapolitanDeck(List<NeapolitanCard> cards) {
        super(cards);
    }


    /**
     * Instantiates a  Neapolitan deck containing the cards represented in the argument deck string representation
     *
     * @param deck The string containing the string representation of the cards to be inserted in the deck. The left-most cards are the top-most.
     *             @see NeapolitanCard
     */
//riceve in ingresso solo la parte della stringa riguardante il deck
    public NeapolitanDeck(String deck){
       super(deck);
    }



    /**
     * Overrides the method of AbstractDeck, so that superclass methods can instantiate a NeapolitanCard having a specified card number and suit
     *
     * @param num The String representing the card number, it is among the cardNumber values of NeapolitanCardNumbers enums
     * @param suit The String representing the card Suit, it is among the cardSuit values of NeapolitanCardSuit enums
     * @return The instance of the NeapolitanCard
     */
    @Override
    public NeapolitanCard buildCardInstance(String num, String suit){
        return new NeapolitanCard(num,suit);
    }

    @Override
    public Integer getMaxNumCardsAllowedInList() {
        return maxNumAllowedCards;
    }
}
