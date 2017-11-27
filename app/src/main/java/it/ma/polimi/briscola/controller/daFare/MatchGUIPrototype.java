package it.ma.polimi.briscola.controller.daFare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PHand;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PSurface;
import it.ma.polimi.briscola.model.deck.Deck;

//THIS IS JUST A PROTOTYPE! NOT PERFECTLY DESIGNED!!!!!!
public class MatchGUIPrototype extends AppCompatActivity {

    TextView card0player1, card1player1, card2player1, surfacecard0,surfacecard1, pointsplayer0,pointsplayer1, briscola, deck, pileplayer0,pileplayer1;
    Button card0player0, card1player0,card2player0;
    Briscola2PMatchController match;

    public Briscola2PMatchController getMatchController() {
        return match;
    }

    public void setMatchController(Briscola2PMatchController match) {
        this.match = match;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_guiprototype);

        card0player1 = (TextView) findViewById(R.id.card0player1);
        card1player1 = (TextView) findViewById(R.id.card1player1);
        card2player1 = (TextView) findViewById(R.id.card2player1);
        surfacecard0 = (TextView) findViewById(R.id.surfacecard0);
        surfacecard1 = (TextView) findViewById(R.id.surfacecard1);
        pointsplayer0 = (TextView) findViewById(R.id.pointsPlayer0);
        pointsplayer1 = (TextView) findViewById(R.id.pointsPlayer1);
        briscola = (TextView) findViewById(R.id.briscola);
        deck = (TextView) findViewById(R.id.deck);
        pileplayer0 = (TextView) findViewById(R.id.pilePlayer0);
        pileplayer1 = (TextView) findViewById(R.id.pilePlayer1);

        card0player0 = (Button) findViewById(R.id.card0player0);
        card1player0 = (Button) findViewById(R.id.card1player0);
        card2player0 = (Button) findViewById(R.id.card2player0);

        card0player0.setOnClickListener(new PlayCardListener(MatchGUIPrototype.this, 0));
        card1player0.setOnClickListener(new PlayCardListener(MatchGUIPrototype.this, 1));
        card2player0.setOnClickListener(new PlayCardListener(MatchGUIPrototype.this, 2));

       // match = new Briscola2PMatchController(this); todo, nei test, resinserire questa riga
        match.startNewMatch();
    }

    public void onShowMessage(String message) {
        onBuildDialog(
                message,
                getString(R.string.ok),
                null,
                false,
                false
        ).show();
    }

    /**
     * Build and AlertDialog instance
     *
     * @param _message:     get the message text
     * @param _positive:    get the positive button text
     * @param _negative:    get the negative button text
     * @param exit:         get the exit text
     * @return AlertDialog instance
     */
    public AlertDialog onBuildDialog(String _message, String _positive, String _negative,
                                     final boolean exit, final boolean restart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(_message);
        builder.setPositiveButton(_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               /* if (exit) {
                    if (restart)
                        startActivity(new Intent(MainCalculatorActivity.this, MainCalculatorActivity.class));
                    else
                        new UpdateAppSettings(MainCalculatorActivity.this).onResetSavedText();
                    finish();
                }*/
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
        if (_negative != null)
            builder.setNegativeButton(_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // dismiss dialog
                    dialogInterface.dismiss();
                }
            });
        builder.setCancelable(false);
        return builder.create();
    }

    public void displayDraw(){
        onBuildDialog("DRAW, both made 60 points!",
                getString(R.string.ok),
                null,
                false,
                false
        ).show();
    };

    public void displayMatchWinner(int player, int score){
        onBuildDialog("The " + player + " won the match! He/She made "+score + " points!",
                getString(R.string.ok),
                null,
                false,
                false
        ).show();
    }

    public void setCurrentPlayer(int currentPlayer){
        if(currentPlayer == Briscola2PMatchConfig.PLAYER1){
            card0player0.setEnabled(false);
            card1player0.setEnabled(false);
            card2player0.setEnabled(false);

        }else{
            card0player0.setEnabled(true);
            card1player0.setEnabled(true);
            card2player0.setEnabled(true);
        }

        onBuildDialog("The current player is " + currentPlayer,
                getString(R.string.ok),
                null,
                false,
                false
        ).show();

    }

    public void initializeNewDeck(Deck deck){
        this.deck.setText("Deck:"+ deck);
    }

    public void initializeFirstPlayer(int currentPlayer){
        //todo, inserisci animazione carina che lancia la moneta
        setCurrentPlayer(currentPlayer);
    }

    public void initializePlayersHands(int currentPlayer, Briscola2PHand hand0, Briscola2PHand hand1){
        //todo, implementare animazione che estrae e ealterna le estrazioni

        card0player0.setText(""+hand0.getCard(0));
        card1player0.setText(""+hand0.getCard(1));
        card2player0.setText(""+hand0.getCard(2));

        card0player1.setText(""+hand1.getCard(0));
        card1player1.setText(""+hand1.getCard(1));
        card2player1.setText(""+hand1.getCard(2));

    }

    public void initializeBriscola(String briscolaSuit){
        briscola.setText(briscolaSuit);
    }

    public void playFirstCard(int move, int currentPlayer){
        switch(currentPlayer){
            case 0:
                switch(move) {
                    case 0: surfacecard0.setText(card0player0.getText()); card0player0.setText("-"); break;
                    case 1: surfacecard0.setText(card1player0.getText());card1player0.setText("-"); break;
                    case 2: surfacecard0.setText(card2player0.getText());card2player0.setText("-"); break;
                    }
                    return;
            case 1:
                switch(move) {
                    case 0: surfacecard0.setText(card0player1.getText());card0player1.setText("-"); break;
                    case 1: surfacecard0.setText(card1player1.getText());card1player1.setText("-"); break;
                    case 2: surfacecard0.setText(card2player1.getText());card2player1.setText("-"); break;
                    }
                    return;
        }
    }

    public void playSecondCard(int move, int currentPlayer){
        switch(currentPlayer){
            case 0:
                switch(move) {
                    case 0: surfacecard0.setText(card0player0.getText()); card0player0.setText("-"); break;
                    case 1: surfacecard0.setText(card1player0.getText());card1player0.setText("-"); break;
                    case 2: surfacecard0.setText(card2player0.getText());card2player0.setText("-"); break;
                }
                return;
            case 1:
                switch(move) {
                    case 0: surfacecard0.setText(card0player1.getText());card0player1.setText("-"); break;
                    case 1: surfacecard0.setText(card1player1.getText());card1player1.setText("-"); break;
                    case 2: surfacecard0.setText(card2player1.getText());card2player1.setText("-"); break;
                }
                return;
        }
    }

    public void clearSurface(int roundWinner, Briscola2PSurface surface){
        if(roundWinner == Briscola2PMatchConfig.PLAYER0) {
            pileplayer0.setText(pileplayer0.getText()+surface.toString());
        }else{
            pileplayer1.setText(pileplayer1.getText()+surface.toString());
        }
    }

}
