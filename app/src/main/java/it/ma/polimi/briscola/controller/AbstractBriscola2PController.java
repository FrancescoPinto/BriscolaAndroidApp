package it.ma.polimi.briscola.controller;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.util.Log;
import android.view.animation.Animation;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PFullMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.view.fragments.Briscola2PMatchFragment;

/**
 * Created by utente on 31/12/17.
 */
public abstract class AbstractBriscola2PController implements Briscola2PController {
    /**
     * The Match fragment.
     */
    Briscola2PMatchFragment matchFragment;
    /**
     * The Config.
     */
    Briscola2PMatchConfig config;
    /**
     * Playing attribute, used to keep the controller synchronized with the GUI status. In particular, it allows to know whether the user can see, in the GUI, a condition in which he/she should be able to play (e.g. has been notified it is his/her turn)
     */
    public boolean playing = false;

    @Override
    public void setIsPlaying(boolean isPlaying) {
        playing = isPlaying;
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public int countCardsOnSurface() {
        return config.countCardsOnSurface();
    }

    @Override
    public int getCurrentPlayer() {
        return config.getCurrentPlayer();
    }

    @Override
    public Integer getTurnsElapsed(){
        if(config == null) //if config hasn't been allready initialized
            return null;

        return config.getNumberTurnsElapsed();
    }


    /**
     * Gets config.
     *
     * @return the config
     */
    public Briscola2PMatchConfig getConfig() {
        return config;
    }

    /**
     * Perform the same operations at a GUI level by first building the animations (the structure of the animations is internally handled by the fragment to keep this class decoupled from the fragment)
     *then setting listeners on those animations (if needed), scheduling their execution and starting them
     */
    void startInitializationAnimationSequence(){
        AnimatorSet initializationSequence = new AnimatorSet();
        AnimatorSet dealFirstHand = matchFragment.getDealFirstHandAnimatorSet(config.getCurrentPlayer(), config.getHands());
        AnimatorSet initializeBriscola = matchFragment.getInitializeBriscolaAnimatorSet(config.getBriscola());

        //if first player of the first round is PLAYER1, then should invoke the AI to let her play
        initializeBriscola.addListener(getPlayer1Listener());

        //if first player of first round is PLAYER0, enable him to play
        if(config.getCurrentPlayer() == config.PLAYER0) {
            AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
            //schedule the animations
            initializationSequence.playSequentially(dealFirstHand, initializeBriscola, displayIsPlayer0Turn);
        }else{ //else, don't enable him to play
            //schedule the animations
            initializationSequence.playSequentially(dealFirstHand, initializeBriscola);
        }

        //play the animation sequence
        initializationSequence.start();

    }

    /**
     * Method responsible of handling the business logic of a first-card play during a turn, also produces the related animation.
     *
     * @param firstCard the first card to be played, should be either a String (passed from RESTClient callbacks) or an Integer (as usual)
     * @return the animator set representing the first card being played in the turn
     */
    AnimatorSet generatePlayFirstCard(Object firstCard){

        AnimatorSet playCard;
         //String if it is passed from Server, Integer if it is handled internally
        //in any case,perform the play with the config, then get the animation from the match fragment
        if(firstCard instanceof String) {
            config.playCard((String) firstCard);
            playCard= matchFragment.playFirstCard((String) firstCard, config.getCurrentPlayer());
        }else if(firstCard instanceof Integer){
            config.playCard((Integer) firstCard);
            playCard = matchFragment.playFirstCard((Integer) firstCard, config.getCurrentPlayer());
        }else //firstCard other than instanceof String/Integer are not allowed
            throw new IllegalArgumentException();

        //toggle the player in the config
        config.toggleCurrentPlayer();
        AnimatorSet playFirstCard = new AnimatorSet();
        //if is PLAYER0 next, then enable him to play
        if(config.getCurrentPlayer() == config.PLAYER0) {
            AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
            //schedule animations
            playFirstCard.playSequentially(playCard, displayIsPlayer0Turn);
        }else{ //if PLAYER1 is the next one
            AnimatorSet hideIsPlayer0Turn = matchFragment.hideIsPlayer0Turn();
            //schedule animations
            playFirstCard.playSequentially(hideIsPlayer0Turn, playCard);
        }
        //if PLAYER1 is the next one, let him play the second card
        playFirstCard.addListener(getPlayer1Listener());
        return playFirstCard;
    }


    /**
     * Method responsible of handling the business logic of a second-card play during a turn, also produces and runs the related animation and handles turn closure.
     *
     * @param secondCard the second card to be played
     */
    void generateAndRunPlaySecondCard(Object secondCard){
        AnimatorSet playCard;
        //String if it is passed from Server, Integer if it is handled internally
        //in any case,perform the play with the config, then get the animation from the match fragment
        if(secondCard instanceof String) {
            config.playCard((String)secondCard);
            playCard = matchFragment.playSecondCard((String)secondCard, config.getCurrentPlayer());
        }else if(secondCard instanceof Integer){
            config.playCard((Integer)secondCard);
            playCard = matchFragment.playSecondCard((Integer)secondCard, config.getCurrentPlayer());
        }else //secondCard not String or Integer not allowed
            throw new IllegalArgumentException();

        AnimatorSet playSecondCard = new AnimatorSet();

        AnimatorSet closeTurn =  getCloseTurnAnimation();
        //schedule animations
        playSecondCard.playSequentially(playCard,closeTurn);
        playSecondCard.start();
    }

    /**
     * Method responsible of turn closure, returns an animator set containing the turn closure animation.
     *
     */
    AnimatorSet getCloseTurnAnimation(){
        AnimatorSet hidePlayer0andCleanSurface = new AnimatorSet();
        int roundWinner = config.chooseRoundWinner(); //choose round winner
        config.clearSurface(roundWinner); //clear surface at configuration level
        AnimatorSet cleanSurface = matchFragment.cleanSurface(roundWinner); //clean surface at GUI level
        config.setCurrentPlayer(roundWinner); //choose next round first player at configuration level
        AnimatorSet hideIsPlayer0Turn = matchFragment.hideIsPlayer0Turn(); //in any case, hide player0Turn (while playing cleaning animations interaction should not be enabled)

        //a listener that handles the end of the surface cleaning operations
        cleanSurface.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                AnimatorSet closeTurnAnimation = new AnimatorSet();
                List<Animator> animators = new ArrayList<Animator>();

                if(config.getNumberTurnsElapsed() == Briscola2PMatchConfig.FINISHED){ //if the match is finished, choose winner
                    stopMatch();
                    switch(config.chooseMatchWinner()){
                        case Briscola2PMatchConfig.PLAYER0: matchFragment.displayMatchWinner(Briscola2PFullMatchConfig.PLAYER0, config.computeScore(Briscola2PMatchConfig.PLAYER0)); break;
                        case Briscola2PMatchConfig.PLAYER1: matchFragment.displayMatchWinner(Briscola2PFullMatchConfig.PLAYER1, config.computeScore(Briscola2PMatchConfig.PLAYER1)); break;
                        case Briscola2PMatchConfig.DRAW: matchFragment.displayMatchWinner(Briscola2PFullMatchConfig.DRAW, config.computeScore(Briscola2PMatchConfig.PLAYER0)); break;
                        default: throw new RuntimeException("Error while computing the winner");
                    }
                }else if (config.getNumberTurnsElapsed()== 19 || config.getNumberTurnsElapsed() == 20){ //if the deck is empty, but players have cards in hand,don't do anything (equivalently, turns 19 and 20: no cards to draw)
                    matchFragment.putPlayer0CardsTouchListeners(config.getHands(),config.getCurrentPlayer());
                    if(config.getCurrentPlayer() == config.PLAYER1){
                        callPlayer1(animators);
                    }  else{
                        //enable player0 to play
                        AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
                        animators.add(displayIsPlayer0Turn);                    }
                }else if(config.getNumberTurnsElapsed() <= 18){ //if deck is not empty, draw cards new round (equivalently, turns <= 18)
                    //since the Server API doesn't give info on the deck, count turns played to know whether lastDraw
                    boolean lastDraw = false;
                    if(config.getNumberTurnsElapsed() == 18) // 20th turn (1card -> 0 card, no draw at turn start), 19th turn (2cards->1card, no draw), 18 turn (3cards->2cards, last draw)
                        lastDraw = true;
                    //draw cards at config level
                    config.drawCardsNewRound();
                    AnimatorSet drawCards = matchFragment.drawCardsNewRound(config.getHands(), config.getCurrentPlayer(), lastDraw); //TODO, sistemare come calcolare il lastdraw in base al comportamento dell'api del prof
                    animators.add(drawCards);
                    if (config.getCurrentPlayer() == config.PLAYER1) {
                        //if next turn PLAYER1 is first, then let him play
                        callPlayer1(animators);
                    } else {
                        AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
                        animators.add(displayIsPlayer0Turn);
                    }
                    Log.d("TAG","Drawing cards from animation");
                }
                //schedule animations
                closeTurnAnimation.playSequentially(animators);
                //play animation
                closeTurnAnimation.start();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        hidePlayer0andCleanSurface.playSequentially(hideIsPlayer0Turn, cleanSurface);
        return hidePlayer0andCleanSurface;
    }

    void closeTurnInConfiguration(){
        int roundWinner = config.chooseRoundWinner(); //choose round winner
        config.clearSurface(roundWinner); //clear surface at configuration level
        config.setCurrentPlayer(roundWinner); //choose next round first player at configuration level


        if(config.getNumberTurnsElapsed() == Briscola2PMatchConfig.FINISHED){ //if the match is finished, choose winner
            /*stopMatch();
            switch(config.chooseMatchWinner()){
                case Briscola2PMatchConfig.PLAYER0: matchFragment.displayMatchWinner(Briscola2PFullMatchConfig.PLAYER0, config.computeScore(Briscola2PMatchConfig.PLAYER0)); break;
                case Briscola2PMatchConfig.PLAYER1: matchFragment.displayMatchWinner(Briscola2PFullMatchConfig.PLAYER1, config.computeScore(Briscola2PMatchConfig.PLAYER1)); break;
                case Briscola2PMatchConfig.DRAW: matchFragment.displayMatchWinner(Briscola2PFullMatchConfig.DRAW, config.computeScore(Briscola2PMatchConfig.PLAYER0)); break;
                default: throw new RuntimeException("Error while computing the winner");
            }*/
        }else if (config.getNumberTurnsElapsed()== 19 || config.getNumberTurnsElapsed() == 20){ //if the deck is empty, but players have cards in hand,don't do anything (equivalently, turns 19 and 20: no cards to draw)
            //matchFragment.putPlayer0CardsTouchListeners(config.getHands(),config.getCurrentPlayer());
          //   if(config.getCurrentPlayer() == config.PLAYER1){
           //     callPlayer1(animators);
          //  }  else{
                //enable player0 to play
          //      AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
          //      animators.add(displayIsPlayer0Turn);                    }
        }else if(config.getNumberTurnsElapsed() <= 18){ //if deck is not empty, draw cards new round (equivalently, turns <= 18)
            //since the Server API doesn't give info on the deck, count turns played to know whether lastDraw
            //boolean lastDraw = false;
           // if(config.getNumberTurnsElapsed() == 18) // 20th turn (1card -> 0 card, no draw at turn start), 19th turn (2cards->1card, no draw), 18 turn (3cards->2cards, last draw)
            //    lastDraw = true;
            //draw cards at config level
            config.drawCardsNewRound();
        //    AnimatorSet drawCards = matchFragment.drawCardsNewRound(config.getHands(), config.getCurrentPlayer(), lastDraw); //TODO, sistemare come calcolare il lastdraw in base al comportamento dell'api del prof
         //   animators.add(drawCards);
        //    if (config.getCurrentPlayer() == config.PLAYER1) {
                //if next turn PLAYER1 is first, then let him play
         //       callPlayer1(animators);
        //    } else {
          //      AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
         //       animators.add(displayIsPlayer0Turn);
           // }
        }


        //a listener that handles the end of the surface cleaning operations
      /*  cleanSurface.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                AnimatorSet closeTurnAnimation = new AnimatorSet();
                List<Animator> animators = new ArrayList<Animator>();

                if(config.getNumberTurnsElapsed() == Briscola2PMatchConfig.FINISHED){ //if the match is finished, choose winner
                    stopMatch();
                    switch(config.chooseMatchWinner()){
                        case Briscola2PMatchConfig.PLAYER0: matchFragment.displayMatchWinner(Briscola2PFullMatchConfig.PLAYER0, config.computeScore(Briscola2PMatchConfig.PLAYER0)); break;
                        case Briscola2PMatchConfig.PLAYER1: matchFragment.displayMatchWinner(Briscola2PFullMatchConfig.PLAYER1, config.computeScore(Briscola2PMatchConfig.PLAYER1)); break;
                        case Briscola2PMatchConfig.DRAW: matchFragment.displayMatchWinner(Briscola2PFullMatchConfig.DRAW, config.computeScore(Briscola2PMatchConfig.PLAYER0)); break;
                        default: throw new RuntimeException("Error while computing the winner");
                    }
                }else if (config.getNumberTurnsElapsed()== 19 || config.getNumberTurnsElapsed() == 20){ //if the deck is empty, but players have cards in hand,don't do anything (equivalently, turns 19 and 20: no cards to draw)
                    matchFragment.putPlayer0CardsTouchListeners(config.getHands(),config.getCurrentPlayer());
                    if(config.getCurrentPlayer() == config.PLAYER1){
                        callPlayer1(animators);
                    }  else{
                        //enable player0 to play
                        AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
                        animators.add(displayIsPlayer0Turn);                    }
                }else if(config.getNumberTurnsElapsed() <= 18){ //if deck is not empty, draw cards new round (equivalently, turns <= 18)
                    //since the Server API doesn't give info on the deck, count turns played to know whether lastDraw
                    boolean lastDraw = false;
                    if(config.getNumberTurnsElapsed() == 18) // 20th turn (1card -> 0 card, no draw at turn start), 19th turn (2cards->1card, no draw), 18 turn (3cards->2cards, last draw)
                        lastDraw = true;
                    //draw cards at config level
                    config.drawCardsNewRound();
                    AnimatorSet drawCards = matchFragment.drawCardsNewRound(config.getHands(), config.getCurrentPlayer(), lastDraw); //TODO, sistemare come calcolare il lastdraw in base al comportamento dell'api del prof
                    animators.add(drawCards);
                    if (config.getCurrentPlayer() == config.PLAYER1) {
                        //if next turn PLAYER1 is first, then let him play
                        callPlayer1(animators);
                    } else {
                        AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
                        animators.add(displayIsPlayer0Turn);
                    }
                }
                //schedule animations
                closeTurnAnimation.playSequentially(animators);
                //play animation
                closeTurnAnimation.start();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });*/

    }

    @Override
    public void playFirstCard(int move){
        generatePlayFirstCard(move).start();
    }

    @Override
    public void playSecondCard(int move){
        generateAndRunPlaySecondCard(move);
    }
    /**
     * Ask player 1 to perform its move, and if necessary, add it to the animators list
     *
     * @param animators the list of Animators that will be played
     */
    public abstract void callPlayer1(List<Animator> animators);

    /**
     * Perform business-logic related actions associated with a normal termination of maps (REMARK: forceMatchEnd is for match interruptions, stopMatch is for normal conditions)
     */
    public abstract void stopMatch();

    /**
     * Get the player1 AnimatorListener, to be attached to an animation after which Player1 interaction is required.
     *
     * @return listener responsible of triggering the Player1 interaction
     */
    public abstract Animator.AnimatorListener getPlayer1Listener();


}
