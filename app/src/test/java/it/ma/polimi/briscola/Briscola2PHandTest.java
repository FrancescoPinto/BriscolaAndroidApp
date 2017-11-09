package it.ma.polimi.briscola;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PHand;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;

import static org.junit.Assert.assertTrue;

/**
 * Created by utente on 23/10/17.
 */

public class Briscola2PHandTest {

    NeapolitanCard[] hand0 = {

    };
    NeapolitanCard[] hand1 = {
            new NeapolitanCard("1","S"),
            new NeapolitanCard("2","B"),
            new NeapolitanCard("K","G"),
    };

    String handS0 = "";
    String handS1 = "1S2BKG";

    //TODO check siano bastoni/coppe ecc. e numeri corretti, siano carte tutte

    @Test
    public void constructorListTest(){
        List<NeapolitanCard> hand = new ArrayList<>();
        Briscola2PHand briscolahand = new Briscola2PHand(hand);
        assertTrue(briscolahand.getHand().isEmpty());
        for(NeapolitanCard nc : hand1){
            hand.add(nc);
        }
        briscolahand = new Briscola2PHand(hand);
        boolean equal = equalCardsList(briscolahand.getHand(),hand1);
        assertTrue(equal);



    }

    @Test
    public void constructorStringTest(){

        Briscola2PHand hand = new Briscola2PHand(handS0);
        assertTrue(hand.getHand().isEmpty());
        hand = new Briscola2PHand(handS1);
        boolean equal = equalCardsList(hand.getHand(),hand1);

        assertTrue(equal);
    }

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

    @Test
    public void putCardInHandTest(){

        //TODO nel putCardInHand assicurati che non può passare Null (in tal caso lanci un'eccezione, per ora invece aggiunge il null come fosse una carta

        Briscola2PHand hand = new Briscola2PHand(handS0);
        assertTrue(hand.getHand().isEmpty());

        hand.putCardInHand(hand1[0]);
        assertTrue(hand.getHand().size() == 1);
        assertTrue(hand.getHand().get(0).equalTo(hand1[0]));

        hand.putCardInHand(hand1[1]);
        assertTrue(hand.getHand().size() == 2);
        assertTrue(hand.getHand().get(1).equalTo(hand1[1]));

        hand.putCardInHand(hand1[2]);
        assertTrue(hand.getHand().size() == 3);
        assertTrue(hand.getHand().get(2).equalTo(hand1[2]));

        boolean equal = equalCardsList(hand.getHand(),hand1);
        assertTrue(equal);

        try {
            hand.putCardInHand(hand1[2]);
        }catch(IllegalStateException e){
            assertTrue(e instanceof IllegalStateException);
        }

        equal = equalCardsList(hand.getHand(),hand1);
        assertTrue(equal);

    }

    @Test
    public void removeCardFromHandTest() {


        Briscola2PHand hand = new Briscola2PHand(handS0);
        assertTrue (hand.getHand().isEmpty());
        for (int i = 0; i < 3; i++) {
            try {
                NeapolitanCard card = hand.removeCardFromHand(i);
                assertTrue (card == null);
                }catch(IndexOutOfBoundsException|IllegalArgumentException e){
                assertTrue (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException);
                }
        }

        hand.putCardInHand(hand1[0]);
        NeapolitanCard card0 = hand.removeCardFromHand(0);
        assertTrue(card0.equalTo(hand1[0]));
        for (int i = 0; i < 3; i++) {
            try {
                NeapolitanCard card = hand.removeCardFromHand(i);
                assertTrue (card == null);
            }catch(IndexOutOfBoundsException|IllegalArgumentException e){
                assertTrue (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException);
            }
        }

        hand.putCardInHand(hand1[0]);
        hand.putCardInHand(hand1[1]);
        card0 = hand.removeCardFromHand(0);
        NeapolitanCard card1 = hand.removeCardFromHand(0);
        for (int i = 0; i < 3; i++) {
            try {
                NeapolitanCard card = hand.removeCardFromHand(i);
                assertTrue (card == null);
            }catch(IndexOutOfBoundsException|IllegalArgumentException e){
                assertTrue (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException);
            }
        }

        assertTrue(card0.equalTo(hand1[0]));
        assertTrue(card1.equalTo(hand1[1]));

        hand.putCardInHand(hand1[0]);
        hand.putCardInHand(hand1[1]);
        card0 = hand.removeCardFromHand(1);
        card1 = hand.removeCardFromHand(0);
        for (int i = 0; i < 3; i++) {
            try {
                NeapolitanCard card = hand.removeCardFromHand(i);
                assertTrue (card == null);
            }catch(IndexOutOfBoundsException|IllegalArgumentException e){
                assertTrue (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException);
            }
        }

        assertTrue(card0.equalTo(hand1[1]));
        assertTrue(card1.equalTo(hand1[0]));


        hand.putCardInHand(hand1[0]);
        hand.putCardInHand(hand1[1]);
        hand.putCardInHand(hand1[2]);
        card0 = hand.removeCardFromHand(0);
        card1 = hand.removeCardFromHand(0);
        NeapolitanCard card2 = hand.removeCardFromHand(0);

        for (int i = 0; i < 3; i++) {
            try {
                NeapolitanCard card = hand.removeCardFromHand(i);
                assertTrue (card == null);
            }catch(IndexOutOfBoundsException|IllegalArgumentException e){
                assertTrue (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException);
            }
        }
        assertTrue(card0.equalTo(hand1[0]));
        assertTrue(card1.equalTo(hand1[1]));
        assertTrue(card2.equalTo(hand1[2]));

        hand.putCardInHand(hand1[0]);
        hand.putCardInHand(hand1[1]);
        hand.putCardInHand(hand1[2]);
        card0 = hand.removeCardFromHand(2);
        card1 = hand.removeCardFromHand(1);
        card2 = hand.removeCardFromHand(0);

        for (int i = 0; i < 3; i++) {
            try {
                NeapolitanCard card = hand.removeCardFromHand(i);
                assertTrue (card == null);
            }catch(IndexOutOfBoundsException|IllegalArgumentException e){
                assertTrue (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException);
            }
        }
        assertTrue(card0.equalTo(hand1[2]));
        assertTrue(card1.equalTo(hand1[1]));
        assertTrue(card2.equalTo(hand1[0]));

        hand.putCardInHand(hand1[0]);
        hand.putCardInHand(hand1[1]);
        hand.putCardInHand(hand1[2]);
        card0 = hand.removeCardFromHand(1);
        card1 = hand.removeCardFromHand(1);
        card2 = hand.removeCardFromHand(0);

        for (int i = 0; i < 3; i++) {
            try {
                NeapolitanCard card = hand.removeCardFromHand(i);
                assertTrue (card == null);
            }catch(IndexOutOfBoundsException|IllegalArgumentException e){
                assertTrue (e instanceof IndexOutOfBoundsException || e instanceof IllegalArgumentException);
            }
        }
        assertTrue(card0.equalTo(hand1[1]));
        assertTrue(card1.equalTo(hand1[2]));
        assertTrue(card2.equalTo(hand1[0]));



    }

    private boolean equalCardsList(List<NeapolitanCard> l1, NeapolitanCard[] l2){
     for(int i = 0; i < l1.size() && i < l2.length;i++) {
         NeapolitanCard nc1 = l1.get(i);
         NeapolitanCard nc2 = l2[i];
         if (!nc1.equalTo(nc2))
             return false;
     }
     return true;
    }

}
