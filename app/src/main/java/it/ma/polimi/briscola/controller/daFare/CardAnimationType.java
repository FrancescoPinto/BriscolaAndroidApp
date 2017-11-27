package it.ma.polimi.briscola.controller.daFare;

import it.ma.polimi.briscola.R;

/**
 * Created by utente on 26/11/17.
 */

public enum CardAnimationType {

    ToSurfaceSlot0(R.id.surfaceSlot0),
    ToSurfaceSlot1(R.id.surfaceSlot1),
    ToPilePlayer0(R.id.player0PileSlot),
    ToPilePlayer1(R.id.player1PileSlot),
    ToCard0SlotHand1(R.id.player1Card0Slot),
    ToCard1SlotHand1(R.id.player1Card1Slot),
    ToCard2SlotHand1(R.id.player1Card2Slot),
    ToCard0SlotHand0(R.id.player0Card0Slot),
    ToCard1SlotHand0(R.id.player0Card1Slot),
    ToCard2SlotHand0(R.id.player0Card2Slot);
    //todo, aggiungi briscola slot

    private int idDestinationView;
    CardAnimationType(int destinationViewId) {
        this.idDestinationView = destinationViewId;
    }
}
