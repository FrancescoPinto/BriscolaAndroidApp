package it.ma.polimi.briscola;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PPile;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;

import static org.junit.Assert.assertTrue;

/**
 * Created by utente on 23/10/17.
 */

public class Briscola2PPileTest {
    NeapolitanCard[] pile0 = {

   };
    NeapolitanCard[] pile1 = {
            new NeapolitanCard("1","S"),
            new NeapolitanCard("2","B"),
            new NeapolitanCard("K","G"),
            new NeapolitanCard("H","C")
    };

    String pileS0 = "";
    String pileS1 = "1S2BKGHC";

            //TODO check siano bastoni/coppe ecc. e numeri corretti, siano carte tutte

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
