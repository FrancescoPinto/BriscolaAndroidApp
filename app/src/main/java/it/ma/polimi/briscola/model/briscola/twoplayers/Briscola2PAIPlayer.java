package it.ma.polimi.briscola.model.briscola.twoplayers;

/**
 * Created by utente on 31/10/17.
 */

public interface Briscola2PAIPlayer {
    public int chooseMove(Briscola2PMatch match); //todo the AI SHOULDN'T (but could cheat ...) know the hand of the human player, the Match class should pass an obscured config (destroy the info on the other hand)
}
