package it.ma.polimi.briscola.controller;

import android.app.Fragment;
import android.content.Context;

import it.ma.polimi.briscola.view.fragments.Briscola2PMatchFragment;

/**
 * Interface that should be implemented by any Controller that wants to interact with a server. It imposes the controller to have methods that are used in Callbacks to react to server response.
 */
public interface ControllerWithServerResponseManager {

    /**
     * Manage the occurrence of an error. Actually, since the app is structured to avoid error occurrence, only "fatal" errors can actually occur (e.g. timeout)
     *
     * @param error   the error, sent by the server
     * @param message the message, sent by the server
     */
    void manageError(String error, String message);

    /**
     * Manage the response to an opponent played card request.
     *
     * @param opponent the opponent card, sent by the server
     * @param card     the card for the next turn, if any, sent by the server
     */
    void manageOpponentPlayedCard(String opponent, String card);

    /**
     * Gets match fragment, mainly used by callbacks to know whether the fragment is visible.
     *
     * @return the match fragment
     */
    Briscola2PMatchFragment getMatchFragment();

    /**
     * Manage next turn card.
     *
     * @param card the next turn card, sent by the server
     */
    void manageNextTurnCard(String card);

    /**
     * Manage started match.
     *
     * @param game     the game url, sent by the server
     * @param lastCard the last card (briscola), sent by the server
     * @param cards    the cards in player0 hand, sent by the server
     * @param yourTurn the your turn variable, sent by the server
     * @param url      the url, sent by the server
     */
    void manageStartedMatch(String game, String lastCard,String cards,String yourTurn,String url);

    /**
     * Force match end (REMARK: this is an interrupt of the match, that should be properly handled. For normal termination, use StopMatch)
     *
     * @param context the context
     * @param url     the url
     */
    void forceMatchEnd(Context context, String url);
}
