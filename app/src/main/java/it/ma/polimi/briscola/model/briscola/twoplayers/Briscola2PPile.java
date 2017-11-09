package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;

/**
 * Class representing the pile in a Briscola 2 Players match
 *
 * @author Francesco Pinto
 */
public class Briscola2PPile {


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
     * @see it.ma.polimi.briscola.model.deck.NeapolitanCard
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
     * @return The List form of the pile
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
     * @return   String representing the Pile. Contains two chars for each card in the list: the first char indicates the CardNumber, the second the CardSuit. Topmost cards are appended at the end of the string.
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
     * Compare if the content of two piles is the same (same cards in the same order)
     *
     * @param pile The pile to be used for the comparison
     * @return True if the two piles contain the same cards in the same order, false otherwise
     */
    public boolean equalTo(Briscola2PPile pile){
        if(this.pile.isEmpty() && pile.getPile().size() == 0)
            return true;
        else if(this.pile.isEmpty() && !(pile.getPile().size() == 0))
            return false;
        else if(!this.pile.isEmpty() && (pile.getPile().size() == 0))
            return false;
        else {
            for (int i = 0; i < getPile().size() && i < pile.getPile().size(); i++) {
                NeapolitanCard c1 = this.getPile().get(i);
                NeapolitanCard c2 = pile.getPile().get(i);
                if (!c1.equalTo(c2))
                    return false;
            }
            return true;

        }
    }
}
