package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.List;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;

/**
 * Interface declaring all the methods a generic Briscol2PMatchConfiguration should provide
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
    DRAW = -1, /**
     * Finished, i.e. the number of turns passed to finish a match (20 + 1 to represent termination)
     */
    FINISHED = 21;

    /**
     * Count the number of cards currently on surface.
     *
     * @return the int representing the number of cards currently on surface.
     */
    int countCardsOnSurface();

    /**
     * Gets number turns elapsed.
     *
     * @return the number turns elapsed
     */
    int getNumberTurnsElapsed();

    /**
     * Gets current player.
     *
     * @return Index of the current player, is either the public final int PLAYER0 or PLAYER1 provided by this class
     */
    int getCurrentPlayer();

    /**
     * Play card. Removes the card represented by the String card from hand and puts it on the surface.
     *
     * @param card the String representing the card to be played (should be in hand of the current player!)
     */
    void playCard(String card);

    /**
     * Play card. Removes the i-th card from the hand of the current player and puts it onto the surface.
     *
     * @param card the index of the card to be played (should be either CARD1,CARD2 or CARD3 of the public final ints provided by the Briscola2PHand class)
     */
    void playCard(Integer card);

    /**
     * Toggle current player.
     * @throws IllegalStateException if the currentPlayer has not been initialized
     */
    void toggleCurrentPlayer();

    /**
     * Choose round winner int, should be called at the end of the round, after clearSurface has been called.
     *
     * @return the int representing the round winner (either PLAYER0 or PLAYER1)
     * @throws IllegalStateException if the surface is not filled (round is not finished), or if the surface is in an inconsistent state (i.e. does not satisfy any of the round winner evaluation rules)
     */
    int chooseRoundWinner();

    /**
     * Clear surface. Clears the surface and appends the cards on the surface to the pile of the winner.
     *
     * @param winner the winner index, is either the public final int PLAYER0 or PLAYER1 provided by this class
     * @throws IllegalStateException if the surface is not completely filled
     */
    List<NeapolitanCard> clearSurface(int winner);

    /**
     * Sets current player.
     *
     * @param currentPlayer the current player, should be either the public final int PLAYER0 or PLAYER1 provided by this class
     * @throws IllegalArgumentException if invalid player is specified.
     */
    void setCurrentPlayer(int currentPlayer);

    /**
     * Choose match winner, should be called after the end of the whole match.
     *
     * @return the int representing the player who won the match (PLAYER0,PLAYER1 or DRAW in case of draw)
     * @throws IllegalStateException if all 120 points are not in the player's piles
     */
    int chooseMatchWinner();

    /**
     * Draw cards for the new round. Should be called at the beginning of the round, after the current player has been set. If the deck is empty, the configuration is unaffected.
     */
    List<NeapolitanCard> drawCardsNewRound();

    /**
     * Gets hands.
     *
     * @return the hands
     */
    List<Briscola2PHand> getHands();

    /**
     * Compute score examining the piles of the specified player (this class does not enforce the match end, since it could be called during the match, for instance, to show the current points of each player on the GUI)
     *
     * @param playerIndex the player index, should be either PLAYER0 or PLAYER1 provided by this class
     * @return the int representing the score, computed summing the point values of the cards in the player's pile
     */
    int computeScore(int playerIndex);

    /**
     * Gets the briscola.
     *
     * @return the briscola
     */
    NeapolitanCard getBriscola();

    /**
     * Sets surface.
     *
     * @param surface the surface
     */
    void setSurface(Briscola2PSurface surface);

    /**
     * Gets pile of the i-th player
     *
     * @param playerIndex the player index, should be the public final int PLAYER0 or PLAYER1 provided by this class
     * @return the pile
     */
    Briscola2PPile getPile(int playerIndex);

    /**
     * Gets surface.
     *
     * @return the surface
     */
    Briscola2PSurface getSurface();
    /**
     * Gets briscola suit.
     *
     * @return the briscola suit, among NeapolitanCardSuit enum values
     */
    String getBriscolaSuit();
    /**
     * Set pile of the i-th player from Briscola2PPile
     *
     * @param i    the player's index, should be either the public final int PLAYER0 or PLAYER1 provided by this class
     * @param pile the pile represented as a Briscola2PPile object
     * @throws IllegalArgumentException if invalid index is specified
     */
    void setPile(int i, Briscola2PPile pile);
    /**
     * Sets briscola suit.
     *
     * @param briscolaSuit the briscola suit, should be among NeapolitanCardSuit enum values
     */
    void setBriscolaSuit(String briscolaSuit);
    /**
     * Set pile of the i-th player from String
     *
     * @param i    the player's index, should be either the public final int PLAYER0 or PLAYER1 provided by this class
     * @param pile String representing the pile, format as specified in the slides
     */
    void setPile(int i, String pile);

}
