package it.ma.polimi.briscola.model.briscola;

import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;


/**
 * Enum representing the point values and ranking rules of a typical Briscola match.
 *
 * @author Francesco Pinto
 */
public enum BriscolaCardPointsAndRankingRules {

    /**
     * Ace briscola card points and ranking rules.
     */
    ACE(NeapolitanCardNumbers.ACE.getNumber(),11,1), //(number, points, rank)
    /**
     * Two briscola card points and ranking rules.
     */
    TWO(NeapolitanCardNumbers.TWO.getNumber(),0,10),
    /**
     * Three briscola card points and ranking rules.
     */
    THREE(NeapolitanCardNumbers.THREE.getNumber(),10,2),
    /**
     * Four briscola card points and ranking rules.
     */
    FOUR(NeapolitanCardNumbers.FOUR.getNumber(),0,9),
    /**
     * Five briscola card points and ranking rules.
     */
    FIVE(NeapolitanCardNumbers.FIVE.getNumber(),0,8),
    /**
     * Six briscola card points and ranking rules.
     */
    SIX(NeapolitanCardNumbers.SIX.getNumber(),0,7),
    /**
     * Seven briscola card points and ranking rules.
     */
    SEVEN(NeapolitanCardNumbers.SEVEN.getNumber(),0,6),
    /**
     * Jack briscola card points and ranking rules.
     */
    JACK(NeapolitanCardNumbers.JACK.getNumber(),2,5), //fante
    /**
     * Horse briscola card points and ranking rules.
     */
    HORSE(NeapolitanCardNumbers.HORSE.getNumber(),3,4),
    /**
     * King briscola card points and ranking rules.
     */
    KING(NeapolitanCardNumbers.KING.getNumber(),4,3);

    private String cardNumber;
    private int pointValue, rank;
    private static final String invalidCardNumberError = "Invalid cardNumber. No rule associated to that cardNumber exists";

    BriscolaCardPointsAndRankingRules(String cardNumber, int pointValue, int rank) {
        this.cardNumber = cardNumber;
        this.pointValue = pointValue;
        this.rank = rank;
    }

    /**
     * Gets card number.
     *
     * @return the card number
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Sets card number.
     *
     * @param cardNumber the card number
     */
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Gets point value.
     *
     * @return the point value
     */
    public int getPointValue() {
        return pointValue;
    }

    /**
     * Gets point value based on the cardNumber passed as an argument
     *
     * @param cardNumber String representing the cardNumber
     * @return The point value if the cardNumber is valid (i.e. equal to one of the cardNumber values of the BriscolaCardPoitnsAndRankingRules enum)
     * @throws IllegalArgumentException If the cardNumber is invalid
     */
    public static int getPointValue(String cardNumber) {
        for(BriscolaCardPointsAndRankingRules bcr : BriscolaCardPointsAndRankingRules.values())
            if(bcr.getCardNumber().equals(cardNumber))
                return bcr.getPointValue();

        throw new IllegalArgumentException(invalidCardNumberError);
    }

    /**
     * Sets point value.
     *
     * @param pointValue the point value
     */
    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    /**
     * Gets rank.
     *
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * Gets rank.
     *
     * @param cardNumber String representing the cardNumber
     * @return The rank if the cardNumber is valid (i.e. equal to one of the cardNumber values of the BriscolaCardPoitnsAndRankingRules enum)
     * @throws IllegalArgumentException If the cardNumber is invalid
     */
    public static int getRank(String cardNumber) {
        for(BriscolaCardPointsAndRankingRules bcr : BriscolaCardPointsAndRankingRules.values())
            if(bcr.getCardNumber().equals(cardNumber))
                return bcr.getRank();

        throw new IllegalArgumentException(invalidCardNumberError);
    }

    /**
     * Sets rank.
     *
     * @param rank the rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }
}
