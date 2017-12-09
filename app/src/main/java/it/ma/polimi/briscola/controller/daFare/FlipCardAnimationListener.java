package it.ma.polimi.briscola.controller.daFare;

import android.animation.Animator;
import android.widget.ImageView;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.audio.GameEvent;
import it.ma.polimi.briscola.audio.SoundManager;

/**
 * Created by utente on 08/12/17.
 */

public class FlipCardAnimationListener implements Animator.AnimatorListener {

    private final ImageView card;
    private final SoundManager soundManager;

    public FlipCardAnimationListener(ImageView card, SoundManager soundManager){
        super();
        this.card = card;
        this.soundManager = soundManager;
    }

    @Override
    public void onAnimationStart(Animator animator) {
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        String[] cardTag = ((String) card.getTag()).split(";");
        if (Boolean.valueOf(cardTag[1])) {
            card.setImageResource(Integer.valueOf(cardTag[0]));
            card.setTag(card.getId() + ";" + false);
            //todo, inserire animazione migliore di flip
        } else {
            card.setImageResource(R.drawable.default_card_background);
            card.setTag(card.getId() + ";" + true);

        }
        soundManager.playSoundForGameEvent(GameEvent.FlipCard);
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
