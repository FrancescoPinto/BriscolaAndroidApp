package it.ma.polimi.briscola.model.deck;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class implementing a Uniform Probability Shuffling algorithm using the Durstenfeld-Knuth Shuffling Algorithm
 *
 * @author Francesco Pinto
 */
public class UniformProbabilityShuffler implements Shuffler{



    @Override
    public Deck shuffleDeck(Deck deck){
        int randomNum;
        List<Card> cards = (List<Card>) deck.getCards();

        for(int i = 1; i < cards.size();i++) {
            randomNum = ThreadLocalRandom.current().nextInt(0, cards.size()); //Choose a card at random
            //Swap the card chosen at random and the i-th card
            Card tempCard =  cards.get(randomNum);
            cards.set(randomNum, cards.get(i));
            cards.set(i,tempCard);
        }

        deck.setCards(cards); //update the deck with the shuffled cards
        return deck; //convenience
    }

}
