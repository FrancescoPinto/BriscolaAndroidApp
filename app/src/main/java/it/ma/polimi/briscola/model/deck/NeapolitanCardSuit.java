package it.ma.polimi.briscola.model.deck;

/**
 * Created by utente on 19/10/17.
 */

public enum NeapolitanCardSuit {
    BATONS("B"),
    CUPS("C"),
    GOLDS("G"),
    SWORDS("S");
    private String suit;


    NeapolitanCardSuit(String suit) {
        this.suit = suit;
    }

    public String getSuit() {
        return suit;
    }
}
