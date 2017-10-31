package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by utente on 31/10/17.
 */

public class Briscola2PAIRandomPlayer implements Briscola2PAIPlayer {
    @Override
    public int chooseMove(Briscola2PMatch match) {
     //   Briscola2PMatchConfig obscuredConfig = match.getObscuredConfig();
     //   int max = obscuredConfig.getHand(obscuredConfig.PLAYER1).countCards();
    //     ThreadLocalRandom.current().nextInt(0, max);
        return 0;
    }
}
