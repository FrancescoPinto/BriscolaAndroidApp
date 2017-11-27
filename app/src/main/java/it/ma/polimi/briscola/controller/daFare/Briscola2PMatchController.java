package it.ma.polimi.briscola.controller.daFare;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.forfullrelease.ai.Briscola2PAIPlayer;
import it.ma.polimi.briscola.forfullrelease.ai.Briscola2PAIRandomPlayer;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PPile;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PSurface;

/**
 * Created by utente on 13/11/17.
 */

public class Briscola2PMatchController {


    // private MatchStatus matchStatus; //todo necessario? credo di sì! E' per forzare il seguire le regole <- invece no, perché faccio un controller per la GUI e uno non per la gui
    private Briscola2PAIPlayer player1 = new Briscola2PAIRandomPlayer();
    private Briscola2PMatchActivity activity;

    private Briscola2PMatchConfig config;


    /**
     * Instantiates a new Briscola 2 p match no gui controller. If this constructor is used, then should be initialized programmatically by invoking either startNewmatch or resumeMatch
     */
    public Briscola2PMatchController(Briscola2PMatchActivity activity){
        this.activity = activity;
    }


    /**
     * Start new match (initializes the deck, chooses first player, initializes player's hands and the briscola)
     */
    public void startNewMatch(){
        config = new Briscola2PMatchConfig();
        config.initializeNewDeck();
        config.initializeFirstPlayer();
        config.initializePlayersHands();
        config.initializeBriscola();
        config.setSurface( new Briscola2PSurface(""));
        config.setPile(config.PLAYER0, new Briscola2PPile(""));
        config.setPile(config.PLAYER1, new Briscola2PPile(""));

        activity.initializeNewDeck();
        activity.initializeFirstPlayer(config.getCurrentPlayer());
        activity.initializePlayersHands(config.getCurrentPlayer(),config.getHand(config.PLAYER0), config.getHand(config.PLAYER1));
        /*activity.initializeBriscola(config.getBriscolaSuit());*/ //todo reinserire



    }

   //todo, tutto il resto l'ho salvato in OldMatchController

}
