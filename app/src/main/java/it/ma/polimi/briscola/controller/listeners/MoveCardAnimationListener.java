package it.ma.polimi.briscola.controller.listeners;

import android.animation.Animator;

import it.ma.polimi.briscola.audio.GameEvent;
import it.ma.polimi.briscola.audio.SoundService;

/**
 * The MoveCardAnimationListener is an AnimationListener that overrides onAnimationStart to play a sound when animation start (mainly, a card translation)
 *
 * @author Francesco Pinto
 */
public class MoveCardAnimationListener implements Animator.AnimatorListener {

    private final SoundService soundManager;

    /**
     * Instantiates a new Move card animation listener.
     *
     * @param soundManager the sound manager
     */
    public MoveCardAnimationListener(SoundService soundManager){
        super();
        this.soundManager = soundManager;
    }

    @Override
    public void onAnimationStart(Animator animator) {
        if(soundManager != null)
        soundManager.playSoundForGameEvent(GameEvent.MoveCard);
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