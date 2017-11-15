package it.ma.polimi.briscola;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.controller.Briscola2PMatchController;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;

//THIS IS JUST A PROTOTYPE! NOT PERFECTLY DESIGNED!!!!!!
public class MatchGUIPrototype extends AppCompatActivity {

    TextView card0player1, card1player1, card2player1, surfacecard0,surfacecard1, pointsplayer0,pointsplayer1, briscola;
    Spinner deck, pileplayer0,pileplayer1;
    Button card0player0, card1player0,card2player0;
    Briscola2PMatchController match;
    ArrayAdapter<String> deckAdapter, pile0adapter, pile1adapter;

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

        deck = (Spinner) findViewById(R.id.deck);
        pileplayer0 = (Spinner) findViewById(R.id.pile0);
        pileplayer1 = (Spinner) findViewById(R.id.pile1);

        card0player0 = (Button) findViewById(R.id.card0player0);
        card1player0 = (Button) findViewById(R.id.card1player0);
        card2player0 = (Button) findViewById(R.id.card2player0);


        pile0adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new ArrayList<NeapolitanCard>());
        pile0adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pileplayer0.setAdapter(pile0adapter);


        pile1adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, new ArrayList<NeapolitanCard>());
        pile1adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pileplayer1.setAdapter(pile1adapter);

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

    public void initializeNewDeck(List<NeapolitanCard> deck){

// Create an ArrayAdapter using the string array and a default spinner layout
        deckAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, deck);

// Specify the layout to use when the list of choices appears
        deckAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        this.deck.setAdapter(deckAdapter);
    }

    public void initializeFirstPlayer(int currentPlayer){
        //todo, inserisci animazione carina che lancia la moneta
        setCurrentPlayer(currentPlayer);
    }

    public void initializePlayersHands(int currentPlayer, List<NeapolitanCard> hand0, List<NeapolitanCard> hand1){
        //todo, implementare animazione che estrae e ealterna le estrazioni

        card0player0.setText(""+hand0.get(0));
        card1player0.setText(""+hand0.get(1));
        card2player0.setText(""+hand0.get(2));

        card0player1.setText(""+hand1.get(0));
        card1player1.setText(""+hand1.get(1));
        card2player1.setText(""+hand1.get(2));

    }

    public void initializeBriscola(String briscolaSuit){
        briscola.setText(briscolaSuit);
    }

    public void playCard(int move, int currentPlayer){
        switch(currentPlayer){
            case 0:
                switch(move) {
                    case 0: card0player0.setText("-"); break;
                    case 1: card1player0.setText("-"); break;
                    case 2: card2player0.setText("-"); break;
                    }
                    return;
            case 1:
                switch(move) {
                    case 0: card0player1.setText("-"); break;
                    case 1: card1player1.setText("-"); break;
                    case 2: card2player1.setText("-"); break;
                    }
                    return;
        }
    }

    public void clearSurface(int roundWinner, List<NeapolitanCard> surface){
        if(roundWinner == Briscola2PMatchConfig.PLAYER0) {
            //pile0adapter.add.addAll(surface);
        }
           // pileplayer0.get
    }

}
