package it.ma.polimi.briscola.controller.daFare;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.Briscola2PMatchActivity;
import it.ma.polimi.briscola.forfullrelease.ai.Briscola2PAIPlayer;
import it.ma.polimi.briscola.forfullrelease.ai.Briscola2PAIRandomPlayer;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PPile;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PSurface;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;

/**
 * Created by utente on 03/12/17.
 */

public class Briscola2PMatchController {

        private Briscola2PMatchConfig config;
        // private MatchStatus matchStatus; //todo necessario? credo di sì! E' per forzare il seguire le regole <- invece no, perché faccio un controller per la GUI e uno non per la gui
        private Briscola2PAIPlayer player1 = new Briscola2PAIRandomPlayer();
        private Briscola2PMatchActivity activity;
        public boolean playing = false;
        private boolean done = false;

        public Briscola2PMatchController(Briscola2PMatchActivity activity){
            super();
            this.activity = activity;
        }

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
            AnimatorSet dealFirstHand = activity.getDealFirstHandAnimatorSet(config.getCurrentPlayer(), config.getHands());
            AnimatorSet initializeBriscola = activity.getInitializeBriscolaAnimatorSet(config.getDeck().getCard(config.getDeck().size()-1));
            AnimatorSet displayCurrentPlayer =activity.displayCurrentPlayer(config.getCurrentPlayer());
            //animators.add(dealFirstHand);
            //animators.add(initializeBriscola);
            activity.showToast("Current player is "+ config.getCurrentPlayer());


            initializeBriscola.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {}
                @Override
                public void onAnimationEnd(Animator animator) {
                    if(config.getCurrentPlayer() == config.PLAYER1){
                        playFirstCard(player1.chooseMove(config)).start();

                    }
                }
                @Override
                public void onAnimationCancel(Animator animator) {}
                @Override
                public void onAnimationRepeat(Animator animator) {}
            });

            initializationSequence.playSequentially(displayCurrentPlayer,dealFirstHand,initializeBriscola);
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
           // activity.initializeBriscola(config.getBriscolaSuit());

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

        public AnimatorSet playFirstCard(int move){
            config.playCard(move);
            AnimatorSet playCard = activity.playFirstCard(move, config.getCurrentPlayer());
            AnimatorSet adjust = activity.adjustCards(move, config.getCurrentPlayer());
            config.toggleCurrentPlayer();
            AnimatorSet displayCurrentPlayer = activity.displayCurrentPlayer(config.getCurrentPlayer());
            AnimatorSet playFirstCard = new AnimatorSet();
            playFirstCard.playSequentially(playCard,adjust, displayCurrentPlayer);
            playFirstCard.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {}
                @Override
                public void onAnimationEnd(Animator animator) {
                     if(config.getCurrentPlayer() == config.PLAYER1){
                        playSecondCard(player1.chooseMove(config)).start();
                    }
                }
                @Override
                public void onAnimationCancel(Animator animator) {}
                @Override
                public void onAnimationRepeat(Animator animator) {}
            });
            return playFirstCard;
        }

        public AnimatorSet playSecondCard(int move){
            config.playCard(move);
            AnimatorSet playCard = activity.playSecondCard(move, config.getCurrentPlayer());
            AnimatorSet adjust = activity.adjustCards(move, config.getCurrentPlayer());
            AnimatorSet playSecondCard = new AnimatorSet();
            int roundWinner = config.chooseRoundWinner(); //choose round winner
            config.clearSurface(roundWinner);
            AnimatorSet cleanSurface = activity.cleanSurface(roundWinner);
            config.setCurrentPlayer(roundWinner);
            activity.showToast("Current player = " + config.getCurrentPlayer() + ", roundWInner = "+roundWinner);
            AnimatorSet displayCurrentPlayer = activity.displayCurrentPlayer(config.getCurrentPlayer());

            displayCurrentPlayer.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {}
                @Override
                public void onAnimationEnd(Animator animator) {
                    AnimatorSet closeTurnAnimation = new AnimatorSet();
                    List<Animator> animators = new ArrayList<Animator>();
                    if(config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the match is finished, choose winner
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
                        animators.add(activity.drawCardsNewRound(config.getHands(), config.getCurrentPlayer()));
                    }

                    if(config.getCurrentPlayer() == config.PLAYER1){
                        animators.add(playFirstCard(player1.chooseMove(config)));
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
            playSecondCard.playSequentially(playCard,adjust, cleanSurface,displayCurrentPlayer);
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
    }
