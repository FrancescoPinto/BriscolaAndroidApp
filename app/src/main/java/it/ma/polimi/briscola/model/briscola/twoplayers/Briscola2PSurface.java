package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.deck.AbstractCardListWrapper;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Class representing the surface in a Briscola 2 players match. Extends AbstractCardListWrapper<NeapolitanCard>, that provides a default implementation of all the basic methods that can be performed on the surface.
 * Imposes that no more than 2 cards can be put on surface at the same time.
 *
 * @author Francesco Pinto
 */
public class Briscola2PSurface extends AbstractCardListWrapper<NeapolitanCard> {

    /**
     * The constant FIRSTCARD.
     */
    public static final int FIRSTCARD = 0;
    /**
     * The constant SECONDCARD.
     */
    public static final int SECONDCARD = 1;

    private static int maxNumCardsAllowedOnSurface = 2;

    /**
     * Instantiates a new Briscola 2 p surface.
     *
     * @param list the String representing the surface (format as specified in the slides)
     */
    public Briscola2PSurface(String list){
        super(list);
    }

    /**
     * Instantiates a new Briscola 2 p surface from a list of NeapolitanCards
     *
     * @param cards the list of NeapolitanCards
     */
    public Briscola2PSurface(List<NeapolitanCard> cards){
        super(cards);
    }

    @Override
    public Integer getMaxNumCardsAllowedInList(){
        return maxNumCardsAllowedOnSurface;
    }

    @Override
    public NeapolitanCard buildCardInstance(String num, String suit){
        return new NeapolitanCard(num,suit);
    }


}
