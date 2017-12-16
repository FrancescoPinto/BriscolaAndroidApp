package it.ma.polimi.briscola.view.activities;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.view.fragments.Briscola2PMatchFragment;

/**
 * Created by utente on 14/12/17.
 */

public interface Briscola2PMatchActivity {
    public static final int EXIT_BUTTON = 0, START_NEW_OFFLINE = 1, START_NEW_ONLINE = 2, LOAD_OLD_MATCH = 3;
    public static final int NO_MOTIVATION = -1;



    public void startMenu(boolean isOverlay);

    public void startOfflineMatch();

    public void startOnlineMatch();
    public void loadOfflineMatch(Briscola2PMatchConfig config);
    public void showSavedMatches();
    public Briscola2PMatchFragment getOldMatches();
    public void hideOverlayMenu();
}
