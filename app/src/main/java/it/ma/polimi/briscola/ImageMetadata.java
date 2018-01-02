package it.ma.polimi.briscola;

import android.content.Context;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;
import it.ma.polimi.briscola.persistency.SettingsManager;

/**
 * Class that performs a mapping between the assets names and the model names of the cards, also provides methods to retrieve resources IDs
 */
public class ImageMetadata {

    /**
     * The enum containing the mapping between Suit and the way they are represented in the assets names.
     */
    enum SuitNameMapping{
        /**
         * Batons suit name mapping.
         */
        BATONS("c","c"),
        /**
         * Golds suit name mapping.
         */
        GOLDS("s","s"),
        /**
         * Cups suit name mapping.
         */
        CUPS("d","d"),
        /**
         * Swords suit name mapping.
         */
        SWORDS("h","h");

        /**
         * The French Cards name
         */
        String french, /**
         * The Minimal French Cards name
         */
        minimal;
        SuitNameMapping(String french,String minimal){
            this.french = french;
            this.minimal = minimal;
        }
    }

    /**
     * The enum containing the mapping between Number and the way they are represented in the assets names.
     */
    enum NumberNameMapping{
        /**
         * Ace number name mapping.
         */
        ACE("a","1"),
        /**
         * Two number name mapping.
         */
        TWO("t","2"),
        /**
         * Three number name mapping.
         */
        THREE("th","3"),
        /**
         * Four number name mapping.
         */
        FOUR("f","4"),
        /**
         * Five number name mapping.
         */
        FIVE("fi","5"),
        /**
         * Six number name mapping.
         */
        SIX("s","6"),
        /**
         * Seven number name mapping.
         */
        SEVEN("se","7"),
        /**
         * Jack number name mapping.
         */
        JACK("j","11"),
        /**
         * Horse number name mapping.
         */
        HORSE("q","12"),
        /**
         * King number name mapping.
         */
        KING("k","13");

        /**
         * The French Cards Number name.
         */
        String french, /**
         * The Minimal French Cards Number name.
         */
        minimal;
        NumberNameMapping(String french,String minimal){
            this.french = french;
            this.minimal = minimal;
        }
    }

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
                case BATONS: fns+= SuitNameMapping.BATONS.french; break;
                case GOLDS: fns+=SuitNameMapping.GOLDS.french; break;
                case CUPS: fns+=SuitNameMapping.CUPS.french; break;
                case SWORDS: fns+=SuitNameMapping.SWORDS.french; break;

            }

            switch(nn){
                case ACE: fnn+=NumberNameMapping.ACE.french;break;
                case TWO: fnn+=NumberNameMapping.TWO.french;break;
                case THREE: fnn+=NumberNameMapping.THREE.french;break;
                case FOUR: fnn+=NumberNameMapping.FOUR.french;break;
                case FIVE: fnn+=NumberNameMapping.FIVE.french;break;
                case SIX: fnn+=NumberNameMapping.SIX.french;break;
                case SEVEN: fnn+=NumberNameMapping.SEVEN.french;break;
                case JACK: fnn+=NumberNameMapping.JACK.french;break;
                case HORSE: fnn+=NumberNameMapping.HORSE.french;break;
                case KING: fnn+=NumberNameMapping.KING.french;break;
            }

            return fnn+fns;
        }

    /**
     * Get french card image (minimal images, with no complex drawing ... for minimalist fans!) name string from neapolitan card.
     *
     * @param nc the neapolitan card
     * @return the string representing the name of the french card image
     */
    public static String getMinimalFrenchCardImageName(NeapolitanCard nc){
            NeapolitanCardSuit ns = nc.getCardSuitEnum();
            NeapolitanCardNumbers nn = nc.getCardNumberEnum();

            if(ns == null && ns == null){ //unknown card value, used for online matches when the PLAYER1 suit/number of the card is unknown
                return "default_card_background";
            }
            String fns = "", fnn = "";
            //perform the mapping, following the convention specified below (images are named after this convention)
        switch(ns){
            case BATONS: fns+= SuitNameMapping.BATONS.minimal; break;
            case GOLDS: fns+=SuitNameMapping.GOLDS.minimal; break;
            case CUPS: fns+=SuitNameMapping.CUPS.minimal; break;
            case SWORDS: fns+=SuitNameMapping.SWORDS.minimal; break;

        }

        switch(nn){
            case ACE: fnn+=NumberNameMapping.ACE.minimal;break;
            case TWO: fnn+=NumberNameMapping.TWO.minimal;break;
            case THREE: fnn+=NumberNameMapping.THREE.minimal;break;
            case FOUR: fnn+=NumberNameMapping.FOUR.minimal;break;
            case FIVE: fnn+=NumberNameMapping.FIVE.minimal;break;
            case SIX: fnn+=NumberNameMapping.SIX.minimal;break;
            case SEVEN: fnn+=NumberNameMapping.SEVEN.minimal;break;
            case JACK: fnn+=NumberNameMapping.JACK.minimal;break;
            case HORSE: fnn+=NumberNameMapping.HORSE.minimal;break;
            case KING: fnn+=NumberNameMapping.KING.minimal;break;
        }

            return fns+fnn;
    }

    /**
     * Based on the current preference, get the name of the image corresponding to the Neapolitan Card.
     *
     * @param context the context, to retrieve preferences
     * @param card    the card that needs to be represented
     * @return the string representing the name of the card
     */
    public static String getCardName(Context context, NeapolitanCard card ){
        if(card == null) //no null cards allowed
            throw new IllegalArgumentException();

        String cardName = "";
        //get the preference
        SettingsManager settingsManager = new SettingsManager(context);
        int preference = settingsManager.getCardViewPreference();
        //based on preference,build the card name
        if(preference == settingsManager.FRENCH)
            cardName= ImageMetadata.getFrenchCardImageName(card);
        else if(preference == settingsManager.MINIMAL_FRENCH)
            cardName= ImageMetadata.getMinimalFrenchCardImageName(card);

        if(cardName.equals("")) //should not be empty
            throw new IllegalArgumentException();

        return cardName;
    }


    /**
     * Based on the current preference, get the name of the image corresponding to the Neapolitan Card represented by a String (format convetion as specified in the slides).
     *
     * @param context the context, to retrieve preferences
     * @param card    String representing the card that needs to be represented
     * @return the string representing the name of the card
     */
    public static String getCardName(Context context, String card ){
        if(card == null || card.equals(""))
            throw new IllegalArgumentException();
        //reuse getCardName(Context,NeapolitanCard)
        return getCardName(context, new NeapolitanCard(card.charAt(0),card.charAt(1)));
    }

    /**
     * Given the french card name, convert it to minimal french card name string.
     *
     * @param french the french card name
     * @return the string representing the corresponding minimal french card name
     */
    public static String fromFrenchToMinimalFrench(String french){
        String newNumber = "";
        String number = french.substring(0,french.length()-1);//french cards have the suit as the last char, hence to get the number take all chars but the last one
        for(NumberNameMapping n: NumberNameMapping.values()){
            if(n.french.equals(number))
                newNumber += n.minimal; //get the equivalent minimal value
        }

        if(newNumber.equals("")) //if no match is found, something's wrong
            throw new IllegalArgumentException();

        String suit = String.valueOf(french.charAt(french.length()-1));
        String newSuit = suit; //no need for mapping, suits are identical in the enum

     //   for(SuitNameMapping s: SuitNameMapping.values()){
      //      if(s.french.equals(suit))
      //          newSuit += s.minimal;
      //  }

        if(newSuit.equals(""))
            throw new IllegalArgumentException();

        return newSuit+newNumber; //minimal cards name have first the suit, then the number

    }

    /**
     * Given the minimal french card name, convert it to french card name string.
     *
     * @param minimal the minimal french card name string
     * @return the string representing the corresponding french card name
     */
    public static String fromMinimalFrenchToFrench(String minimal){

        String suit = String.valueOf(minimal.charAt(0)); //minimal french cards have suit as first char
        String newSuit = suit; //no need for mapping, suits are identical in the enum
      //  for(SuitNameMapping s: SuitNameMapping.values()){
       //     if(s.minimal.equals(suit))
       //         newSuit += s.french;
      //  }

        if(newSuit.equals(""))
            throw new IllegalArgumentException();

        String number = minimal.substring(1); //the remaining of minimal is the number
        String newNumber = "";
        for(NumberNameMapping n: NumberNameMapping.values()){
            if(n.minimal.equals(number))
                newNumber += n.french; //perform the mapping
        }

        if(newNumber.equals(""))
            throw new IllegalArgumentException();

        return newNumber+newSuit; //french cards have first the number, then the suit

    }

    /**
     * Get card back Android resource id according to the current preference.
     *
     * @param context the context, to retrieve preference
     * @return the int representing the resource
     */
    public static int getCardBackID(Context context){
        //retrieve the preference
        SettingsManager settingsManager = new SettingsManager(context);
        int cardViewPreference = settingsManager.getCardViewPreference();
        switch (cardViewPreference){ //based on the preference, return a different resource id
            case SettingsManager.FRENCH: return R.drawable.default_card_background;
            case SettingsManager.MINIMAL_FRENCH:return R.drawable.minimal_card_back;
            default: throw new IllegalStateException(); //no other preferences are allowed
        }
    }

    /**
     * Given the name of the card (any valid card type name), get card Android resource id according to the current preference
     *
     * @param context the context, to retrieve preference
     * @param name    the name representing the card (any valid card type name is allowed)
     * @return the int representing the resource
     */
    public static int getCardIDWithCurrentPreference(Context context, String name){
        //get the preference
        SettingsManager settingsManager = new SettingsManager(context);
        int cardViewPreference = settingsManager.getCardViewPreference();

        String newName;

        //if need to convert french2minimal, do it
        if(isNameFrench(name) && cardViewPreference == SettingsManager.MINIMAL_FRENCH)
            newName = fromFrenchToMinimalFrench(name);
        else if(isNameMinimalFrench(name) && cardViewPreference == SettingsManager.FRENCH) //if need to convert minimal2french, do it
            newName = fromMinimalFrenchToFrench(name);
        else //else no change, the name is already ok
            newName = name;
        //get image id
        return context.getResources().getIdentifier(newName, "drawable", context.getPackageName());
    }

    //convenience method, determines whether the string name represnets a french card
    private static boolean isNameFrench(String name){
        boolean isFrench = false;
        for(SuitNameMapping s:SuitNameMapping.values())
            for(NumberNameMapping n:NumberNameMapping.values())
                if((n.french + s.french).equals(name)) //check for all possible cards
                    isFrench = true;

        return isFrench;
    }
    //convenience method, determines whether the string name represnets a minimal french card
    private static boolean isNameMinimalFrench(String name){
        boolean isMinimalFrench = false;
        for(SuitNameMapping s:SuitNameMapping.values())
            for(NumberNameMapping n:NumberNameMapping.values())
                if((s.minimal + n.minimal).equals(name)) //check for all possible cards
                    isMinimalFrench = true;

        return isMinimalFrench;
    }
}
