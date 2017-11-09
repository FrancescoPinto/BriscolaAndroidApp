package it.ma.polimi.briscola;

import org.junit.Test;

import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Created by utente on 09/11/17.
 */


public class EnumsTest {
    String neapolitanCardNumbers = "1234567JHK";
    String neapolitanCardSuit = "BCGS";

    @Test
    public void testgetNeapolitanCardNumbers(){
        NeapolitanCardNumbers[] ncn = NeapolitanCardNumbers.values();
        for(int i = 0; i < neapolitanCardNumbers.length(); i++){
            ncn[i] = NeapolitanCardNumbers.getCardNumber(""+neapolitanCardNumbers.charAt(i));
        }
    }

    @Test
    public void testgetNeapolitanCardSuit(){
        NeapolitanCardSuit[] ncs = NeapolitanCardSuit.values();
        for(int i = 0; i < neapolitanCardSuit.length(); i++){
            ncs[i] = NeapolitanCardSuit.getCardSuit(""+neapolitanCardSuit.charAt(i));
        }
    }
}
