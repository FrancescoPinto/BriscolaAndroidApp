package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.List;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;

/**
 * Created by utente on 31/12/17.
 */

public interface Briscola2PMatchConfig {

    /**
     * The constant PLAYER0 is an index used to represent the Local Player (i.e. the human player on the local device)
     */
    public static final int PLAYER0 = 0, /**
     * The Player 1 is an index used to represent the "other player" (i.e. either a local/remote AI or another remote human player)
     */
    PLAYER1 = 1, /**
     * The Draw, index used to represent a Draw in the match
     */
    DRAW = -1,/**
    * Finished, i.e. the number of turns passed to finish a match (20 + 1 to represent termination)
    */
    FINISHED = 21;

    /**
     * Count the number of cards currently on surface.
     *
     * @return the int representing the number of cards currently on surface.
     */
    int countCardsOnSurface();
    int getNumberTurnsElapsed();

    /**
     * Gets current player.
     *
     * @return Index of the current player, is either the public final int PLAYER0 or PLAYER1 provided by this class
     */
    int getCurrentPlayer();
    void playCard(String card);
    void playCard(Integer firstCard);
    void toggleCurrentPlayer();
    int chooseRoundWinner();
    List<NeapolitanCard> clearSurface(int winner);
    /**
     * Sets current player.
     *
     * @param currentPlayer the current player, should be either the public final int PLAYER0 or PLAYER1 provided by this class
     * @throws IllegalArgumentException if invalid player is specified.
     */
    void setCurrentPlayer(int currentPlayer);
    int chooseMatchWinner();
    List<NeapolitanCard> drawCardsNewRound();
    List<Briscola2PHand> getHands();
    int computeScore(int playerIndex);
    NeapolitanCard getBriscola();
    /**
     * Sets surface.
     *
     * @param surface the surface
     */
    void setSurface(Briscola2PSurface surface);
    Briscola2PPile getPile(int playerIndex);
    /**
     * Gets surface.
     *
     * @return the surface
     */
    Briscola2PSurface getSurface();


}
