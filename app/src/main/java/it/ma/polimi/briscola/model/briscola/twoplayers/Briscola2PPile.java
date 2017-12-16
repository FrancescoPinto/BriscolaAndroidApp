package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.deck.AbstractCardListWrapper;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;

/**
 * Class representing the pile in a Briscola 2 Players match. Extends AbstractCardListWrapper<NeapolitanCard>, that provides a default implementation of all the basic methods that can be performed on the pile.
 *
 * @author Francesco Pinto
 */
public class Briscola2PPile extends AbstractCardListWrapper<NeapolitanCard> implements Serializable {


    /**
     * Instantiates a new Briscola 2 p pile.
     *
     * @param list the String representing the surface (format as specified in the slides)
     */
    public Briscola2PPile(String list){
        super(list);
    }

    /**
     * Instantiates a new Briscola 2 p pile from a list of NeapolitanCards
     *
     * @param cards the list of NeapolitanCards
     */
    public Briscola2PPile(List<NeapolitanCard> cards){
        super(cards);
    }

    @Override
    public NeapolitanCard buildCardInstance(String num, String suit) {
        return new NeapolitanCard(num,suit);
    }
}
