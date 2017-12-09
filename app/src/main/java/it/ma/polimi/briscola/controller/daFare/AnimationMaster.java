package it.ma.polimi.briscola.controller.daFare;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.DisplayMetrics;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import it.ma.polimi.briscola.Briscola2PMatchActivity;
import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PHand;

import static java.security.AccessController.getContext;

/**
 * Created by utente on 03/12/17.
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


       /* int temp1 = card1.getHeight();
        int temp2 = card1.getWidth();
        Toast.makeText(act,"Destinazione briscola: "+ y1+ " ",Toast.LENGTH_SHORT).show();
*/
        /*ObjectAnimator animation1 = ObjectAnimator.ofFloat(card1, "translationX", x1-c1x1);
        animation1.setDuration(1000);
        ObjectAnimator animation11 = ObjectAnimator.ofFloat(card1, "translationY", y1- c1y1);
        animation11.setDuration(1000);
        ObjectAnimator animation2 = ObjectAnimator.ofFloat(card2, "translationX", x2- c2x2);
        animation2.setDuration(1000);
        ObjectAnimator animation22 = ObjectAnimator.ofFloat(card2, "translationY", y2-c2y2);
        animation22.setDuration(1000);
        AnimatorSet cardDealer = new AnimatorSet();
        cardDealer.setStartDelay(1000);
        cardDealer.playTogether(animation1,animation11);
        cardDealer.play(animation1).before(animation2);
        cardDealer.playTogether(animation2,animation22);*/
    //cardDealer.playTogether(animation1,animation11);
    //cardDealer.play(animation1).before(animation2);
    //cardDealer.playTogether(animation2,animation22);
    //cardDealer.start();
    //todo: qui nel commento l'esempio della documentazione android, molto educativo: ti mostra come persino 2 AnimatorSet possono essere combinati
       /* ObjectAnimator animation1 = ObjectAnimator.ofFloat(card, "translationX", 100f);
        animation1.setDuration(1000);
        animation1.start();
        AnimatorSet bouncer = new AnimatorSet();
        bouncer.play(bounceAnim).before(squashAnim1);
        bouncer.play(squashAnim1).with(squashAnim2);
        bouncer.play(squashAnim1).with(stretchAnim1);
        bouncer.play(squashAnim1).with(stretchAnim2);
        bouncer.play(bounceBackAnim).after(stretchAnim2);
        ValueAnimator fadeAnim = ObjectAnimator.ofFloat(newBall, "alpha", 1f, 0f);
        fadeAnim.setDuration(250);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(bouncer).before(fadeAnim);
        animatorSet.start();
        */

    public static AnimatorSet getTranslationAnimationSet(ImageView targetView, Point origin, Point target, Briscola2PMatchActivity activity) {


        AnimatorSet translation = new AnimatorSet();
        ObjectAnimator translationX = new ObjectAnimator().ofFloat(targetView, "translationX", target.x - origin.x);
        translationX.setDuration(1000);
        translationX.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(targetView, "translationY", target.y - origin.y);
        translationY.setDuration(1000);
        translationY.setInterpolator(new AccelerateDecelerateInterpolator());
        translation.playTogether(translationX, translationY);
        translation.addListener(new MoveCardAnimationListener(activity.getSoundManager()));

        return translation;
    }

    /*public static AnimatorSet getTranslationAnimationSet(ImageSwitcher targetView, Point origin, Point target, boolean flip, int elevation) {
        AnimatorSet translation = getTranslationAnimationSet(targetView, origin, target, flip, elevation);
        return translation;

    }*/
}