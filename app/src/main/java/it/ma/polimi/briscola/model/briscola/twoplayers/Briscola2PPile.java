package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;

/**
 * The type Briscola 2 p pile.
 *
 * @author PintoFrancesco  Class containing the Pile of a Briscola 2 Players Pile and methods to manipulate it
 */
public class Briscola2PPile {

    /**
     * The Pile.
     */
    List<NeapolitanCard> pile = new ArrayList<>();

    /**
     * Constructor that builds a Briscola2PPile from a list of NeapolitanCards contained in a List
     *
     * @param pile List of NeapolitanCards that make up the pile
     */
    public Briscola2PPile(List<NeapolitanCard> pile) {
        this.pile = pile;
    }

    /**
     * Constructor that builds a Briscola2PPile starting from a stringified list of NeapolitanCards
     *
     * @param pile String representing the Pile. Should contain two chars for each card in the list: the first char indicates the CardNumber, the second the CardSuit
     * @see it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers
     * @see it.ma.polimi.briscola.model.deck.NeapolitanCardSuit
     */
    public Briscola2PPile(String pile){
        for(int i = 0; i <= pile.length()-1; i = i+2){
            String num = ""+pile.charAt(i);
            String suit = ""+pile.charAt(i+1);
            this.pile.add(new NeapolitanCard(num,suit));
        }
    }

    /**
     * Method that returns the Pile in the form of a list
     *
     * @return pile The List form of the pile
     */
    public List<NeapolitanCard> getPile() {
        return pile;
    }

    /**
     * Method used to push new cards on the pile
     *
     * @param cards The list of cards to be pushed on the pile (no check is performed)
     */
    public void pushOnPile(List<NeapolitanCard> cards) {
        pile.addAll(cards);
    }


    /**
     * Builds a String representation of the pile
     * @return pile  String representing the Pile. Contains two chars for each card in the list: the first char indicates the CardNumber, the second the CardSuit. Topmost cards are appended at the end of the string.
     */
    @Override
    public String toString(){
        String temp = "";
        if(pile.isEmpty())
            return temp;

        for(NeapolitanCard c: pile){

            temp += c;
        }
        return temp;
    }

    /**
     * Compare if the content of two piles is the same
     *
     * @param pile the pile
     * @return Boolean representing whether the two piles are equal or not
     */
    public boolean equalTo(Briscola2PPile pile){
        for(int i = 0; i < getPile().size() && i < pile.getPile().size(); i++) {
            NeapolitanCard c1 = ((List<NeapolitanCard>)this.getPile()).get(i);
            NeapolitanCard c2  = ((List<NeapolitanCard>)pile.getPile()).get(i);
            if (!c1.equalTo(c2))
                return false;
        }
        return true;

    }
}
