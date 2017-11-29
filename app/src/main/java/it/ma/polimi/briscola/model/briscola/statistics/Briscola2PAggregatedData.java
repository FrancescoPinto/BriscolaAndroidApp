package it.ma.polimi.briscola.model.briscola.statistics;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.List;

/**
 * Created by utente on 28/11/17.
 */

public class Briscola2PAggregatedData implements Comparable {

    private String playerName;
    private int numberOfMatchesPlayed,
                numberOfMatchesWinned,
                numberOfDraws,
                totalScore; //contains the total number of points made by the player (no matter if he won or lost)

    //todo ATTENZIONE, poiché i match online non ritornano identificatori, il ranking è fatto solo da giocatori diversi
    //todo che usano lo stesso dispositivo! Quindi basta chiedere il player0
    //IMPORTANTE: l'argomento todo CONTIENE SOLO DI UN UTENTE
    public Briscola2PAggregatedData(List<Briscola2PMatchRecord> userPreviousMatches) { //todo, dovrai passare SOLO quelli di un utente
        if(userPreviousMatches.isEmpty())
            throw new IllegalArgumentException("No records in the argument");

        playerName = userPreviousMatches.get(0).getPlayer0Name();
        numberOfMatchesPlayed = totalScore = numberOfMatchesWinned = numberOfDraws = 0;

        for(Briscola2PMatchRecord mr : userPreviousMatches){
            String temp = mr.getWinnerName();
            if( temp == null)
                numberOfDraws++;
            else if(temp.equals(playerName))
                numberOfMatchesWinned++;

            numberOfMatchesPlayed++;
            totalScore+=mr.getPlayer0Score();
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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
