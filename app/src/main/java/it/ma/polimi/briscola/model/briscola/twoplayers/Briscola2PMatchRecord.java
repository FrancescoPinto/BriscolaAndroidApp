package it.ma.polimi.briscola.model.briscola.twoplayers;

import android.support.annotation.NonNull;

/**
 * Created by utente on 25/10/17.
 */

public class Briscola2PMatchRecord implements Comparable<Briscola2PMatchRecord> {

    public static final String computerPlayerName = "CPU";

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String player0Name, player1Name;
    private int player0Score, player1Score;
    //todo di default player1 è il giocatore umano del dispositivo, player 0 è l'ALTRO (sia esso umano o meno non importa)

    public Briscola2PMatchRecord(String player0Name, String player1Name, int player0Score, int player1Score) {
        this.player0Name = player0Name;
        this.player1Name = player1Name;
        this.player0Score = player0Score;
        this.player1Score = player1Score;
    }

    //if returns null, then pareggio
    public String getWinnerName(){
        if(player0Score < player1Score)
            return player1Name;
        else if (player0Score > player1Score)
            return player0Name;
        else return null;
    }

    //if returns null, then pareggio
    public String getLoserName(){
        if(player0Score > player1Score)
            return player1Name;
        else if (player0Score < player1Score)
            return player0Name;
        else return null;
    }

    public Integer getWinnerScore(){
        if(player0Score > player1Score)
            return player0Score;
        else if(player0Score < player1Score)
            return player1Score;
        else return null;
    }

    public Integer getLoserScore(){
        if(player0Score > player1Score)
            return player0Score;
        else if(player0Score < player1Score)
            return player1Score;
        else return null;
    }

    public String getPlayer0Name() {
        return player0Name;
    }

    public void setPlayer0Name(String player0Name) {
        this.player0Name = player0Name;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public int getPlayer0Score() {
        return player0Score;
    }

    public void setPlayer0Score(int player0Score) {
        this.player0Score = player0Score;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }


    //il ranking dovrà avere lista di chi ha fatto più punti in una sola partita (che non sia un computer)

    @Override
    public int compareTo(@NonNull Briscola2PMatchRecord o) {

        //poiché player0 è sempre il giocatore "umano" (e anche nel gioco in remoto, è sempre "IO umano" allora se chiamo il compareTo tra due record locali è perfetto
        //confrontare player0 perchè confronto il giocatore locale con sé stesso, se è in remoto confronto i due umani, se sono diversi giocatori in locale vs computer o locale vs umano
        //cioè la funzionalità per cui ogni giocatore ha un login va comunque bene perché l'info di come si chiama il giocatore ce l'ho salvata
        if (player1Name.equals(computerPlayerName) && o.getPlayer1Name().equals(computerPlayerName)) { //confronto due Record locali (ovvero vs. CPU)
            if (player0Score > o.getPlayer0Score()) // IL CONFRONTO AVVIENE SUI PUNTEGGI DEL GIOCATORE LOCALEthe CPU d
                return +1;
            else if (player0Score == o.getPlayer0Score())
                return 0;
            else
                return -1;
        } else if (player1Name.equals(computerPlayerName)) { //primo record locale e l'altro contro un umano
            if (player0Score > o.getWinnerScore()) //semplicemente se l'umano del primo record ha vinto contro chiunque del secondo, vinco io
                return +1;
            else if (player0Score == o.getWinnerScore()) //se c'è pareggio o io perdo, nell'altro record chiunque abbia vinto è un umano, quindi il suo record è migliore
                return 0;
            else
                return -1;
        } else if(o.getPlayer1Name().equals(computerPlayerName)) { //l'altro locale, il primo contro umano.
            if(o.getPlayer0Score() > getWinnerScore())
                return -1;
            else if(o.getPlayer0Score() == getWinnerScore())
                return 0;
            else
                return 1;

        } else { //entrambi umano
            if(getWinnerScore() > o.getWinnerScore())
                return 1;
            else if (getWinnerScore() == o.getWinnerScore())
                return 0;
            else
                return -1;
        }
    }

}
