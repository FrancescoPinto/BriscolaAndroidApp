package it.ma.polimi.briscola.controller.listeners;

import android.view.View;
import android.widget.ImageView;

import java.util.Map;
import java.util.Set;


import it.ma.polimi.briscola.controller.offline.Briscola2PController;
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
        //if controller
        if(controller.isPlaying() && controller.countCardsOnSurface() == 0 && controller.getCurrentPlayer() == Briscola2PMatchConfig.PLAYER0) {
            controller.playFirstCard(getPlayerCardIndex(view));//.start();
            view.setOnClickListener(null); //disable the listener on this card
        }else if(controller.isPlaying() && controller.countCardsOnSurface() == 1 && controller.getCurrentPlayer() == Briscola2PMatchConfig.PLAYER0){
            controller.playSecondCard(getPlayerCardIndex(view));//.start();
            view.setOnClickListener(null); //disable the listener on this card
        }else if(!controller.isPlaying()){
            fragment.showToast("In this moment you can't choose a card, please, wait!");
        }

    }

    private int getPlayerCardIndex(View view){
        Map<SlotIndices,ImageView> cards = fragment.getCards();
        Set<SlotIndices> indices = cards.keySet();
        for(SlotIndices slotIndex: cards.keySet()){
            if(cards.get(slotIndex).getId() == view.getId())
                return slotIndex.playerCardIndex(slotIndex);
        }

        throw new IllegalStateException();
    }



}