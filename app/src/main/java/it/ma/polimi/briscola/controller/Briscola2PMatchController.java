package it.ma.polimi.briscola.controller;

import it.ma.polimi.briscola.MatchGUIPrototype;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.ai.Briscola2PAIPlayer;
import it.ma.polimi.briscola.model.briscola.twoplayers.ai.Briscola2PAIRandomPlayer;

/**
 * Created by utente on 13/11/17.
 */

public class Briscola2PMatchController {


    private Briscola2PMatchConfig config;
    // private MatchStatus matchStatus; //todo necessario? credo di sì! E' per forzare il seguire le regole <- invece no, perché faccio un controller per la GUI e uno non per la gui
    private Briscola2PAIPlayer player1 = new Briscola2PAIRandomPlayer();


    public Briscola2PMatchController(){
        super();
    }

    public void startNewMatch(MatchGUIPrototype activity){
        config = new Briscola2PMatchConfig();
        config.initializeNewDeck();
        activity.initializeNewDeck(config.getDeck().getCardList()); //todo, altero il pattern di De Bernardi (lui fa initializeNewDeck(activity) e tale metodo è in questa classe ... ma così il controller deve conoscere la rappresentazione della view ... bruttissimo ... non è meglio invocare un metodo della view che se ne occupa?
        config.initializeFirstPlayer();
        activity.initializeFirstPlayer(config.getCurrentPlayer());
        config.initializePlayersHands();
        activity.initializePlayersHands(config.getCurrentPlayer(),config.getHand(config.PLAYER0).getCardList(), config.getHand(config.PLAYER1).getCardList());
        config.initializeBriscola();
        activity.initializeBriscola(config.getBriscolaSuit());
    }

    public void resumeMatch(MatchGUIPrototype activity, String config)
    { //da un salvataggio
        this.config = new Briscola2PMatchConfig(config);
        //activity.resumeMatch(); todo implementare un metodo che inizializza correttamente tutte le componenti dell'interfaccia partendo dalla configurazione già avviata di un match
    }

    public void makeMove(MatchGUIPrototype activity, int move){

        try {
            if (config.countCardsOnSurface() == 0) {
                playFirstCard(activity,move);
            } else if (config.countCardsOnSurface() == 1) {
                playSecondCard(activity,move);
            }

            if (config.countCardsOnSurface() == 2) {

                closeRound(activity);

                if (config.arePlayersHandsEmpty() && config.isDeckEmpty()) {
                    switch (config.chooseMatchWinner()) {
                        case Briscola2PMatchConfig.PLAYER0:
                            activity.displayMatchWinner(Briscola2PMatchConfig.PLAYER0, config.computeScore(Briscola2PMatchConfig.PLAYER0));
                            return;
                        case Briscola2PMatchConfig.PLAYER1:
                            activity.displayMatchWinner(Briscola2PMatchConfig.PLAYER1, config.computeScore(Briscola2PMatchConfig.PLAYER1));
                            return;
                        case Briscola2PMatchConfig.DRAW:
                            activity.displayDraw();
                            return;
                        default:
                            throw new RuntimeException("Error while computing the winner");
                    }
                    //  }else if (config.arePlayersHandsEmpty() && !config.isDeckEmpty()) { //todo non è compito di questo metodo controllare la consistenza! vedi il metodo che devi fare in matchConfig
                    //     throw new RuntimeException("Players Hands are Empty but Deck is not Empty: inconsistent configuration");
                } else if (!config.arePlayersHandsEmpty() && config.isDeckEmpty()) {
                    return;
                } else if (!config.isDeckEmpty()) {
                    config.drawCardsNewRound();
                    return;  //cards have been colleted from surface, new cards have been drawn
                }

            }
        }catch(Exception e) {
            e.printStackTrace();
            activity.onShowMessage(e.getMessage()); //todo, chiedere a De Bernardi se è meglio il suo approccio (ovvero il controller manipola internamente gli attributi della view, o l'alternativa a cui ho pensato (ovvero il controller invoca i metodi della activity, che manipolano la view
        }

    }

    private void playFirstCard(MatchGUIPrototype activity, int move){
        config.playCard(move);
        activity.playCard(move, config.getCurrentPlayer());
        config.toggleCurrentPlayer();
        activity.setCurrentPlayer(config.getCurrentPlayer());
        if(config.getCurrentPlayer() == config.PLAYER1){
            makeMove(activity, player1.chooseMove(config));
        }
    }

    private void playSecondCard(MatchGUIPrototype activity, int move){
        config.playCard(move);
        activity.playCard(move,config.getCurrentPlayer());
    }

    private void closeRound(MatchGUIPrototype activity){
        int roundWinner = config.chooseRoundWinner();
        config.clearSurface(roundWinner);
        activity.clearSurface(roundWinner, config.getSurface().getCardList());
        config.setCurrentPlayer(roundWinner);
        activity.setCurrentPlayer(config.getCurrentPlayer());
    }

    public Briscola2PMatchConfig getConfig() {
        return config;
    }



}
