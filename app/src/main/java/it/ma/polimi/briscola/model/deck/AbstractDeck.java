package it.ma.polimi.briscola.model.deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class implementing some operations whose implementations are common to any deck.
 *
 * @param <CARD> the type parameter
 * @author Francesco Pinto
 */
public abstract class AbstractDeck<CARD extends Card> implements Deck{
    private List<CARD> cards = new ArrayList<>();
    String deckIsEmpty = "Deck is empty. Operation not permitted.";

    /**
     * Abstract method used by AbstractDeck to get an instance of the generic type CARD based on two Strings representing the number and the suit (respectively) of the desired card
     *
     * @param num  String representing the number of the card, its value must be among the cardNumber values in the CARD associated CardNumber enum (name convention CARDCardNumber, e.g. NeapolitanCardNumber)
     * @param suit  String representing the suit of the card, its value must be among the cardSuit values in the CARD associated CardSuit enum (name convention CARDCardSuit, e.g. NeapolitanCardSuit)
     * @return A CARD object having the desired number and suit
     */
    public abstract CARD buildCardInstance(String num, String suit);

    /**
     * Default constructor, only implemented to allow descendants to have their own default constructor
     */
    public AbstractDeck(){

    }

    /**
     * Constructor that builds a deck containing the cards contained in the List of CARD objects passed as a parameter
     *
     * @param cards The cards that should be contained in the new deck
     */
    public AbstractDeck(List<CARD> cards) {
        this.cards = cards;
    }


    /**
     * Constructor that builds a deck starting from a String, containing String representations of cards (represented by their number and suit)
     *
     * @param deck String whose values represent a sequence of number/suit of the cards that should  be contained in the new deck. The topmost card is the leftmost in the input string.
     */
//riceve in ingresso solo la parte della stringa riguardante il deck
    public AbstractDeck(String deck){
        List<CARD> temp = new ArrayList<>();

        for(int i = 0; i <= deck.length()-1; i = i+2){
            String num = ""+deck.charAt(i);
            String suit = ""+deck.charAt(i+1);
            temp.add(this.buildCardInstance(num,suit));

        }
        //@TODO q>irrobustire controllando che la stringa sia valida, ovvero: è pari e ogni coppia è una carta (questo già lo controlli), le carte sono tutte diverse (NON puoi forzare che ci siano tutte le carte perché la configurazione da cui ricostruisci potrebbe non essere con deck pieno, PERO' SE SONO 40 CARTE ALLORA DEVONOE ESSERE TUTTE)

        this.cards = temp;
    }



    @Override
    public String toString(){
        String temp = "";
        for(CARD c: cards){
            temp += c;
        }
        return temp;
    }

    @Override
    public boolean isEmpty(){
        return cards.isEmpty();
    }

    @Override
    public int getCurrentDeckSize(){
        return cards.size();
    }

    @Override
    public boolean containsCard(Card card){
        for(CARD c : cards)
            if(c.equalTo(card))
                return true;

        return false;
    }

    @Override
    public AbstractDeck<CARD> shuffleDeck(Shuffler shuffler){
        return (AbstractDeck<CARD>) shuffler.shuffleDeck(this);
    }

    @Override
    public List<CARD> getCards() {
        return cards;
    }

    @Override
    public void setCards(List<? extends Card> cards){
        this.cards = (List<CARD>) cards;
    }

    @Override
    public CARD drawCardFromTop(){
        if(cards.isEmpty())
            throw new IllegalStateException(deckIsEmpty);
        CARD temp = cards.get(0);
        cards.remove(0);
        return temp;
    }

    @Override
    //pesca una carta dalla cima del deck
    public CARD drawCardFromBottom(){
        if(cards.isEmpty())
            throw new IllegalStateException(deckIsEmpty);
        CARD temp = cards.get(cards.size()-1);
        cards.remove(cards.size()-1);
        return temp;
    }

    @Override
    public boolean equalTo(Deck deck){
        if(cards.isEmpty() && deck.getCurrentDeckSize() == 0)
            return true;
        else if(cards.isEmpty() && !(deck.getCurrentDeckSize() == 0))
            return false;
        else if(!cards.isEmpty() && (deck.getCurrentDeckSize() == 0))
            return false;
        else {

            for (int i = 0; i < getCards().size() && i < deck.getCards().size(); i++) {
                Card c1 = this.getCards().get(i);
                Card c2 = deck.getCards().get(i);
                if (!c1.equalTo(c2))
                    return false;
            }
            return true;
        }
    }

    @Override
    public void putCardToBottom(Card card){
        cards.add((CARD) card);
    }

}
