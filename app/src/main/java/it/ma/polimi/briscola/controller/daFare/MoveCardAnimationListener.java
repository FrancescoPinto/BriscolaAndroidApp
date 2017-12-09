package it.ma.polimi.briscola.controller.daFare;

import android.animation.Animator;
import android.widget.ImageView;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.audio.GameEvent;
import it.ma.polimi.briscola.audio.SoundManager;

/**
 * Created by utente on 09/12/17.
 */

public class MoveCardAnimationListener implements Animator.AnimatorListener {

    private final SoundManager soundManager;

    public MoveCardAnimationListener(SoundManager soundManager){
        super();
        this.soundManager = soundManager;
    }

    @Override
    public void onAnimationStart(Animator animator) {
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