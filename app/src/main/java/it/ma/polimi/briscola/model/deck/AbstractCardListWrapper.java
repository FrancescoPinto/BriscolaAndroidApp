package it.ma.polimi.briscola.model.deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by utente on 10/11/17.
 */

public abstract class AbstractCardListWrapper <CARD extends Card> {

    public AbstractCardListWrapper(String list){
        List<CARD> temp = new ArrayList<>();

        for(int i = 0; i <= list.length()-1; i = i+2){
            String num = ""+list.charAt(i);
            String suit = ""+list.charAt(i+1);
            temp.add(this.buildCardInstance(num,suit));

        }
        //@TODO q>irrobustire controllando che la stringa sia valida, ovvero: è pari e ogni coppia è una carta (questo già lo controlli), le carte sono tutte diverse (NON puoi forzare che ci siano tutte le carte perché la configurazione da cui ricostruisci potrebbe non essere con deck pieno, PERO' SE SONO 40 CARTE ALLORA DEVONOE ESSERE TUTTE)

        this.setCardList(temp);
    }

    public AbstractCardListWrapper(List<CARD> cards){
        setCardList(cards);
    }

    public boolean equalTo(AbstractCardListWrapper clw){
        if(this.getCardList().isEmpty() && clw.getCardList().size() == 0)
            return true;
        else if(this.getCardList().isEmpty() && !(clw.getCardList().size() == 0))
            return false;
        else if(!this.getCardList().isEmpty() && (clw.getCardList().size() == 0))
            return false;
        else {
            for (int i = 0; i < getCardList().size() && i < clw.getCardList().size(); i++) {
                CARD c1 = this.getCardList().get(i);
                CARD c2 = (CARD) clw.getCardList().get(i);
                if (!c1.equalTo(c2))
                    return false;
            }
            return true;

        }
    }

    public abstract List<CARD> getCardList();
    public abstract void setCardList(List<CARD> cards);
    public abstract CARD buildCardInstance(String num, String suit);


    @Override
    public String toString(){
        String temp = "";
        if(getCardList().isEmpty())
            return temp;

        for(CARD c: getCardList()){

            temp += c;
        }
        return temp;
    }

    public int size(){
        return getCardList().size();
    }


    public boolean isEmpty(){
        return getCardList().isEmpty();
    }


    public boolean containsCard(Card card){
        for(CARD c : getCardList())
            if(c.equalTo(card))
                return true;

        return false;
    }

    public CARD getCard(int i){
        return getCardList().get(i);
    } //todo check dei boundaries

    public void appendCard(CARD card){
        getCardList().add(card);
    }

    public void removeCard(int i){
        getCardList().remove(i);
    }



}
