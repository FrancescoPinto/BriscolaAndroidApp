package it.ma.polimi.briscola.audio;

/**
 * Enum representing game events that should be notified to the sound service
 *
 * @author Francesco Pinto
 */
public enum GameEvent {
    /**
     * Move card game event.
     */
    MoveCard,
    /**
     * Flip card game event.
     */
    FlipCard,
    /**
     * Win round game event.
     */
    WinRound,
    /**
     * Lose round game event.
     */
    LoseRound,
    /**
     * Win match game event.
     */
    WinMatch,
    /**
     * Lose match game event.
     */
    LoseMatch;
}
