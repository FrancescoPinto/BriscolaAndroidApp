package it.ma.polimi.briscola.model.briscola.statistics;

import android.support.annotation.NonNull;

/**
 * Class that represents a Briscola 2 Players Match Record, containing information about previously played matches.
 *
 * @author Francesco Pinto
 */
public class Briscola2PMatchRecord implements Comparable<Briscola2PMatchRecord> {

    /**
     * The constant computerPlayerName.
     */
    public static final String computerPlayerName = "CPU";
    public static final String remotePlayerDefault = "Remote";

    public static final int totPoints = 120;

    private int id;


    private String player0Name, player1Name; //by convention, the local human player is player0, the opponent (local/remote AI, remote human player) is player1
    private int player0Score, player1Score;

    private static final int drawScore = 60;

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Instantiates a new Briscola 2 p match record.
     *
     * @param player0Name  the player 0 name, is the name of the local human player
     * @param player1Name  the player 1 name, is the name of the "other" player (could be local/remote AI or a remote human player)
     * @param player0Score the player 0 score
     * @param player1Score the player 1 score
     */
    public Briscola2PMatchRecord(String player0Name, String player1Name, int player0Score, int player1Score) {
        this.player0Name = player0Name;
        this.player1Name = player1Name;
        this.player0Score = player0Score;
        this.player1Score = player1Score;
    }

    /**
     * Get winner name string.
     *
     * @return the string representing the winner name, if null is a draw
     */
//if returns null, then pareggio
    public String getWinnerName(){
        if(player0Score < player1Score)
            return player1Name;
        else if (player0Score > player1Score)
            return player0Name;
        else return null; //in case of a draw
    }

    /**
     * Get loser name string.
     *
     * @return the string representing the loser name, if null is a draw
     */

    public String getLoserName(){
        if(player0Score > player1Score)
            return player1Name;
        else if (player0Score < player1Score)
            return player0Name;
        else return null; //in case of draw.
    }

    /**
     * Get winner score integer.
     *
     * @return the integer representing the winner score, if equal to drawScore, it is a draw
     */
    public Integer getWinnerScore(){
        if(player0Score > player1Score)
            return player0Score;
        else if(player0Score < player1Score)
            return player1Score;
        else return drawScore;
    }

    /**
     * Get loser score integer.
     *
     * @return the integer representing the loser score, if equal to drawScore, it is a draw
     */
    public Integer getLoserScore(){
        if(player0Score > player1Score)
            return player0Score;
        else if(player0Score < player1Score)
            return player1Score;
        else return drawScore;
    }

    /**
     * Get draw score integer.
     *
     * @return the integer representing the score of a draw
     */
    public Integer getDrawScore(){
        return drawScore;
    }

    /**
     * Gets player 0 name. By convention, player 0 represents the local human player.
     *
     * @return the player 0 name
     */
    public String getPlayer0Name() {
        return player0Name;
    }

    /**
     * Sets player 0 name. By convention, player 0 represents the local human player.
     *
     * @param player0Name the player 0 name
     */
    public void setPlayer0Name(String player0Name) {
        this.player0Name = player0Name;
    }

    /**
     * Gets player 1 name. By convention, player 1 represents the "other" player (i.e. both local/remote AI and remote human player)
     *
     * @return the player 1 name
     */
    public String getPlayer1Name() {
        return player1Name;
    }

    /**
     * Sets player 1 name. By convention, player 1 represents the "other" player (i.e. both local/remote AI and remote human player)
     *
     * @param player1Name the player 1 name
     */
    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    /**
     * Gets player 0 score. By convention, player 0 represents the local human player.
     *
     * @return the player 0 score
     */
    public int getPlayer0Score() {
        return player0Score;
    }

    /**
     * Sets player 0 score. By convention, player 0 represents the local human player.
     *
     * @param player0Score the player 0 score
     */
    public void setPlayer0Score(int player0Score) {
        this.player0Score = player0Score;
    }

    /**
     * Gets player 1 score. By convention, player 1 represents the "other" player (i.e. both local/remote AI and remote human player)
     *
     * @return the player 1 score
     */
    public int getPlayer1Score() {
        return player1Score;
    }

    /**
     * Sets player 1 score. By convention, player 1 represents the "other" player (i.e. both local/remote AI and remote human player)
     *
     * @param player1Score the player 1 score
     */
    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }


    //To be used for the rankings of the matches with best winner scores
    @Override
    public int compareTo(@NonNull Briscola2PMatchRecord o) {

        //Since player0 is always the local human player
        //poiché player0 è sempre il giocatore "umano" (e anche nel gioco in remoto, è sempre "IO umano" allora se chiamo il compareTo tra due record locali è perfetto
        //confrontare player0 perchè confronto il giocatore locale con sé stesso, se è in remoto confronto i due umani, se sono diversi giocatori in locale vs computer o locale vs umano
        //cioè la funzionalità per cui ogni giocatore ha un login va comunque bene perché l'info di come si chiama il giocatore ce l'ho salvata
        if (player1Name.equals(computerPlayerName) && o.getPlayer1Name().equals(computerPlayerName)) { //both records are local human vs CPU
            if (player0Score > o.getPlayer0Score()) // Check what human player got the best score (it makes no sense awarding the CPU for being a good player, besides satisfying the AI programmer)
                return +1;
            else if (player0Score == o.getPlayer0Score())
                return 0;
            else
                return -1;
        } else if (player1Name.equals(computerPlayerName)) { //this record is local human vs. cpu, the other is human vs human
            if (player0Score > o.getWinnerScore()) //if the human in the first record performed better than any other in the other, this record wins
                return +1;
            else if (player0Score == o.getWinnerScore()) //if same score
                return 0;
            else
                return -1; //if this is worse, in the other whoever won is human, hence his/her record is better
        } else if(o.getPlayer1Name().equals(computerPlayerName)) { //the other record is local human vs. cpu, this record is human vs. human
            if(o.getPlayer0Score() > getWinnerScore()) //if the human of the other record performed better than any other in the second
                return -1;
            else if(o.getPlayer0Score() == getWinnerScore()) //if the same score of the winner
                return 0;
            else
                return 1;

        } else { //both records are human vs. human
            if(getWinnerScore() > o.getWinnerScore())
                return 1;
            else if (getWinnerScore() == o.getWinnerScore())
                return 0;
            else
                return -1;
        }
    }

    @Override
    public String toString(){
        return "p0="+player0Name+",p1"+player1Name+",s0="+player0Score+",s1="+player1Score;
    }
}
