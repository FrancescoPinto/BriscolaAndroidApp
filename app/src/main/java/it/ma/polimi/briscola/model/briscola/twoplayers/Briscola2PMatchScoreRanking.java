package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a ranking of Briscola2PMatchRecors of previously played matches. In the ranking, records are ordered according to the inverse of the compareTo method of Briscola2PMatchRecord logic, i.e. top ranked players are the humans (only humans) with the best scores.
 */
public class Briscola2PMatchScoreRanking {

    private List<Briscola2PMatchRecord> ranking = new ArrayList<>();

    /**
     * Instantiates a new Briscola 2 p match score ranking based on an (unordered) list of Briscola2PMatchRecords
     *
     * @param records the list of records
     */
    public Briscola2PMatchScoreRanking(List<Briscola2PMatchRecord> records) {
        Collections.sort(records);
        Collections.reverse(records);
        this.ranking = records;
    }

    /**
     * Gets ranking. Top ranked players are the humans (only humans) with the best scores.
     *
     * @return the ranking
     */
    public List<Briscola2PMatchRecord> getRanking() {
        return ranking;
    }

    /**
     * Sets ranking given an (unordered) list of Briscola2PMatchRecords
     *
     * @param records the list of records
     */
    public void setRanking(List<Briscola2PMatchRecord> records) {
        Collections.sort(records);
        Collections.reverse(records);
        this.ranking = records;
    }
}
