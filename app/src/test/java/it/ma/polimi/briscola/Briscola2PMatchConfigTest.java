package it.ma.polimi.briscola;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PHand;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PPile;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PSurface;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;
import it.ma.polimi.briscola.model.deck.NeapolitanDeck;

import static org.junit.Assert.assertTrue;

/**
 * Class that contains Birscola2PMatchConfig tests. It is a quite long class, however, for each tested method it usually provides first the used data and convenience methods used for testing, then the test. Seldom, this convention is broken (not often, in order to avoid making the class more confusing)
 */
public class Briscola2PMatchConfigTest {

    //trivial setters and getters (mostly automatically generated) are not tested



     //Create easy config briscola 2 p match config. Is a convenience method for testing.
      //the briscola 2 p match config represented by "0C5S4G6S2C.6G7G.JCKG2B.1CKS3G.KC5C.4B1B"
    private Briscola2PMatchConfig createEasyConfig(){
        Briscola2PMatchConfig easyConfig = new Briscola2PMatchConfig();
        easyConfig.setCurrentPlayer(0);
        easyConfig.setBriscolaSuit("C");
        easyConfig.setDeck(new NeapolitanDeck("5S4G6S2C")); //todo prima quando hai sbagliato e gli hai infilato anche i primi due caratteri non ha fatto una piega, non va bene!
        easyConfig.setSurface(new Briscola2PSurface("6G7G"));
        easyConfig.setHand(0,"JCKG2B");
        easyConfig.setHand(1,"1CKS3G");
        easyConfig.setPile(0,"KC5C");
        easyConfig.setPile(1,"4B1B");
        return easyConfig;

    }

    //Create starting config briscola 2 p match config. Is a convenience method for testing.
     //returns the briscola 2 p match config represented by 0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B..JCKG2B.1CKS3G..
    private Briscola2PMatchConfig createStartingConfig(){
        Briscola2PMatchConfig startingConfig = new Briscola2PMatchConfig();
        startingConfig.setCurrentPlayer(0);
        startingConfig.setBriscolaSuit("B");
        startingConfig.setDeck(new NeapolitanDeck("5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B"));
        startingConfig.setSurface(new Briscola2PSurface(""));
        startingConfig.setHand(0,"JCKG2B");
        startingConfig.setHand(1,"1CKS3G");
        startingConfig.setPile(0,"");
        startingConfig.setPile(1,"");
        return startingConfig;

    }


   //data from prof slides
    private String startingConfig =  "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B..JCKG2B.1CKS3G..";
    private String config1 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.JC.KG2B.1CKS3G..";
    private String config2 = "1B6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B..KG2B4G.KS3G5S..JC1C";
    private String config3 =  "0B6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.3G.KG2B4G.KS5S..JC1C";

    // a full match
    private String[] fullMatchArray = {
            "1GHGKGHS7C2S3B3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..3G1C6C.JC2C5B..",
            "0GHGKGHS7C2S3B3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.5B.3G1C6C.JC2C..",//player 1 played 2
            "1GHS7C2S3B3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..3G6CKG.JC2CHG..5B1C",//player 0 played 1
            "0GHS7C2S3B3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.HG.3G6CKG.JC2C..5B1C",//ecc. see the moveSequenceConfig2 string
            "0G2S3B3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..6CKGHS.JC2C7C.HG3G.5B1C",
            "1G2S3B3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.HS.6CKG.JC2C7C.HG3G.5B1C",
            "0G3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..6CKG2S.2C7C3B.HG3GHSJC.5B1C",
            "1G3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.6C.KG2S.2C7C3B.HG3GHSJC.5B1C",
            "1G7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..KG2S4S.2C3B3S.HG3GHSJC.5B1C6C7C",
            "0G7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.3B.KG2S4S.2C3S.HG3GHSJC.5B1C6C7C",
            "0G5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..2S4S7B.2C3S4B.HG3GHSJC3BKG.5B1C6C7C",
            "1G5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.7B.2S4S.2C3S4B.HG3GHSJC3BKG.5B1C6C7C",
            "0GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..2S4S5S.2C4B6G.HG3GHSJC3BKG7B3S.5B1C6C7C",
            "1GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.2S.4S5S.2C4B6G.HG3GHSJC3BKG7B3S.5B1C6C7C",
            "1G6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..4S5SKB.2C4BKS.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G",
            "0G6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.2C.4S5SKB.4BKS.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G",
            "1G1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..4SKBJB.4BKS6S.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5S",
            "0G1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.KS.4SKBJB.4B6S.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5S",
            "1GJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..4SKB5C.4B6S1G.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB",
            "0GJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.4B.4SKB5C.6S1G.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB",
            "1G2G6BHB4CHC4GKC1S7G5G2B3C1BJG..KB5C7S.6S1GJS.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4S",
            "0G2G6BHB4CHC4GKC1S7G5G2B3C1BJG.JS.KB5C7S.6S1G.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4S",
            "1GHB4CHC4GKC1S7G5G2B3C1BJG..KB5C6B.6S1G2G.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S",
            "0GHB4CHC4GKC1S7G5G2B3C1BJG.1G.KB5C6B.6S2G.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S",
            "1GHC4GKC1S7G5G2B3C1BJG..5C6B4C.6S2GHB.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKB",
            "0GHC4GKC1S7G5G2B3C1BJG.HB.5C6B4C.6S2G.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKB",
            "1GKC1S7G5G2B3C1BJG..5C4C4G.6S2GHC.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "0GKC1S7G5G2B3C1BJG.HC.5C4C4G.6S2G.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "0G7G5G2B3C1BJG..5C4CKC.6S2G1S.HG3GHSJC3BKG7B3SHC4G.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "1G7G5G2B3C1BJG.KC.5C4C.6S2G1S.HG3GHSJC3BKG7B3SHC4G.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "0G2B3C1BJG..5C4C7G.6S2G5G.HG3GHSJC3BKG7B3SHC4GKC1S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "1G2B3C1BJG.7G.5C4C.6S2G5G.HG3GHSJC3BKG7B3SHC4GKC1S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "0G1BJG..5C4C2B.6S2G3C.HG3GHSJC3BKG7B3SHC4GKC1S7G5G.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "1G1BJG.4C.5C2B.6S2G3C.HG3GHSJC3BKG7B3SHC4GKC1S7G5G.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "1G..5C2BJG.6S2G1B.HG3GHSJC3BKG7B3SHC4GKC1S7G5G.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C",
            "0G.1B.5C2BJG.6S2G.HG3GHSJC3BKG7B3SHC4GKC1S7G5G.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C",
            "0G..5C2B.6S2G.HG3GHSJC3BKG7B3SHC4GKC1S7G5G1BJG.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C",
            "1G.2B.5C.6S2G.HG3GHSJC3BKG7B3SHC4GKC1S7G5G1BJG.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C",
            "1G..5C.6S.HG3GHSJC3BKG7B3SHC4GKC1S7G5G1BJG.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C2B2G",
            "0G.6S.5C..HG3GHSJC3BKG7B3SHC4GKC1S7G5G1BJG.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C2B2G",
    };
    /**
     * Initialize new deck test
     */
    @Test
    public void initializeNewDeck(){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig();
        config.initializeNewDeck();
        NeapolitanDeck deck = config.getDeck();
        boolean containsAllCards = true; //check if the deck contains all cards
        for(NeapolitanCardNumbers cn : NeapolitanCardNumbers.values())
            for(NeapolitanCardSuit cs : NeapolitanCardSuit.values()){
               // System.out.println(""+cn + cs);
                if(!deck.containsCard(new NeapolitanCard(cn,cs)))
                    containsAllCards = false;
            }

        assertTrue(containsAllCards);

    }

    /**
     * Initialize first player test. Initializes first player with either PLAYER0 or PLAYER1 (since initialization is at random, the test is repeated multiple times)
     */
    @Test
    public void initializeFirstPlayer(){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig();
        for(int i = 0; i < 100; i++) { //repeating the test 100 times should ensure that at least once each of the possible values (PLAYER0, PLAYER1) is used in the test
            config.initializeFirstPlayer(); //initialize at random
            int currentPlayer = config.getCurrentPlayer();
            assertTrue(currentPlayer == config.PLAYER0 || currentPlayer == config.PLAYER1);
        }
    }

    /**
     * Initialize players hands test
     */
    @Test
    public void initializePlayersHands(){
        //config is in the right state
        Briscola2PMatchConfig config = new Briscola2PMatchConfig();

        config.initializeNewDeck();
        NeapolitanDeck deck = config.getDeck();
        List<NeapolitanCard> cards =  new ArrayList<>(deck.getCardList());

        config.initializeFirstPlayer();
        config.initializePlayersHands();
        List<NeapolitanCard> firstCards = cards.subList(0,6);

        int currentPlayer = config.getCurrentPlayer();
        Briscola2PHand hand1 = config.getHand(currentPlayer);
        Briscola2PHand hand2 = config.getHand((currentPlayer+1)%2);

        assertTrue(hand1.getCard(0).equalTo(firstCards.get(0))); //check cards are drawn alterned and in the right order
        assertTrue(hand1.getCard(1).equalTo(firstCards.get(2)));
        assertTrue(hand1.getCard(2).equalTo(firstCards.get(4)));
        assertTrue(hand2.getCard(0).equalTo(firstCards.get(1)));
        assertTrue(hand2.getCard(1).equalTo(firstCards.get(3)));
        assertTrue(hand2.getCard(2).equalTo(firstCards.get(5)));


        //config is in the wrong state
            //config's deck and current player not been initialized
             config = new Briscola2PMatchConfig();
            try{
                config.initializePlayersHands();
            }catch(Exception e){
                assertTrue(e instanceof IllegalStateException);
            }
            //config's current player has not been initialized
            try{
                config.initializeNewDeck();
                config.initializePlayersHands();
            }catch(Exception e){
                assertTrue(e instanceof IllegalStateException);
            }
            //config's deck has not been initialized
            try{
                config.initializeFirstPlayer();
                config.initializePlayersHands();
            }catch(Exception e){
                assertTrue(e instanceof IllegalStateException);
            }
    }

    /**
     * Initialize briscola test
     */
    @Test
    public void initializeBriscola(){
        //config is in the right state
        Briscola2PMatchConfig config = new Briscola2PMatchConfig();

        config.initializeNewDeck();
        NeapolitanDeck deck = config.getDeck();
        List<NeapolitanCard> cards = new ArrayList<>(deck.getCardList());

        config.initializeFirstPlayer();
        config.initializePlayersHands();
        config.initializeBriscola();
        assertTrue(config.getBriscolaSuit().equals(cards.get(6).getCardSuit()));

        //config is in the wrong state
        //config's deck and player's hands not initialized
        config = new Briscola2PMatchConfig();
        try{
            config.initializeBriscola();
        }catch(Exception e){
            assertTrue(e instanceof IllegalStateException);
        }
        //config's player'shands have not been initialized
        try{
            config.initializeNewDeck();
            config.initializeBriscola();
        }catch(Exception e){
            assertTrue(e instanceof IllegalStateException);
        }
        //config's deck has not been initialized
        try{
            config.initializeFirstPlayer();
            config.initializePlayersHands();
            config.initializeBriscola();
        }catch(Exception e){
            assertTrue(e instanceof IllegalStateException);
        }

    }

    //data
    private String drawCards1 = "0G7G5G2B3C1BJG..5C4C.6S2G..";
    private String drawCards2 = "0G2B3C1BJG..5C4C7G.6S2G5G..";
    private String drawCards3 = "1G7G5G2B3C1BJG..5C4C.6S2G..";
    private String drawCards4 = "1G2B3C1BJG..5C4C5G.6S2G7G..";

    private String drawCards11 = "0G7G5G..5C4C.6S2G..";
    private String drawCards12 = "0G..5C4C7G.6S2G5G..";
    private String drawCards13 = "1G7G5G..5C4C.6S2G..";
    private String drawCards14 = "1G..5C4C5G.6S2G7G..";

    //convenience method
    private void testDrawCards(String beginConfig, String endConfig){
        Briscola2PMatchConfig config1,config2;
        config1 = new Briscola2PMatchConfig(beginConfig);
        config1.drawCardsNewRound();
        config2 = new Briscola2PMatchConfig(endConfig);//check that in the following config cards have been appended to player's hands
        assertTrue(config1.getHand(config1.PLAYER0).getCard(2).equalTo(config2.getHand(config2.PLAYER0).getCard(2)));
        assertTrue(config1.getHand(config1.PLAYER1).getCard(2).equalTo(config2.getHand(config2.PLAYER1).getCard(2)));
    }

    /**
     * Draw cards new round test. REMARK: the currentplayer indicates the winner of the previous match, draws card first
     */
    @Test
    public void drawCardsNewRound(){
        Briscola2PMatchConfig config;

        //test with empty deck
        for(int i = 34; i <= 39; i++){
            config = new Briscola2PMatchConfig(fullMatchArray[i]);
            assertTrue(config.toString().equals(fullMatchArray[i])); //if deck is empty, after cards are drawn, no change in config
        }

        //test with deck with two cards
       testDrawCards(drawCards11,drawCards12);
        testDrawCards(drawCards13,drawCards14);

        //test with deck with more than cards
        testDrawCards(drawCards1,drawCards2);
        testDrawCards(drawCards3,drawCards4);

    }


//those configurations don't perform surface cleaning after the cards have been played
    private String configPlayCard0 = "0B5B..JCKG2B.1CKS3G.."; //the first char is relevant for who plays (but not updated by playCard), the other 3 are kept just to not break the constructor function, the following matters (apart from piles)
    private String configPlayCard1 = "1B5B.JC.KG2B.1CKS3G..";
    private String configPlayCard2 = "1B5B.JC1C.KG2B.KS3G.."; //current player not updated, irrelevant for the test logic
    private String configPlayCard3 = "1B5B.KG.JC2B.1CKS3G..";
    private String configPlayCard4 = "1B5B.KG3G.JC2B.1CKS.."; //current player irrelevant
    private String configPlayCard5 = "1B5B.2B.JCKG.1CKS3G..";
    private String configPlayCard6 = "1B5B.2BKS.JCKG.1C3G.."; //current player irrelevant
    private String configPlayCard10 = "1B5B..JCKG2B.1CKS3G.."; //the first char is relevant for who plays (but not updated by playCard), the other 3 are kept just to not break the constructor function, the following matters (apart from piles)
    private String configPlayCard11 = "0B5B.1C.JCKG2B.KS3G..";
    private String configPlayCard12 = "0B5B.1CJC.KG2B.KS3G.."; //current player not updated, irrelevant for the test logic
    private String configPlayCard13 = "0B5B.3G.JCKG2B.1CKS..";
    private String configPlayCard14 = "0B5B.3GKG.JC2B.1CKS.."; //current player irrelevant
    private String configPlayCard15 = "0B5B.KS.JCKG2B.1C3G..";
    private String configPlayCard16 = "0B5B.KS2B.JCKG.1C3G.."; //current player irrelevant
    private String configPlayCard20 = "0B5B..JCKG.1CKS.."; //the first char is relevant for who plays (but not updated by playCard), the other 3 are kept just to not break the constructor function, the following matters (apart from piles)
    private String configPlayCard21 = "1B5B.JC.KG.1CKS..";
    private String configPlayCard22 = "1B5B.JC1C.KG.KS.."; //current player not updated, irrelevant for the test logic
    private String configPlayCard23 = "1B5B.KG.JC.1CKS..";
    private String configPlayCard24 = "1B5B.KGKS.JC.1C.."; //current player irrelevant
    private String configPlayCard30 = "1B5B..JCKG.1CKS.."; //the first char is relevant for who plays (but not updated by playCard), the other 3 are kept just to not break the constructor function, the following matters (apart from piles)
    private String configPlayCard31 = "0B5B.1C.JCKG.KS..";
    private String configPlayCard32 = "0B5B.1CJC.KG.KS.."; //current player not updated, irrelevant for the test logic
    private String configPlayCard33 = "0B5B.KS.JCKG.1C..";
    private String configPlayCard34 = "0B5B.KSKG.JC.1C.."; //current player irrelevant
    private String configPlayCard40 = "0B5B..JC.1C..";
    private String configPlayCard41 = "1B5B.JC..1C..";
    private String configPlayCard42 = "1B5B.JC1C...."; // current player irrelevant
    private String configPlayCard50 = "1B5B..JC.1C..";
    private String configPlayCard51 = "0B5B.1C.JC...";
    private String configPlayCard52 = "0B5B.1CJC...."; // current player irrelevant


    //convenience method that plays a card
    private void testPlayCard(int card, String configStart, String configEnd){
        Briscola2PMatchConfig config =  new Briscola2PMatchConfig(configStart);
        config.playCard(card);

        assertTrue(config.getSurface().toString().equals(configEnd.split("\\.",6)[1]));
        assertTrue(config.getHand(config.PLAYER0).toString().equals(configEnd.split("\\.",6)[2]));
        assertTrue(config.getHand(config.PLAYER1).toString().equals(configEnd.split("\\.",6)[3]));
    }

    /**
     * Play card test. REMARK: this test aims to check the configuration changes occur correctly, not to throw exceptions testing for illegal moves or sequence of moves and not to check card shifting after removal (since this is already tested for Briscola2PHand and AbstractCardlistWrapper)
     */
    @Test
    public void playCard() {

        //starting from configPlayCard0, try some correct index combinations
        testPlayCard(0,configPlayCard0, configPlayCard1);
        testPlayCard(0,configPlayCard1, configPlayCard2);

        testPlayCard(1,configPlayCard0, configPlayCard3);
        testPlayCard(2,configPlayCard3, configPlayCard4);

        testPlayCard(2,configPlayCard0, configPlayCard5);
        testPlayCard(1,configPlayCard5, configPlayCard6);

        //starting from configPlayCard0, try some correct index combinations
        testPlayCard(0,configPlayCard10, configPlayCard11);
        testPlayCard(0,configPlayCard11, configPlayCard12);

        testPlayCard(2,configPlayCard10, configPlayCard13);
        testPlayCard(1,configPlayCard13, configPlayCard14);

        testPlayCard(1,configPlayCard10, configPlayCard15);
        testPlayCard(2,configPlayCard15, configPlayCard16);

        testPlayCard(0,configPlayCard20, configPlayCard21);
        testPlayCard(0,configPlayCard21, configPlayCard22);

        testPlayCard(1,configPlayCard20, configPlayCard23);
        testPlayCard(1,configPlayCard23, configPlayCard24);

        //starting from configPlayCard30, try some correct index combinations
        testPlayCard(0,configPlayCard30, configPlayCard31);
        testPlayCard(0,configPlayCard31, configPlayCard32);

        testPlayCard(1,configPlayCard30, configPlayCard33);
        testPlayCard(1,configPlayCard33, configPlayCard34);

        //starting from configPlayCard40, try all correct index combinations
        testPlayCard(0,configPlayCard40, configPlayCard41);
        testPlayCard(0,configPlayCard41, configPlayCard42);

        //starting from configPlayCard50, try all correct index combinations
        testPlayCard(0,configPlayCard50, configPlayCard51);
        testPlayCard(0,configPlayCard51, configPlayCard52);


    }

//data
//current player (first char in the config string) represents the player who played the second card that now is on surface!
    private String[] configRoundWinnerArray = new String[]
        {"1B5B.JC1C.KG2B.KS3G..", //same suit, no trump, wins 1
        "1B5B.KGHG.KG2B.KS3G..", //same suit, no trump, wins 0
        "1B5B.4S6S.KG2B.KS3G.3G4G.7BJB",
        "1B5B.1BKB.KG2B.KS3G.KSJB3G4G.", //both trumps, wins 0
        "1B5B.JB3B.KG2B.KS3G..KSJB3G4G", //both trumps, wins 1
        "1B5B.6B5B.KG2B.KS3G..", //trumps, both zero valued wins 0
        "1B5B.5B7B.KG2B.KS3G..", //trumps, both zero valued wins 1
        "1B5B.JC3B.KG2B.KS3G..", //different suits, one of them is trump wins 1
        "1B5B.6B5G.KG2B.KS3G..", // " ", wins 0
        "1B5B.7S5G.KG2B.KS3G..", //different suits, no trump,  wins first player
                //let's invert roles of first and second player
        "0B5B.JC1C.KG2B.KS3G..",
        "0B5B.KGHG.KG2B.KS3G..",
        "0B5B.4S6S.KG2B.KS3G.7BJB.3G4G",
        "0B5B.1BKB.KG2B.KS3G..KSJB3G4G",
        "0B5B.JB3B.KG2B.KS3G.KSJB3G4G.",
        "0B5B.6B5B.KG2B.KS3G..",
        "0B5B.5B7B.KG2B.KS3G..",
        "0B5B.JC3B.KG2B.KS3G..",
        "0B5B.6B5G.KG2B.KS3G.."
        ,"0B5B.7S5G.KG2B.KS3G.."};

    private int[] expectedWinner1 = new int[]{ //corresponding to the first 10 rows of the configWinenrArray
            1,0,1,0,1,0,1,1,0,0};
    private int[] expectedWinner2 = new int[]{ //corresponding to the last 10 rows of the configWinenrArray
            0,1,0,1,0,1,0,0,1,1};

    private void testWinner(String configuration, int expectedWinner){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(configuration);
        int winner = config.chooseRoundWinner();
        assertTrue(winner == expectedWinner);
    }

    /**
     * Choose winner test
     */
    @Test
    public void chooseRoundWinner(){

        for(int i = 0; i < 10; i++){
            testWinner(configRoundWinnerArray[i],expectedWinner1[i]);
        }
        //invert roles of current players with respect to previous tests
        for(int i = 10; i < 20; i++){
            testWinner(configRoundWinnerArray[i],expectedWinner2[i-10]);
        }

    }

    //convenience method
    private void testClearSurface(String configuration){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(configuration);
        int winner = config.chooseRoundWinner();

        String tempPileLoser = ""+ config.getPile((winner+1)%2);
        String tempPileWinner = "" + config.getPile(winner);

        config.clearSurface(winner);

        String newPileWinner = config.getPile(winner).toString();
        String newPileLoser = config.getPile((winner+1)%2).toString();

        assertTrue(newPileWinner.equals(tempPileWinner + configuration.split("\\.",6)[1])); //the surface has been appended correctly
        assertTrue(newPileLoser.equals(tempPileLoser)); //no variation for loser
    }

    /**
     * Clear surface test
     */
    @Test
    public void clearSurface(){
        //correct config test
        for(int i = 0; i < 20; i++){
            testClearSurface(configRoundWinnerArray[i]);
        }

        //wrong config
        //clearing empty surface
        try{
            Briscola2PMatchConfig config = new Briscola2PMatchConfig(configRoundWinnerArray[0]);
            //no matter who wins, I'll use the first player as winner
            config.clearSurface(1); //clearing the surface two times implies that the second time we attempt to clear the surface, it is empty (clear should not be called in that case)
            config.clearSurface(1);

        }catch(Exception e){
            assertTrue(e instanceof IllegalStateException);
        }

        //clearing surface with one card
        try{
            Briscola2PMatchConfig config = new Briscola2PMatchConfig(configRoundWinnerArray[0]);
            //no matter who wins, I'll use the first player as winner
            config.clearSurface(1); //clear the surface, put a card on it, then attempt to clear it (with one card on surface -> illegal)
            config.playCard(0);
            config.clearSurface(1);

        }catch(Exception e){
            assertTrue(e instanceof IllegalStateException);
        }
    }

    private int[] scores1 = new int[]{ //corresponding to the first 10 rows of the configWinenrArray
            0,7,10,31,0,0,0,0,0,0};

    private int[] scores2 = new int[]{ //corresponding to the last 10 rows of the configWinenrArray
            13,0,2,0,28,0,0,12,0,0};


    private int[] scores3 = new int[]{ //corresponding to the array (declared much below) winnerArray
            73,47,
            90,30,
            45,75,
            120,0,
            0,120,
            60,60
    };

    private void testComputeScore(String configuration, int expectedResult0, int expectedResult1){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(configuration);
        int winner = config.chooseRoundWinner();
        config.clearSurface(winner);
        assertTrue(config.computeScore(config.PLAYER0) == expectedResult0);
        assertTrue(config.computeScore(config.PLAYER1) == expectedResult1);

    }

    /**
     * Compute score test
     */
    @Test
    public void computeScore(){

        for(int i = 0; i < 10; i++){
            testComputeScore(configRoundWinnerArray[i],scores1[i],scores2[i]);
        }
        //invert roles of current players with respect to previous tests
        for(int i = 10; i < 20; i++){
            testComputeScore(configRoundWinnerArray[i],scores2[i-10],scores1[i-10]);
        }

        Briscola2PMatchConfig config;
        for(int i = 0, j = 0; i < winnerArray.length; i++, j+=2){
            config = new Briscola2PMatchConfig(winnerArray[i]);

            assertTrue(config.computeScore(config.PLAYER0) == scores3[j]);
            assertTrue(config.computeScore(config.PLAYER1) == scores3[j+1]);
        }
    }

    //data
    private String configToggle1 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.JC1C.KG2B.KS3G..";
    private String configToggle2 = "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.JC1C.KG2B.KS3G..";

    /**
     * Toggle current player test
     */
    @Test
    public void toggleCurrentPlayer(){
        //right config state
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(configToggle1);
        config.toggleCurrentPlayer();
        assertTrue(config.getCurrentPlayer() == Integer.parseInt(""+configToggle2.charAt(0)));
        config.toggleCurrentPlayer();
        assertTrue(config.getCurrentPlayer() == Integer.parseInt(""+configToggle1.charAt(0)));

        //wrong config state: can't be tested since there is no way to bring currentPlayer to wrong value
    }

    //data
    private String easyConfig = "0C5S4G6S2C.6G7G.JCKG2B.1CKS3G.KC5C.4B1B";

    /**
     * String constructor test.
     */
    @Test
    public void stringConstructorTest(){
        //for these tests we just use equalTo
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(easyConfig);
        assertTrue(createEasyConfig().equalTo(config));
        config = new Briscola2PMatchConfig(startingConfig);
        assertTrue(createStartingConfig().equalTo(config));

        //for these tests we use string representation
        for(int i = 0; i < fullMatchArray.length; i++){
            config = new Briscola2PMatchConfig(fullMatchArray[i]);
            assertTrue(config.toString().equals(fullMatchArray[i]));
        }

        //if the first token of the config has size smaller than 2 throw exception (does not specify briscola/currentPlayer)
        try{
            config = new Briscola2PMatchConfig(".6G7G.JCKG2B.1CKS3G.KC5C.4B1B");
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

    }

    /**
     * To string test.
     */
    @Test
    public void toStringTest(){
        System.out.println(createEasyConfig().toString());

        assertTrue(easyConfig.equals(createEasyConfig().toString()));
        assertTrue(startingConfig.equals(createStartingConfig().toString()));
        assertTrue(config1.equals(new Briscola2PMatchConfig(config1).toString()));
        assertTrue(config2.equals(new Briscola2PMatchConfig(config2).toString()));
        assertTrue(config3.equals(new Briscola2PMatchConfig(config3).toString()));

        Briscola2PMatchConfig config;
        //build with the string constructor (for simplicity) then check the input string is equal to the stringified config
        for(int i = 0; i < fullMatchArray.length; i++){
            config = new Briscola2PMatchConfig(fullMatchArray[i]);
            assertTrue(config.toString().equals(fullMatchArray[i]));
        }
    }

    /**
     * Equal to test.
     */
    @Test
    public void equalToTest(){
        Briscola2PMatchConfig config1 = new Briscola2PMatchConfig(easyConfig);
        Briscola2PMatchConfig config2 = new Briscola2PMatchConfig(easyConfig);
        Briscola2PMatchConfig config3 = new Briscola2PMatchConfig(startingConfig);
        Briscola2PMatchConfig config4 = new Briscola2PMatchConfig(startingConfig);

        assertTrue(config1.equalTo(config2));
        assertTrue(!config1.equalTo(config3));
        assertTrue(config3.equalTo(config4));

        //let's make more extensive testing for a full match
        Briscola2PMatchConfig configA,configB;
        for(int i = 0; i < fullMatchArray.length; i++){
            for(int j = i; j < fullMatchArray.length; j++)
            {
                configA = new Briscola2PMatchConfig(fullMatchArray[i]);
                configB = new Briscola2PMatchConfig(fullMatchArray[j]);

                if(j == i) //if the same config, then should be true
                    assertTrue(configA.equalTo(configB));
                else //else false
                    assertTrue(!configA.equalTo(configB));
            }
        }

        /* used to discover a bug
        configA = new Briscola2PMatchConfig(fullMatchArray[34]);
        configB = new Briscola2PMatchConfig(fullMatchArray[38]);
        assertTrue(configA.getCurrentPlayer() == configB.getCurrentPlayer()) ;
        assertTrue(configA.getBriscolaSuit().equals(configB.getBriscolaSuit()));
        assertTrue(configA.getDeck().equalTo(configB.getDeck()));
        assertTrue(configA.getSurface().equalTo(configB.getSurface()));
        System.out.println(configA.getHand(configA.PLAYER0) + " " + configB.getHand(configA.PLAYER0));
        assertTrue(!configA.getHand(configA.PLAYER0).equalTo(configB.getHand(configA.PLAYER0)));
        assertTrue(!configA.getHand(configA.PLAYER1).equalTo(configB.getHand(configA.PLAYER1)));
        assertTrue(!configA.getPile(configA.PLAYER0).equalTo(configB.getPile(configA.PLAYER0)));
        assertTrue(!configA.getPile(configA.PLAYER1).equalTo(configB.getPile(configA.PLAYER1)));
        */

    }

    /**
     * setCurrentPlayer test.
     */
    @Test
    public void setCurrentPlayerTest(){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(easyConfig);
        config.setCurrentPlayer(config.PLAYER0);
        assertTrue(config.getCurrentPlayer() == config.PLAYER0);
        config.setCurrentPlayer(config.PLAYER1);
        assertTrue(config.getCurrentPlayer() == config.PLAYER1);

        try {
            config.setCurrentPlayer(43); //wrong argument
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

    }

    /**
     * setHand test.
     */
    @Test
    public void setHandTest(){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(easyConfig);
        config.setHand(config.PLAYER0,"3B4G"); //REMARK: since exceptions on Hand size/format errors are handled internally to the hand class, the tests of hand (and of AbstractCardListWrapper are enough to check all possible hands)
        assertTrue(config.getHand(config.PLAYER0).equalTo(new Briscola2PHand("3B4G")));
        config.setHand(config.PLAYER1,"JSKC4B");
        assertTrue(config.getHand(config.PLAYER1).equalTo(new Briscola2PHand("JSKC4B")));

        try {
            config.setHand(54,"3B4G"); ///wrong argument
        }catch(Exception e){
            System.out.println(e.getClass().getName());
            assertTrue(e instanceof IllegalArgumentException);
        }

    }

    /**
     * setPile test.
     */
    @Test
    public void setPileTest(){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(easyConfig);//REMARK: since exceptions on Pile size/format errors are handled internally to the pile class, the tests of pile (and of AbstractCardListWrapper are enough to check all possible piles)
        config.setPile(config.PLAYER0,"3B4G");
        assertTrue(config.getPile(config.PLAYER0).equalTo(new Briscola2PPile("3B4G")));
        config.setPile(config.PLAYER1,"JSKC4B");
        assertTrue(config.getPile(config.PLAYER1).equalTo(new Briscola2PPile("JSKC4B")));

        try {
            config.setPile(54,"3B4G"); ///wrong argument
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }

    }

    //data
    private String winnerArray[] = new String[]{
            "0G....HG3GHSJC3BKG7B3SHC4GKC1S7G5G1BJG6S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C2B2G5C", // 73 vs 47
            "0G....HG3GHSJC3BKG7B3SHC4GKC1S7G5G1BJG6S5B1C6C7C2S6G2C5SKSJB4B.4SJS7S1GKBHB6B4C3C2B2G5C", //90 vs 30
            "0G....HG3GHSJC3BKG7B3SHC4G.KC1S7G5G1BJG6S5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C2B2G5C", //35 vs 85
            "0G....HG3GHSJC3BKG7B3SHC4GKC1S7G5G1BJG6S5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C2B2G5C.", //120 vs 0
            "0G.....HG3GHSJC3BKG7B3SHC4GKC1S7G5G1BJG6S5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C2B2G5C", //0 vs 120
            "0G....HSJC3BKG7B3SHC4GKC1S7G5G1BJG6S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C2B2GHG3G5C", // 60 vs 60 draw

    };

    private int expectedWinnerMatch[] = new int[] {
            0,0,1,0,1,-1 //-1 is a draw
    };

    /**
     * chooseMatchWinner.
     */
    @Test
    public void chooseMatchWinner(){
        //good config status
        Briscola2PMatchConfig config;
        for(int i = 0; i < winnerArray.length; i++){
            System.out.println("Index "+i);
            config = new Briscola2PMatchConfig(winnerArray[i]);
            assertTrue(config.chooseMatchWinner()==expectedWinnerMatch[i]);
        }

        //wrong config status
        try{
            config = new Briscola2PMatchConfig("0G....HSJC3B7G5G1BJG6S.JB4B4SJS7S1GKBHB6B4C3C2B2GHG3G5C"); //not 120 points total
            config.chooseMatchWinner();
        }catch(Exception e){
            assertTrue(e instanceof IllegalStateException);
        }
    }

}
