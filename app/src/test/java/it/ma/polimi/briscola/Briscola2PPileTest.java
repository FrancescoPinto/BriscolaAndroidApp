package it.ma.polimi.briscola;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PPile;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;

import static org.junit.Assert.assertTrue;

/**
 *Class for test of Briscola2PPile. REMARK: not all the inherited methods are tested! (They have been already exhaustively tested in NeapolitanDeckTest.
 * This class just performs further tests on methods that are actually invoked on the pile (just to be perfectly sure everything works as desired, some of them are not necessary!), some of which are inherited.
 * */

public class Briscola2PPileTest {

    private NeapolitanCard[] pile0 = {

   };

    private NeapolitanCard[] pile1 = {
            new NeapolitanCard("1","S"),
            new NeapolitanCard("2","B"),
            new NeapolitanCard("K","G"),
            new NeapolitanCard("H","C")
    };


    private String pileS0 = "";

    private String pileS1 = "1S2BKGHC";


    /**
     * Constructor list test.  Not strictly necessary, already tested in NeapolitanDeckTest
     */
    @Test
    public void constructorListTest(){
          List<NeapolitanCard> pile = new ArrayList<>();
          Briscola2PPile briscolaPile = new Briscola2PPile(pileS0);
        assertTrue(briscolaPile.isEmpty());
          for(NeapolitanCard nc : pile1){
               pile.add(nc);
          }
         briscolaPile = new Briscola2PPile(pileS1);
         boolean equal = true;
         List<NeapolitanCard> pileList = briscolaPile.getCardList();
         for(int i = 0; i < pileList.size() && i < pile1.length;i++){
             NeapolitanCard nc1 = pileList.get(i);
             NeapolitanCard nc2 = pile1[i];
           if(!nc1.equalTo(nc2))
            equal = false;
         }
        assertTrue(equal);



    }

    /**
     * Constructor string test.  Not strictly necessary, already tested in NeapolitanDeckTest
     */
    @Test
    public void constructorStringTest(){

         Briscola2PPile pile = new Briscola2PPile(pileS0);
         assertTrue(pile.isEmpty());
         pile = new Briscola2PPile(pileS1);
         boolean equal = true;
         List<NeapolitanCard> pileList = pile.getCardList();
            for(int i = 0; i < pileList.size() && i < pile1.length;i++){
                NeapolitanCard nc1 = pileList.get(i);
                NeapolitanCard nc2 = pile1[i];
                if(!nc1.equalTo(nc2))
                    equal = false;
            }
          assertTrue(equal);
    }

    /**
     * To string test.  Not strictly necessary, already tested in NeapolitanDeckTest
     */
    @Test
    public void toStringTest(){



        List<NeapolitanCard> pileList = new ArrayList<>();
        Briscola2PPile pile = new Briscola2PPile(pileS0);
        assertTrue(pile.toString().equals(pileS0));
        for(NeapolitanCard nc : pile1){
            pileList.add(nc);
        }
        pile = new Briscola2PPile(pileS1);
        assertTrue(pile.toString().equals(pileS1));
    }

    /**
     * Push on pile test.  Not strictly necessary, already tested in NeapolitanDeckTest
     */
    @Test
    public void pushOnPileTest(){
        Briscola2PPile pile = new Briscola2PPile(pileS0);
        List<NeapolitanCard> pileList = new ArrayList<>();
        pile.appendAll(pileList);
        assertTrue(pile.isEmpty());

        for(NeapolitanCard nc : pile1){
            pileList.add(nc);
        }

        pile.appendAll(pileList);
        boolean equal = true;
        for(int i = 0; i < pile.size() && i < pile1.length;i++){
            NeapolitanCard nc1 = pile.getCard(i);
            NeapolitanCard nc2 = pile1[i];
            if(!nc1.equalTo(nc2))
                equal = false;
        }

        System.out.println(pile);
        assertTrue(equal);
    }


}
