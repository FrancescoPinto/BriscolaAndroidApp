package it.ma.polimi.briscola.model.deck;

import java.util.List;

/**
 * Created by utente on 20/10/17.
 */

public interface Deck  { //TODO vedi se puoi usare T invece che Card, mettendo qui a sinistra (prima della {) <T extends Card> così da ottenere correttamente il fatto che non usi mai NeapolitanCard in modo esplicito da certe parti

    public List<? extends Card> getCards();
    public void setCards(List<? extends Card> cards);
    public Deck shuffleDeck(Shuffler shuffler);
    public Card drawCardFromTop();
    public boolean isEmpty();
    public int getCurrentDeckSize();
    public void putCardToBottom(Card card);
    public Card drawCardFromBottom();

}
