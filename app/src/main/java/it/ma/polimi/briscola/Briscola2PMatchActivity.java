package it.ma.polimi.briscola;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.ma.polimi.briscola.controller.daFare.AnimationMaster;
import it.ma.polimi.briscola.controller.daFare.Briscola2PMatchController;
import it.ma.polimi.briscola.controller.daFare.CardAnimationListener;
import it.ma.polimi.briscola.controller.daFare.CardAnimationType;
import it.ma.polimi.briscola.controller.daFare.ImageNameBuilder;
import it.ma.polimi.briscola.forfullrelease.view.Briscola2PMatchView;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PHand;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;

public class Briscola2PMatchActivity extends AppCompatActivity {

    private Briscola2PMatchView matchView;
    private List<ImageView> hand0, hand1;
    private ImageView deck;
    private RelativeLayout layout;
    private Map<Integer,CardAnimationType> cardAnimationDestination = new HashMap<>();
    private Briscola2PMatchController controller;
    private final Map<SlotIndices,ImageView> cards = new HashMap<>();
    private TextView player0Turn, player1Turn;


    private RelativeLayout player0RelativeLayout,player1RelativeLayout,surfaceRelativeLayout;


    private final Map<SlotIndices,View> slots = new HashMap<>();
    private final Map<SlotIndices,Point> slotCoordinates = new HashMap<>();
    private final List<ImageView> allCards = new ArrayList<>();

    private int[] elevationLevels = new int[] {0,1,2,3,4,5,6};

    public Briscola2PMatchController getController() {
        return controller;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_briscola2_pmatch);
        layout = (RelativeLayout) findViewById(R.id.layout);

        slots.put(SlotIndices.Player0Card0, findViewById(R.id.player0Card0Slot));
        slots.put(SlotIndices.Player0Card1, findViewById(R.id.player0Card1Slot));
        slots.put(SlotIndices.Player0Card2, findViewById(R.id.player0Card2Slot));
        slots.put(SlotIndices.Player0PileSlot, findViewById(R.id.player0PileSlot));

        slots.put(SlotIndices.Player1Card0, findViewById(R.id.player1Card0Slot));
        slots.put(SlotIndices.Player1Card1, findViewById(R.id.player1Card1Slot));
        slots.put(SlotIndices.Player1Card2, findViewById(R.id.player1Card2Slot));
        slots.put(SlotIndices.Player1PileSlot, findViewById(R.id.player1PileSlot));

        slots.put(SlotIndices.DeckSlot, findViewById(R.id.deckSlot));
        slots.put(SlotIndices.BriscolaSlot, findViewById(R.id.briscolaSlot));
        slots.put(SlotIndices.SurfaceSlot0, findViewById(R.id.surfaceSlot0));
        slots.put(SlotIndices.SurfaceSlot1, findViewById(R.id.surfaceSlot1));

        slots.put(SlotIndices.OffsetLayoutPlayer0, findViewById(R.id.relativeLayout1));
        slots.put(SlotIndices.OffsetLayoutPlayer1, findViewById(R.id.relativeLayout2));
        slots.put(SlotIndices.OffsetLayoutSurface, findViewById(R.id.relativeLayout3));

        player0Turn = (TextView) findViewById(R.id.player0Turn);
        player1Turn = (TextView) findViewById(R.id.player0Turn);

        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                SlotIndices[] indices = SlotIndices.values();
                Point tempCoordinates;

                for(int i = 0; i <indices.length; i++ ) {
                    if (i < 3) //i relative layout
                        slotCoordinates.put(indices[i], new Point(slots.get(indices[i]).getLeft(), slots.get(indices[i]).getTop()));
                    else if (i >= 3 && i < 7) {
                        tempCoordinates =new Point(slots.get(indices[i]).getLeft(), slots.get(indices[i]).getTop());
                        tempCoordinates.x += slotCoordinates.get(SlotIndices.OffsetLayoutPlayer0).x;
                        tempCoordinates.y += slotCoordinates.get(SlotIndices.OffsetLayoutPlayer0).y;
                        slotCoordinates.put(indices[i],tempCoordinates);
                    } else if (i >= 7 && i < 11) {
                        tempCoordinates =new Point(slots.get(indices[i]).getLeft(), slots.get(indices[i]).getTop());
                        tempCoordinates.x += slotCoordinates.get(SlotIndices.OffsetLayoutPlayer1).x;
                        tempCoordinates.y += slotCoordinates.get(SlotIndices.OffsetLayoutPlayer1).y;
                        slotCoordinates.put(indices[i],tempCoordinates);

                    } else if (i >= 11 && i <= 14) {
                        tempCoordinates =new Point(slots.get(indices[i]).getLeft(), slots.get(indices[i]).getTop());
                        tempCoordinates.x += slotCoordinates.get(SlotIndices.OffsetLayoutSurface).x;
                        tempCoordinates.y += slotCoordinates.get(SlotIndices.OffsetLayoutSurface).y;
                        slotCoordinates.put(indices[i],tempCoordinates);
                    }
                }

                controller = new Briscola2PMatchController(Briscola2PMatchActivity.this);
                controller.startNewMatch();                //todo, uyna volta che il layout è stato inizializzato, fai "start game" (porta qui dentro il controller)

                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

       // controller = new Briscola2PMatchController(Briscola2PMatchActivity.this);
        //controller.startNewMatch();



        //todo, per ricavarne il vertice sinistro-alto fai view.getLeft() e view.getTop()

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

    }

    private ImageView positionCardOnScreen(NeapolitanCard c, Point initialPosition, int elevation){
        String cardName = ImageNameBuilder.getFrenchCardImageName(c);
        int resID = getResources().getIdentifier(cardName, "drawable", getPackageName());
        ImageView image = new ImageView(this);
        image.setImageResource(resID);
        image.setId(View.generateViewId());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) this.getResources().getDimension(R.dimen.card_width),
                (int) this.getResources().getDimension(R.dimen.card_height));
        params.leftMargin = initialPosition.x;
        params.topMargin =  initialPosition.y;
        image.setElevation(elevation);

        layout.addView(image,params);
      /*  layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        (int) this.getResources().getDimension(R.dimen.card_width),
                        (int) this.getResources().getDimension(R.dimen.card_height));
                params.leftMargin = initialPosition.x;
                params.topMargin =  initialPosition.y;
                image.setElevation(elevation);

                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });*/

        return image;
    }

    public Map<SlotIndices, ImageView> getCards() {
        return cards;
    }

    public AnimatorSet getDealFirstHandAnimatorSet(int firstPlayerIndex, List<Briscola2PHand> playersHands){
        Briscola2PHand firstPlayer = playersHands.get(firstPlayerIndex);
        Briscola2PHand secondPlayer = playersHands.get((firstPlayerIndex+1)%2);
        AnimatorSet dealFirstHand = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();
        SlotIndices[] slotIndices = SlotIndices.values();
        String cardsNames = "";

        showToast("FirstPlayer is: "+firstPlayerIndex);

        for(int i = 0,j = 6; i < 3; i++, j-=2){
            cardsNames+= "FirstPlayer: "+ImageNameBuilder.getFrenchCardImageName( firstPlayer.getCard(i)) + " ";
            cardsNames+= "SecondPlayer: "+ImageNameBuilder.getFrenchCardImageName( secondPlayer.getCard(i)) + " ";
           /* TODO MOLTO IMPORTANTE: qui c'è un bug, metti la roba del firstPlayer sempre come se fosse Player0

                    ALTRI BUG OSSERVATI: PUOI GIOCARE CARTE ANCHE SE NON E' IL ' TUO TURNO ...
                                    LE CARTE VENGONO DISTRIBUITE MALE AL PLAYER1 (mentre sempre bene al player 0)
                                    IL GIOCO CRASHA QUANDO ARRIVI QUASI ALLA FINE
                                    I FADE-IN/FADE-OUT DEI TURNI SONO SBALLATI

*/
           if(firstPlayerIndex == Briscola2PMatchConfig.PLAYER0){
            cards.put(slotIndices[i+3],positionCardOnScreen(firstPlayer.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j]));
            cards.put(slotIndices[i+7],positionCardOnScreen(secondPlayer.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j-1]));
            animators.add(AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[i+3]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+3])));
            animators.add(AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[i+7]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+7])));
            }else if(firstPlayerIndex == Briscola2PMatchConfig.PLAYER1) {
                cards.put(slotIndices[i+7],positionCardOnScreen(firstPlayer.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j]));
                cards.put(slotIndices[i+3],positionCardOnScreen(secondPlayer.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j-1]));
                animators.add(AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[i+7]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+7])));
                animators.add(AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[i+3]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+3])));
            }

        }

        cards.get(SlotIndices.Player0Card0).setOnClickListener(new CardAnimationListener(Briscola2PMatchActivity.this));
        cards.get(SlotIndices.Player0Card1).setOnClickListener(new CardAnimationListener(Briscola2PMatchActivity.this));
        cards.get(SlotIndices.Player0Card2).setOnClickListener(new CardAnimationListener(Briscola2PMatchActivity.this));

        dealFirstHand.playSequentially(animators);
        dealFirstHand.setStartDelay(1000);
        Toast.makeText(this,cardsNames,Toast.LENGTH_LONG).
                show();
        return dealFirstHand;

    }

    public AnimatorSet getInitializeBriscolaAnimatorSet(NeapolitanCard briscola){
        cards.put(SlotIndices.BriscolaSlot, positionCardOnScreen(briscola, slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[0]));
        AnimatorSet initialize = AnimationMaster.getTranslationAnimationSet(cards.get(SlotIndices.BriscolaSlot),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(SlotIndices.BriscolaSlot));
        initialize.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Briscola2PMatchActivity.this.controller.playing = true; //todo, sposta il settaggio di questo listener nel controller
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        initialize.setStartDelay(300);
        return initialize;
    }

    private AnimatorSet playCard(int cardIndex, int currentPlayer, SlotIndices destinationSlot){

        AnimatorSet playCard = new AnimatorSet();
        ImageView card = cards.remove(SlotIndices.getPlayerCardSlotIndex(cardIndex,currentPlayer)); //todo, qui ho modificato
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
        Point pos = new Point(params.leftMargin,params.topMargin);
        //Point pos = new Point(card.getLeft(),card.getTop());
        AnimatorSet putCardOnSurface = AnimationMaster.getTranslationAnimationSet(card,
                //slotCoordinates.get(SlotIndices.getPlayerCardSlotIndex(cardIndex,currentPlayer)),
                pos,
                slotCoordinates.get(destinationSlot));
        cards.put(destinationSlot,card);
       /* playCard.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });*/

        playCard.play(putCardOnSurface);
        playCard.setStartDelay(300);
        return playCard;
    }

    public AnimatorSet playFirstCard(int cardIndex, int currentPlayer){
        return playCard(cardIndex,currentPlayer,SlotIndices.SurfaceSlot0);
    }
    public AnimatorSet playSecondCard(int cardIndex, int currentPlayer){
        return playCard(cardIndex,currentPlayer,SlotIndices.SurfaceSlot1);
    }
    public AnimatorSet adjustCards(int cardIndex, int currentPlayer){
        AnimatorSet adjust = new AnimatorSet();
        List<Animator> adjustCards = new ArrayList<>();



        for(int j = cardIndex+1; j < 3;j++){

                ImageView card = this.cards.remove(SlotIndices.getPlayerCardSlotIndex(j,currentPlayer));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
                Point pos = new Point(params.leftMargin,params.topMargin);

                SlotIndices slot = SlotIndices.getPlayerCardSlotIndex(j-1,currentPlayer);

                adjustCards.add(AnimationMaster.getTranslationAnimationSet(
                        card,
                        pos,
                        slotCoordinates.get(slot) //todo <- possibile pericolo
                ));
                this.cards.put(SlotIndices.getPlayerCardSlotIndex(j-1,currentPlayer),card);

        }

        adjust.playSequentially(adjustCards);
        return adjust;
    }

    public AnimatorSet cleanSurface(int winner){
        AnimatorSet cleanSurface = new AnimatorSet();
        ImageView card0 = cards.remove(SlotIndices.SurfaceSlot0);
        ImageView card1 = cards.remove(SlotIndices.SurfaceSlot1);
        Point pos0 = new Point(card0.getLeft(),card0.getTop());
        Point pos1 = new Point(card1.getLeft(),card1.getTop());
        AnimatorSet cleanFirst = AnimationMaster.getTranslationAnimationSet(card0,
                pos0,
                slotCoordinates.get(SlotIndices.getPileSlotOfPlayer(winner)));
        AnimatorSet cleanSecond = AnimationMaster.getTranslationAnimationSet(card1,
                pos1,
                slotCoordinates.get(SlotIndices.getPileSlotOfPlayer(winner)));
        cleanSurface.playSequentially(cleanFirst,cleanSecond);
        cleanSurface.setStartDelay(300);
        return cleanSurface;
    }

    public AnimatorSet displayCurrentPlayer(int currentPlayer){
        ObjectAnimator displayCurrentPlayer;
        ObjectAnimator hideNextPlayer;
        if(currentPlayer ==Briscola2PMatchConfig.PLAYER0){
            displayCurrentPlayer = ObjectAnimator.ofFloat(player0Turn, "alpha", 1f);//0f, 1f);
             hideNextPlayer= ObjectAnimator.ofFloat(player1Turn, "alpha",0f);//1f,0f);
        }else if(currentPlayer == Briscola2PMatchConfig.PLAYER1){
            displayCurrentPlayer= ObjectAnimator.ofFloat(player0Turn, "alpha",0f);//0f,1f);
            hideNextPlayer= ObjectAnimator.ofFloat(player1Turn, "alpha",1f);//1f,0f);
        } else
            throw new IllegalStateException();
        AnimatorSet animation = new AnimatorSet();
        animation.playSequentially(hideNextPlayer,displayCurrentPlayer);
        return animation;
    }

    public void displayMatchOutcome(String message){
        onBuildDialog(message,
                getString(R.string.ok),
                null,
                false,
                false
        ).show();
    }
    public AnimatorSet drawCardsNewRound(List<Briscola2PHand> playersHands, int firstPlayerIndex){
        Briscola2PHand firstPlayer = playersHands.get(firstPlayerIndex);
        Briscola2PHand secondPlayer = playersHands.get((firstPlayerIndex+1)%2);

        AnimatorSet dealTwoCards = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();
        SlotIndices[] slotIndices = SlotIndices.values();

        int firstHandSize = firstPlayer.size();
        int secondHandSize = secondPlayer.size();
        int firstPlayerOffset, secondPlayerOffset;
        if(firstPlayerIndex == Briscola2PMatchConfig.PLAYER0) {
            firstPlayerOffset = SlotIndices.player0Offset;
            secondPlayerOffset = SlotIndices.player1Offset;
        }
        else {
            firstPlayerOffset = SlotIndices.player1Offset;
            secondPlayerOffset = SlotIndices.player0Offset;

        }

        ImageView cardFirst = positionCardOnScreen(firstPlayer.getCard(firstHandSize-1),slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[2]);
        ImageView cardSecond = positionCardOnScreen(secondPlayer.getCard(secondHandSize-1),slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[1]);

        SlotIndices first = slotIndices[firstHandSize+firstPlayerOffset-1];
        SlotIndices second = slotIndices[secondHandSize+secondPlayerOffset-1];
            cards.put(
                    first,
                    cardFirst
                    );
            cards.put(
                    second,
                    cardSecond
                    );
            animators.add(AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[firstHandSize+firstPlayerOffset-1]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[firstHandSize+firstPlayerOffset-1])));
            animators.add(AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[secondHandSize+secondPlayerOffset-1]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[secondHandSize+secondPlayerOffset-1])));


        switch(playersHands.get((firstPlayerIndex == Briscola2PMatchConfig.PLAYER0)?firstPlayerIndex:(firstPlayerIndex+1)%2).size()){
            case 3:;cards.get(SlotIndices.Player0Card2).setOnClickListener(new CardAnimationListener(Briscola2PMatchActivity.this));
            case 2:cards.get(SlotIndices.Player0Card1).setOnClickListener(new CardAnimationListener(Briscola2PMatchActivity.this));
            case 1:cards.get(SlotIndices.Player0Card0).setOnClickListener(new CardAnimationListener(Briscola2PMatchActivity.this)); break;
        }

        dealTwoCards.playSequentially(animators);
        dealTwoCards.setStartDelay(1000);
        return dealTwoCards;

    }
    public void initializeNewDeck(){
      /*  deck = new ImageView(this); todo, ripristina
        deck.setImageResource(R.drawable.default_card_background);
        deck.setId(View.generateViewId());
        layout.addView(deck);
        //todo, inserire rotazione mediante picasso

        initializeVerticalViewPositionInSlot(R.id.deckSlot,deck.getId());
        deck.setOnClickListener(new DeckCardDealerListener(layout,Briscola2PMatchActivity.this));*/

    }
    public void initializePlayersHands(int currentPlayer, Briscola2PHand handPlayer0, Briscola2PHand handPlayer1){

      /*  hand0 = new ArrayList<>(); todo ripristina
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
        hand0.get(0).setOnClickListener(new CardAnimationListener(layout,Briscola2PMatchActivity.this));*/


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
    /*    ConstraintSet constraints = new ConstraintSet();
        constraints.clone(layout);
        constraints.constrainWidth(idView, ConstraintSet.WRAP_CONTENT);
        constraints.constrainHeight(idView, ConstraintSet.WRAP_CONTENT);
        constraints.connect(idView, ConstraintSet.LEFT,idSlot,ConstraintSet.LEFT);
        constraints.connect(idView, ConstraintSet.RIGHT,idSlot,ConstraintSet.RIGHT);
        constraints.connect(idView, ConstraintSet.BOTTOM,idSlot, ConstraintSet.BOTTOM);
        constraints.applyTo(layout);*/
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

    public void showToast(String string){
        Toast.makeText(this,string,Toast.LENGTH_LONG).show();
    }
}

