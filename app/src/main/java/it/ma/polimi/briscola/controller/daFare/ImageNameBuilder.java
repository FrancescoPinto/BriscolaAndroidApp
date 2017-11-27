package it.ma.polimi.briscola.controller.daFare;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Created by utente on 26/11/17.
 */

public class ImageNameBuilder {

    public static String getFrenchCardImageName(NeapolitanCard nc){
        NeapolitanCardSuit ns = nc.getCardSuitEnum();
        NeapolitanCardNumbers nn = nc.getCardNumberEnum();
        String fns = "", fnn = "";
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
