package it.ma.polimi.briscola.controller.listeners;

import android.animation.Animator;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.audio.GameEvent;
import it.ma.polimi.briscola.audio.SoundService;

/**
 * Created by utente on 01/01/18.
 */

public class FixBriscolaLayoutParams implements Animator.AnimatorListener {
    private final ImageView card;
    /**
     * Instantiates a new Flip card animation listener.
     *
     * @param card         the card
     */
    public FixBriscolaLayoutParams(ImageView card){
        super();
        this.card = card;
    }

    @Override
    public void onAnimationStart(Animator animator) {
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        Log.d("TAG","Coordinates = ("+card.getX() + ","+card.getY()+")");
        Log.d("TAG","MARGIN = ("+((RelativeLayout.LayoutParams)card.getLayoutParams()).leftMargin + ","+((RelativeLayout.LayoutParams)card.getLayoutParams()).topMargin+")");
       // ((RelativeLayout.LayoutParams)card.getLayoutParams()).leftMargin = (int)card.getX();
       // ((RelativeLayout.LayoutParams)card.getLayoutParams()).topMargin = (int) card.getY();
        Log.d("TAG","AFTER MARGIN FIX = ("+((RelativeLayout.LayoutParams)card.getLayoutParams()).leftMargin + ","+((RelativeLayout.LayoutParams)card.getLayoutParams()).topMargin+")");

    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
