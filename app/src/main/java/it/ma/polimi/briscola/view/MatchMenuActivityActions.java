package it.ma.polimi.briscola.view;

/**
 * Enum representing the actions a MatchMenu Activity should perform (when this enum is passed as an argument)
 */
public enum MatchMenuActivityActions {
    /**
     * Stop online match menu activity action.
     */
    STOP_ONLINE(0),
    /**
     * Stop offline match menu activity action.
     */
    STOP_OFFLINE(1),
    /**
     * Warn stop offline match menu activity action.
     */
    WARN_STOP_OFFLINE(2),
    /**
     * Save stop offline match menu activity action.
     */
    SAVE_STOP_OFFLINE(3),
    /**
     * Do nothing match menu activity action.
     */
    DO_NOTHING(4);

    private int code;

    MatchMenuActivityActions(int code){
        this.code = code;
    }
}
