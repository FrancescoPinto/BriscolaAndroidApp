package it.ma.polimi.briscola.controller.offline;

/**
 * Created by utente on 10/12/17.
 */

public interface Briscola2PController {

    public void startNewMatch();
    public void setIsPlaying(boolean isPlaying);
    public boolean isPlaying();
    public int getCurrentPlayer();
    public int countCardsOnSurface();
    public void playFirstCard(int cardIndex);
    public void playSecondCard(int cardIndex);
    public int getHandSize(int playerIndex);
    public void forceMatchEnd();
    public void resumeMatch();
    public int inferTurnsElapsed();
}
