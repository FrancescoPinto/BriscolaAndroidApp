package it.ma.polimi.briscola.model.deck;

/**
 * Enum containing the Neapolitan Card Number values
 *
 * @author Francesco Pinto
 */
public enum NeapolitanCardNumbers {


    /**
     * Ace neapolitan card number.
     */
    ACE("1"),
    /**
     * Two neapolitan card number.
     */
    TWO("2"),
    /**
     * Three neapolitan card number.
     */
    THREE("3"),
    /**
     * Four neapolitan card number.
     */
    FOUR("4"),
    /**
     * Five neapolitan card number.
     */
    FIVE("5"),
    /**
     * Six neapolitan card number.
     */
    SIX("6"),
    /**
     * Seven neapolitan card number.
     */
    SEVEN("7"),
    /**
     * Jack (eight) neapolitan card number.
     */
    JACK("J"), //fante
    /**
     * Horse (nine) neapolitan card number.
     */
    HORSE("H"),
    /**
     * King (ten) neapolitan card number.
     */
    KING("K");

    private String cardNumber;

    NeapolitanCardNumbers(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Gets number.
     *
     * @return String representation of the number
     */
    public String getNumber() {
        return cardNumber;
    }

    /**
     * Gets NeapolitanCardNumbers enum corresponding to the input String value.
     *
     * @param number String representing the number of the card, its value must be among the cardNumber values in NeapolitanCardNumber enum
     * @return The NeapolitanCardNumbers enum value corresponding to the parameter number
     * @throws IllegalArgumentException If the num isn't among the cardNumber values in NeapolitanCardNumber enum
     */
    public static NeapolitanCardNumbers getCardNumber(String number){
        for(NeapolitanCardNumbers cn : NeapolitanCardNumbers.values())  //scan all the NeapolitanCardNumbers values
            if(cn.getNumber().equals(number)) //if a match is found, return its Enum
                return cn;

        throw new IllegalArgumentException("Invalid Card Number input string "); //if the input string is invalid, notify with an exception
    }
}
