package it.ma.polimi.briscola.model.deck;

/**
 * Enum containing the Neapolitan Card Suit values
 *
 * @author Francesco Pinto
 */

public enum NeapolitanCardSuit {
    /**
     * Batons neapolitan card suit.
     */
    BATONS("B"),
    /**
     * Cups neapolitan card suit.
     */
    CUPS("C"),
    /**
     * Golds neapolitan card suit.
     */
    GOLDS("G"),
    /**
     * Swords neapolitan card suit.
     */
    SWORDS("S");
    private String cardSuit;


    NeapolitanCardSuit(String suit) {
        this.cardSuit = suit;
    }

    /**
     * Gets suit.
     *
     * @return the suit
     */
    public String getSuit() {
        return cardSuit;
    }

    /**
     * Gets NeapolitanCardSuit enum corresponding to the input String value.
     *
     * @param suit  String representing the suit of the card, its value must be among the cardSuit values in NeapolitanCardSuit enum
     * @return The NeapolitanCardSuit enum value corresponding to the parameter suit
     * @throws IllegalArgumentException If the suit isn't among the cardSuit values in NeapolitanCardSuit enums
     */
    public static NeapolitanCardSuit getCardSuit(String suit){
        for(NeapolitanCardSuit cs : NeapolitanCardSuit.values()) //scan all the NeapolitanCardSuit values
            if(cs.getSuit().equals(suit)) //if a match is found, return its Enum
                return cs;
        throw new IllegalArgumentException("Invalid Card Suit input string "); //if the input string is invalid, notify with an exception
    }
}
