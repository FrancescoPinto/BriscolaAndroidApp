package it.ma.polimi.briscola.controller.offline;

import android.animation.Animator;

/**
 * Created by utente on 08/12/17.
 */

public class GameInteractionEnabler implements Animator.AnimatorListener{

        private final Briscola2PController controller;
        private final boolean enable;

        public GameInteractionEnabler(Briscola2PController controller, boolean enable){
            this.controller = controller;
            this.enable = enable;
        }
        @Override
        public void onAnimationStart(Animator animator) {
        }
        @Override
        public void onAnimationEnd(Animator animator) {
            if(enable)
               controller.setIsPlaying(true);
            else
                controller.setIsPlaying(false);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }

}
