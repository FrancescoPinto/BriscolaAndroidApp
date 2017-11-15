package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.deck.AbstractCardListWrapper;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;

/**
 * Class representing the pile in a Briscola 2 Players match. Extends AbstractCardListWrapper<NeapolitanCard>, that provides a default implementation of all the basic methods that can be performed on the pile.
 *
 * @author Francesco Pinto
 */
public class Briscola2PPile extends AbstractCardListWrapper<NeapolitanCard> {


    public Briscola2PPile(String list){
        super(list);
    }

    public Briscola2PPile(List<NeapolitanCard> cards){
        super(cards);
    }

    @Override
    public NeapolitanCard buildCardInstance(String num, String suit) {
        return new NeapolitanCard(num,suit);
    }
}
