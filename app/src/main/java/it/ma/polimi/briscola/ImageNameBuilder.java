package it.ma.polimi.briscola;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Class that performs a mapping between the assets names and the model names of the cards
 */
public class ImageNameBuilder {


    /**
     * Get french card image name string from neapolitan card.
     *
     * @param nc the neapolitan card
     * @return the string representing the name of the french card image
     */
    public static String getFrenchCardImageName(NeapolitanCard nc){
            NeapolitanCardSuit ns = nc.getCardSuitEnum();
            NeapolitanCardNumbers nn = nc.getCardNumberEnum();

            if(ns == null && ns == null){ //unknown card value, used for online matches when the PLAYER1 suit/number of the card is unknown
                return "default_card_background";
            }
            String fns = "", fnn = "";
            //perform the mapping, following the convention specified below (images are named after this convention)
            switch(ns){
                case BATONS: fns+="c"; break;
                case GOLDS: fns+="s"; break;
                case CUPS: fns+="d"; break;
                case SWORDS: fns+="h"; break;

            }

            switch(nn){
                case ACE: fnn+="a";break;
                case TWO: fnn+="t";break;
                case THREE: fnn+="th";break;
                case FOUR: fnn+="f";break;
                case FIVE: fnn+="fi";break;
                case SIX: fnn+="s";break;
                case SEVEN: fnn+="se";break;
                case JACK: fnn+="j";break;
                case HORSE: fnn+="q";break;
                case KING: fnn+="k";break;
            }

            return fnn+fns;
        }

}
