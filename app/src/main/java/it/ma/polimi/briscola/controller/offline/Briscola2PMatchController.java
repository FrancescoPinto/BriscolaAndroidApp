package it.ma.polimi.briscola.controller.offline;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.ai.Briscola2PAIDumbGreedyPlayer;
import it.ma.polimi.briscola.ai.Briscola2PAIPlayer;
import it.ma.polimi.briscola.ai.Briscola2PAIRandomPlayer;
import it.ma.polimi.briscola.ai.Briscola2PAISmarterGreedyPlayer;
import it.ma.polimi.briscola.model.briscola.statistics.Briscola2PMatchRecord;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PPile;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PSurface;
import it.ma.polimi.briscola.persistency.SettingsManager;
import it.ma.polimi.briscola.view.fragments.Briscola2PMatchFragment;

/**
 * Created by utente on 03/12/17.
 */

public class Briscola2PMatchController implements Briscola2PController{

        private Briscola2PMatchConfig config;
        // private MatchStatus matchStatus; //todo necessario? credo di sì! E' per forzare il seguire le regole <- invece no, perché faccio un controller per la GUI e uno non per la gui
        private Briscola2PAIPlayer player1;
        private Briscola2PMatchFragment matchFragment;
        public boolean playing = false;
        private boolean done = false;

        public Briscola2PMatchController(Briscola2PMatchFragment matchFragment,int difficulty){
            super();
            this.matchFragment = matchFragment;
            Log.d("TAG", "difficulty "+ difficulty);
            switch(difficulty) {
                case SettingsManager.EASY:
                    player1 = new Briscola2PAIRandomPlayer();
                    break;
                case SettingsManager.MEDIUM:
                    player1 = new Briscola2PAIDumbGreedyPlayer();
                    break;
                case SettingsManager.HARD:
                    player1 = new Briscola2PAISmarterGreedyPlayer();
                    break;
                case SettingsManager.VERY_HARD:
                    player1 = new Briscola2PAISmarterGreedyPlayer(); //todo, metti IA ancora più intelligente (se hai tempo)
                    break;
            }
        }


        @Override
        public void setIsPlaying(boolean isPlaying) {
            playing = isPlaying;
        }


    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public int getCurrentPlayer() {
        return config.getCurrentPlayer();
    }

    @Override
    public int countCardsOnSurface() {
        return config.countCardsOnSurface();
    }

    @Override
        public void startNewMatch(){
            config = new Briscola2PMatchConfig();
            config.initializeNewDeck();
            config.initializeFirstPlayer();
            config.initializePlayersHands();
            config.initializeBriscola();
            config.setSurface( new Briscola2PSurface(""));
            config.setPile(config.PLAYER0, new Briscola2PPile(""));
            config.setPile(config.PLAYER1, new Briscola2PPile(""));

            //List<AnimatorSet> animators = new ArrayList<>();
            AnimatorSet initializationSequence = new AnimatorSet();
            AnimatorSet dealFirstHand = matchFragment.getDealFirstHandAnimatorSet(config.getCurrentPlayer(), config.getHands());
            AnimatorSet initializeBriscola = matchFragment.getInitializeBriscolaAnimatorSet(config.getDeck().getCard(config.getDeck().size()-1));
            AnimatorSet displayCurrentPlayer =matchFragment.displayCurrentPlayer(config.getCurrentPlayer());
            //animators.add(dealFirstHand);
            //animators.add(initializeBriscola);
            matchFragment.showToast("Current player is "+ config.getCurrentPlayer());


            initializeBriscola.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {}
                @Override
                public void onAnimationEnd(Animator animator) {
                    if(config.getCurrentPlayer() == config.PLAYER1){
                        playFirstCard(player1.chooseMove(config));

                    }
                }
                @Override
                public void onAnimationCancel(Animator animator) {}
                @Override
                public void onAnimationRepeat(Animator animator) {}
            });

            initializationSequence.playSequentially(dealFirstHand,initializeBriscola,displayCurrentPlayer);
            initializationSequence.start();

           /* if(config.getCurrentPlayer() == config.PLAYER1){
                int aiChoice = player1.chooseMove(config);
                AnimatorSet playFirstCard = playFirstCard(aiChoice);
                initializationSequence.playSequentially(displayCurrentPlayer,dealFirstHand,initializeBriscola, playFirstCard);

            }
            else{

            }

            //activity.initializeMatch(); todo <- DECOMMENTA QUESTO, e usalo per invocare tutte le animazioni corrispondenti ai seguenti
            //questo metodo orchestra l'esecuzione delle animazioni previste da:
            //activity.initializeNewDeck(config.getDeck().getCardList()); //todo, altero il pattern di De Bernardi (lui fa initializeNewDeck(activity) e tale metodo è in questa classe ... ma così il controller deve conoscere la rappresentazione della view ... bruttissimo ... non è meglio invocare un metodo della view che se ne occupa?
            //activity.initializeFirstPlayer(config.getCurrentPlayer());
           // activity.initializePlayersHands(config.getCurrentPlayer(),config.getHand(config.PLAYER0).getCardList(), config.getHand(config.PLAYER1).getCardList());
           // activity.initializeBriscola(config.getBriscolaString());

        }

        public void resumeMatch(String config)
        { //da un salvataggio
            this.config = new Briscola2PMatchConfig(config);
            //activity.resumeMatch(); todo implementare un metodo che inizializza correttamente tutte le componenti dell'interfaccia partendo dalla configurazione già avviata di un match
        }

        public void makeMove(int move){

          //  try {
             /*   if (config.countCardsOnSurface() == 0) {
                    playFirstCard(activity,move);
                } else if (config.countCardsOnSurface() == 1) {
                    playSecondCard(activity,move);
                }
/*
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
*/
        }

        public AnimatorSet generatePlayFirstCardAnimatorSet(int move){
            config.playCard(move);
            AnimatorSet playCard = matchFragment.playFirstCard(move, config.getCurrentPlayer());
            config.toggleCurrentPlayer();
            AnimatorSet displayCurrentPlayer = matchFragment.displayCurrentPlayer(config.getCurrentPlayer());
            AnimatorSet playFirstCard = new AnimatorSet();
            playFirstCard.playSequentially(playCard, displayCurrentPlayer);
            playFirstCard.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {}
                @Override
                public void onAnimationEnd(Animator animator) {
                    if(config.getCurrentPlayer() == config.PLAYER1){
                        playSecondCard(player1.chooseMove(config));
                    }
                }
                @Override
                public void onAnimationCancel(Animator animator) {}
                @Override
                public void onAnimationRepeat(Animator animator) {}
            });
            return playFirstCard;
        }

        @Override
        public void playFirstCard(int move){
           generatePlayFirstCardAnimatorSet(move).start();
        }

        public AnimatorSet generatePlaySecondCardAnimatorSet(int move){
            config.playCard(move);
            AnimatorSet playCard = matchFragment.playSecondCard(move, config.getCurrentPlayer());
            AnimatorSet playSecondCard = new AnimatorSet();
            int roundWinner = config.chooseRoundWinner(); //choose round winner
            config.clearSurface(roundWinner);
            AnimatorSet cleanSurface = matchFragment.cleanSurface(roundWinner);
            config.setCurrentPlayer(roundWinner);
            matchFragment.showToast("Current player = " + config.getCurrentPlayer() + ", roundWInner = "+roundWinner);
            AnimatorSet displayCurrentPlayer = matchFragment.displayCurrentPlayer(config.getCurrentPlayer());

            displayCurrentPlayer.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {}
                @Override
                public void onAnimationEnd(Animator animator) {
                    AnimatorSet closeTurnAnimation = new AnimatorSet();
                    List<Animator> animators = new ArrayList<Animator>();
                    if(config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the match is finished, choose winner
                        switch(config.chooseMatchWinner()){
                            case Briscola2PMatchConfig.PLAYER0:  matchFragment.displayMatchWinner(Briscola2PMatchConfig.PLAYER0, config.computeScore(Briscola2PMatchConfig.PLAYER0)); break;
                            case Briscola2PMatchConfig.PLAYER1: matchFragment.displayMatchWinner(Briscola2PMatchConfig.PLAYER1, config.computeScore(Briscola2PMatchConfig.PLAYER1)); break;
                            case Briscola2PMatchConfig.DRAW: matchFragment.displayMatchWinner(Briscola2PMatchConfig.DRAW, Briscola2PMatchRecord.totPoints/2); break;
                            default: throw new RuntimeException("Error while computing the winner");
                        }
                        //todo: va fatto il salavataggio dei dati per l'online? però il server non permette di salvare l'id remoto ...



                    }else if (!config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the deck is empty, but players have cards in hand,don't do anything
                    }else if(!config.isDeckEmpty()){ //if deck is not empty, draw cards new round
                        boolean noDraw = false;
                        boolean lastDraw = false;
                        if(config.getDeck().size() == 2)
                            lastDraw = true;
                        if(config.getDeck().size() == 0)
                            noDraw = true;

                        config.drawCardsNewRound();
                        //cards have been collected from surface, new cards have been drawn
                        if(noDraw){
                            matchFragment.enablePlayer0CardsTouch(config.getHands(),config.getCurrentPlayer());
                        }else {
                            AnimatorSet drawCards = matchFragment.drawCardsNewRound(config.getHands(), config.getCurrentPlayer(), lastDraw);
                            animators.add(drawCards);
                        }
                    }

                    if(config.getCurrentPlayer() == config.PLAYER1){
                        animators.add(generatePlayFirstCardAnimatorSet(player1.chooseMove(config)));
                    }

                    closeTurnAnimation.playSequentially(animators);
                    closeTurnAnimation.start();

                   /*TODO ATTENZIONE, c'è un bug' qui per cui quando il primo turno è dell'altro giocatore' e pesca butta due carte sulla superficie, di cui una è giocata, l'altra'
                            praticamente è in mano, solo che è messa nel posto sbagliato!*/
                }
                @Override
                public void onAnimationCancel(Animator animator) {}
                @Override
                public void onAnimationRepeat(Animator animator) {}
            });
            playSecondCard.playSequentially(playCard, cleanSurface,displayCurrentPlayer);
            return playSecondCard;


            //todo SISTEMARE QUEL CHE SEGUE
            //todo: riattiva questo codice
           /* if(config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the match is finished, choose winner
                switch(config.chooseMatchWinner()){
                    case Briscola2PMatchConfig.PLAYER0: activity.displayMatchOutcome("WINNER" + Briscola2PMatchConfig.PLAYER0 + config.computeScore(Briscola2PMatchConfig.PLAYER0)); break;
                    case Briscola2PMatchConfig.PLAYER1: activity.displayMatchOutcome("WINNER" + Briscola2PMatchConfig.PLAYER1 + config.computeScore(Briscola2PMatchConfig.PLAYER1)); break;
                    case Briscola2PMatchConfig.DRAW: activity.displayMatchOutcome("DRAW"); break;
                    default: throw new RuntimeException("Error while computing the winner");
                }
            }else if (!config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the deck is empty, but players have cards in hand,don't do anything
            }else if(!config.isDeckEmpty()){ //if deck is not empty, draw cards new round
                config.drawCardsNewRound();
                //cards have been collected from surface, new cards have been drawn
            }*/
        }

        public void playSecondCard(int move){
            generatePlaySecondCardAnimatorSet(move).start();
        }


/*
        private void closeRound(){
            int roundWinner = config.chooseRoundWinner();
            config.clearSurface(roundWinner);
            activity.clearSurface(roundWinner, config.getSurface().getCardList());
            config.setCurrentPlayer(roundWinner);
            activity.setCurrentPlayer(config.getCurrentPlayer());
        }
*/
        public Briscola2PMatchConfig getConfig() {
            return config;
        }

    @Override
    public int getHandSize(int playerIndex) {
        return config.getHand(playerIndex).size();
    }
}
