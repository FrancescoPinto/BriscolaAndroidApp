package it.ma.polimi.briscola.controller.listeners;

import android.animation.Animator;
import android.widget.ImageView;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.audio.GameEvent;
import it.ma.polimi.briscola.audio.SoundService;

/**
 * The FlipCardAnimationListener is an AnimationListener that overrides onAnimationEnd to flip a card when the animation it is attached to is ended (generally, a translation animation)
 *
 * @author Francesco Pinto
 */
public class FlipCardAnimationListener implements Animator.AnimatorListener {

    private final ImageView card;
    private final SoundService soundManager;

    /**
     * Instantiates a new Flip card animation listener.
     *
     * @param card         the card
     * @param soundManager the sound manager, in order to play the flip sound
     */
    public FlipCardAnimationListener(ImageView card, SoundService soundManager){
        super();
        this.card = card;
        this.soundManager = soundManager;
    }

    @Override
    public void onAnimationStart(Animator animator) {
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        String[] cardTag = ((String) card.getTag()).split(";"); //extract the tag (contains info about whether the card is covered and the uncovered card image id)
        if (Boolean.valueOf(cardTag[1])) {//is covered
            card.setImageResource(Integer.valueOf(cardTag[0])); //flip the card setting the image resource to the uncovered image id
            card.setTag(cardTag[0] + ";" + false); //false = card is no more covered
        } else { //is not covered
            card.setImageResource(R.drawable.default_card_background); // show card back
            card.setTag(cardTag[0] + ";" + true); //true = card is covered now

        }
        soundManager.playSoundForGameEvent(GameEvent.FlipCard); //play sound
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
