package it.ma.polimi.briscola.controller.listeners;

import android.view.View;
import android.widget.ImageView;

import java.util.Map;


import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.controller.Briscola2PController;
import it.ma.polimi.briscola.view.fragments.SlotIndices;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.view.fragments.Briscola2PMatchFragment;

/**
 * The CardAnimationListener listens for card touches from the user, and, if the game logic allows it, plays the touched card
 *
 * @author Francesco Pinto
 */
public class CardAnimationListener implements View.OnClickListener {


    private Briscola2PMatchFragment fragment;
    private Briscola2PController controller;
    private boolean alreadyPressed = false;
    /**
     * Instantiates a new Card animation listener.
     *
     * @param fragment the fragment
     */
    public CardAnimationListener(Briscola2PMatchFragment fragment){
        this.fragment = fragment;
        this.controller = fragment.getController();
    }

    @Override
    public void onClick(View view) {
        //controller.isPlaying() is "synchronized" with the view
        //indeed, configuration info is not enough to determine when the user should or shouldn't interact with the card, since config computations happen almost istantaneously
        //while animations take some time to be executed ... then isPlaying() is true only when the user on the GUI a status that means he can play a card

        //if can play (synchronized with the view), and is your turn, choose what to do based on the number of cards on surface
        if(controller.isPlaying() && controller.countCardsOnSurface() == 0 && controller.getCurrentPlayer() == Briscola2PMatchConfig.PLAYER0) {
            controller.playFirstCard(getPlayerCardIndex(view));//.start();
            view.setOnClickListener(null); //disable the listener on this card
        }else if(controller.isPlaying() && controller.countCardsOnSurface() == 1 && controller.getCurrentPlayer() == Briscola2PMatchConfig.PLAYER0){
            controller.playSecondCard(getPlayerCardIndex(view));//.start();
            view.setOnClickListener(null); //disable the listener on this card
        }else if(!controller.isPlaying()){ //if the user can't play, tell him via a toast
            if(alreadyPressed == false) {
                fragment.showToast(fragment.getString(R.string.warn_cant_play));
                alreadyPressed = true; //avoid to clutter the GUI with many toasts in case user presses the button many times
                //the user has a "Is your turn box", can clearly understand when it is his/her turn, once it has been warned, stop warning him every time he touches the same card
            }
        }

    }


    //helper method, determines the card index of the clicked view
    private int getPlayerCardIndex(View view){
        //get all the available cards in slot positions in the fragment
        Map<SlotIndices,ImageView> cards = fragment.getCards();

        for(SlotIndices slotIndex: cards.keySet()){
            if(cards.get(slotIndex).getId() == view.getId())
                //compute the card index based on the matching slot index
                return slotIndex.playerCardIndex(slotIndex);
        }

        throw new IllegalStateException();
    }



}