package it.ma.polimi.briscola.controller.daFare;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;




/**
 * Created by utente on 26/11/17.
 */

public class AnimationMaster {

    /*public static void centerViewInSlot(int idSlot, int idView, ConstraintLayout layout){
        TransitionManager.beginDelayedTransition(layout);
        ConstraintSet constraints = new ConstraintSet();
        constraints.clone(layout);
        constraints.constrainWidth(idView, ConstraintSet.WRAP_CONTENT);
        constraints.constrainHeight(idView, ConstraintSet.WRAP_CONTENT);
        constraints.connect(idView, ConstraintSet.LEFT,idSlot,ConstraintSet.LEFT);
        constraints.connect(idView, ConstraintSet.RIGHT,idSlot,ConstraintSet.RIGHT);
        constraints.connect(idView, ConstraintSet.BOTTOM,idSlot,ConstraintSet.BOTTOM);
        constraints.applyTo(layout);
    }*/

    public static void moveCardToSlot(int idSlot, int idView, ConstraintLayout layout){

        //risorsa molto utile todo https://medium.com/@andkulikov/animate-all-the-things-transitions-in-android-914af5477d50
        //TransitionManager.beginDelayedTransition(layout,
                //new ChangeBounds().setPathMotion(new ArcMotion()).setDuration(500));
        ConstraintSet constraints = new ConstraintSet();
        constraints.clone(layout);
        constraints.constrainWidth(idView, ConstraintSet.WRAP_CONTENT);
        constraints.constrainHeight(idView, ConstraintSet.WRAP_CONTENT);
        constraints.connect(idView, ConstraintSet.LEFT,idSlot,ConstraintSet.LEFT);
        constraints.connect(idView, ConstraintSet.RIGHT,idSlot,ConstraintSet.RIGHT);
        constraints.connect(idView, ConstraintSet.BOTTOM,idSlot,ConstraintSet.BOTTOM);
        constraints.applyTo(layout);
    }


}
