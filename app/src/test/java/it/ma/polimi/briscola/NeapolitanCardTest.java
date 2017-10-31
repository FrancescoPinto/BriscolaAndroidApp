package it.ma.polimi.briscola;

import org.junit.Test;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

import static org.junit.Assert.assertTrue;

/**
 * Created by utente on 27/10/17.
 */

public class NeapolitanCardTest {

    @Test
    public void testCharConstructor(){
        NeapolitanCard c = new NeapolitanCard('1','B');
        assertTrue((new NeapolitanCard(NeapolitanCardNumbers.ACE, NeapolitanCardSuit.BATONS)).equalTo(c));
    }

    @Test
    public void equalToTest(){
        NeapolitanCard c1 = new NeapolitanCard('1','B');
        NeapolitanCard c2 = new NeapolitanCard('1','B');
        NeapolitanCard c3 = new NeapolitanCard('K','B');
        NeapolitanCard c4 = new NeapolitanCard('1','G');
        NeapolitanCard c5 = new NeapolitanCard('4','G');

        assertTrue(c1.equalTo(c2));
        assertTrue(!c1.equalTo(c3));
        assertTrue(!c1.equalTo(c4));
        assertTrue(!c1.equalTo(c5));
    }
}
