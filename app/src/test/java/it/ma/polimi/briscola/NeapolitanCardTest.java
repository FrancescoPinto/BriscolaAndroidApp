package it.ma.polimi.briscola;

import org.junit.Test;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

import static org.junit.Assert.assertTrue;

/**
 * Tests for NeapolitanCard.
 */
public class NeapolitanCardTest {

    /**
     * The Cards collection 1.
     */
    NeapolitanCard[] cardsCollection1 = {
            new NeapolitanCard(NeapolitanCardNumbers.KING, NeapolitanCardSuit.GOLDS),
            new NeapolitanCard(NeapolitanCardNumbers.ACE, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.TWO, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.THREE, NeapolitanCardSuit.SWORDS),
            new NeapolitanCard(NeapolitanCardNumbers.FOUR, NeapolitanCardSuit.GOLDS),
            new NeapolitanCard(NeapolitanCardNumbers.FIVE, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.SIX, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.SEVEN, NeapolitanCardSuit.SWORDS),
            new NeapolitanCard(NeapolitanCardNumbers.HORSE, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.JACK, NeapolitanCardSuit.SWORDS)

    };


    /**
     * The Cards collection 1 stringified.
     */
    String[] cardsCollection1Stringified = {
            "KG",
            "1C",
            "2B",
            "3S",
            "4G",
            "5C",
            "6B",
            "7S",
            "HB",
            "JS"
    };

    /**
     * The Cards collection 1 built with string constructor.
     */
    NeapolitanCard[] cardsCollection1WithStrings = {
            new NeapolitanCard("K","G"),
            new NeapolitanCard("1", "C"),
            new NeapolitanCard("2", "B"),
            new NeapolitanCard("3", "S"),
            new NeapolitanCard("4", "G"),
            new NeapolitanCard("5", "C"),
            new NeapolitanCard("6", "B"),
            new NeapolitanCard("7", "S"),
            new NeapolitanCard("H", "B"),
            new NeapolitanCard("J", "S")

    };

    /**
     * The Cards collection 1 built with char constructor.
     */
    NeapolitanCard[] cardsCollection1WithChars = {
            new NeapolitanCard('K','G'),
            new NeapolitanCard('1', 'C'),
            new NeapolitanCard('2', 'B'),
            new NeapolitanCard('3', 'S'),
            new NeapolitanCard('4', 'G'),
            new NeapolitanCard('5', 'C'),
            new NeapolitanCard('6', 'B'),
            new NeapolitanCard('7', 'S'),
            new NeapolitanCard('H', 'B'),
            new NeapolitanCard('J', 'S')

    };

    /**
     * Test getters and to string and constructors.
     */
    @Test
    public void testGettersAndToStringAndConstructors(){

        for(int i = 0; i < cardsCollection1.length; i++) { //for each card in each of the collection, check cardNumber, suit and toString
            //constructor with enums
            assertTrue(cardsCollection1[i].getCardNumber().equals(""+cardsCollection1Stringified[i].charAt(0)));
            assertTrue(cardsCollection1[i].getCardSuit().equals(""+cardsCollection1Stringified[i].charAt(1)));
            assertTrue(cardsCollection1[i].toString().equals(cardsCollection1Stringified[i]));

            //constructor with strings
            assertTrue(cardsCollection1WithStrings[i].getCardNumber().equals(""+cardsCollection1Stringified[i].charAt(0)));
            assertTrue(cardsCollection1WithStrings[i].getCardSuit().equals(""+cardsCollection1Stringified[i].charAt(1)));
            assertTrue(cardsCollection1WithStrings[i].toString().equals(cardsCollection1Stringified[i]));

            //constructor with chars
            assertTrue(cardsCollection1WithChars[i].getCardNumber().equals(""+cardsCollection1Stringified[i].charAt(0)));
            assertTrue(cardsCollection1WithChars[i].getCardSuit().equals(""+cardsCollection1Stringified[i].charAt(1)));
            assertTrue(cardsCollection1WithChars[i].toString().equals(cardsCollection1Stringified[i]));
        }

    }

    /**
     * Test invalid configs in constructors.
     */
    @Test
    public void testInvalidConfigsInConstructors(){
        NeapolitanCard c;
        //invalid configurations
        try{
            c = new NeapolitanCard("1","P");
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

        try{
            c = new NeapolitanCard("DA","G");
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

        try{
            c = new NeapolitanCard("P","1");
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }
        try{
            c = new NeapolitanCard('1','P');
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

        try{
            c = new NeapolitanCard('D','G');
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

        try{
            c = new NeapolitanCard('P','1');
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

    }

    /**
     * Equal to test. Test to check equal cards are detected correctly. The same for inequal cards.
     */
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

        for(int i = 0; i < cardsCollection1.length; i++){
            for(int j = 0; j < cardsCollection1.length;j++){
                if(i == j)
                    assertTrue(cardsCollection1[i].equalTo(cardsCollection1[j]));
                else
                    assertTrue(!cardsCollection1[i].equalTo(cardsCollection1[j]));
            }
        }
    }
}
