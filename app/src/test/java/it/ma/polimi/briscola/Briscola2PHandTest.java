package it.ma.polimi.briscola;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PHand;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanDeck;

import static org.junit.Assert.assertTrue;

/**
 * Class for test of Briscola2PHand. REMARK: not all the inherited methods are tested! (They have been already exhaustively tested in NeapolitanDeckTest.
 * This class just performs further tests on methods that are actually invoked on the hand (just to be perfectly sure everything works as desired, some of them are not necessary!), some of which are inherited.
 */
public class Briscola2PHandTest {


    private NeapolitanCard[] hand0 = {

    };

    private NeapolitanCard[] hand1 = {
            new NeapolitanCard("1","S"),
            new NeapolitanCard("2","B"),
            new NeapolitanCard("K","G"),
    };


    private String handS0 = "";

    private String handS1 = "1S2BKG";

    /**
     * Constructor list test.Not strictly necessary, already tested in NeapolitanDeckTest
     */
    @Test
    public void constructorListTest(){
        List<NeapolitanCard> hand = new ArrayList<>();
        Briscola2PHand briscolahand = new Briscola2PHand(hand);
        assertTrue(briscolahand.isEmpty());
        for(NeapolitanCard nc : hand1){
            hand.add(nc);
        }
        briscolahand = new Briscola2PHand(hand);
        boolean equal = equalCardsList(briscolahand.getCardList(),hand1);
        assertTrue(equal);



    }

    /**
     * Constructor string test. Not strictly necessary, already tested in NeapolitanDeckTest
     */
    @Test
    public void constructorStringTest(){

        Briscola2PHand hand = new Briscola2PHand(handS0);
        assertTrue(hand.isEmpty());
        hand = new Briscola2PHand(handS1);
        boolean equal = equalCardsList(hand.getCardList(),hand1);

        assertTrue(equal);
    }

    /**
     * To string test. Not strictly necessary, already tested in NeapolitanDeckTest
     */
    @Test
    public void toStringTest(){

        List<NeapolitanCard> handList = new ArrayList<>();
        Briscola2PHand hand = new Briscola2PHand(handS0);
        assertTrue(hand.toString().equals(handS0));
        for(NeapolitanCard nc : hand1){
            handList.add(nc);
        }
        hand = new Briscola2PHand(handS1);
        assertTrue(hand.toString().equals(handS1));
    }

    /**
     * Put card in hand test. Not strictly necessary, already tested in NeapolitanDeckTest
     */
    @Test
    public void putCardInHandTest(){

        Briscola2PHand hand = new Briscola2PHand(handS0); //start from empty
        assertTrue(hand.isEmpty());

        hand.appendCard(hand1[0]); //add one card
        assertTrue(hand.size() == 1);
        assertTrue(hand.getCard(0).equalTo(hand1[0]));

        hand.appendCard(hand1[1]); //add a second card
        assertTrue(hand.size() == 2);
        assertTrue(hand.getCard(1).equalTo(hand1[1]));

        hand.appendCard(hand1[2]); //add a third card
        assertTrue(hand.size() == 3);
        assertTrue(hand.getCard(2).equalTo(hand1[2]));

        boolean equal = equalCardsList(hand.getCardList(),hand1);
        assertTrue(equal);

        try {
            hand.appendCard(hand1[2]); //try to add a fourth card
        }catch(IllegalStateException e){
            assertTrue(e instanceof IllegalStateException);
        }

        equal = equalCardsList(hand.getCardList(),hand1);
        assertTrue(equal);

        try {
            hand.appendCard(null);
        }catch(Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }

    }

    /**
     * Remove card from hand test. In this test many possible removal sequences are tested, with different number of cards in hand. It is also checked that on card removal cards shift properly, for instance, if card 1 is removed, then card 2 becomes card 1.
     */
    @Test
    public void removeCardFromHandTest() {


        Briscola2PHand hand = new Briscola2PHand(handS0); //from empty
        assertTrue (hand.isEmpty());
        for (int i = 0; i < 3; i++) {
            try {
                NeapolitanCard card = hand.removeCard(i); //try to remove cards
                }catch(Exception e){
                assertTrue (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException);
                }
        }

        hand.appendCard(hand1[0]); //from one-card hand
        NeapolitanCard card0 = hand.removeCard(0); //remove one
        assertTrue(card0.equalTo(hand1[0]));
        for (int i = 0; i < 3; i++) {
            try {
                NeapolitanCard card = hand.removeCard(i); //try to remove others
            }catch(Exception e){
                assertTrue (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException);
            }
        }

        hand.appendCard(hand1[0]);
        hand.appendCard(hand1[1]); //from two card hands
        card0 = hand.removeCard(0);
        NeapolitanCard card1 = hand.removeCard(0); //remove two hands
        for (int i = 0; i < 3; i++) {
            try {
                NeapolitanCard card = hand.removeCard(i);
                assertTrue (card == null);
            }catch(Exception e){
                assertTrue (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException);
            }
        }

        assertTrue(card0.equalTo(hand1[0]));
        assertTrue(card1.equalTo(hand1[1]));

        //change the order we remove the cards
        hand.appendCard(hand1[0]);
        hand.appendCard(hand1[1]);
        card0 = hand.removeCard(1);
        card1 = hand.removeCard(0);
        for (int i = 0; i < 3; i++) {
            try {
                NeapolitanCard card = hand.removeCard(i);
                assertTrue (card == null);
            }catch(Exception e){
                assertTrue (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException);
            }
        }

        assertTrue(card0.equalTo(hand1[1]));
        assertTrue(card1.equalTo(hand1[0]));


        //add a third card, and try different removal orders
        hand.appendCard(hand1[0]);
        hand.appendCard(hand1[1]);
        hand.appendCard(hand1[2]);
        card0 = hand.removeCard(0);
        card1 = hand.removeCard(0);
        NeapolitanCard card2 = hand.removeCard(0);

        for (int i = 0; i < 3; i++) {
            try {
                NeapolitanCard card = hand.removeCard(i);
                assertTrue (card == null);
            }catch(Exception e){
                assertTrue (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException);
            }
        }
        assertTrue(card0.equalTo(hand1[0]));
        assertTrue(card1.equalTo(hand1[1]));
        assertTrue(card2.equalTo(hand1[2]));

        hand.appendCard(hand1[0]);
        hand.appendCard(hand1[1]);
        hand.appendCard(hand1[2]);
        card0 = hand.removeCard(2);
        card1 = hand.removeCard(1);
        card2 = hand.removeCard(0);

        for (int i = 0; i < 3; i++) {
            try {
                NeapolitanCard card = hand.removeCard(i);
                assertTrue (card == null);
            }catch(Exception e){
                assertTrue (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException);
            }
        }
        assertTrue(card0.equalTo(hand1[2]));
        assertTrue(card1.equalTo(hand1[1]));
        assertTrue(card2.equalTo(hand1[0]));

        hand.appendCard(hand1[0]);
        hand.appendCard(hand1[1]);
        hand.appendCard(hand1[2]);
        card0 = hand.removeCard(1);
        card1 = hand.removeCard(1);
        card2 = hand.removeCard(0);

        for (int i = 0; i < 3; i++) {
            try {
                NeapolitanCard card = hand.removeCard(i);
                assertTrue (card == null);
            }catch(Exception e){
                assertTrue (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException);
            }
        }
        assertTrue(card0.equalTo(hand1[1]));
        assertTrue(card1.equalTo(hand1[2]));
        assertTrue(card2.equalTo(hand1[0]));



    }

    //just a convenience method
    private boolean equalCardsList(List<NeapolitanCard> l1, NeapolitanCard[] l2){
     for(int i = 0; i < l1.size() && i < l2.length;i++) {
         NeapolitanCard nc1 = l1.get(i);
         NeapolitanCard nc2 = l2[i];
         if (!nc1.equalTo(nc2))
             return false;
     }
     return true;
    }

    /**
     * Test that maxNumCardsAllowed is 40.
     */
    @Test
    public void getMaxNumCArdsTest(){
        assertTrue(new Briscola2PHand("").getMaxNumCardsAllowedInList() == 3);
    }

}
