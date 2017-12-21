package it.ma.polimi.briscola.ai;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;

/**
 * Class implementing a dull AI that always makes a random move, choosing a number at random among the indices representing the available cards in the AI's hand
 *
 * @author Francesco Pinto
 */

public class Briscola2PAIRandomPlayer implements Briscola2PAIPlayer {
    private static Random random = new Random();
    @Override
    public int chooseMove(Briscola2PMatchConfig config, int playerIndex) {
        int max = config.getHand(playerIndex).size();
        if(max == 0)
            return 0;
        else
            return random.nextInt(max);
            //return ThreadLocalRandom.current().nextInt(0, max);

    }
}
