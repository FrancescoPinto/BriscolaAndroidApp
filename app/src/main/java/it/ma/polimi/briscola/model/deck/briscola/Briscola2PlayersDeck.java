package it.ma.polimi.briscola.model.deck.briscola;

/**
 * Created by utente on 19/10/17.
 */

//TODO QUESTA LA ELIMINEREI ... TUTTE LE COSE "BRISCOLA-DIPENDENTI" LE GESTIREI NELLA LOGICA DI GIOCO, NON QUI! Tipo: prendi la briscola è qualcosa che deve fare il gamemaster (CHE SA DI AVER MESSO LA BRISCOLA ALLA FINE DEL DECK)
public class Briscola2PlayersDeck /*implements Deck*/ {

    //ultima carta è la BRISCOLA, ricordatelo!


    //riceve in ingresso solo la parte della stringa riguardante il deck
   /* public static Briscola2PlayersDeck fromString(String deckString) {
        this.cards = cards;
    }*/

   //inizializza ordinati
  /*  public Briscola2PlayersDeck() {

    }

    //Inizializza da List<Briscola2PlayersCard>


    public Briscola2PlayersDeck(List<Card> cards) {
        this.cards = cards;
    }







    //per "vedere la briscola" ma in realtà il deck NON sa quando è briscola o è una carta normale (prima di scegliere la briscola) quindi sarà il gamemaster a chiamare questo metodo, lui saprà se è la briscola o meno
  /*  public Card getBriscola(){
        if(cards.isEmpty())
            return null;
        return cards.get(cards.size()-1);
    }
    /*

    public

    //metodo per inizializzare il deck dalla stringa di configurazione
    //metodo per shuffle del deck (magari invoca una classe DeckShuffler con tutti i metodi di suffle
    //metodo per fare il pop della carta dal deck (quando pesca)
    //metodo per sapere se c'è ancora qualcosa nel deck (da usare prima di fare il pop)

    @Override
    public String toString(){
        String result = "";
        for(Briscola2PlayersCard c: cards){

        }

    }*/


}
