package it.ma.polimi.briscola.model.briscola.statistics;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.List;

/**
 * Created by utente on 28/11/17.
 */

public class Briscola2PAggregatedData implements Comparable {

    private int numberOfMatchesPlayed,
                numberOfMatchesWinned,
                numberOfDraws,
                numberOfMatchesOnline,
                totalScore; //contains the total number of points made by the player (no matter if he won or lost)


    public Briscola2PAggregatedData(List<Briscola2PMatchRecord> userPreviousMatches) {
        if(userPreviousMatches.isEmpty())
            throw new IllegalArgumentException("No records in the argument");

        numberOfMatchesPlayed = totalScore = numberOfMatchesWinned = numberOfDraws = numberOfMatchesOnline= 0;

        for(Briscola2PMatchRecord mr : userPreviousMatches){
            String temp = mr.getWinnerName();
            if( temp == null)
                numberOfDraws++;
            else if(temp.equals(mr.getPlayer0Name()))
                numberOfMatchesWinned++;

            if(mr.getPlayer1Name().equals(Briscola2PMatchRecord.remotePlayerDefault))
                numberOfMatchesOnline++;

            numberOfMatchesPlayed++;
            totalScore+=mr.getPlayer0Score();
        }
    }

    public int getNumberOfMatchesOnline() {
        return numberOfMatchesOnline;
    }

    public void setNumberOfMatchesOnline(int numberOfMatchesOnline) {
        this.numberOfMatchesOnline = numberOfMatchesOnline;
    }


    public int getNumberOfMatchesPlayed() {
        return numberOfMatchesPlayed;
    }

    public void setNumberOfMatchesPlayed(int numberOfMatchesPlayed) {
        this.numberOfMatchesPlayed = numberOfMatchesPlayed;
    }

    public int getNumberOfMatchesWinned() {
        return numberOfMatchesWinned;
    }

    public void setNumberOfMatchesWinned(int numberOfMatchesWinned) {
        this.numberOfMatchesWinned = numberOfMatchesWinned;
    }

    public int getNumberOfDraws() {
        return numberOfDraws;
    }

    public void setNumberOfDraws(int numberOfDraws) {
        this.numberOfDraws = numberOfDraws;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

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
