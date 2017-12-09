package it.ma.polimi.briscola.controller.daFare;

import android.animation.Animator;

import it.ma.polimi.briscola.Briscola2PMatchActivity;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;

/**
 * Created by utente on 08/12/17.
 */

public class GameInteractionEnabler implements Animator.AnimatorListener{

        private final Briscola2PMatchController controller;
        private final boolean enable;

        public GameInteractionEnabler(Briscola2PMatchController controller, boolean enable){
            this.controller = controller;
            this.enable = enable;
        }
        @Override
        public void onAnimationStart(Animator animator) {
        }
        @Override
        public void onAnimationEnd(Animator animator) {
            if(enable)
               controller.playing = true;
            else
                controller.playing = false;
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }

}
