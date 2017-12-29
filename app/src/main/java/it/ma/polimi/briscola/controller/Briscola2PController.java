package it.ma.polimi.briscola.controller;

import android.content.Context;

/**
 *Interface providing the basic functionalities required by a GUI controller in any case (used to improve reusability of the View part of the applicaion, e.g. in online/offline cases)
 *
 * @author Francesco Pinto
 */
public interface Briscola2PController {

    /**
     * Perform operations to start a new match
     */
    public void startNewMatch();

    /**
     * Set whether the player can interact with the game or not
     *
     * @param isPlaying True if the player should be able to interact with the game, false otherwise
     */
    public void setIsPlaying(boolean isPlaying);

    /**
     * Know whether the player can interact with the game or not
     *
     * @return True if the player should be able to interact with the game, false otherwise
     */
    public boolean isPlaying();

    /**
     * Gets current player index
     *
     * @return either PLAYER0 for local player, PLAYER1 for AI/Remote, cfr. Briscola2PMatchController
     */
    public int getCurrentPlayer();

    /**
     * Count cards currently on surface.
     *
     * @return number of cards currently on surface
     */
    public int countCardsOnSurface();

    /**
     * Make the current player play the first card of the turn.
     *
     * @param cardIndex the index of the card to be played
     */
    public void playFirstCard(int cardIndex);

    /**
     * Make the current player play the second card of the turn.
     *
     * @param cardIndex the index of the card to be played
     */
    public void playSecondCard(int cardIndex);

    /**
     * Gets player hand size.
     *
     * @param playerIndex the player index (can be either PLAYER0 or PLAYER1, see Briscola2PMatchConfig)
     * @return the hand size
     */
    public int getHandSize(int playerIndex);

    /**
     * Performs operations to close the match
     *
     * @param context the context, used to get the string to show to the user (if any)
     */
    public void forceMatchEnd(Context context);

    /**
     * Performs operations to resume the match.
     */
    public void resumeMatch();

    /**
     * Gets turns elapsed from match start (starts counting from 1)
     *
     * @return the current turn
     */
    public int getTurnsElapsed();
}
