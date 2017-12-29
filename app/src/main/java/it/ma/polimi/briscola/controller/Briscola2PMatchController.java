package it.ma.polimi.briscola.controller;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;

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
 *Class implementing a controller for a offline human vs. AI Briscola 2 players match.
 *
 * @author Francesco Pinto
 */
public class Briscola2PMatchController implements Briscola2PController {

    private Briscola2PMatchConfig config;
    Briscola2PAIPlayer player1;
    private Briscola2PMatchFragment matchFragment;
    /**
     * Playing attribute, used to keep the controller synchronized with the GUI status. In particular, it allows to know whether the user can see, in the GUI, a condition in which he/she should be able to play (e.g. has been notified it is his/her turn)
     */
    public boolean playing = false;

    /**
     * Instantiates a new Briscola 2 p match controller.
     *
     * @param matchFragment the match fragment executing the view part of the match
     * @param difficulty    the difficulty, one of the difficulty values in the SettingsManager public values.
     */
    public Briscola2PMatchController(Briscola2PMatchFragment matchFragment,int difficulty){
        super();
        this.matchFragment = matchFragment;
        switch(difficulty) {
            case SettingsManager.DIFFICULTY_EASY:
                player1 = new Briscola2PAIRandomPlayer();
                break;
            case SettingsManager.DIFFICULTY_MEDIUM:
                player1 = new Briscola2PAIDumbGreedyPlayer();
                break;
            case SettingsManager.DIFFICULTY_HARD:
                player1 = new Briscola2PAISmarterGreedyPlayer(); //todo, se PROPRIO non riesci a fare un'IA più intelligente del greedy, fai una SuperDumbGreedy
                break;
            case SettingsManager.DIFFICULTY_VERY_HARD:
                player1 = new Briscola2PAISmarterGreedyPlayer(); //todo, metti IA ancora più intelligente (se hai tempo)
                break;
        }
    }

    /**
     * Instantiates a new Briscola 2 p match controller STARTING from a given configuration (used to reload the match)
     *
     * @param config        the configuration from which the match should resume
     * @param matchFragment the match fragment executing the view part of the match
     * @param difficulty    the difficulty, one of the difficulty values in the SettingsManager public values.
     */
    public Briscola2PMatchController(Briscola2PMatchConfig config, Briscola2PMatchFragment matchFragment,int difficulty){
        this(matchFragment,difficulty);
        this.config = config;

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
        //perform all the initialization operations at a configuration level
        config = new Briscola2PMatchConfig();
        config.initializeNewDeck();
        config.initializeFirstPlayer();
        config.initializePlayersHands();
        config.initializeBriscola();
        config.setSurface( new Briscola2PSurface(""));
        config.setPile(config.PLAYER0, new Briscola2PPile(""));
        config.setPile(config.PLAYER1, new Briscola2PPile(""));

        //perform the same operations at a GUI level by first building the animations (the structure of the animations is internally handled by the fragment to keep this class decoupled from the fragment)
        //then setting listeners on those animations (if needed), scheduling their execution and starting them

        AnimatorSet initializationSequence = new AnimatorSet();
        AnimatorSet dealFirstHand = matchFragment.getDealFirstHandAnimatorSet(config.getCurrentPlayer(), config.getHands());
        AnimatorSet initializeBriscola = matchFragment.getInitializeBriscolaAnimatorSet(config.getDeck().getCard(config.getDeck().size()-1));

        //if first player of the first round is PLAYER1, then should invoke the AI to let her play
        initializeBriscola.addListener(new Player1AnimationListener(config,this));

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


    //helper method, used to generate (but not play) a PlayFirstCard animation
    private AnimatorSet generatePlayFirstCardAnimatorSet(int move){
        //perform the config play
        config.playCard(move);
        //get the animation from the match fragment
        AnimatorSet playCard = matchFragment.playFirstCard(move, config.getCurrentPlayer());
        //toggle the player in the config
        config.toggleCurrentPlayer();

        AnimatorSet playFirstCard = new AnimatorSet();
        //if is PLAYER0 next, then enable him to play
        if(config.getCurrentPlayer() == config.PLAYER0) {
            AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
            //schedule animations
            playFirstCard.playSequentially(playCard, displayIsPlayer0Turn);
        }else{ //if PLAYER1 is the next one
            AnimatorSet hideIsPlayer0Turn = matchFragment.hideIsPlayer0Turn(/*config.getCurrentPlayer()*/); //hide the signal that user can play
            //schedule animations
            playFirstCard.playSequentially(hideIsPlayer0Turn, playCard);
        }

        //if PLAYER1 is the next one, let him play the second card
        playFirstCard.addListener(new Player1AnimationListener(config,this));
        return playFirstCard;
    }

    @Override
    public void playFirstCard(int move){
       generatePlayFirstCardAnimatorSet(move).start();
    }

    /**
     * Generate play second card animator set animator set.
     *
     * @param move the move
     * @return the animator set
     */
//helper method, used to generate (but not play) a PlaySecondCardAnimation animation
    public AnimatorSet generatePlaySecondCardAnimatorSet(int move){
        //play the card at configuration level
        config.playCard(move);
        //prepare playSecondCard animation
        AnimatorSet playCard = matchFragment.playSecondCard(move, config.getCurrentPlayer());

        AnimatorSet playSecondCard = new AnimatorSet();


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

                if(config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the match is finished, choose winner
                    switch(config.chooseMatchWinner()){ //tell the fragment to show the match outcome to the user
                        case Briscola2PMatchConfig.PLAYER0:  matchFragment.displayMatchWinner(Briscola2PMatchConfig.PLAYER0, config.computeScore(Briscola2PMatchConfig.PLAYER0)); break;
                        case Briscola2PMatchConfig.PLAYER1: matchFragment.displayMatchWinner(Briscola2PMatchConfig.PLAYER1, config.computeScore(Briscola2PMatchConfig.PLAYER1)); break;
                        case Briscola2PMatchConfig.DRAW: matchFragment.displayMatchWinner(Briscola2PMatchConfig.DRAW, Briscola2PMatchRecord.totPoints/2); break;
                        default: throw new RuntimeException("Error while computing the winner");
                    }

                }else if (!config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the deck is empty, but players have cards in hand
                    //instantiate new listeners for the cards touch
                    matchFragment.putPlayer0CardsTouchListeners(config.getHands(),config.getCurrentPlayer());
                    if(config.getCurrentPlayer() == config.PLAYER1){
                        //if next turn PLAYER1 is first, then let him play
                        animators.add(generatePlayFirstCardAnimatorSet(player1.chooseMove(config,config.PLAYER1)));
                    }  else{
                        //else, enable player0 to play
                        AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
                        animators.add(displayIsPlayer0Turn);
                    }


                }else if(!config.isDeckEmpty()){ //if deck is not empty, draw cards new round

                    boolean lastDraw = false; //used to perform different animation on briscola draw
                    if(config.getDeck().size() == 2) //deck in config contains only last card and briscola
                        lastDraw = true;

                    config.drawCardsNewRound(); //draw cards at config level

                    //get drawCards animation
                    AnimatorSet drawCards = matchFragment.drawCardsNewRound(config.getHands(), config.getCurrentPlayer(), lastDraw);
                    animators.add(drawCards);

                    if(config.getCurrentPlayer() == config.PLAYER1){
                        //if next turn is of player1, let him play
                        animators.add(generatePlayFirstCardAnimatorSet(player1.chooseMove(config,config.PLAYER1)));
                    }else{
                        //else, enable player0 to play
                        AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
                        animators.add(displayIsPlayer0Turn);
                    }


                }

                //schedule animations
                closeTurnAnimation.playSequentially(animators);
                //play animations
                closeTurnAnimation.start();

            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        //schedule animations
        playSecondCard.playSequentially(playCard, hideIsPlayer0Turn,cleanSurface);
        return playSecondCard;

    }

    @Override
    public void playSecondCard(int move){
        generatePlaySecondCardAnimatorSet(move).start();
    }


    @Override
    public int getHandSize(int playerIndex) {
        return config.getHand(playerIndex).size();
    }

    @Override
    public void forceMatchEnd(Context context) {
        //don't do anything ... the objects will be destroyed authomatically
    }


    @Override
    public void resumeMatch(){
        matchFragment.loadPiles(config.getPile(Briscola2PMatchConfig.PLAYER0).isEmpty(),config.getPile(Briscola2PMatchConfig.PLAYER1).isEmpty());
        matchFragment.loadSurface(config.getSurface().getCardList()); //questi li si può fare subito dato che sono molto semplici
        matchFragment.loadHands(config.getHands());
        matchFragment.loadBriscolaIfNeeded(config.inferBriscolaIfInDeck());
        matchFragment.loadCurrentPlayer(config.getCurrentPlayer());

        if(Briscola2PMatchController.this.config.getCurrentPlayer() == Briscola2PMatchConfig.PLAYER1){
            playFirstCard(player1.chooseMove(Briscola2PMatchController.this.config,config.PLAYER1));
        }
    }


    @Override
    public int getTurnsElapsed(){
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

}

/**
 * The Player 1 animation listener used to let the IA play the game.
 */
//helper class
class Player1AnimationListener implements Animator.AnimatorListener {

        Briscola2PMatchConfig config;
        Briscola2PMatchController controller;

        Player1AnimationListener(Briscola2PMatchConfig config, Briscola2PMatchController controller){
            this.config = config;
            this.controller = controller;
        }

        @Override
        public void onAnimationStart(Animator animator) {}
        @Override
        public void onAnimationEnd(Animator animator) {
            if(config.getCurrentPlayer() == config.PLAYER1){
                if(config.countCardsOnSurface() ==0)
                    controller.playFirstCard(controller.player1.chooseMove(config,config.PLAYER1));
                else
                    controller.playSecondCard(controller.player1.chooseMove(config,config.PLAYER1));
            }
        }
        @Override
        public void onAnimationCancel(Animator animator) {}
        @Override
        public void onAnimationRepeat(Animator animator) {}

}