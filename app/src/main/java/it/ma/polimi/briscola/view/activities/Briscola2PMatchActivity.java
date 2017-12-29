package it.ma.polimi.briscola.view.activities;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.view.fragments.Briscola2PMatchFragment;

/**
 * Interface containing methods and data that any Activity desiring to play a match should provide.
 *
 * @author Francesco Pinto
 */
public interface Briscola2PMatchActivity {
    /**
     * These constans represent motivations for same actions. They are used to take decisions along chains of interactions.
     * They are: EXIT_BUTTON.
     */
    public static final int EXIT_BUTTON = 0, /**
     * START_NEW_OFFLINE.
     */
    START_NEW_OFFLINE = 1, /**
     * START_NEW_ONLINE.
     */
    START_NEW_ONLINE = 2, /**
     * LOAD_OLD_MATCH.
     */
    LOAD_OLD_MATCH = 3, /**
     * BACK_PRESSED.
     */
    BACK_PRESSED = 4,
    /**
     * ONLINE_ERROR
     */
    ONLINE_ERROR = 5;
    /**
     * NO_MOTIVATION.
     */
    public static final int NO_MOTIVATION = -1;


    /**
     * Handles the operations associated with the display of the match menu.
     *
     * @param isOverlay whether the menu should be shown as an overlay on another fragment or not
     */
    public void startMenu(boolean isOverlay);

    /**
     * Handle the operations associated with the start of an offline match.
     */
    public void startOfflineMatch();

    /**
     * Handle the operations associated with the start of an online match.
     */
    public void startOnlineMatch();

    /**
     * Handle the operations associated with loading an offline match.
     *
     * @param config the configuration from which the match should be started
     */
    public void loadOfflineMatch(Briscola2PMatchConfig config);


    /**
     * Gets matches fragments if the fragment manager has one of them.
     *
     * @return the matches fragments
     */
    public Briscola2PMatchFragment getMatchesFragments();

    /**
     * Hide overlay menu.
     */
    public void hideOverlayMenu();

    /**
     * Handle the operations associated with showing the previously saved matches.
     */
    public void showSavedMatches();
}
