package it.ma.polimi.briscola.controller.daFare;

import android.animation.AnimatorSet;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import java.util.Map;
import java.util.Set;

import it.ma.polimi.briscola.Briscola2PMatchActivity;
import it.ma.polimi.briscola.R;


import it.ma.polimi.briscola.SlotIndices;
import it.ma.polimi.briscola.controller.daFare.CardAnimationType;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;

/**
 * Created by utente on 03/12/17.
 */

public class CardAnimationListener implements View.OnClickListener {


    private Briscola2PMatchActivity activity;
    private Briscola2PMatchController controller;

    public CardAnimationListener(Briscola2PMatchActivity activity){
        this.activity = activity;
        this.controller = activity.getController();
    }

    @Override
    public void onClick(View view) {
        Briscola2PMatchConfig config = controller.getConfig();
        if(controller.playing && config.countCardsOnSurface() == 0 && config.getCurrentPlayer() == config.PLAYER0) {
            controller.playFirstCard(getPlayerCardIndex(view)).start();
            view.setOnClickListener(null);
        }else if(controller.playing && config.countCardsOnSurface() == 1 && config.getCurrentPlayer() == config.PLAYER0){
            controller.playSecondCard(getPlayerCardIndex(view)).start();
            view.setOnClickListener(null);
        }else if(!controller.playing){
            activity.showToast("In this moment you can't choose a card, please, wait!");
        }

    }

    private int getPlayerCardIndex(View view){
        Map<SlotIndices,ImageView> cards = activity.getCards();
        Set<SlotIndices> indices = cards.keySet();
        for(SlotIndices slotIndex: cards.keySet()){
            if(cards.get(slotIndex).getId() == view.getId())
                return slotIndex.playerCardIndex(slotIndex);
        }

        throw new IllegalStateException();
    }



}