package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by utente on 25/10/17.
 */

public class Briscola2PMatchScoreRanking {
    List<Briscola2PMatchRecord> ranking = new ArrayList<>();

    public Briscola2PMatchScoreRanking(List<Briscola2PMatchRecord> ranking) {
        Collections.sort(ranking);
        this.ranking = ranking;
    }

    public List<Briscola2PMatchRecord> getRanking() {
        return ranking;
    }

    public void setRanking(List<Briscola2PMatchRecord> ranking) {
        Collections.sort(ranking);
        this.ranking = ranking;
    }
}
