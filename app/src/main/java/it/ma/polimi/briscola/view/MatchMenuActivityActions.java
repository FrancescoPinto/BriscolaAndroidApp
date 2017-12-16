package it.ma.polimi.briscola.view;

/**
 * Created by utente on 14/12/17.
 */

public enum MatchMenuActivityActions {
    STOP_ONLINE(0),
    STOP_OFFLINE(1),
    WARN_STOP_OFFLINE(2),
    SAVE_STOP_OFFLINE(3);

    private int code;

    MatchMenuActivityActions(int code){
        this.code = code;
    }
}
