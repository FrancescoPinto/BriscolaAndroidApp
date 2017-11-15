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

    public static final int FIRSTCARD = 0,SECONDCARD = 1;

    private static int maxNumCardsAllowedOnSurface = 2;

    public Briscola2PSurface(String list){
        super(list);
    }

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
