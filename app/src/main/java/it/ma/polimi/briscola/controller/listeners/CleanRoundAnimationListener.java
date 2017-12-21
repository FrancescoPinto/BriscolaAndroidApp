package it.ma.polimi.briscola.controller.listeners;

import android.animation.Animator;

import it.ma.polimi.briscola.audio.GameEvent;
import it.ma.polimi.briscola.audio.SoundService;

/**
 * The CleanRoundAnimationListener is an AnimatorListener. Overrides the onAnimationStart method to play a sound choosen based on the round winner
 *
 * @author Francesco Pinto
 */
public class CleanRoundAnimationListener implements Animator.AnimatorListener {

    private final boolean player0Won;
    private final SoundService soundManager;

    /**
     * Instantiates a new Clean round animation listener.
     *
     * @param player0Won   whether PLAYER0 won the match
     * @param soundManager the sound manager
     */
    public CleanRoundAnimationListener(Boolean player0Won, SoundService soundManager){
        super();
        this.player0Won = player0Won;
        this.soundManager = soundManager;
    }

    @Override
    public void onAnimationStart(Animator animator) {
        if(player0Won){ //if player0 won
            soundManager.playSoundForGameEvent(GameEvent.WinRound); //cheers
        }else
            soundManager.playSoundForGameEvent(GameEvent.LoseRound); //buuuuu

    }

    @Override
    public void onAnimationEnd(Animator animator) {
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
