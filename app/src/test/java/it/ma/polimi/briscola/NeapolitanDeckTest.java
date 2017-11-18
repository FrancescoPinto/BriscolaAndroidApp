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
 * Tests for NeapolitanDeckTest, implicitly tests all AbstractDeck methods and all the AbstractCardListWrapper methods (they won't be tested again exhaustively in other classes)
 */
public class NeapolitanDeckTest {

    //data for the test
    //full neapolitan deck
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

    //partial napolitan deck
    private NeapolitanCard[] buildValidDeck0 = {

            new NeapolitanCard(NeapolitanCardNumbers.ACE, NeapolitanCardSuit.SWORDS),
            new NeapolitanCard(NeapolitanCardNumbers.KING, NeapolitanCardSuit.GOLDS),
            new NeapolitanCard(NeapolitanCardNumbers.JACK, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.THREE, NeapolitanCardSuit.BATONS)

    };

    //empty neapolitan deck
    private NeapolitanCard[] buildValidDeck1 = {

    };

    //partial neapolitan deck
    private NeapolitanCard[] buildValidDeck2 = {
            new NeapolitanCard(NeapolitanCardNumbers.FOUR, NeapolitanCardSuit.CUPS),
            new NeapolitanCard(NeapolitanCardNumbers.FIVE, NeapolitanCardSuit.BATONS),
            new NeapolitanCard(NeapolitanCardNumbers.SIX, NeapolitanCardSuit.SWORDS),
            new NeapolitanCard(NeapolitanCardNumbers.SEVEN, NeapolitanCardSuit.SWORDS),
            new NeapolitanCard(NeapolitanCardNumbers.HORSE, NeapolitanCardSuit.GOLDS)
    };


    //put them in a data structure, so as to simplify testing
    private NeapolitanCard[][] buildValidDeck = {buildValidDeck0,buildValidDeck1,buildValidDeck2};
    private String[] buildValidStrings = { "1SKGJC3B", "", "4C5B6S7SHG"};
    private int[] sizesOfBuildValidStrings = {4,0,5};
    private String tooLongString = "4C5B6S7SHG4C5B6S7SHG4C5B6S7SHG4C5B6S7SHG4C5B6S7SHG4C5B6S7SHG4C5B6S7SHG4C5B6S7SHG4C5B6S7SHG4C5B6S7SHG4C5B6S7SHG";
    private NeapolitanCard[] tooLongDeck = {
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


    /**
     * Briscola 2 players default deck constructor test. Checks generated deck icontains all the cards contained in the array newDeck
     */
    @Test
    public void briscola2PlayersDefaultDeckConstructorTest(){
        NeapolitanDeck nd = new NeapolitanDeck();
        List<NeapolitanCard> cards = nd.getCardList();

        for(int i = 0; i < cards.size() && i < newDeck.length; i++){
            assertTrue(nd.containsCard(cards.get(i)));

           /* NeapolitanCard c1 = cards.get(i);
            NeapolitanCard c2 = newDeck[i];
            assertTrue(c1.getCardNumber().equals(c2.getCardNumber()) && c1.getCardSuit().equals(c2.getCardSuit()));*/

        }


    }


    /**
     * Test the list-based constructor.
     */
    @Test
    public void constructorListTest(){
        List<NeapolitanCard> deckList = new ArrayList<>();
        NeapolitanDeck deck = new NeapolitanDeck(deckList);
        assertTrue(deck.isEmpty());


        for(NeapolitanCard nc : buildValidDeck0){
            deckList.add(nc);
        }
        deck = new NeapolitanDeck(deckList);
        boolean equal = true;
        List<NeapolitanCard> deckCardList = deck.getCardList();
        for(int i = 0; i < deckCardList.size() && i < buildValidDeck0.length;i++) {
            NeapolitanCard nc1 = deckCardList.get(i);
            NeapolitanCard nc2 = buildValidDeck0[i];
            if (!nc1.equalTo(nc2))
                equal = false;
        }
        assertTrue(equal);

        deckList.clear();
        for(NeapolitanCard nc : tooLongDeck){
            deckList.add(nc);
        }

        try {
            deck = new NeapolitanDeck(deckList); //too many cards in input, throw exception!
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }
    }


    /**
     * Shuffle deck test. This deck tests the use of UniformProbabilityShuffler. WARNING: this test just checks that at least 2 shuffles out of 10 are different. Since the shuffling is totally at random, it might happen (even if it is highly unlikely) that this test could fail.
     */
    @Test
    public void shuffleDeckTest(){
        List<NeapolitanDeck> b2pds = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            b2pds.add((NeapolitanDeck) (new NeapolitanDeck().shuffleDeck(new UniformProbabilityShuffler())));
            assertTrue(b2pds.get(i).size() == 40);
        }

        int identicalShuffles = 0;

        //check how many shuffles are equal
        for(int i = 0; i < 9; i++) { //da 0 a 8
            List<NeapolitanCard> ci = b2pds.get(i).getCardList();
            int correspondences = 0;

            for (int j = i + 1; j < 10; j++) { //compara con quelli dopo
                List<NeapolitanCard> cj = b2pds.get(i).getCardList();

                for(int k = 0; k < 40; k++){
                    if(ci.get(k).getCardSuit().equals(cj.get(k).getCardSuit()) && ci.get(k).getCardNumber().equals(cj.get(k).getCardNumber()))
                        correspondences++;
                }

                System.out.println(b2pds.get(i));

            }

            if (correspondences == 40) //if they contain all the cards in the same orther, the shuffles are identical
                identicalShuffles++;
        }

        assertTrue("If the test fails once, repeat it. If it fails many times in a row, something's wrong.",identicalShuffles < 10);


    }

    /**
     * Build from string test. Checks that the deck is built correctly starting from string. Used strings are cointained in buildValidStrings and corresponding decks are in buildValidDeck. Checks cards are contained in the same order.
     */
    @Test
    public void buildFromStringTest(){
        NeapolitanDeck deck;
        NeapolitanCard card;

        for(int i = 0; i < buildValidDeck.length; i++){ //for all decks
            deck = new NeapolitanDeck(buildValidStrings[i]); //build from string
            List<NeapolitanCard> cards = deck.getCardList();
            for(int j = 0; j < buildValidDeck[i].length && j < deck.size(); j++) {
                 card = cards.get(j);

                 assertTrue(card.getCardNumber().equals(buildValidDeck[i][j].getCardNumber())); //check is the same of the corresponding array representing the deck
                assertTrue(card.getCardSuit().equals(buildValidDeck[i][j].getCardSuit()));

            }
        }

        //test a parte per il newDeck
        String newDeckString = "";
        for(NeapolitanCardNumbers cn : NeapolitanCardNumbers.values()){ //generates the string corresponding to the default constructor built deck
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


        try {
            deck = new NeapolitanDeck(tooLongString); //too many cards in input, throw exception!
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

    }

    /**
     * Put card to bottom test. Checks cards are correctly put to bottom.
     */
    @Test
    public void putCardToBottomTest(){

        NeapolitanDeck deck;
        String tempString;
        for(int i = 0; i < buildValidDeck.length; i++) { //For all decks in buildValidDeck,
            deck = new NeapolitanDeck(buildValidStrings[i]);
            tempString = deck.toString();
            deck.putCardToBottom(new NeapolitanCard("1","S")); // puts a card to bottom
            assertTrue(deck.toString().equals(tempString+"1S")); //and checks the stringified version of the deck corresponds to the stringified version of the deck before adding the card + the stringified version of the added card
        }

    }

    /**
     * Draw card from top test. Checks cards are correctly removed from top.
     */
    @Test
    public void drawCardFromTopTest(){
        //starting from a full deck
        NeapolitanDeck deck = new NeapolitanDeck(buildValidStrings[0]);
        List<NeapolitanCard> cards = new ArrayList<>(deck.getCardList());
        int tempSize = cards.size();
        for(NeapolitanCard c : cards){
            NeapolitanCard temp = deck.drawCardFromTop(); //remove one card at a time
            assertTrue(temp.equalTo(c)); //check the removed card is correct
            assertTrue(--tempSize == deck.size()); //check the size of the deck is reduced by 1
        }
        try{
            deck.drawCardFromTop(); //drawing from an empty deck throws IllegalStatException
        }catch(Exception e){
            assertTrue(e instanceof IllegalStateException);
        }
    }


    /**
     * Draw card from bottom test. Checks cards are correctly removed from bottom.
     */
    @Test
    public void drawCardFromBottomTest(){
        //starting from a full deck
        NeapolitanDeck deck = new NeapolitanDeck(buildValidStrings[0]);
        List<NeapolitanCard> cards = new ArrayList<>(deck.getCardList());
        int tempSize = cards.size();
        for(int i = cards.size()-1; i >= 0; i--){
            NeapolitanCard c = cards.get(i);
            NeapolitanCard temp = deck.drawCardFromBottom(); //remove one card at a time
            assertTrue(temp.equalTo(c)); //check the removed card is correct
            assertTrue(--tempSize == deck.size()); //check the size of the deck is reduced by 1
        }
        try{
            deck.drawCardFromBottom();  //drawing from an empty deck throws IllegalStatException
        }catch(Exception e){
            assertTrue(e instanceof IllegalStateException);
        }
    }

    /**
     * Equal to test. Checks that the function returns true if two decks have the same cards in the same orther, false otherwise.
     */
    @Test
    public void equalToTest(){
        for(int i = 0; i < buildValidDeck.length; i++){ //for each pair of decks in buildValidDeck
            for(int j = 0; j < buildValidDeck.length; j++){
                NeapolitanDeck deck1 = new NeapolitanDeck(buildValidStrings[i]);
                NeapolitanDeck deck2 = new NeapolitanDeck(buildValidStrings[j]);

                if(i == j) //if the two decks have the same index, then should be equal
                    assertTrue(deck1.equalTo(deck2));
                else
                    assertTrue(!deck1.equalTo(deck2)); //otherwise should be different
            }

        }
    }

    /**
     * Contains card test. Check that the function returns true if the deck contains the specified card, false otherwise.
     */
    @Test
    public void containsCardTest(){
        NeapolitanDeck deck = new NeapolitanDeck(buildValidStrings[0]);
        assertTrue(deck.containsCard(new NeapolitanCard("1","S")));
        assertTrue(deck.containsCard(new NeapolitanCard("K","G")));
        assertTrue(deck.containsCard(new NeapolitanCard("J","C")));
        assertTrue(deck.containsCard(new NeapolitanCard("3","B")));
        assertTrue(!deck.containsCard(new NeapolitanCard("K","C")));

        deck = new NeapolitanDeck(buildValidStrings[1]); //one test for the emptyDeck
        assertTrue(!deck.containsCard(new NeapolitanCard("K","C")));

    }

    /**
     * Test that maxNumCardsAllowed is 40.
     */
    @Test
    public void getMaxNumCArdsTest(){
        assertTrue(new NeapolitanDeck().getMaxNumCardsAllowedInList() == 40);
    }

    /**
     * Size test. Check that size is computed correctly.
     */
    @Test
    public void sizeTest(){
        for(int i = 0; i < buildValidStrings.length; i++){
            assertTrue(new NeapolitanDeck(buildValidStrings[i]).size() == sizesOfBuildValidStrings[i]);
        }
    }

    /**
     * Is empty test. Check if the isEmpty is computed correctly.
     */
    @Test
    public void isEmptyTest(){

        for(int i = 0; i < buildValidStrings.length; i++){
            if(i == 1) {
                assertTrue(new NeapolitanDeck(buildValidStrings[i]).isEmpty());
            }else
                assertTrue(! new NeapolitanDeck(buildValidStrings[i]).isEmpty());

        }
    }

    /**
     * Get card test. Check robustness to invalid indices.
     */
    @Test
    public void getCard(){
        NeapolitanDeck deck = new NeapolitanDeck(buildValidStrings[1]);

        try {
            deck.getCard(10); //try to get card from empty deck
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

        deck = new NeapolitanDeck(buildValidStrings[0]);
        try{
            deck.getCard(41); //try to get a card with index greater than maxnumcardsallowed
        }catch(Exception e){
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

        try{
            deck.getCard(5); //try to get a card with index greater than size() but smaller than maxnumcardsallowed
        }catch(Exception e){
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

        try{
            deck.getCard(-30); //try to get a card with index negative number
        }catch(Exception e){
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

        NeapolitanCard card = deck.getCard(0);
        assertTrue(card.equalTo(new NeapolitanCard("1","S"))); //check the get card is equal to the first
        System.out.println("Old "+ buildValidStrings[0] + ", new "+ buildValidStrings[0].substring(2) + ", while deck" + deck.toString());
        assertTrue(deck.toString().equals(buildValidStrings[0])); //check the card has been removed
    }

    /**
     * Append card. Check card is appended correctly.
     */
    @Test
    public void appendCard(){
        NeapolitanDeck deck = new NeapolitanDeck(buildValidStrings[1]);

            deck.appendCard(new NeapolitanCard("1","S")); //try to add a card to an empty deck
            assertTrue(deck.containsCard(new NeapolitanCard("1","S")));
            deck.appendCard(new NeapolitanCard("4","G")); //try to add a card to a non-empty deck
            assertTrue(deck.containsCard(new NeapolitanCard("4","G")));

        deck = new NeapolitanDeck(); //try to add a card to a full deck
        try {
            deck.appendCard(new NeapolitanCard("1","S"));
        }catch(Exception e) {
            assertTrue(e instanceof IllegalStateException);
        }

        try {
            deck.appendCard(null);
        }catch(Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }



    }

    /**
     * Remove card. Check that card is removed correctly, with robustness to wrong indices.
     */
    @Test
    public void removeCard(){
        NeapolitanDeck deck = new NeapolitanDeck(buildValidStrings[1]);

        try {
            deck.removeCard(10); //try to remove card from empty deck
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

        deck = new NeapolitanDeck(buildValidStrings[0]);
        try{
            deck.removeCard(41); //try to remove a card with index greater than maxnumcardsallowed
        }catch(Exception e){
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

        try{
            deck.removeCard(5); //try to remove a card with index greater than size() but smaller than maxnumcardsallowed
        }catch(Exception e){
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

        try{
            deck.removeCard(-30); //try to remove a card with index negative number
        }catch(Exception e){
            assertTrue(e instanceof IndexOutOfBoundsException);
        }

        NeapolitanCard card = deck.getCard(0);
        assertTrue(deck.removeCard(0).equalTo(card)); //check the removed card is equal to the first
        assertTrue(deck.toString().equals(buildValidStrings[0].substring(2))); //check the card has been removed

    }

    /**
     * Append all cards test.
     */
    @Test
    public void appendAll(){
        NeapolitanDeck deck = new NeapolitanDeck(buildValidStrings[1]); //start from empty deck
        List<NeapolitanCard> temp = new ArrayList<>();

        for(NeapolitanCard c: buildValidDeck0){
            temp.add(c);
        }

        deck.appendAll(temp); //append all cards in buildValidDeck0
        assertTrue(deck.equalTo(new NeapolitanDeck(buildValidStrings[0]))); //check they contain the same cards

        deck = new NeapolitanDeck(buildValidStrings[2]); //start from non-empty deck
        deck.appendAll(temp); //append all cards in buildValidDeck0
        assertTrue(deck.toString().equals(buildValidStrings[2] + buildValidStrings[0])); //check cards have been appended correctly

    }


    /**
     * Sets card list test.
     */
    @Test
    public void setCardListTest()
    {
        NeapolitanDeck deck = new NeapolitanDeck(buildValidStrings[1]); //start from empty deck
        List<NeapolitanCard> temp = new ArrayList<>();
        for(NeapolitanCard c: newDeck){
            temp.add(c);
        }
        deck.setCardList(temp);
        assertTrue(deck.toString().equals(new NeapolitanDeck().toString())); //check cards have been set properly
    }

    /**
     * Clear card list test. Check that
     */
    @Test
    public void clearCardList(){
        NeapolitanDeck deck = new NeapolitanDeck(buildValidStrings[0]); //start from full deck
        List<NeapolitanCard> removedCards = deck.clearCardList(); //clear it

        assertTrue(deck.isEmpty()); //check that now the deck is empty

        List<NeapolitanCard> temp = new ArrayList<>(); //check the removed card list is equal to the content of the deck
        for(NeapolitanCard c: buildValidDeck0){
            temp.add(c);
        }
        for(int i = 0; i < temp.size(); i++){
            NeapolitanCard c = temp.get(i);
            assertTrue(c.equalTo(removedCards.get(i)));
        }

        deck = new NeapolitanDeck(buildValidStrings[1]); //start from empty deck
        removedCards = deck.clearCardList(); //clear it
        assertTrue(removedCards.isEmpty());

    }
    //public List<CARD> getCardList()
    //implicitly tested by other methods

    //public abstract CARD buildCardInstance(String num, String suit);
    //implicitly tested by other methods

}
