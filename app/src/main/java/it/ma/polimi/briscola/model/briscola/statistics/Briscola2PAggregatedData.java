package it.ma.polimi.briscola.model.briscola.statistics;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.List;

/**
 * Class representing aggregated data about a list of saved match records
 */
public class Briscola2PAggregatedData implements Comparable {

    private int numberOfMatchesPlayed,
                numberOfMatchesWinned,
                numberOfDraws,
                numberOfMatchesOnline,
                totalScore; //contains the total number of points made by the player (no matter if he won or lost)


    /**
     * Instantiates a new Briscola 2 p aggregated data from a list of Briscola2PMatchRecords
     *
     * @param userPreviousMatches the records of user previous matches
     */
    public Briscola2PAggregatedData(List<Briscola2PMatchRecord> userPreviousMatches) {

        numberOfMatchesPlayed = totalScore = numberOfMatchesWinned = numberOfDraws = numberOfMatchesOnline= 0;

        for(Briscola2PMatchRecord mr : userPreviousMatches){
            String temp = mr.getWinnerName();
            if( temp == null)
                numberOfDraws++; //count the number of draws
            else if(temp.equals(mr.getPlayer0Name()))
                numberOfMatchesWinned++; //cunt the number of matches winned

            if(mr.getPlayer1Name().equals(Briscola2PMatchRecord.remotePlayerDefault))
                numberOfMatchesOnline++; //count the number of online matches

            numberOfMatchesPlayed++; //count the number of matches played
            totalScore+=mr.getPlayer0Score(); //increase the totalScore
        }
    }

    /**
     * Gets number of matches online.
     *
     * @return the number of matches online
     */
    public int getNumberOfMatchesOnline() {
        return numberOfMatchesOnline;
    }

    /**
     * Sets number of matches online.
     *
     * @param numberOfMatchesOnline the number of matches online
     */
    public void setNumberOfMatchesOnline(int numberOfMatchesOnline) {
        this.numberOfMatchesOnline = numberOfMatchesOnline;
    }


    /**
     * Gets number of matches played.
     *
     * @return the number of matches played
     */
    public int getNumberOfMatchesPlayed() {
        return numberOfMatchesPlayed;
    }

    /**
     * Sets number of matches played.
     *
     * @param numberOfMatchesPlayed the number of matches played
     */
    public void setNumberOfMatchesPlayed(int numberOfMatchesPlayed) {
        this.numberOfMatchesPlayed = numberOfMatchesPlayed;
    }

    /**
     * Gets number of matches winned.
     *
     * @return the number of matches winned
     */
    public int getNumberOfMatchesWinned() {
        return numberOfMatchesWinned;
    }

    /**
     * Sets number of matches winned.
     *
     * @param numberOfMatchesWinned the number of matches winned
     */
    public void setNumberOfMatchesWinned(int numberOfMatchesWinned) {
        this.numberOfMatchesWinned = numberOfMatchesWinned;
    }

    /**
     * Gets number of draws.
     *
     * @return the number of draws
     */
    public int getNumberOfDraws() {
        return numberOfDraws;
    }

    /**
     * Sets number of draws.
     *
     * @param numberOfDraws the number of draws
     */
    public void setNumberOfDraws(int numberOfDraws) {
        this.numberOfDraws = numberOfDraws;
    }

    /**
     * Gets total score.
     *
     * @return the total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Sets total score.
     *
     * @param totalScore the total score
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    //Actually never used ... server does not provide enough info to make a ranking (that makes sense!)
    @Override
    public int compareTo(@NonNull Object o) {
        if(! (o instanceof Briscola2PAggregatedData))
            throw new IllegalArgumentException("Comparison should be made on Briscola2PAggregatedData objects");
        Briscola2PAggregatedData temp = (Briscola2PAggregatedData) o;
        if(totalScore < temp.getTotalScore())
            return -1;
        else if (totalScore == temp.getTotalScore())
            return 0;
        else
            return 1;
    }
}
