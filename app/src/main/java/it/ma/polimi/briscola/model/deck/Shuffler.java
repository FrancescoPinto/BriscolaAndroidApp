package it.ma.polimi.briscola.model.deck;

/**
 * Interface of a Shuffler, created in order to easily allow the implementation of different shuffling algorithms (both fair and unfair; unfair algorithms may be used in order to increase the match difficulty for the human player!)
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
