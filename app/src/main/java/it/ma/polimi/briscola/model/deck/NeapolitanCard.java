package it.ma.polimi.briscola.model.deck;

/**
 * Created by utente on 21/10/17.
 */

public class NeapolitanCard implements Card{

    private final NeapolitanCardNumbers napolitanCardNumber;

    private final NeapolitanCardSuit neapolitanCardSuit;

    public NeapolitanCard(NeapolitanCardNumbers napolitanCardNumber, NeapolitanCardSuit neapolitanCardSuit) {
        this.napolitanCardNumber = napolitanCardNumber;
        this.neapolitanCardSuit = neapolitanCardSuit;
    }

    public NeapolitanCard(String num, String suit){
        NeapolitanCardNumbers tempCn = null;
        NeapolitanCardSuit tempCs = null;
        for(NeapolitanCardNumbers cn : NeapolitanCardNumbers.values())
            for(NeapolitanCardSuit cs: NeapolitanCardSuit.values()){
                if(cs.getSuit().equals(suit) && cn.getNumber().equals(num)) {
                    tempCn = cn;
                    tempCs = cs;
                }
            }

        if(tempCn == null || tempCs == null)
            throw new IllegalArgumentException();
        this.napolitanCardNumber = tempCn;
        this.neapolitanCardSuit = tempCs;
    }

    public NeapolitanCard(char num, char suit){
        this(""+num,""+suit);
    }


    @Override
    public String getCardNumber() {
        return napolitanCardNumber.getNumber();
    }

    @Override
    public String getCardSuit() {
        return neapolitanCardSuit.getSuit();
    }

    @Override
    public String toString(){
        return ""+napolitanCardNumber.getNumber() + neapolitanCardSuit.getSuit();
    }

    public boolean equalTo(NeapolitanCard nc){
        return nc.getCardSuit().equals(neapolitanCardSuit.getSuit()) && nc.getCardNumber().equals(napolitanCardNumber.getNumber());
    }
}
