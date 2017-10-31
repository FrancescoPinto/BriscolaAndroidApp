package it.ma.polimi.briscola;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PSurface;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;

import static junit.framework.Assert.assertTrue;

/**
 * Created by utente on 24/10/17.
 */

public class Briscola2PSurfaceTest {

    NeapolitanCard[] surface0 = {

    };
    NeapolitanCard[] surface1 = {
            new NeapolitanCard("1","S"),
            new NeapolitanCard("2","B")
    };

    NeapolitanCard[] surface2 ={
            new NeapolitanCard("1","S"),
            new NeapolitanCard("2","B"),
            new NeapolitanCard("K","G"),
            new NeapolitanCard("H","C")
    };
    String surfaceS0 = "";
    String surfaceS1 = "1S2B";
    String surfaceS2 = "1S2BKGHC";

    //TODO check siano bastoni/coppe ecc. e numeri corretti, siano carte tutte

    @Test
    public void constructorListTest(){
        List<NeapolitanCard> surface = new ArrayList<>();
        Briscola2PSurface briscolasurface = new Briscola2PSurface(surface);
        assertTrue(briscolasurface.getSurface().isEmpty());


        for(NeapolitanCard nc : surface1){
            surface.add(nc);
        }
        briscolasurface = new Briscola2PSurface(surface);
        boolean equal = true;
        List<NeapolitanCard> surfaceList = briscolasurface.getSurface();
        for(int i = 0; i < surfaceList.size() && i < surface1.length;i++) {
            NeapolitanCard nc1 = surfaceList.get(i);
            NeapolitanCard nc2 = surface1[i];
            if (!nc1.equalTo(nc2))
                equal = false;
        }
        assertTrue(equal);

       surface.clear();
        for(NeapolitanCard nc : surface2){
            surface.add(nc);
        }

        try {
            briscolasurface = new Briscola2PSurface(surface);
        }catch(IllegalArgumentException e){
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void constructorStringTest(){

        Briscola2PSurface surface = new Briscola2PSurface(surfaceS0);
        assertTrue(surface.getSurface().isEmpty());
        surface = new Briscola2PSurface(surfaceS1);
        boolean equal = true;
        List<NeapolitanCard> surfaceList = surface.getSurface();
        for(int i = 0; i < surfaceList.size() && i < surface1.length;i++) {
            NeapolitanCard nc1 = surfaceList.get(i);
            NeapolitanCard nc2 = surface1[i];
            if (!nc1.equalTo(nc2))
                equal = false;
        }
        assertTrue(equal);

        try {
            surface = new Briscola2PSurface(surfaceS2);
        }catch(IllegalArgumentException e){
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void toStringTest(){

        List<NeapolitanCard> surfaceList = new ArrayList<>();
        Briscola2PSurface surface = new Briscola2PSurface(surfaceS0);
        assertTrue(surface.toString().equals(surfaceS0));
        for(NeapolitanCard nc : surface1){
            surfaceList.add(nc);
        }
        surface = new Briscola2PSurface(surfaceS1);
        assertTrue(surface.toString().equals(surfaceS1));
    }




    @Test
    public void putCardOnSurfaceTest(){

        //TODO nel putCardInsurface assicurati che non può passare Null (in tal caso lanci un'eccezione, per ora invece aggiunge il null come fosse una carta

        Briscola2PSurface surface = new Briscola2PSurface(surfaceS0);
        assertTrue(surface.getSurface().isEmpty());

        surface.putCardOnSurface(surface1[0]);
        assertTrue(surface.getSurface().size() == 1);
        assertTrue(surface.getSurface().get(0).equalTo(surface1[0]));

        surface.putCardOnSurface(surface1[1]);
        assertTrue(surface.getSurface().size() == 2);
        assertTrue(surface.getSurface().get(0).equalTo(surface1[0]));
        assertTrue(surface.getSurface().get(1).equalTo(surface1[1]));

    }

    @Test
    public void clearSurfaceTest() {

        Briscola2PSurface surface = new Briscola2PSurface(surfaceS0);
        assertTrue (surface.getSurface().isEmpty());

        List<NeapolitanCard> surfList = surface.clearSurface();
        assertTrue(surfList.isEmpty());

        surface.putCardOnSurface(surface1[0]);
        surface.putCardOnSurface(surface1[1]);

        surfList = surface.clearSurface();
        assertTrue(surfList.get(0).equalTo(surface1[0]));
        assertTrue(surfList.get(1).equalTo(surface1[1]));

    }


    @Test
    public void getCardTest() {

        Briscola2PSurface surface = new Briscola2PSurface(surfaceS0);
        assertTrue (surface.getSurface().isEmpty());

        NeapolitanCard card0,card1;
        try{
             card0 = surface.getCard(0);
        }catch(IndexOutOfBoundsException e){
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

        try{
             card0 = surface.getCard(1);
        }catch(IndexOutOfBoundsException e){
            assertTrue(e instanceof IndexOutOfBoundsException);
        }


        surface.putCardOnSurface(surface1[0]);
        surface.putCardOnSurface(surface1[1]);

         card0 = surface.getCard(0);
         card1 = surface.getCard(1);
        assertTrue(card0.equalTo(surface1[0]));
        assertTrue(card1.equalTo(surface1[1]));

    }

    @Test
    public void countCardsOnSurfaceTest(){
        Briscola2PSurface surface = new Briscola2PSurface(surfaceS0);
        assertTrue(surface.countCardsOnSurface() == 0);

        surface.putCardOnSurface(surface1[0]);
        assertTrue(surface.countCardsOnSurface() == 1);
        surface.putCardOnSurface(surface1[1]);
        assertTrue(surface.countCardsOnSurface() == 2);

    }
}
