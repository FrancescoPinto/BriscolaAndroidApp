package it.ma.polimi.briscola.model.briscola;

import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;

/**
 * Created by utente on 19/10/17.
 */

public enum BriscolaCardRanking {

    ACE(NeapolitanCardNumbers.ACE.getNumber(),11,1),
    TWO(NeapolitanCardNumbers.TWO.getNumber(),0,10),
    THREE(NeapolitanCardNumbers.THREE.getNumber(),10,2),
    FOUR(NeapolitanCardNumbers.FOUR.getNumber(),0,9),
    FIVE(NeapolitanCardNumbers.FIVE.getNumber(),0,8),
    SIX(NeapolitanCardNumbers.SIX.getNumber(),0,7),
    SEVEN(NeapolitanCardNumbers.SEVEN.getNumber(),0,6),
    JACK(NeapolitanCardNumbers.JACK.getNumber(),2,5), //fante
    HORSE(NeapolitanCardNumbers.HORSE.getNumber(),3,4),
    KING(NeapolitanCardNumbers.KING.getNumber(),4,3);

    private String cardNumber;
    private int pointValue, rank;

    BriscolaCardRanking(String cardNumber, int pointValue, int rank) {
        this.cardNumber = cardNumber;
        this.pointValue = pointValue;
        this.rank = rank;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getPointValue() {
        return pointValue;
    }

    public static int getPointValue(String i) {
        for(BriscolaCardRanking bcr : BriscolaCardRanking.values())
            if(bcr.getCardNumber().equals(i))
                return bcr.getPointValue();

        return -1;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public int getRank() {
        return rank;
    }

    public static int getRank(String i) {
        for(BriscolaCardRanking bcr : BriscolaCardRanking.values())
            if(bcr.getCardNumber().equals(i))
                return bcr.getRank();

        return -1;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
