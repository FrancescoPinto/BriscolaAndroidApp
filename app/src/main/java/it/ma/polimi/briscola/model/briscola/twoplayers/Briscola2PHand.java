package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Class representing the hand of a Briscola player in a 2 players match
 *
 * @author Francesco Pinto
 */
public class Briscola2PHand {
    /**
     * The list of cards contained in the hand.
     */
    private List<NeapolitanCard> hand = new ArrayList<>();
    private static final int maxNumAllowedCards = 3;
    private static final String maxNumAllowedCardsErrorMessage = "The hand must contain "+maxNumAllowedCards + " cards at most";
    private static final String indexOutOfBounds = "The index of the card to be removed must be between "+0+" and ";

    /**
     * Instantiates a new Briscola player hand in a 2 players match based on the list of cards passed as a parameter
     *
     * @param hand The list of cards contained in the hand. Must contain a number of cards smaller than maxNumAllowedCards
     * @throws IllegalArgumentException If the number of cards contained in hand is greater than the maxNumAllowedCards
     */
    public Briscola2PHand(List<NeapolitanCard> hand) {
        if(hand.size() > maxNumAllowedCards)
            throw new IllegalArgumentException(maxNumAllowedCardsErrorMessage);

        this.hand = hand;
    }


    /**
     * Instantiates a new Briscola player hand in a 2 players match based on the String passed as a parameter
     *
     * @param hand String representing the list of cards contained in the hand. Must contain a number of cards smaller than maxNumAllowedCards
     * @throws IllegalArgumentException If the number of cards contained in hand is greater than the maxNumAllowedCards
     */
    public Briscola2PHand(String hand){

        if(hand.length() > maxNumAllowedCards*2)
            throw new IllegalArgumentException(maxNumAllowedCardsErrorMessage);

        for(int i = 0; i <= hand.length()-1; i = i+2){
            String num = ""+hand.charAt(i);
            String suit = ""+hand.charAt(i+1);
            this.hand.add(new NeapolitanCard(num,suit));

        }
    }

    /**
     * Gets the list of Neapolitan Cards contained in the hand
     *
     * @return The hand
     */
    public List<NeapolitanCard> getHand() {
        return hand;
    }

    /**
     * Put card in hand. The new card is appended.
     *
     * @param card The card to be added to the hand
     * @throws IllegalStateException If the number of cards contained after the addition of the new card would be is greater than the maxNumAllowedCards
     */
    public void putCardInHand(NeapolitanCard card) {
        if(hand.size() >= maxNumAllowedCards)
            throw new IllegalStateException(maxNumAllowedCardsErrorMessage); //tenta di mettere più di tre carte in mano

        hand.add(card);
    }

    //TODO sto pensando di fare così: tu DEFINISCI l'interfaccia' BriscolaHand che ha un numero arbitrario di robe ... e poi aggiungi questi che vincolano ad avere 3 carte ecc. ecc.    così se vuoi modificare il gioco della briscola lo puoi fare facilmente ...


    /**
     * Remove the specified card from hand, return it
     *
     * @param i The index of the card to be removed from the hand. Should be specified using the public static int FIRST, SECOND or THIRD provided by the class
     * @return The removed card
     */
    public NeapolitanCard removeCardFromHand(int i){ //Should pass FIRST, SECOND or THIRD
        if(i >= hand.size() || i < 0)
            throw new IllegalArgumentException(indexOutOfBounds + (hand.size() - 1));

        NeapolitanCard temp = hand.get(i);
        hand.remove(i);
        return temp;
        //TODO e se non c'è? -> index out of bounds, previenilo o lancialo tu esplicitamente!
    }

    @Override
    public String toString(){
        String temp = "";
        if(hand.isEmpty())
            return temp;

        for(NeapolitanCard c: hand){

            temp += c;
        }
        return temp;
    }

    /**
     * Check whether the hand contains a specified card.
     *
     * @param card The card one wants to check the presence in the hand
     * @return True if the two cards have the same class, suit and number, false otherwise
     */
    public boolean containsCard(NeapolitanCard card){
        for(NeapolitanCard c : hand)
            if(c.equalTo(card))
                return true;

        return false;
    }

    /**
     * Check whether two hands contain the same cards (in the same order)
     *
     * @param hand The hand that needs to be used for the comparison
     * @return True if the two hands contain the same cards in the same order, false otherwise
     */
    public boolean equalTo(Briscola2PHand hand){
        if(this.hand.isEmpty() && hand.getHand().size() == 0)
            return true;
        else if(this.hand.isEmpty() && !(hand.getHand().size() == 0))
            return false;
        else if(!this.hand.isEmpty() && (hand.getHand().size() == 0))
            return false;
        else {
            for (int i = 0; i < getHand().size() && i < hand.getHand().size(); i++) {
                NeapolitanCard c1 = (this.getHand()).get(i);
                NeapolitanCard c2 = (hand.getHand()).get(i);
                if (!c1.equalTo(c2))
                    return false;
            }
            return true;
        }

    }

    /**
     * Get the number of cards in hand
     *
     * @return The number of cards in hand
     */
    public int getSize(){
        return hand.size();
    }
}
