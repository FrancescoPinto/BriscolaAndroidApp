package it.ma.polimi.briscola.model.deck;

/**
 * Created by utente on 20/10/17.
 */

public enum NeapolitanCardNumbers {


    ACE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    JACK("J"), //fante
    HORSE("H"),
    KING("K");

    private String cardNumber;

    NeapolitanCardNumbers(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getNumber() {
        return cardNumber;
    }
}
