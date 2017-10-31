package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Class representing the Surface during a Briscola-2-Players match.
 * @author Francesco Pinto
 */

public class Briscola2PSurface {
    private List<NeapolitanCard> surface = new ArrayList<NeapolitanCard>();
    private static int maxCardsOnSurface = 2;

    private static final String wrongConstructorArgumentErrorMessage= "The surface can contain a maximum of "+maxCardsOnSurface + "cards. The number of cards in the argument of the constructor should be between 0 and "+maxCardsOnSurface;
    private static final String alreadyFilledSurfaceErrorMessage = "The surface already contains "+maxCardsOnSurface+"cards. No other cards can be added";


    /**
     *
     * @param surface List
     */
    public Briscola2PSurface(List<NeapolitanCard> surface) {

        if(surface.size() > maxCardsOnSurface || surface.size() < 0)
            throw new IllegalArgumentException(wrongConstructorArgumentErrorMessage);

            this.surface = surface;
    }

    public List<NeapolitanCard> getSurface() {
        return surface;
    }

    public void putCardOnSurface(NeapolitanCard card) {
        if(surface.size() >= maxCardsOnSurface)
            throw new IllegalStateException(alreadyFilledSurfaceErrorMessage);

        surface.add(card);


    }

    public Briscola2PSurface(String surface){
        if(surface.length() > 4)
            throw new IllegalArgumentException(wrongConstructorArgumentErrorMessage);
        for(int i = 0; i <= surface.length()-1; i = i+2){
            String num = ""+surface.charAt(i);
            String suit = ""+surface.charAt(i+1);
            this.surface.add(new NeapolitanCard(num,suit));

        }
    }

    public List<NeapolitanCard> clearSurface(){
        List<NeapolitanCard> temp = new ArrayList<>(this.surface);
        surface.clear();
        return temp;
    }

    public int countCardsOnSurface(){
        return surface.size();
    }

    public NeapolitanCard getCard(int i){
            return surface.get(i);
    }

    @Override
    public String toString(){
        String temp = "";
        if(surface.isEmpty())
            return temp;

        for(NeapolitanCard c: surface){

            temp += c;
        }
        return temp;
    }

    public boolean equalTo(Briscola2PSurface surface){
        for(int i = 0; i < getSurface().size() && i < surface.getSurface().size(); i++) {
            NeapolitanCard c1 = ((List<NeapolitanCard>)this.getSurface()).get(i);
            NeapolitanCard c2  = ((List<NeapolitanCard>)surface.getSurface()).get(i);
            if (!c1.equalTo(c2))
                return false;
        }
        return true;

    }
}
