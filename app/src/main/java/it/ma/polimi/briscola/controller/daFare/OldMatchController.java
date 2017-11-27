package it.ma.polimi.briscola.controller.daFare;

import it.ma.polimi.briscola.forfullrelease.ai.Briscola2PAIPlayer;
import it.ma.polimi.briscola.forfullrelease.ai.Briscola2PAIRandomPlayer;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PPile;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PSurface;

/**
 * Created by utente on 27/11/17.
 */

public class OldMatchController {


        // private MatchStatus matchStatus; //todo necessario? credo di sì! E' per forzare il seguire le regole <- invece no, perché faccio un controller per la GUI e uno non per la gui
        private Briscola2PAIPlayer player1 = new Briscola2PAIRandomPlayer();
        private Briscola2PMatchActivity activity;

        private Briscola2PMatchConfig config;


        /**
         * Instantiates a new Briscola 2 p match no gui controller. If this constructor is used, then should be initialized programmatically by invoking either startNewmatch or resumeMatch
         */
        public OldMatchController(Briscola2PMatchActivity activity){
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
            activity.initializeBriscola(config.getBriscolaSuit());

       /* if(config.getCurrentPlayer() == config.PLAYER1){ //todo, riaggiungere dopo
            makePlayer1Move(player1.chooseMove(config));
        }*/

        }

        /**
         * Resume match from a given configuration.
         *
         * @param config the configuration, format as specified in the slides
         */
        public void resumeMatch(String config){ //da un salvataggio
            this.config = new Briscola2PMatchConfig(config);
            //todo, aggiungere tutte le chiamate per inizializzare l'interfaccia
        }


   /* public void makePlayer1Move(int move){ //todo, riaggiungere dopo
        if(config.countCardsOnSurface() == 0) { //if the roud has just begun

            config.playCard(move); //play first card
            config.toggleCurrentPlayer();
            activity.playFirstCard(move, config.PLAYER1);
            activity.setCurrentPlayer(config.PLAYER0);


        }else if (config.countCardsOnSurface() == 1){ //if the first card has already been played
            config.playCard(move); //play second card
            activity.playSecondCard(move,config.PLAYER1);

        }

        if (config.countCardsOnSurface() == 2){ //if two cards are on surface, close the round

            int roundWinner = config.chooseRoundWinner(); //choose round winner
            config.clearSurface(roundWinner); //put cards on surface to the winner's pile
            config.setCurrentPlayer(roundWinner); //choose the next round winner
            activity.clearSurface(roundWinner, config.getSurface());
            activity.setCurrentPlayer(roundWinner);

            if(config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the match is finished, choose winner
                onMatchEnd();
            }else if (!config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the deck is empty, but players have cards in hand, return new config
                if(config.getCurrentPlayer() == config.PLAYER1){
                    makePlayer1Move(player1.chooseMove(config));
                }
                return;
            }else if(!config.isDeckEmpty()){ //if deck is not empty, return new config
                config.drawCardsNewRound();
                //activity.drawCardsNewRound(); //todo, implementa drawcardsnewround
                if(config.getCurrentPlayer() == config.PLAYER1){
                    makePlayer1Move(player1.chooseMove(config));
                }
                return;  //cards have been collected from surface, new cards have been drawn
            }

        }

        return;
    }

    public void makePlayer0Move(int move){ //todo riaggiungere dopo
        if(config.countCardsOnSurface() == 0) { //if the roud has just begun

            config.playCard(move); //play first card
            config.toggleCurrentPlayer();
            activity.playFirstCard(move, config.PLAYER0);
            activity.setCurrentPlayer(config.PLAYER1);


        }else if (config.countCardsOnSurface() == 1){ //if the first card has already been played
            config.playCard(move); //play second card
            activity.playSecondCard(move,config.PLAYER0);

        }

        if (config.countCardsOnSurface() == 2){ //if two cards are on surface, close the round

            int roundWinner = config.chooseRoundWinner(); //choose round winner
            config.clearSurface(roundWinner); //put cards on surface to the winner's pile
            config.setCurrentPlayer(roundWinner); //choose the next round winner
            activity.clearSurface(roundWinner, config.getSurface());
            activity.setCurrentPlayer(roundWinner);


            if(config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the match is finished, choose winner
                onMatchEnd();
            }else if (!config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the deck is empty, but players have cards in hand, return new config
                if(config.getCurrentPlayer() == config.PLAYER1){
                    makePlayer1Move(player1.chooseMove(config));
                }
                return;

            }else if(!config.isDeckEmpty()){ //if deck is not empty, return new config

                config.drawCardsNewRound();
                //activity.drawCardsNewRound(); todo implementa drawCardsNewround
                if(config.getCurrentPlayer() == config.PLAYER1){
                    makePlayer1Move(player1.chooseMove(config));
                }
                return;  //cards have been collected from surface, new cards have been drawn
            }

        }

        return;
    }

    public void onMatchEnd(){
        switch(config.chooseMatchWinner()){
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
    }






    /**
     * Make a single move specified by an input.
     *
     * @param move          the move (shoudld be a valid index of card in hand of the current player
     * @return the string representing either the new configuration (if the match is not finished), the outcome of the match (if the match is finished), an error message (if a fatal error occurs)
     */
   /* public void makeMove(int move){

        if(config.countCardsOnSurface() == 0) { //if the roud has just begun
            config.playCard(move); //play first card
            activity.playFirstCard(move, config.getCurrentPlayer());

            config.toggleCurrentPlayer();
            activity.setCurrentPlayer(config.getCurrentPlayer());

            if(config.getCurrentPlayer() == config.PLAYER1){ //If the first player is PLAYER0 then PLAYER1 should make the following move
                config.playCard(player1.chooseMove(config));
                activity.playSecondCard(move, config.getCurrentPlayer());

            }

        }else if (config.countCardsOnSurface() == 1){ //if the first card has already been played
            config.playCard(move); //play second card
            activity.playSecondCard(move,config.getCurrentPlayer());

        }

        if (config.countCardsOnSurface() == 2){ //if two cards are on surface, close the round

            int roundWinner = config.chooseRoundWinner(); //choose round winner
            config.clearSurface(roundWinner); //put cards on surface to the winner's pile
            activity.clearSurface(roundWinner, config.getSurface());
            config.setCurrentPlayer(roundWinner); //choose the next round winner
            activity.setCurrentPlayer(config.getCurrentPlayer());


            if(config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the match is finished, choose winner
                switch(config.chooseMatchWinner()){
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
            }else if (!config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the deck is empty, but players have cards in hand, return new config

                if(config.getCurrentPlayer() == config.PLAYER1){ //If the first player of the first round is PLAYER1, play the first card

                    config.playCard(player1.chooseMove(config));
                    activity.playFirstCard(player1.chooseMove(config), config.getCurrentPlayer());
                    config.toggleCurrentPlayer();
                    activity.setCurrentPlayer(config.getCurrentPlayer());
                }
                return;

            }else if(!config.isDeckEmpty()){ //if deck is not empty, return new config

                config.drawCardsNewRound();
                if(config.getCurrentPlayer() == config.PLAYER1){ //If the first player of the first round is PLAYER1, play the first card

                    config.playCard(player1.chooseMove(config));
                    activity.playFirstCard(player1.chooseMove(config), config.getCurrentPlayer());
                    config.toggleCurrentPlayer();
                    activity.setCurrentPlayer(config.getCurrentPlayer());
                }
                return;  //cards have been collected from surface, new cards have been drawn
            }

        }

        return;

    }



    private void playSecondCard(MatchGUIPrototype activity, int move){
        config.playCard(move);
    }*/

/*
    public Briscola2PMatchController(){
        super();
    }

    public void startNewMatch(MatchGUIPrototype activity){
        config = new Briscola2PMatchConfig();
        config.initializeNewDeck();
        activity.initializeNewDeck(config.getDeck()); //todo, altero il pattern di De Bernardi (lui fa initializeNewDeck(activity) e tale metodo è in questa classe ... ma così il controller deve conoscere la rappresentazione della view ... bruttissimo ... non è meglio invocare un metodo della view che se ne occupa?
        config.initializeFirstPlayer();
        activity.initializeFirstPlayer(config.getCurrentPlayer());
        config.initializePlayersHands();
        activity.initializePlayersHands(config.getCurrentPlayer(),config.getHand(config.PLAYER0), config.getHand(config.PLAYER1));
        config.initializeBriscola();
        activity.initializeBriscola(config.getBriscolaSuit());
        config.setSurface( new Briscola2PSurface(""));
        config.setPile(config.PLAYER0, new Briscola2PPile(""));
        config.setPile(config.PLAYER1, new Briscola2PPile(""));

        if(config.getCurrentPlayer() == config.PLAYER1)
            playFirstCard(activity,player1.chooseMove(config));



    }

    public void resumeMatch(MatchGUIPrototype activity, String config)
    { //da un salvataggio
        this.config = new Briscola2PMatchConfig(config);
        if(this.config.getCurrentPlayer() == this.config.PLAYER1)
            playFirstCard(activity,player1.chooseMove(this.config));

        //activity.resumeMatch(); todo implementare un metodo che inizializza correttamente tutte le componenti dell'interfaccia partendo dalla configurazione già avviata di un match
    }

    public void makeMove(MatchGUIPrototype activity, int move){

        try {
            //casi possibili:
            //giocatore0 è il primo, poi giocatore1
            //giocatore1 è il primo, poi giocatore0
            if (config.countCardsOnSurface() == 0) { //giocatore0 è il primo, allora il listener invoca il metodo e fa così
                playFirstCard(activity,move);
                playSecondCard(activity, player1.chooseMove(config));
            } else if (config.countCardsOnSurface() == 1) { //giocatore0 è il secondo, allora il listener invoca il metodo e arriva qui
                playSecondCard(activity,move); //todo PROBLEMA: ora devi far sì che anche quando il giocatore0 è secondo l'AI faccia la sua mossa: implementalo nello switch che segue che determina il winner <- questo va bene solo una volta che la partita è avviata! quindi ora ti serve quando inizia
            }

            if (config.countCardsOnSurface() == 2) {

                closeRound(activity);

                if (config.arePlayersHandsEmpty() && config.isDeckEmpty()) {
                    switch (config.chooseMatchWinner()) {
                        case Briscola2PMatchConfig.PLAYER0:
                            activity.displayMatchWinner(Briscola2PMatchConfig.PLAYER0, config.computeScore(Briscola2PMatchConfig.PLAYER0));
                            return;
                        case Briscola2PMatchConfig.PLAYER1:
                            activity.displayMatchWinner(Briscola2PMatchConfig.PLAYER1, config.computeScore(Briscola2PMatchConfig.PLAYER1)); playFirstCard(activity, player1.chooseMove(config));
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



    public Briscola2PMatchConfig getConfig() {
        return config;
    }

*/


}
