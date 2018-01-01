package it.ma.polimi.briscola.view.fragments;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PFullMatchConfig;

/**
 * Class that helds some indices that will be used by the view
 *
 * @author Francesco Pinto
 */
public enum SlotIndices {
    /**
     * Offset layout player 0 slot indices.
     */
    OffsetLayoutPlayer0(0),
    /**
     * Offset layout player 1 slot indices.
     */
    OffsetLayoutPlayer1(1),
    /**
     * Offset layout surface slot indices.
     */
    OffsetLayoutSurface(2),

    /**
     * Player 0 card 0 slot indices.
     */
    Player0Card0(3),
    /**
     * Player 0 card 1 slot indices.
     */
    Player0Card1(4),
    /**
     * Player 0 card 2 slot indices.
     */
    Player0Card2(5),
    /**
     * Player 0 pile slot slot indices.
     */
    Player0PileSlot(6),

    /**
     * Player 1 card 0 slot indices.
     */
    Player1Card0(7),
    /**
     * Player 1 card 1 slot indices.
     */
    Player1Card1(8),
    /**
     * Player 1 card 2 slot indices.
     */
    Player1Card2(9),
    /**
     * Player 1 pile slot slot indices.
     */
    Player1PileSlot(10),

    /**
     * Deck slot slot indices.
     */
    DeckSlot(11),
    /**
     * Briscola slot slot indices.
     */
    BriscolaSlot(12),
    /**
     * Surface slot 0 slot indices.
     */
    SurfaceSlot0(13),
    /**
     * Surface slot 1 slot indices.
     */
    SurfaceSlot1(14);

    private int index;
    /**
     * The constant player0Offset.
     */
    public static final int player0Offset = 3;
    /**
     * The constant player1Offset.
     */
    public static final int player1Offset = 7;
    private SlotIndices(int index){
        this.index = index;
    }

    /**
     * Method that computes the card index in hand given the slot index of the card
     *
     * @param i the slot index of the card in player's hand
     * @return the int representing the card index (i.e. 0,1,2)
     */
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

    /**
     * Given the card index (i.e. 0,1,2) of a player, compute the corresponding SlotIndices
     *
     * @param cardIndex     the card index
     * @param currentPlayer the current player
     * @return the slot indices
     */
    public static SlotIndices getPlayerCardSlotIndex(int cardIndex, int currentPlayer){
        switch(currentPlayer){
            case Briscola2PFullMatchConfig.PLAYER0:
                switch(cardIndex){
                    case 0: return SlotIndices.Player0Card0;
                    case 1:return SlotIndices.Player0Card1;
                    case 2:return SlotIndices.Player0Card2;
                };
            case Briscola2PFullMatchConfig.PLAYER1:
                switch(cardIndex){
                    case 0: return SlotIndices.Player1Card0;
                    case 1:return SlotIndices.Player1Card1;
                    case 2:return SlotIndices.Player1Card2;
                };
        }
        throw new IllegalArgumentException();
    }

    /**
     * Get pile slot index of player slot.
     *
     * @param player the player
     * @return the slot indices
     */
    public static SlotIndices getPileSlotOfPlayer(int player){
        if(player == Briscola2PFullMatchConfig.PLAYER0)
            return SlotIndices.Player0PileSlot;
        else if(player == Briscola2PFullMatchConfig.PLAYER1)
            return SlotIndices.Player1PileSlot;

        throw new IllegalArgumentException();
    }

    /**
     * Gets index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }
}