package it.ma.polimi.briscola;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.deck.AbstractDeck;
import it.ma.polimi.briscola.model.deck.Card;
import it.ma.polimi.briscola.model.deck.Deck;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;
import it.ma.polimi.briscola.model.deck.NeapolitanDeck;
import it.ma.polimi.briscola.model.deck.UniformProbabilityShuffler;
import static org.junit.Assert.assertTrue;


/**
 * Created by utente on 20/10/17.
 */

public class NeapolitanDeckTest {

    private NeapolitanCard[] newDeck = {
            new NeapolitanCard(NeapolitanCardNumbers.ACE, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.ACE, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.ACE, NeapolitanCardSuit.GOLDS),
            new NeapolitanCard(NeapolitanCardNumbers.ACE, NeapolitanCardSuit.SWORDS),

            new NeapolitanCard(NeapolitanCardNumbers.TWO, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.TWO, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.TWO, NeapolitanCardSuit.GOLDS),
            new NeapolitanCard(NeapolitanCardNumbers.TWO, NeapolitanCardSuit.SWORDS),

            new NeapolitanCard(NeapolitanCardNumbers.THREE, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.THREE, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.THREE, NeapolitanCardSuit.GOLDS),
            new NeapolitanCard(NeapolitanCardNumbers.THREE, NeapolitanCardSuit.SWORDS),

            new NeapolitanCard(NeapolitanCardNumbers.FOUR, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.FOUR, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.FOUR, NeapolitanCardSuit.GOLDS),
            new NeapolitanCard(NeapolitanCardNumbers.FOUR, NeapolitanCardSuit.SWORDS),

            new NeapolitanCard(NeapolitanCardNumbers.FIVE, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.FIVE, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.FIVE, NeapolitanCardSuit.GOLDS),
            new NeapolitanCard(NeapolitanCardNumbers.FIVE, NeapolitanCardSuit.SWORDS),

            new NeapolitanCard(NeapolitanCardNumbers.SIX, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.SIX, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.SIX, NeapolitanCardSuit.GOLDS),
            new NeapolitanCard(NeapolitanCardNumbers.SIX, NeapolitanCardSuit.SWORDS),

            new NeapolitanCard(NeapolitanCardNumbers.SEVEN, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.SEVEN, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.SEVEN, NeapolitanCardSuit.GOLDS),
            new NeapolitanCard(NeapolitanCardNumbers.SEVEN, NeapolitanCardSuit.SWORDS),

            new NeapolitanCard(NeapolitanCardNumbers.JACK, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.JACK, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.JACK, NeapolitanCardSuit.GOLDS),
            new NeapolitanCard(NeapolitanCardNumbers.JACK, NeapolitanCardSuit.SWORDS),

            new NeapolitanCard(NeapolitanCardNumbers.HORSE, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.HORSE, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.HORSE, NeapolitanCardSuit.GOLDS),
            new NeapolitanCard(NeapolitanCardNumbers.HORSE, NeapolitanCardSuit.SWORDS),

            new NeapolitanCard(NeapolitanCardNumbers.KING, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.KING, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.KING, NeapolitanCardSuit.GOLDS),
            new NeapolitanCard(NeapolitanCardNumbers.KING, NeapolitanCardSuit.SWORDS),
    };

    private NeapolitanCard[] buildValidDeck0 = {

            new NeapolitanCard(NeapolitanCardNumbers.ACE, NeapolitanCardSuit.SWORDS),
            new NeapolitanCard(NeapolitanCardNumbers.KING, NeapolitanCardSuit.GOLDS),
            new NeapolitanCard(NeapolitanCardNumbers.JACK, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.THREE, NeapolitanCardSuit.BATONS)

    };

    private NeapolitanCard[] buildValidDeck1 = {

    };

    private NeapolitanCard[] buildValidDeck2 = {
            new NeapolitanCard(NeapolitanCardNumbers.FOUR, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.FIVE, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.SIX, NeapolitanCardSuit.SWORDS),
            new NeapolitanCard(NeapolitanCardNumbers.SEVEN, NeapolitanCardSuit.SWORDS),
            new NeapolitanCard(NeapolitanCardNumbers.HORSE, NeapolitanCardSuit.GOLDS)
    };


    private NeapolitanCard[][] buildValidDeck = {buildValidDeck0,buildValidDeck1,buildValidDeck2};
    private String[] buildValidStrings = { "1SKGJC3B", "", "4C5B6S7SHG"}; //prova con qualcosa di più lungo o qualcosa di invalido

    @Test
    public void briscola2PlayersDeckConstructorTest(){
        NeapolitanDeck nd = new NeapolitanDeck();
        List<NeapolitanCard> cards = nd.getCardList();

        for(int i = 0; i < cards.size() && i < newDeck.length; i++){
            NeapolitanCard c1 = cards.get(i);
            NeapolitanCard c2 = newDeck[i];
            //System.out.println(" " + c1.getCardNumber() + " " + c2.getCardNumber() + " " + c1.getCardSuit()+ " "+c2.getCardSuit());
            assertTrue(c1.getCardNumber().equals(c2.getCardNumber()) && c1.getCardSuit().equals(c2.getCardSuit()));

        }


    }

    //Attento, questo test controlla solo che su 10 shuffle ce ne siano almeno 2 diversi
    //probabilisticamente può avvenire che il test fallisca (escono 10 shuffle tutti uguali)
    //ma in pratica dovrebbe essere un evento molto improbabile avere una sequenza così lunga
    //di shuffle tutti uguali
    @Test
    public void shuffleDeckTest(){
        List<AbstractDeck> b2pds = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            b2pds.add(new NeapolitanDeck().shuffleDeck(new UniformProbabilityShuffler()));
            assertTrue(b2pds.get(i).size() == 40);
        }

        int identicalShuffles = 0;

        for(int i = 0; i < 9; i++) { //da 0 a 8
            List<? extends Card> ci = b2pds.get(i).getCardList();
            int correspondences = 0;

            for (int j = i + 1; j < 10; j++) { //compara con quelli dopo
                List<? extends Card> cj = b2pds.get(i).getCardList();

                for(int k = 0; k < 40; k++){
                    if(ci.get(k).getCardSuit() == cj.get(k).getCardSuit() && ci.get(k).getCardNumber() == cj.get(k).getCardNumber())
                        correspondences++;
                }

                System.out.println(b2pds.get(i));

            }

            if (correspondences == 40)
                identicalShuffles++;
        }

        assertTrue("If the test fails once, repeat it. If it fails many times in a row, something's wrong.",identicalShuffles < 10);


    }

    @Test
    public void buildFromStringTest(){
        NeapolitanDeck deck;
        NeapolitanCard card;
        for(int i = 0; i < buildValidDeck.length; i++){
            deck = new NeapolitanDeck(buildValidStrings[i]);
            List<NeapolitanCard> cards = deck.getCardList();
            for(int j = 0; j < buildValidDeck[i].length && j < deck.size(); j++) {
                 card = cards.get(j);
                 assertTrue(card.getCardNumber().equals(buildValidDeck[i][j].getCardNumber()));
                assertTrue(card.getCardSuit().equals(buildValidDeck[i][j].getCardSuit()));

            }
        }

                //test a parte per il newDeck
        String newDeckString = "";
        for(NeapolitanCardNumbers cn : NeapolitanCardNumbers.values()){
            for(NeapolitanCardSuit cs : NeapolitanCardSuit.values()){
                newDeckString+= cn.getNumber()+cs.getSuit();
            }
        }

        deck = new NeapolitanDeck(newDeckString);

        for(int j = 0; j < newDeck.length && j < deck.size(); j++) {
            card = deck.getCard(j);
            assertTrue(card.getCardNumber().equals(newDeck[j].getCardNumber()));
            assertTrue(card.getCardSuit().equals(newDeck[j].getCardSuit()));

        }


    }

    @Test
    public void putCardToBottomTest(){

        NeapolitanDeck deck;
        String tempString;
        for(int i = 0; i < buildValidDeck.length; i++) {
            deck = new NeapolitanDeck(buildValidStrings[i]);
            tempString = deck.toString();
            deck.putCardToBottom(new NeapolitanCard("1","S"));
            assertTrue(deck.toString().equals(tempString+"1S"));
            System.out.println(deck);
        }

    }

    @Test
    public void drawCardFromTopTest(){
        NeapolitanDeck deck = new NeapolitanDeck(buildValidStrings[0]);
        List<NeapolitanCard> cards = new ArrayList<>(deck.getCardList());
        int tempSize = cards.size();
        for(NeapolitanCard c : cards){
            NeapolitanCard temp = deck.drawCardFromTop();
            assertTrue(temp.equalTo(c));
            assertTrue(--tempSize == deck.size());
        }
        try{
            deck.drawCardFromTop();
        }catch(Exception e){
            assertTrue(e instanceof IllegalStateException);
        }
    }


    @Test
    public void drawCardFromBottomTest(){
        NeapolitanDeck deck = new NeapolitanDeck(buildValidStrings[0]);
        List<NeapolitanCard> cards = new ArrayList<>(deck.getCardList());
        int tempSize = cards.size();
        for(int i = cards.size()-1; i >= 0; i--){
            NeapolitanCard c = cards.get(i);
            NeapolitanCard temp = deck.drawCardFromBottom();
            assertTrue(temp.equalTo(c));
            assertTrue(--tempSize == deck.size());
        }
        try{
            deck.drawCardFromBottom();
        }catch(Exception e){
            assertTrue(e instanceof IllegalStateException);
        }
    }

    @Test
    public void equalToTest(){
        for(int i = 0; i < buildValidDeck.length; i++){
            for(int j = 0; j < buildValidDeck.length; j++){
                NeapolitanDeck deck1 = new NeapolitanDeck(buildValidStrings[i]);
                NeapolitanDeck deck2 = new NeapolitanDeck(buildValidStrings[j]);

                if(i == j)
                    assertTrue(deck1.equalTo(deck2));
                else
                    assertTrue(!deck1.equalTo(deck2));
            }

        }
    }

    @Test
    public void containsCardTest(){
        NeapolitanDeck deck = new NeapolitanDeck(buildValidStrings[0]);
        assertTrue(deck.containsCard(new NeapolitanCard("1","S")));
        assertTrue(deck.containsCard(new NeapolitanCard("K","G")));
        assertTrue(deck.containsCard(new NeapolitanCard("J","C")));
        assertTrue(deck.containsCard(new NeapolitanCard("3","B")));
        assertTrue(!deck.containsCard(new NeapolitanCard("K","C")));

        deck = new NeapolitanDeck(buildValidStrings[1]);
        assertTrue(!deck.containsCard(new NeapolitanCard("K","C")));

    }
}
