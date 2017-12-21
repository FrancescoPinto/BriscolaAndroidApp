package it.ma.polimi.briscola.controller.listeners;

import android.animation.Animator;

import it.ma.polimi.briscola.controller.Briscola2PController;

/**
 * The GameInteractionEnabler is an AnimationListener that overrides onAnimationEnd to toggle the Controller.isPlaying() value, so that the user interaction is enabled/disabled according to what the user actually can see in the GUI.
 * Indeed, changes in the config happen too fast: in order to keep the controller "synchronized" with the GUI, should "wait" for slow animations to be completely executed.
 *
 * @author Francesco Pinto
 */
public class GameInteractionEnabler implements Animator.AnimatorListener{

        private final Briscola2PController controller;
        private final boolean enable;

    /**
     * Instantiates a new Game interaction enabler.
     *
     * @param controller the controller
     * @param enable     whether to enable or not the interaction
     */
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
               controller.setIsPlaying(true); //enable interaction
            else
                controller.setIsPlaying(false); //disable interaction
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }

}
