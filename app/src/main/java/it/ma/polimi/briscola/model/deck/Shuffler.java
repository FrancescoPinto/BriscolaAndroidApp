package it.ma.polimi.briscola.model.deck;

/**
 * Interface of a Shuffler, intended so that it is possible to specify and use both fair and unfair shuffling algorithms easily
 *
 * @author Francesco Pinto
 */
public interface Shuffler {

    /**
     * Given an input Deck, shuffle it and return it. Implement this method with the desired shuffling algorithm
     *
     * @param deck The Deck to be shuffled
     * @return The shuffled Deck
     */
    public Deck shuffleDeck(Deck deck);
}
