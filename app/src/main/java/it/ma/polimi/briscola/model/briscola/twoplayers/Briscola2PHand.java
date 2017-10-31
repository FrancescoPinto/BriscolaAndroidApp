package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Created by utente on 21/10/17.
 */

public class Briscola2PHand {
    List<NeapolitanCard> hand = new ArrayList<>();

    public Briscola2PHand(List<NeapolitanCard> hand) {
        this.hand = hand;
    }


    public Briscola2PHand(String hand){
        for(int i = 0; i <= hand.length()-1; i = i+2){
            String num = ""+hand.charAt(i);
            String suit = ""+hand.charAt(i+1);
            this.hand.add(new NeapolitanCard(num,suit));

        }
    }

    public List<NeapolitanCard> getHand() {
        return hand;
    }

    public void putCardInHand(NeapolitanCard card) {
        if(hand.size() >= 3)
            throw new IllegalStateException(); //tenta di mettere più di tre carte in mano TODO fare un'eccezione ben strutturata per questo

        hand.add(card);
    }

    //TODO sto pensando di fare così: tu DEFINISCI l'interfaccia' BriscolaHand che ha un numero arbitrario di robe ... e poi aggiungi questi che vincolano ad avere 3 carte ecc. ecc.    così se vuoi modificare il gioco della briscola lo puoi fare facilmente ...


    public NeapolitanCard removeCardFromHand(int i){ //Should pass FIRST, SECOND or THIRD
        NeapolitanCard temp = hand.get(i);
        hand.remove(i);
        return temp;
        //TODO e se non c'è?
    }

    @Override
    public String toString(){
        String temp = "";
        if(hand.isEmpty())
            return temp;

        for(NeapolitanCard c: hand){

            temp += c;
        }
        return temp;
    }

    public boolean containsCard(NeapolitanCard card){
        for(NeapolitanCard c : hand)
            if(c.equalTo(card))
                return true;

        return false;
    }

    public boolean equalTo(Briscola2PHand hand){

        for(int i = 0; i < getHand().size() && i < hand.getHand().size(); i++) {
            NeapolitanCard c1 = ((List<NeapolitanCard>)this.getHand()).get(i);
            NeapolitanCard c2  = ((List<NeapolitanCard>)hand.getHand()).get(i);
            if (!c1.equalTo(c2))
                return false;
        }
        return true;

    }
}
