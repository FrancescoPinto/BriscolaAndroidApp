package it.ma.polimi.briscola.model.briscola.twoplayers.ai;

import java.util.concurrent.ThreadLocalRandom;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;

/**
 * Class implementing a dull AI that always makes a random move, choosing a number at random among the indices representing the available cards in the AI's hand
 *
 * @author Francesco Pinto
 */

public class Briscola2PAIRandomPlayer implements Briscola2PAIPlayer {
    @Override
    public int chooseMove(Briscola2PMatchConfig config) {
        int max = config.getHand(config.PLAYER1).size();
        ThreadLocalRandom.current().nextInt(0, max);
        return 0;
    }
}
