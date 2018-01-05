package it.ma.polimi.briscola.controller;

import android.animation.Animator;
import android.content.Context;
import android.util.Log;

import java.util.List;

import it.ma.polimi.briscola.ai.Briscola2PAIDumbGreedyPlayer;
import it.ma.polimi.briscola.ai.Briscola2PAIPlayer;
import it.ma.polimi.briscola.ai.Briscola2PAIRandomPlayer;
import it.ma.polimi.briscola.ai.Briscola2PAISmarterGreedyPlayer;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PFullMatchConfig;
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
public class OfflineBriscola2PMatchController extends AbstractBriscola2PController {

    //Game AI
    Briscola2PAIPlayer player1;

    /**
     * Instantiates a new Briscola 2 p match controller.
     *
     * @param matchFragment the match fragment executing the view part of the match
     * @param difficulty    the difficulty, one of the difficulty values in the SettingsManager public values.
     */
    public OfflineBriscola2PMatchController(Briscola2PMatchFragment matchFragment, int difficulty){
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
                player1 = new Briscola2PAISmarterGreedyPlayer();
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
    public OfflineBriscola2PMatchController(Briscola2PFullMatchConfig config, Briscola2PMatchFragment matchFragment, int difficulty){
        this(matchFragment,difficulty);
        this.config = config;

    }


    @Override
    public void startNewMatch(){
        //perform all the initialization operations at a configuration level
        config = new Briscola2PFullMatchConfig();
        ((Briscola2PFullMatchConfig) config).initializeNewDeck();
        ((Briscola2PFullMatchConfig) config).initializeFirstPlayer();
        ((Briscola2PFullMatchConfig) config).initializePlayersHands();
        ((Briscola2PFullMatchConfig) config).initializeBriscola();
        config.setSurface( new Briscola2PSurface(""));
        ((Briscola2PFullMatchConfig) config).setPile(config.PLAYER0, new Briscola2PPile(""));
        ((Briscola2PFullMatchConfig) config).setPile(config.PLAYER1, new Briscola2PPile(""));

        //perform the same operations at a GUI level by first building the animations (the structure of the animations is internally handled by the fragment to keep this class decoupled from the fragment)
        //then setting listeners on those animations (if needed), scheduling their execution and starting them
        startInitializationAnimationSequence();
    }

    @Override
    public void callPlayer1(List<Animator> animators){
        //make the AI make its move, and add the related animation to the animators list
        animators.add(generatePlayFirstCard(player1.chooseMove((Briscola2PFullMatchConfig) config,config.PLAYER1)));
    }
    @Override
    public Animator.AnimatorListener getPlayer1Listener(){
        //return an instance of the Player1AnimationListener
        return new Player1AnimationListener((Briscola2PFullMatchConfig) config, this);
    }
    @Override
    public void stopMatch(){
        //by default, don't do anything (it is an offline match, there are no business-logic related things to do here
    }

    @Override
    public int getHandSize(int playerIndex) {
        return ((Briscola2PFullMatchConfig) config).getHand(playerIndex).size();
    }


    @Override
    public void resumeMatch(){
        //in case the rotation of the screen occurs in a particular moment, the config might be locked in the 2-cards on surface status, not allowing the game to proceed.
        if(config.countCardsOnSurface() == 2)
            closeTurnInConfiguration();
        //load all the needed widgets in the interface in order to resume the match
        matchFragment.loadPiles(config.getPile(Briscola2PMatchConfig.PLAYER0).isEmpty(),config.getPile(Briscola2PFullMatchConfig.PLAYER1).isEmpty());
        matchFragment.loadSurface(config.getSurface().getCardList());
        matchFragment.loadHands(config.getHands());
        matchFragment.loadBriscolaIfNeeded(((Briscola2PFullMatchConfig)config).inferBriscolaIfInDeck());
        matchFragment.loadCurrentPlayer(config.getCurrentPlayer());
        //if it was the AI turn, let it play
        if(config.getCurrentPlayer() == Briscola2PFullMatchConfig.PLAYER1){
        //    playFirstCard(player1.chooseMove((Briscola2PFullMatchConfig) config,Briscola2PMatchConfig.PLAYER1));
        }

        Log.d("TAG",config.toString());
    }


    @Override
    public void forceMatchEnd(Context context) {
        //don't do anything if offline match
    }
}

/**
 * The Player 1 animation listener used to let the IA play the game when the animation on which it is set ends
 */
//helper class
class Player1AnimationListener implements Animator.AnimatorListener {

        Briscola2PFullMatchConfig config;
        OfflineBriscola2PMatchController controller;

        Player1AnimationListener(Briscola2PFullMatchConfig config, OfflineBriscola2PMatchController controller){
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