package it.ma.polimi.briscola.model.deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by utente on 21/10/17.
 */

public class NeapolitanDeck {
    List<NeapolitanCard> cards = new ArrayList<>();

    public NeapolitanDeck(){
        NeapolitanCardNumbers[] numberValues = NeapolitanCardNumbers.values();
        NeapolitanCardSuit[] suitValues = NeapolitanCardSuit.values();

        for(NeapolitanCardNumbers cn : numberValues){
            for(NeapolitanCardSuit cs : suitValues){
                this.cards.add(new NeapolitanCard(cn,cs));
            }
        }
    }

    public NeapolitanDeck(List<NeapolitanCard> cards) {
        this.cards = cards;
    }


    public NeapolitanDeck shuffleDeck(UniformProbabilityShuffler shuffler){
        return shuffler.shuffleDeck(this);
    }

    public List<? extends Card> getCards() {
        return cards;
    }

    public  void setCards(List<? extends Card> cards) {
        this.cards = (List<NeapolitanCard>) cards;
    }

    public Card drawCardFromTop(){
        if(cards.isEmpty())
            return null;
        Card temp = cards.get(0);
        cards.remove(0);
        return temp;
    }

     //pesca una carta dalla cima del deck
    public Card drawCardFromBottom(){
        if(cards.isEmpty())
            return null;
        Card temp = cards.get(cards.size()-1);
        cards.remove(cards.size()-1);
        return temp;
    }

    //riceve in ingresso solo la parte della stringa riguardante il deck
    public NeapolitanDeck(String deck){
        List<NeapolitanCard> temp = new ArrayList<>();

        for(int i = 0; i <= deck.length()-1; i = i+2){
            String num = ""+deck.charAt(i);
            String suit = ""+deck.charAt(i+1);
            temp.add(new NeapolitanCard(num,suit));

        }
        //@TODO irrobustire controllando che la stringa sia valida, ovvero: è pari e ogni coppia è una carta, le carte sono tutte diverse (NON puoi forzare che ci siano tutte le carte perché la configurazione da cui ricostruisci potrebbe non essere con deck pieno, PERO' SE SONO 40 CARTE ALLORA DEVONOE ESSERE TUTTE)

        this.cards = temp;
    }

    public boolean equalTo(NeapolitanDeck deck){
        for(int i = 0; i < getCards().size() && i < deck.getCards().size(); i++) {
            NeapolitanCard c1 = ((List<NeapolitanCard>)this.getCards()).get(i);
            NeapolitanCard c2  = ((List<NeapolitanCard>)deck.getCards()).get(i);
            if (!c1.equalTo(c2))
               return false;
        }
        return true;
    }
    @Override
    public String toString(){
        String temp = "";
        for(NeapolitanCard c: cards){
            temp += c;
        }
        return temp;
    }


    public boolean isEmpty(){
        return cards.isEmpty();
    }


    public int getCurrentDeckSize(){
        return cards.size();
    }


    public boolean containsCard(NeapolitanCard card){
        for(NeapolitanCard c : cards)
            if(c.equalTo(card))
                return true;

        return false;
    }
    public void putCardToBottom(Card card){
        cards.add((NeapolitanCard) card);
    }
}
