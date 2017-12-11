package it.ma.polimi.briscola.controller.offline;

import android.animation.Animator;

import it.ma.polimi.briscola.audio.GameEvent;
import it.ma.polimi.briscola.audio.SoundManager;

/**
 * Created by utente on 09/12/17.
 */

public class CleanRoundAnimationListener implements Animator.AnimatorListener {

    private final boolean player0Won;
    private final SoundManager soundManager;

    public CleanRoundAnimationListener(Boolean player0Won, SoundManager soundManager){
        super();
        this.player0Won = player0Won;
        this.soundManager = soundManager;
    }

    @Override
    public void onAnimationStart(Animator animator) {
        if(player0Won){
            soundManager.playSoundForGameEvent(GameEvent.WinRound);
        }else
            soundManager.playSoundForGameEvent(GameEvent.LoseRound);

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
