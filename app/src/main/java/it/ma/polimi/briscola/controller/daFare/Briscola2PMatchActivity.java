package it.ma.polimi.briscola.controller.daFare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.forfullrelease.view.Briscola2PMatchView;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PHand;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;

public class Briscola2PMatchActivity extends AppCompatActivity {

    private Briscola2PMatchView matchView;
    private List<ImageView> hand0, hand1;
    private ImageView deck;
    private ConstraintLayout layout;
    private Map<Integer,CardAnimationType> cardAnimationDestination = new HashMap<>();
    private Briscola2PMatchController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_briscola2_pmatch);
        layout = (ConstraintLayout) findViewById(R.id.constraintLayout);


       /* ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();*/

        //todo  per il flip della carta: https://developer.android.com/training/animation/cardflip.html
   /*     ImageView mDeck = new ImageView(this);
        mDeck.setImageResource(R.mipmap.defaultCardBackground);

        mDeck.setId(id);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(0, WRAP_CONTENT);
        layoutParams.rightToRight = PARENT_ID;
        layoutParams.leftToLeft = guideline_60.getId(); //Vertical GuideLine of 60%
        layoutParams.rightMargin = 8;
        textView.setLayoutParams(layoutParams);

        ConstraintSet set = new ConstraintSet();
        set.connect(textView.getId(), ConstraintSet.TOP,
                previousTextView.getId(), ConstraintSet.BOTTOM, 60);
        ConstraintLayout mConstraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        mConstraintLayout.addView(mDeck, position);

        per connettere oggetti creati in modo simile
                puoi fare
        set.clone(markerLayout);
        //set.addToVerticalChain(textView.getId(),previousTextViewId,PARENT_ID);
        set.connect(textView.getId(), ConstraintSet.TOP, markerLayout.getId(), ConstraintSet.TOP, 60);
        set.applyTo(markerLayout);
        vedi
        https://stackoverflow.com/questions/42589868/android-constraintlayout-how-to-add-a-dynamic-view-one-below-another
        // init surface view
       /* matchView = new Briscola2PMatchView(this);*/

        controller = new Briscola2PMatchController(this);
        controller.startNewMatch();

    }



    public void initializeNewDeck(){
        deck = new ImageView(this);
        deck.setImageResource(R.drawable.default_card_background);
        deck.setId(View.generateViewId());
        layout.addView(deck);
        //todo, inserire rotazione mediante picasso

        initializeVerticalViewPositionInSlot(R.id.deckSlot,deck.getId());
        deck.setOnClickListener(new DeckCardDealerListener(layout,Briscola2PMatchActivity.this));

    }


    public void initializePlayersHands(int currentPlayer, Briscola2PHand handPlayer0, Briscola2PHand handPlayer1){

        hand0 = new ArrayList<>();
        hand1 = new ArrayList<>();

        String cardName = ImageNameBuilder.getFrenchCardImageName(handPlayer0.getCard(0));
        int resID0 = getResources().getIdentifier(cardName, "drawable", getPackageName());

        hand0.add(new ImageView(this));
        hand0.get(0).setImageResource(resID0);
        hand0.get(0).setId(View.generateViewId());
        layout.addView(hand0.get(0));
        initializeVerticalViewPositionInSlot(R.id.deckSlot, this.hand0.get(0).getId());
        AnimationMaster.moveCardToSlot(R.id.player0Card0Slot, hand0.get(0).getId(),layout);

         cardAnimationDestination.put(hand0.get(0).getId(), CardAnimationType.ToCard0SlotHand1);
         hand0.get(0).setOnClickListener(new CardAnimationListener(layout,Briscola2PMatchActivity.this));


        /*
        //todo, implementare animazione che estrae e ealterna le estrazioni

        for(int i = 0; i < hand0.getMaxNumCardsAllowedInList(); i++){
            int resID1 = getResources().getIdentifier(ImageNameBuilder.getFrenchCardImageName(hand0.getCard(i)) , "drawable", getPackageName());

            this.hand0.add(new ImageView(this));
            this.hand0.get(i).setImageResource(resID0);
            this.hand0.get(i).setId(View.generateViewId());
            this.hand0.get(i).setOnClickListener(new CardAnimationListener(layout,Briscola2PMatchActivity.this));
            layout.addView(this.hand0.get(i));
             //todo, questo slot va settato dinamicamente durante il match

            this.hand1.add(new ImageView(this));
            this.hand1.get(i).setImageResource(resID1);
            this.hand1.get(i).setId(View.generateViewId());
            this.hand1.get(i).setOnClickListener(new CardAnimationListener(layout,Briscola2PMatchActivity.this));
            layout.addView(this.hand1.get(i));

            initializeVerticalViewPositionInSlot(R.id.deckSlot, this.hand0.get(i).getId());
            initializeVerticalViewPositionInSlot(R.id.deckSlot, this.hand1.get(i).getId());


            this.hand0.get(i).setOnClickListener(new CardAnimationListener(layout,this));
            this.hand1.get(i).setOnClickListener(new CardAnimationListener(layout,this));

        }

        //todo ordinare le animazioni in modo che sembri che si stia seguendo l'ordine del gioco
        cardAnimationDestination.put(this.hand0.get(0).getId(), CardAnimationType.ToCard0SlotHand0);
        cardAnimationDestination.put(this.hand0.get(1).getId(), CardAnimationType.ToCard1SlotHand0);
        cardAnimationDestination.put(this.hand0.get(2).getId(), CardAnimationType.ToCard2SlotHand0);

        cardAnimationDestination.put(this.hand1.get(0).getId(), CardAnimationType.ToCard0SlotHand1);
        cardAnimationDestination.put(this.hand1.get(1).getId(), CardAnimationType.ToCard1SlotHand1);
        cardAnimationDestination.put(this.hand1.get(2).getId(), CardAnimationType.ToCard2SlotHand1);*/

    }

    private void initializeVerticalViewPositionInSlot(int idSlot, int idView){
        ConstraintSet constraints = new ConstraintSet();
        constraints.clone(layout);
        constraints.constrainWidth(idView, ConstraintSet.WRAP_CONTENT);
        constraints.constrainHeight(idView, ConstraintSet.WRAP_CONTENT);
        constraints.connect(idView, ConstraintSet.LEFT,idSlot,ConstraintSet.LEFT);
        constraints.connect(idView, ConstraintSet.RIGHT,idSlot,ConstraintSet.RIGHT);
        constraints.connect(idView, ConstraintSet.BOTTOM,idSlot,ConstraintSet.BOTTOM);
        constraints.applyTo(layout);
    }




    public void initializeFirstPlayer(int currentPlayer){
        //todo, inserisci animazione carina che lancia la moneta
        setCurrentPlayer(currentPlayer);
    }

    public void setCurrentPlayer(int currentPlayer){
        if(currentPlayer == Briscola2PMatchConfig.PLAYER1){
            //todo, abilita/disabilita listener sulle carte del player0
        }else{

        }

        onBuildDialog("The current player is " + currentPlayer,
                getString(R.string.ok),
                null,
                false,
                false
        ).show();

    }

    public void initializeBriscola(String briscolaSuit){

        onBuildDialog("Briscola is " + briscolaSuit,
                getString(R.string.ok),
                null,
                false,
                false
        ).show();
    }

    /*public void setCurrentPlayer(int currentPlayer){
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


    public void initializeFirstPlayer(int currentPlayer){
        //todo, inserisci animazione carina che lancia la moneta
        setCurrentPlayer(currentPlayer);
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


    @Override
    public void onResume() {
        super.onResume();
        // set content view
      //  setContentView(matchView);
    }

    @Override
    public void onPause() {
        super.onPause();
      /*  matchView.getThread().setGo(false);
        try {
            matchView.getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

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
     * @param _message:  get the message text
     * @param _positive: get the positive button text
     * @param _negative: get the negative button text
     * @param exit:      get the exit text
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

    public void displayDraw() {
        onBuildDialog("DRAW, both made 60 points!",
                getString(R.string.ok),
                null,
                false,
                false
        ).show();
    }

    ;

    public void displayMatchWinner(int player, int score) {
        onBuildDialog("The " + player + " won the match! He/She made " + score + " points!",
                getString(R.string.ok),
                null,
                false,
                false
        ).show();
    }

    public CardAnimationType getCardAnimationType(int idView){
        return cardAnimationDestination.get(idView);
    }

    public void deleteCardAnimationType(int idView){
        cardAnimationDestination.remove(idView);
    }
}

