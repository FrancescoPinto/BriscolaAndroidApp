package it.ma.polimi.briscola.controller.daFare;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * Created by utente on 26/11/17.
 */

public class PlayCardListener implements View.OnClickListener {

    //private /*ImageView*/ Button card; //todo, metti ImageView
    private MatchGUIPrototype activity;
    private int index;

    public PlayCardListener(MatchGUIPrototype activity, int index){ //todo, vedere come disaccoppiare le componenti, così da rendere riusabile questo anche in altri casi
        //this.card = card;
        this.activity = activity;
        this.index = index;
    }
    @Override
    public void onClick(View view) {
        //todo chiama onAnimate
        //activity.getMatchController().makePlayer0Move(index); todo, reinserire
    }

    /** set rotate animation from Java code */ //todo, usare, come qui, l'imageView (converti i Button in ImageView)
    private void onAnimate(ImageView icon) {
        icon.animate().rotation(icon.getRotation() + 360).setInterpolator(new DecelerateInterpolator()).start();
    }
}
