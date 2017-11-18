package it.ma.polimi.briscola;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PSurface;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;

import static junit.framework.Assert.assertTrue;

/**
 * Test class containing tests for Briscola2PSurface. REMARK: not all the inherited methods are tested! (They have been already exhaustively tested in NeapolitanDeckTest.
 * This class just performs further tests on methods that are actually invoked on the surface (just to be perfectly sure everything works as desired, some of them are not necessary!), some of which are inherited.
 */
public class Briscola2PSurfaceTest {

    /**
     * The Surface 0.
     */
//data for testing
    NeapolitanCard[] surface0 = {

    };
    /**
     * The Surface 1.
     */
    NeapolitanCard[] surface1 = {
            new NeapolitanCard("1","S"),
            new NeapolitanCard("2","B")
    };

    /**
     * The Surface 2.
     */
    NeapolitanCard[] surface2 ={
            new NeapolitanCard("1","S"),
            new NeapolitanCard("2","B"),
            new NeapolitanCard("K","G"),
            new NeapolitanCard("H","C")
    };
    /**
     * The Surface 0 stringified .
     */
    String surfaceS0 = "";
    /**
     * The Surface 1 stringified.
     */
    String surfaceS1 = "1S2B";
    /**
     * The Surface 2 stringified.
     */
    String surfaceS2 = "1S2BKGHC";

    /**
     * Constructor list test. Not strictly necessary, already tested in NeapolitanDeckTest
     */
    @Test
    public void constructorListTest(){
        List<NeapolitanCard> surface = new ArrayList<>();
        Briscola2PSurface briscolasurface = new Briscola2PSurface(surface);
        assertTrue(briscolasurface.isEmpty());


        for(NeapolitanCard nc : surface1){
            surface.add(nc);
        }
        briscolasurface = new Briscola2PSurface(surface);
        boolean equal = true;
        List<NeapolitanCard> surfaceList = briscolasurface.getCardList();
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
            briscolasurface = new Briscola2PSurface(surface); //too many cards in input, throw exception!
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    /**
     * Constructor string test. Not strictly necessary, already tested in NeapolitanDeckTest
     */
    @Test
    public void constructorStringTest(){

        Briscola2PSurface surface = new Briscola2PSurface(surfaceS0); //empty input
        assertTrue(surface.isEmpty());
        surface = new Briscola2PSurface(surfaceS1); //non-empty input
        boolean equal = true;
        List<NeapolitanCard> surfaceList = surface.getCardList();
        for(int i = 0; i < surfaceList.size() && i < surface1.length;i++) {
            NeapolitanCard nc1 = surfaceList.get(i);
            NeapolitanCard nc2 = surface1[i];
            if (!nc1.equalTo(nc2))
                equal = false;
        }
        assertTrue(equal);

        try {
            surface = new Briscola2PSurface(surfaceS2); //too many cards in input, throw exception!
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    /**
     * To string test. Checks the surface is correctly stringified.  Not strictly necessary, already tested in NeapolitanDeckTest
     */
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


    /**
     * Put card on surface test.  Not strictly necessary, already tested in NeapolitanDeckTest
     */
    @Test
    public void putCardOnSurfaceTest(){

        Briscola2PSurface surface = new Briscola2PSurface(surfaceS0); //start from empty surface
        assertTrue(surface.isEmpty());

        surface.appendCard(surface1[0]); //ad one card
        assertTrue(surface.size() == 1);
        assertTrue(surface.getCard(0).equalTo(surface1[0]));

        surface.appendCard(surface1[1]); //add another card
        assertTrue(surface.size() == 2);
        assertTrue(surface.getCard(0).equalTo(surface1[0]));
        assertTrue(surface.getCard(1).equalTo(surface1[1]));

        try{
            surface.appendCard(surface1[1]); //add a third card (illegal!)
        }catch(Exception e){
            assertTrue(e instanceof IllegalStateException);
        }

        try {
            surface.appendCard(null);
        }catch(Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }

    }

    /**
     * Clear surface test.  Not strictly necessary, already tested in NeapolitanDeckTest
     */
    @Test
    public void clearCardListTest() {

        Briscola2PSurface surface = new Briscola2PSurface(surfaceS0); //start from empty surface
        assertTrue (surface.isEmpty());

        List<NeapolitanCard> surfList = surface.clearCardList(); //clear empty surface
        assertTrue(surfList.isEmpty());

        surface.appendCard(surface1[0]);
        surface.appendCard(surface1[1]);

        surfList = surface.clearCardList(); //clear filled surface
        assertTrue(surfList.get(0).equalTo(surface1[0]));
        assertTrue(surfList.get(1).equalTo(surface1[1]));

    }


    /**
     * Gets card test.  Not strictly necessary, already tested in NeapolitanDeckTest, this test only checks for FIRSTCARD and SECONDCARD values
     */
    @Test
    public void getCardTest() {

        Briscola2PSurface surface = new Briscola2PSurface(surfaceS0);
        assertTrue (surface.isEmpty());

        NeapolitanCard card0,card1;
        try{
             card0 = surface.getCard(0); //try to remove from empty
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

        try{
             card0 = surface.getCard(1); //try to remove from empty
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }


        surface.appendCard(surface1[0]);
        surface.appendCard(surface1[1]);

         card0 = surface.getCard(0);
         card1 = surface.getCard(1);
        assertTrue(card0.equalTo(surface1[0]));
        assertTrue(card1.equalTo(surface1[1]));


       surface.clearCardList();
        surface.appendCard(surface1[0]);
        try{
            surface.removeCard(41); //try to remove a card with index greater than maxnumcardsallowed
        }catch(Exception e){
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

        try{
            surface.removeCard(1); //try to remove a card with index greater than size()-1 but smaller than maxnumcardsallowed
        }catch(Exception e){
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

        try{
            surface.removeCard(-30); //try to remove a card with index negative number
        }catch(Exception e){
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

    }

    /**
     * Count cards on surface test.
     */
    @Test
    public void countCardsOnSurfaceTest(){
        Briscola2PSurface surface = new Briscola2PSurface(surfaceS0);
        assertTrue(surface.size() == 0);

        surface.appendCard(surface1[0]);
        assertTrue(surface.size() == 1);
        surface.appendCard(surface1[1]);
        assertTrue(surface.size() == 2);

    }

    /**
     * Test that maxNumCardsAllowed is 40.
     */
    @Test
    public void getMaxNumCArdsTest(){
        assertTrue(new Briscola2PSurface("").getMaxNumCardsAllowedInList() == 2);
    }

    //tests for other inherited methods are ontained in NeapolitanDeckTest (are the same!)
}
