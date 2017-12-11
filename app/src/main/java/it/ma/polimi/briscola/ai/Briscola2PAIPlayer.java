package it.ma.polimi.briscola.ai;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;

/**
 * Interface representing a Briscola AI Player for a 2 Players match
 *
 * @author Francesco Pinto
 */
public interface Briscola2PAIPlayer {
    /**
     * Choose move: given the match configuration, the AI should choose a move
     *
     * @param config Object representing the match configuration
     * @return The choosen move, i.e. an integer among {0,1,2} representing the card to be played
     */
    public int chooseMove(Briscola2PMatchConfig config);
}
