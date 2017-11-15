package it.ma.polimi.briscola.model.deck;

/**
 * Class representing a Neapolitan Card, implementing the interface Card
 *
 * @author Francesco Pinto
 */
public class NeapolitanCard implements Card{

    private NeapolitanCardNumbers neapolitanCardNumber;

    private NeapolitanCardSuit neapolitanCardSuit;


    /**
     * Instantiates a new Neapolitan card from enum values of NeapolitanCardNumber and NeapolitanCardSuit
     *
     * @param neapolitanCardNumber The NeapolitanCardNumber enum value
     * @param neapolitanCardSuit   The NeapolitanCardSuit enum value
     */
    public NeapolitanCard(NeapolitanCardNumbers neapolitanCardNumber, NeapolitanCardSuit neapolitanCardSuit) {
        this.neapolitanCardNumber = neapolitanCardNumber;
        this.neapolitanCardSuit = neapolitanCardSuit;
    }


    /**
     * Instantiates a new Neapolitan card from String values representing the card number and suit
     *
     * @param num  String representing the number of the card, its value must be among the cardNumber values in NeapolitanCardNumber enum
     * @param suit String representing the suit of the card, its value must be among the cardSuit values in NeapolitanCardSuit enum
     * @see NeapolitanCardNumbers
     * @see NeapolitanCardSuit
     */
    public NeapolitanCard(String num, String suit){
        this.neapolitanCardNumber = NeapolitanCardNumbers.getCardNumber(num);
        this.neapolitanCardSuit = NeapolitanCardSuit.getCardSuit(suit);

    }

    /**
     * Convenience constructor, instantiates a new Neapolitan card from char values representing the card number and suit
     *
     * @param num  char representing the number of the card, its stringified value must be among the cardNumber values in NeapolitanCardNumber enum
     * @param suit char representing the suit of the card, its stringified value must be among the cardSuit values in NeapolitanCardSuit enum
     * @see NeapolitanCardNumbers
     * @see NeapolitanCardSuit
     */
    public NeapolitanCard(char num, char suit){
        this(""+num,""+suit);
    }


    /**
     * Returns a String representing the Card number
     *
     * @return String representing the Card number, its value is among the cardNumber values in NeapolitanCardNumber enum
     */
    @Override
    public String getCardNumber() {
        return neapolitanCardNumber.getNumber();
    }

    /**
     * Returns a String representing the Card suit
     *
     * @return String representing the Card suit, its value is among the cardSuit values in NeapolitanCardSuit enum
     */
    @Override
    public String getCardSuit() {
        return neapolitanCardSuit.getSuit();
    }

    /**
     * Returns a String representing the Card.
     *
     * @return String representing the Card, format is the concatenation of a String representing the CardNumber and a String representing the CardSuit, with the same format produced by getCardNumber() and getCardSuit()
     */
    @Override
    public String toString(){
        return ""+neapolitanCardNumber.getNumber() + neapolitanCardSuit.getSuit();
    }


    /**
     * Returns a Boolean representing whether the Card is equal to the argument
     *
     * @param nc Instance of NeapolitanCard to be used for the comparison
     * @return True if nc has the same type, suit and number, false otherwise
     */
    @Override
    public boolean equalTo(Card nc){
        return (nc instanceof NeapolitanCard) && nc.getCardSuit().equals(neapolitanCardSuit.getSuit()) && nc.getCardNumber().equals(neapolitanCardNumber.getNumber());
    }



}
