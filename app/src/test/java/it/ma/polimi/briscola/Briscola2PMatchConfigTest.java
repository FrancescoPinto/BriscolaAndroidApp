package it.ma.polimi.briscola;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PSurface;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;
import it.ma.polimi.briscola.model.deck.NeapolitanDeck;

import static org.junit.Assert.assertTrue;

/**
 * Created by utente on 24/10/17.
 */

public class Briscola2PMatchConfigTest {

    String startingConfig =  "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B..JCKG2B.1CKS3G..";
    String config1 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.JC.KG2B.1CKS3G..";
    String config2 = "1B6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B..KG2B4G.KS3G5S..JC1C";
    String config3 =  "0B6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.3G.KG2B4G.KS5S..JC1C";
    String config4 = "0B5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B..KG4G6S.KS5S2C.3G2B.JC1C";




    public Briscola2PMatchConfig createEasyConfig(){
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
    public Briscola2PMatchConfig createStartingConfig(){
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

    @Test
    public void initializeNewDeck(){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig();
        config.initializeNewDeck();
        NeapolitanDeck deck = config.getDeck();
        boolean containsAllCards = true;
        for(NeapolitanCardNumbers cn : NeapolitanCardNumbers.values())
            for(NeapolitanCardSuit cs : NeapolitanCardSuit.values()){
               // System.out.println(""+cn + cs);
                if(!deck.containsCard(new NeapolitanCard(cn,cs)))
                    containsAllCards = false;
            }

        assertTrue(containsAllCards);

    }

    @Test
    public void initializeFirstPlayer(){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig();
        config.initializeFirstPlayer();
        int currentPlayer = config.getCurrentPlayer();
        assertTrue(currentPlayer == config.PLAYER0 || currentPlayer == config.PLAYER1);

    }

    @Test
    public void initializePlayersHands(){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig();
        config.initializeNewDeck();
        NeapolitanDeck deck = config.getDeck();
        List<NeapolitanCard> cards =  new ArrayList<>((List<NeapolitanCard>) deck.getCards());
        config.initializeFirstPlayer();
        int currentPlayer = config.getCurrentPlayer();
        config.initializePlayersHands();
        List<NeapolitanCard> firstCards = cards.subList(0,6);

        //System.out.println(config.getHand(0).toString() + " " +config.getHand(1).toString() + "");
        for(NeapolitanCard c : firstCards){
           // System.out.println(currentPlayer);
           // System.out.println(c);
            assertTrue(config.getHand(currentPlayer%2).containsCard(c));
            currentPlayer++;
        }
    }

    @Test
    public void initializeBriscola(){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig();
        config.initializeNewDeck();
        NeapolitanDeck deck = config.getDeck();
        List<NeapolitanCard> cards = new ArrayList<>((List<NeapolitanCard>) deck.getCards());
        config.initializeFirstPlayer();
        config.initializePlayersHands();
        config.initializeBriscola();
        System.out.println(config.getBriscolaSuit() + " " + cards.get(6));
        assertTrue(config.getBriscolaSuit().equals(cards.get(6).getCardSuit()));
    }

    @Test
    public void drawCardsNewRound(){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(config1);

        //Todo, da fare, assieme agli altri metodi che hai aggiunto in matchconfig
     //todo   da fare tutte le possibili varianti (cioè hanno due carte in mano, pescano prima uno poi l'altro (poi inverti')  <- attento, non si pesca mai quando hanno meno di due carte in mano,  inutile testare i casi una e nessuna carta in mano

    }

    //those configurations don't perform surface cleaning after the cards have been played
    String configPlayCard0 = "0B5B..JCKG2B.1CKS3G.."; //the first char is relevant for who plays (but not updated by playCard), the other 3 are kept just to not break the constructor function, the following matters (apart from piles)
    String configPlayCard1 = "1B5B.JC.KG2B.1CKS3G..";
    String configPlayCard2 = "1B5B.JC1C.KG2B.KS3G.."; //current player not updated, irrelevant for the test logic
    String configPlayCard3 = "1B5B.KG.JC2B.1CKS3G..";
    String configPlayCard4 = "1B5B.KG3G.JC2B.1CKS.."; //current player irrelevant
    String configPlayCard5 = "1B5B.2B.JCKG.1CKS3G..";
    String configPlayCard6 = "1B5B.2BKS.JCKG.1C3G.."; //current player irrelevant

    String configPlayCard10 = "1B5B..JCKG2B.1CKS3G.."; //the first char is relevant for who plays (but not updated by playCard), the other 3 are kept just to not break the constructor function, the following matters (apart from piles)
    String configPlayCard11 = "0B5B.1C.JCKG2B.KS3G..";
    String configPlayCard12 = "0B5B.1CJC.KG2B.KS3G.."; //current player not updated, irrelevant for the test logic
    String configPlayCard13 = "0B5B.3G.JCKG2B.1CKS..";
    String configPlayCard14 = "0B5B.3GKG.JC2B.1CKS.."; //current player irrelevant
    String configPlayCard15 = "0B5B.KS.JCKG2B.1C3G..";
    String configPlayCard16 = "0B5B.KS2B.JCKG.1C3G.."; //current player irrelevant

    String configPlayCard20 = "0B5B..JCKG.1CKS.."; //the first char is relevant for who plays (but not updated by playCard), the other 3 are kept just to not break the constructor function, the following matters (apart from piles)
    String configPlayCard21 = "1B5B.JC.KG.1CKS..";
    String configPlayCard22 = "1B5B.JC1C.KG.KS.."; //current player not updated, irrelevant for the test logic
    String configPlayCard23 = "1B5B.KG.JC.1CKS..";
    String configPlayCard24 = "1B5B.KGKS.JC.1C.."; //current player irrelevant

    String configPlayCard30 = "1B5B..JCKG.1CKS.."; //the first char is relevant for who plays (but not updated by playCard), the other 3 are kept just to not break the constructor function, the following matters (apart from piles)
    String configPlayCard31 = "0B5B.1C.JCKG.KS..";
    String configPlayCard32 = "0B5B.1CJC.KG.KS.."; //current player not updated, irrelevant for the test logic
    String configPlayCard33 = "0B5B.KS.JCKG.1C..";
    String configPlayCard34 = "0B5B.KSKG.JC.1C.."; //current player irrelevant

    String configPlayCard40 = "0B5B..JC.1C..";
    String configPlayCard41 = "1B5B.JC..1C..";
    String configPlayCard42 = "1B5B.JC1C...."; // current player irrelevant

    String configPlayCard50 = "1B5B..JC.1C..";
    String configPlayCard51 = "0B5B.1C.JC...";
    String configPlayCard52 = "0B5B.1CJC...."; // current player irrelevant



    private void testPlayCard(int card, String configStart, String configEnd){
        Briscola2PMatchConfig config =  new Briscola2PMatchConfig(configStart);
        config.playCard(card);
        System.out.println("surf "+config.getSurface() + " expected "+configEnd.split("\\.",6)[1]);
        assertTrue(config.getSurface().toString().equals(configEnd.split("\\.",6)[1]));
        assertTrue(config.getHand(config.PLAYER0).toString().equals(configEnd.split("\\.",6)[2]));
        assertTrue(config.getHand(config.PLAYER1).toString().equals(configEnd.split("\\.",6)[3]));
    }
    @Test
    public void playCard() {
 //TODO QUESTO TEST STA FALLENDO PERCHE' TESTI TANTE FUNZIONALITA' INSIEME, SEMPLIFICA E TESTA PRIMA LE SINGOLE FUNZIONALITA'
        //NOTA: non hai bisogno di pescare per i valori che hai usato per il test, ma dovresti per altri valori todo testare con altri


        testPlayCard(0,configPlayCard0, configPlayCard1);
        testPlayCard(0,configPlayCard1, configPlayCard2);

        testPlayCard(1,configPlayCard0, configPlayCard3);
        testPlayCard(2,configPlayCard3, configPlayCard4);

        testPlayCard(2,configPlayCard0, configPlayCard5);
        testPlayCard(1,configPlayCard5, configPlayCard6);


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

        testPlayCard(0,configPlayCard30, configPlayCard31);
        testPlayCard(0,configPlayCard31, configPlayCard32);

        testPlayCard(1,configPlayCard30, configPlayCard33);
        testPlayCard(1,configPlayCard33, configPlayCard34);

        testPlayCard(0,configPlayCard40, configPlayCard41);
        testPlayCard(0,configPlayCard41, configPlayCard42);

        testPlayCard(0,configPlayCard50, configPlayCard51);
        testPlayCard(0,configPlayCard51, configPlayCard52);


    }

    //current player (first char in the config string) represents the player who played the second card that now is on surface!
    String configWinner1 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.JC1C.KG2B.KS3G..";
    String configWinner2 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.KGHG.KG2B.KS3G..";
    String configWinner3 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.4S6S.KG2B.KS3G.3G4G.7BJB";
    String configWinner4 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.1BKB.KG2B.KS3G.KSJB3G4G.";
    String configWinner5 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.JB3B.KG2B.KS3G..KSJB3G4G";
    String configWinner6 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.6B5B.KG2B.KS3G..";
    String configWinner7 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.7B5B.KG2B.KS3G..";
    String configWinner8 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.JC3B.KG2B.KS3G..";
    String configWinner9 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.6B5G.KG2B.KS3G..";
    String configWinner10 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.7S5G.KG2B.KS3G..";

    String configWinner11 = "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.JC1C.KG2B.KS3G..";
    String configWinner12 = "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.KGHG.KG2B.KS3G..";
    String configWinner13 = "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.4S6S.KG2B.KS3G.7BJB.3G4G";
    String configWinner14 = "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.1BKB.KG2B.KS3G..KSJB3G4G";
    String configWinner15 = "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.JB3B.KG2B.KS3G.KSJB3G4G.";
    String configWinner16 = "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.6B5B.KG2B.KS3G..";
    String configWinner17 = "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.7B5B.KG2B.KS3G..";
    String configWinner18 = "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.JC3B.KG2B.KS3G..";
    String configWinner19 = "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.6B5G.KG2B.KS3G..";
    String configWinner20 = "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.7S5G.KG2B.KS3G..";

    private void testWinner(String configuration, int expectedWinner){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(configuration);
        int winner = config.chooseRoundWinner();
        //System.out.println(winner);
        assertTrue(winner == expectedWinner);
    }
    @Test
    public void chooseWinner(){

        testWinner(configWinner1, Briscola2PMatchConfig.PLAYER1);
        testWinner(configWinner2, Briscola2PMatchConfig.PLAYER0);
        testWinner(configWinner3, Briscola2PMatchConfig.PLAYER1);
        testWinner(configWinner4, Briscola2PMatchConfig.PLAYER0);
        testWinner(configWinner5, Briscola2PMatchConfig.PLAYER1);
        //todo nel caso di pareggio delle briscole? (e.g. gioco due carte da 0 punti, chi vince?) -> io per ora faccio vincere sempre il primo giocatore
        testWinner(configWinner6, Briscola2PMatchConfig.PLAYER0);
        testWinner(configWinner7, Briscola2PMatchConfig.PLAYER0);
        testWinner(configWinner8, Briscola2PMatchConfig.PLAYER1);
        testWinner(configWinner9, Briscola2PMatchConfig.PLAYER0);
        testWinner(configWinner10, Briscola2PMatchConfig.PLAYER0);


        testWinner(configWinner11, Briscola2PMatchConfig.PLAYER0);
        testWinner(configWinner12, Briscola2PMatchConfig.PLAYER1);
        testWinner(configWinner13, Briscola2PMatchConfig.PLAYER0);
        testWinner(configWinner14, Briscola2PMatchConfig.PLAYER1);
        testWinner(configWinner15, Briscola2PMatchConfig.PLAYER0);
        //todo nel caso di pareggio delle briscole? (e.g. gioco due carte da 0 punti, chi vince?) -> io per ora faccio vincere sempre il primo giocatore
        testWinner(configWinner16, Briscola2PMatchConfig.PLAYER1);
        testWinner(configWinner17, Briscola2PMatchConfig.PLAYER1);
        testWinner(configWinner18, Briscola2PMatchConfig.PLAYER0);
        testWinner(configWinner19, Briscola2PMatchConfig.PLAYER1);
        testWinner(configWinner20, Briscola2PMatchConfig.PLAYER1);

    }

    private void testClearSurface(String configuration){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(configuration);
        int winner = config.chooseRoundWinner();
        String tempPileLoser = ""+ config.getPile((winner+1)%2);
        String tempPileWinner = "" + config.getPile(winner);
        config.clearSurface(winner);
        System.out.println(config.getPile(winner)+ " expected " + tempPileWinner+ configuration.split("\\.",6)[1]);
        String newPileWinner = config.getPile(winner).toString();
        String newPileLoser = config.getPile((winner+1)%2).toString();
        System.out.println(tempPileWinner + configuration.split("\\.",6)[1]);
        assertTrue(newPileWinner.equals(tempPileWinner + configuration.split("\\.",6)[1]));
        assertTrue(newPileLoser.equals(tempPileLoser)); //no variation for loser
    }
    @Test
    public void clearSurface(){
        testClearSurface(configWinner1);
        testClearSurface(configWinner2);
        testClearSurface(configWinner3);
        testClearSurface(configWinner4);
        testClearSurface(configWinner5);
        testClearSurface(configWinner6);
        testClearSurface(configWinner7);
        testClearSurface(configWinner8);
        testClearSurface(configWinner9);
        testClearSurface(configWinner10);
        testClearSurface(configWinner11);
        testClearSurface(configWinner12);
        testClearSurface(configWinner13);
        testClearSurface(configWinner14);
        testClearSurface(configWinner15);
        testClearSurface(configWinner16);
        testClearSurface(configWinner17);
        testClearSurface(configWinner18);
        testClearSurface(configWinner19);
        testClearSurface(configWinner20);

    }


    private void testComputeScore(String configuration, int expectedResult0, int expectedResult1){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(configuration);
        int winner = config.chooseRoundWinner();
        config.clearSurface(winner);
        System.out.println(winner);
        System.out.println("Score player0: " + config.computeScore(config.PLAYER0) + " expected: "+expectedResult0);
        System.out.println("Score player1: " +config.computeScore(config.PLAYER1) + " expected: "+expectedResult1);

        assertTrue(config.computeScore(config.PLAYER0) == expectedResult0);
        assertTrue(config.computeScore(config.PLAYER1) == expectedResult1);

    }
    @Test
    public void computeScore(){
        testComputeScore(configWinner1,0,13);
        testComputeScore(configWinner2,7,0);
        testComputeScore(configWinner3,10,2);
        testComputeScore(configWinner4,31,0);
        testComputeScore(configWinner5,0,28);
        testComputeScore(configWinner6,0,0);
        testComputeScore(configWinner7,0,0);
        testComputeScore(configWinner8,0,12);
        testComputeScore(configWinner9,0,0);
        testComputeScore(configWinner10,0,0);


        testComputeScore(configWinner11,13,0);
        testComputeScore(configWinner12,0,7);
        testComputeScore(configWinner13,2,10);
        testComputeScore(configWinner14,0,31);
        testComputeScore(configWinner15,28,0);
        testComputeScore(configWinner16,0,0);
        testComputeScore(configWinner17,0,0);
        testComputeScore(configWinner18,12,0);
        testComputeScore(configWinner19,0,0);
        testComputeScore(configWinner20,0,0);
    }

    // public String swapPlayerRoles() TODO in realtà questo è già fatto da setCurrentPlayer (quando finisce il turno scegli
    // todo chi vince, e lo setti come giocatore corrente (NON LO FAI IN CHOOSE WINNER, MA NEL METODO CHE LO CHIAMA (e che riceve l'intero che indica il vincitore, quindi ti permette di scegliere chi è il prossimo first (o di recuperare il current e continuare ad alternare) inverti chi è il primo a giocare al prossimo turno, altrimenti fai la solita alternanza


    String configToggle1 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.JC1C.KG2B.KS3G..";
    String configToggle2 = "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.JC1C.KG2B.KS3G..";
    @Test
    public void toggleCurrentPlayer(){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(configToggle1);
        config.toggleCurrentPlayer();
        assertTrue(config.getCurrentPlayer() == Integer.parseInt(""+configToggle2.charAt(0)));
        config.toggleCurrentPlayer();
        assertTrue(config.getCurrentPlayer() == Integer.parseInt(""+configToggle1.charAt(0)));
    }

    String easyConfig0 = "0C5S4G6S2C.6G7G.JCKG2B.1CKS3G.KC5C.4B1B";

    @Test
    public void stringConstructorTest(){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(easyConfig0);
        assertTrue(createEasyConfig().equalTo(config));
        config = new Briscola2PMatchConfig(startingConfig);
        assertTrue(createStartingConfig().equalTo(config));
    }

    @Test
    public void toStringTest(){
        System.out.println(createEasyConfig().toString());

        assertTrue(easyConfig0.equals(createEasyConfig().toString()));
        assertTrue(startingConfig.equals(createStartingConfig().toString()));
        assertTrue(config1.equals(new Briscola2PMatchConfig(config1).toString()));
        assertTrue(config2.equals(new Briscola2PMatchConfig(config2).toString()));
        assertTrue(config3.equals(new Briscola2PMatchConfig(config3).toString()));
    }

    @Test
    public void equalToTest(){
        Briscola2PMatchConfig config1 = new Briscola2PMatchConfig(easyConfig0);
        Briscola2PMatchConfig config2 = new Briscola2PMatchConfig(easyConfig0);
        Briscola2PMatchConfig config3 = new Briscola2PMatchConfig(startingConfig);
        Briscola2PMatchConfig config4 = new Briscola2PMatchConfig(startingConfig);

        assertTrue(config1.equalTo(config2));
        assertTrue(!config1.equalTo(config3));
        assertTrue(config3.equalTo(config4));

    }

   //Todo drawCardsNewRound da testare
}
