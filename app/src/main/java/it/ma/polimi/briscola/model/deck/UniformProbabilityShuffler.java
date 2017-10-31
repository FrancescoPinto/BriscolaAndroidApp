package it.ma.polimi.briscola.model.deck;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by utente on 21/10/17.
 */

public class UniformProbabilityShuffler {


    //Durstenfeld-Knuth Shuffle, uniform probability of each configuration
    public NeapolitanDeck shuffleDeck(NeapolitanDeck deck){
        int randomNum;
        List<Card> cards = (List<Card>) deck.getCards();

        for(int i = 1; i < cards.size();i++) {
            randomNum = ThreadLocalRandom.current().nextInt(0, cards.size());
            //Swap cards
            Card tempCard =  cards.get(randomNum);
            cards.set(randomNum, cards.get(i));
            cards.set(i,tempCard);
        }

        deck.setCards(cards);
        return deck;
    }

}
