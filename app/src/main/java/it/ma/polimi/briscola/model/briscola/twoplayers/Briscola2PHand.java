package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.deck.AbstractCardListWrapper;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Class representing the hand of a Briscola player in a 2 players match. Extends AbstractCardListWrapper<NeapolitanCard>, that provides a default implementation of all the basic methods that can be performed on the hand.
 * Imposes no more than three cards can be contained in hand.
 *
 * @author Francesco Pinto
 */
public class Briscola2PHand extends AbstractCardListWrapper<NeapolitanCard> {

    /**
     * The constants representing the indices of CARD1, CARD2 and CARD3.
     */
    public static final int CARD1 = 0,CARD2 = 1,CARD3 = 2;
    //Todo, se devi implementare regole diverse dalle solite per la seconda parte del corso, crea un'interfaccia BriscolaHand


    private static final int maxNumAllowedCards = 3;

    /**
     * Instantiates a new Briscola 2 p hand from the string
     *
     * @param list String representing the hand (format as specified in the slides)
     */
    public Briscola2PHand(String list){
        super(list);
    }

    /**
     * Instantiates a new Briscola 2 p hand from a list of NeapolitanCards
     *
     * @param cards the list of NeapolitanCards
     */
    public Briscola2PHand(List<NeapolitanCard> cards){
        super(cards);
    }


    @Override
    public NeapolitanCard buildCardInstance(String num, String suit) {
        return new NeapolitanCard(num,suit);
    }

    @Override
    public Integer getMaxNumCardsAllowedInList() {
        return maxNumAllowedCards;
    }

}
