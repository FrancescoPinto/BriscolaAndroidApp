package it.ma.polimi.briscola;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;

/**
 * Created by utente on 04/12/17.
 */

public enum SlotIndices {
    OffsetLayoutPlayer0(0),
    OffsetLayoutPlayer1(1),
    OffsetLayoutSurface(2),

    Player0Card0(3),
    Player0Card1(4),
    Player0Card2(5),
    Player0PileSlot(6),

    Player1Card0(7),
    Player1Card1(8),
    Player1Card2(9),
    Player1PileSlot(10),

    DeckSlot(11),
    BriscolaSlot(12),
    SurfaceSlot0(13),
    SurfaceSlot1(14);

    private int index;
    public static final int player0Offset = 3;
    public static final int player1Offset = 7;
    private SlotIndices(int index){
        this.index = index;
    }

    public static int playerCardIndex(SlotIndices i){
        switch(i.index){
            case 3:
            case 4:
            case 5: return i.index - player0Offset;
            case 7:
            case 8:
            case 9: return i.index - player1Offset;
            default: throw new IllegalArgumentException();
        }
    }

    public static SlotIndices getPlayerCardSlotIndex(int cardIndex, int currentPlayer){
        switch(currentPlayer){
            case Briscola2PMatchConfig.PLAYER0:
                switch(cardIndex){
                    case 0: return SlotIndices.Player0Card0;
                    case 1:return SlotIndices.Player0Card1;
                    case 2:return SlotIndices.Player0Card2;
                };
            case Briscola2PMatchConfig.PLAYER1:
                switch(cardIndex){
                    case 0: return SlotIndices.Player1Card0;
                    case 1:return SlotIndices.Player1Card1;
                    case 2:return SlotIndices.Player1Card2;
                };
        }
        throw new IllegalArgumentException();
    }

    public static SlotIndices getPileSlotOfPlayer(int player){
        if(player == Briscola2PMatchConfig.PLAYER0)
            return SlotIndices.Player0PileSlot;
        else if(player == Briscola2PMatchConfig.PLAYER1)
            return SlotIndices.Player1PileSlot;

        throw new IllegalArgumentException();
    }
}