package it.ma.polimi.briscola.controller.daFare;

import android.support.constraint.ConstraintLayout;
import android.view.View;

import it.ma.polimi.briscola.R;

/**
 * Created by utente on 26/11/17.
 */

public class DeckCardDealerListener implements View.OnClickListener {

    private AnimationTest activityTest;
    private ConstraintLayout layout;
    private Briscola2PMatchActivity activity;

    public DeckCardDealerListener(ConstraintLayout layout, AnimationTest activity){
        this.activityTest = activity;
        this.layout = layout;
    }

    public DeckCardDealerListener(ConstraintLayout layout, Briscola2PMatchActivity activity){
        this.activity = activity;
        this.layout = layout;
    }

    @Override
    public void onClick(View view) {
        switch(activity.getCardAnimationType(view.getId())){
            case ToCard0SlotHand1:
                AnimationMaster.moveCardToSlot(R.id.player1Card0Slot, view.getId(),layout); break;
            case ToCard1SlotHand1:AnimationMaster.moveCardToSlot(R.id.player1Card1Slot, view.getId(),layout); break;
            case ToCard2SlotHand1:AnimationMaster.moveCardToSlot(R.id.player1Card2Slot, view.getId(),layout); break;
            case ToCard0SlotHand0:AnimationMaster.moveCardToSlot(R.id.player0Card0Slot, view.getId(),layout); break;
            case ToCard1SlotHand0:AnimationMaster.moveCardToSlot(R.id.player0Card1Slot, view.getId(),layout); break;
            case ToCard2SlotHand0:AnimationMaster.moveCardToSlot(R.id.player0Card2Slot, view.getId(),layout); break;
        }
        activity.deleteCardAnimationType(view.getId()); //pulisci

    }




}
