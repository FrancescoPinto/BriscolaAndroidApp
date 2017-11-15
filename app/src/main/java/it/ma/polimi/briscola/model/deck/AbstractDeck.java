package it.ma.polimi.briscola.model.deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class implementing some operations whose implementations are common to any deck. Extends AbstractCardListWrapper, thus inheriting the convenience methods implemented in AbstractCardListWrapper
 *
 * @param <CARD> the type parameter
 * @author Francesco Pinto
 */
public abstract class AbstractDeck<CARD extends Card> extends AbstractCardListWrapper<CARD> implements Deck<CARD>{

    /**
     * The Deck is empty.
     */
    String deckIsEmpty = "Deck is empty. Operation not permitted.";


    /**
     * Default constructor, only implemented to allow descendants to have their own default constructor
     */
    public AbstractDeck(){
        super();
    }


    /**
     * Instantiates a new Abstract deck containing the list of cards represented by the string passed as an argument. The string is made of string representations of cards. Left-most card is the topmost.
     *
     * @param list The string representing the list of cards contained in the deck
     */
    public AbstractDeck(String list){
        super(list);
    }

    /**
     * Instantiates a new Abstract deck from a list of cards. The lowest index represents the topmost card.
     *
     * @param cards The list of cards contained in the deck.
     */
    public AbstractDeck(List<CARD> cards){
        super(cards);
    }

    @Override
    public AbstractDeck<CARD> shuffleDeck(Shuffler shuffler){
        return (AbstractDeck<CARD>) shuffler.shuffleDeck(this);
    }


    @Override
    public CARD drawCardFromTop(){
        if(isEmpty())
            throw new IllegalStateException(deckIsEmpty);
        CARD temp = getCard(0); //lowest index is the topmost card
        removeCard(0);
        return temp;
    }

    @Override
    //pesca una carta dalla cima del deck
    public CARD drawCardFromBottom(){
        if(isEmpty())
            throw new IllegalStateException(deckIsEmpty);
        CARD temp = getCard(size()-1); //greatest index is the bottom card
        removeCard(size()-1);
        return temp;
    }

    @Override
    public void putCardToBottom(Card card){
        appendCard((CARD) card);
    }

}
