package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Class representing the surface in a Briscola 2 players match
 *
 * @author Francesco Pinto
 */
public class Briscola2PSurface {
    private List<NeapolitanCard> surface = new ArrayList<NeapolitanCard>();
    private static int maxNumCardsAllowedOnSurface = 2;

    private static final String wrongConstructorArgumentErrorMessage= "The surface can contain a maximum of "+maxNumCardsAllowedOnSurface + "cards. The number of cards in the argument of the constructor should be between 0 and "+maxNumCardsAllowedOnSurface;
    private static final String alreadyFilledSurfaceErrorMessage = "The surface already contains "+maxNumCardsAllowedOnSurface+"cards. No other cards can be added";


    /**
     * Instantiates a new surface for a Briscola 2 Players match
     *
     * @param surface List of cards that are on the surface
     * @throws IllegalArgumentException If the surface argument contains more than maxNumCardsAllowedOnSurface
     */
    public Briscola2PSurface(List<NeapolitanCard> surface) {

        if(surface.size() > maxNumCardsAllowedOnSurface)
            throw new IllegalArgumentException(wrongConstructorArgumentErrorMessage);

            this.surface = surface;
    }

    /**
     * Gets surface.
     *
     * @return The list containing the cards on the surface
     */
    public List<NeapolitanCard> getSurface() {
        return surface;
    }

    /**
     * Put card on surface (the last card is appended)
     *
     * @param card The card to be put on the surface
     * @throws IllegalStateException If adding the card to the surface would mean the surface to contain more than maxNumCardsAllowedOnSurface
     */
    public void putCardOnSurface(NeapolitanCard card) {
        if(surface.size() >= maxNumCardsAllowedOnSurface)
            throw new IllegalStateException(alreadyFilledSurfaceErrorMessage);

        surface.add(card);


    }

    /**
     * Instantiates a new surface in a 2 players match based on the String passed as a parameter
     *
     * @param surface String representing the list of cards on the surface. Must contain a number of cards smaller than maxNumCardsAllowedOnSurface
     * @throws IllegalArgumentException If the number of cards contained in surface is greater than the maxNumCardsAllowedOnSurface
     */
    public Briscola2PSurface(String surface){
        if(surface.length() > 4)
            throw new IllegalArgumentException(wrongConstructorArgumentErrorMessage);
        for(int i = 0; i <= surface.length()-1; i = i+2){
            String num = ""+surface.charAt(i);
            String suit = ""+surface.charAt(i+1);
            this.surface.add(new NeapolitanCard(num,suit));

        }
    }

    /**
     * Clear surface, cards are discarded and returned.
     *
     * @return The cards previously on the surface
     */
    public List<NeapolitanCard> clearSurface(){
        List<NeapolitanCard> temp = new ArrayList<>(this.surface);
        surface.clear();
        return temp;
    }

    /**
     * Count cards currently on surface.
     *
     * @return The number of cards currently on the surface
     */
    public int countCardsOnSurface(){
        return surface.size();
    }

    /**
     * Get i-th card on the surface (card is not removed from the surface as a side effect)
     *
     * @param i index of the desired card
     * @return the desired card
     */
    public NeapolitanCard getCard(int i){
            return surface.get(i);
    } //todo check dei boundaries

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

    /**
     * Check whether two surfaces contain the same cards (in the same order).
     *
     * @param surface The surface to be used for the comparison
     * @return True if the surfaces contain the same cards in the same order, false otherwise
     */
    public boolean equalTo(Briscola2PSurface surface){
        if(this.surface.isEmpty() && surface.countCardsOnSurface() == 0)
            return true;
        else if(this.surface.isEmpty() && !(surface.countCardsOnSurface() == 0))
            return false;
        else if(!this.surface.isEmpty() && (surface.countCardsOnSurface() == 0))
            return false;
        else {

            for (int i = 0; i < getSurface().size() && i < surface.getSurface().size(); i++) {
                NeapolitanCard c1 = (this.getSurface()).get(i);
                NeapolitanCard c2 = (surface.getSurface()).get(i);
                if (!c1.equalTo(c2))
                    return false;
            }
            return true;
        }

    }


}
