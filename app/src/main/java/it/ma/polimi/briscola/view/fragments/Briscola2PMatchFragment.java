package it.ma.polimi.briscola.view.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.ma.polimi.briscola.MatchMenuActivity;
import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.SlotIndices;
import it.ma.polimi.briscola.audio.SoundManager;
import it.ma.polimi.briscola.controller.offline.AnimationMaster;
import it.ma.polimi.briscola.controller.offline.Briscola2PController;
import it.ma.polimi.briscola.controller.offline.Briscola2PMatchController;
import it.ma.polimi.briscola.controller.offline.CardAnimationListener;
import it.ma.polimi.briscola.controller.offline.CleanRoundAnimationListener;
import it.ma.polimi.briscola.controller.offline.FlipCardAnimationListener;
import it.ma.polimi.briscola.controller.offline.GameInteractionEnabler;
import it.ma.polimi.briscola.controller.offline.ImageNameBuilder;
import it.ma.polimi.briscola.controller.offline.SettingsManager;
import it.ma.polimi.briscola.controller.online.OnlineBriscola2PMatchController;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PHand;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;

/**
 * Created by utente on 09/12/17.
 */

public class Briscola2PMatchFragment extends Fragment {

    private MatchMenuActivity activity;
    private RelativeLayout layout;
    private Briscola2PController controller;
    private final Map<SlotIndices,ImageView> cards = new HashMap<>();
    private TextView player0Turn, player1Turn;
    private final Map<SlotIndices,View> slots = new HashMap<>();
    private final Map<SlotIndices,Point> slotCoordinates = new HashMap<>();
    private int[] elevationLevels = new int[] {0,1,2,3,4,5,6};
    private SoundManager soundManager;
    private ProgressBar progress;
    private Button exit;

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void setActivity(MatchMenuActivity activity) {
        this.activity = activity;
    }
    public Briscola2PController getController() {
        return controller;
    }
    public Map<SlotIndices, ImageView> getCards() {
        return cards;
    }



    public static Briscola2PMatchFragment newInstance(boolean isOnline, Integer difficulty){ //pass null if offline
        Briscola2PMatchFragment fragment =  new Briscola2PMatchFragment();
        if(isOnline)
            fragment.controller = new OnlineBriscola2PMatchController(fragment);
        else
            fragment.controller = new Briscola2PMatchController(fragment, difficulty);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_briscola_match, container, false); //todo, refactor del nome

        activity = (MatchMenuActivity) this.getActivity();

        soundManager = activity.getSoundManager();

        layout = (RelativeLayout) mainView.findViewById(R.id.layout);

        slots.put(SlotIndices.Player0Card0, mainView.findViewById(R.id.player0Card0Slot));
        slots.put(SlotIndices.Player0Card1, mainView.findViewById(R.id.player0Card1Slot));
        slots.put(SlotIndices.Player0Card2, mainView.findViewById(R.id.player0Card2Slot));
        slots.put(SlotIndices.Player0PileSlot, mainView.findViewById(R.id.player0PileSlot));

        slots.put(SlotIndices.Player1Card0,mainView.findViewById(R.id.player1Card0Slot));
        slots.put(SlotIndices.Player1Card1, mainView.findViewById(R.id.player1Card1Slot));
        slots.put(SlotIndices.Player1Card2, mainView.findViewById(R.id.player1Card2Slot));
        slots.put(SlotIndices.Player1PileSlot, mainView.findViewById(R.id.player1PileSlot));

        slots.put(SlotIndices.DeckSlot, mainView.findViewById(R.id.deckSlot));
        slots.put(SlotIndices.BriscolaSlot, mainView.findViewById(R.id.briscolaSlot));
        slots.put(SlotIndices.SurfaceSlot0, mainView.findViewById(R.id.surfaceSlot0));
        slots.put(SlotIndices.SurfaceSlot1, mainView.findViewById(R.id.surfaceSlot1));

        slots.put(SlotIndices.OffsetLayoutPlayer0, mainView.findViewById(R.id.relativeLayout1));
        slots.put(SlotIndices.OffsetLayoutPlayer1, mainView.findViewById(R.id.relativeLayout2));
        slots.put(SlotIndices.OffsetLayoutSurface, mainView.findViewById(R.id.relativeLayout3));

        exit = mainView.findViewById(R.id.exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBuildDialog("Are you sure you want to exit?",
                        getString(R.string.ok),
                        "No",
                        false,
                        false
                ).show();
            }
        });
        player0Turn = (TextView) mainView.findViewById(R.id.player0Turn);
        player1Turn = (TextView) mainView.findViewById(R.id.player0Turn);

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

                //controller = new Briscola2PMatchController(Briscola2PMatchFragment.this);
                controller.startNewMatch();                //todo, uyna volta che il layout è stato inizializzato, fai "start game" (porta qui dentro il controller)

                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        // init the view
        return mainView;

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
                AnimatorSet player0Animation = AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[i+3]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+3]), this);
                player0Animation.addListener(new FlipCardAnimationListener(cards.get(slotIndices[i+3]),soundManager));
                animators.add(player0Animation);
                animators.add(AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[i+7]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+7]),this));
            }else if(firstPlayerIndex == Briscola2PMatchConfig.PLAYER1) {
                cards.put(slotIndices[i+7],positionCardOnScreen(firstPlayer.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j]));
                cards.put(slotIndices[i+3],positionCardOnScreen(secondPlayer.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j-1]));
                AnimatorSet player0Animation = AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[i+3]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+3]),this);
                player0Animation.addListener(new FlipCardAnimationListener(cards.get(slotIndices[i+3]),soundManager));
                animators.add(AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[i+7]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+7]),this));
                animators.add(player0Animation);
            }

        }

        cards.get(SlotIndices.Player0Card0).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));
        cards.get(SlotIndices.Player0Card1).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));
        cards.get(SlotIndices.Player0Card2).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));

        dealFirstHand.playSequentially(animators);
        dealFirstHand.setStartDelay(1000);
        Toast.makeText(activity,cardsNames,Toast.LENGTH_LONG).
                show();
        return dealFirstHand;

    }

   /* public AnimatorSet getDealFirstHandAnimatorSet(int firstPlayerIndex, Briscola2PHand localPlayerHand) {

        AnimatorSet dealFirstHand = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();
        SlotIndices[] slotIndices = SlotIndices.values();

        for(int i = 0,j = 6; i < 3; i++, j-=2){

            if(firstPlayerIndex == Briscola2PMatchConfig.PLAYER0){
                cards.put(slotIndices[i+3],positionCardOnScreen(localPlayerHand.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j]));
                cards.put(slotIndices[i+7],positionCardOnScreen(new NeapolitanCard(), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j-1]));
                AnimatorSet player0Animation = AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[i+3]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+3]), this);
                player0Animation.addListener(new FlipCardAnimationListener(cards.get(slotIndices[i+3]),soundManager));
                animators.add(player0Animation);
                animators.add(AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[i+7]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+7]),this));
            }else if(firstPlayerIndex == Briscola2PMatchConfig.PLAYER1) {
                cards.put(slotIndices[i+7],positionCardOnScreen(new NeapolitanCard(), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j]));
                cards.put(slotIndices[i+3],positionCardOnScreen(localPlayerHand.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j-1]));
                AnimatorSet player0Animation = AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[i+3]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+3]),this);
                player0Animation.addListener(new FlipCardAnimationListener(cards.get(slotIndices[i+3]),soundManager));
                animators.add(AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[i+7]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+7]),this));
                animators.add(player0Animation);
            }

        }

        cards.get(SlotIndices.Player0Card0).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));
        cards.get(SlotIndices.Player0Card1).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));
        cards.get(SlotIndices.Player0Card2).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));

        dealFirstHand.playSequentially(animators);
        dealFirstHand.setStartDelay(1000);

        return dealFirstHand;
    }*/


        public AnimatorSet getInitializeBriscolaAnimatorSet(NeapolitanCard briscola){
        cards.put(SlotIndices.BriscolaSlot, positionCardOnScreen(briscola, slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[0]));
        AnimatorSet initialize = AnimationMaster.getTranslationAnimationSet(cards.get(SlotIndices.BriscolaSlot),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(SlotIndices.BriscolaSlot),this);
        initialize.addListener(new FlipCardAnimationListener(cards.get(SlotIndices.BriscolaSlot),soundManager));
        initialize.addListener(new GameInteractionEnabler(controller, true)); //true enables
        initialize.setStartDelay(300);
        return initialize;
    }

    private AnimatorSet playCard(int cardIndex, int currentPlayer, SlotIndices destinationSlot){

        AnimatorSet playCard = new AnimatorSet();
        ImageView card = cards.remove(SlotIndices.getPlayerCardSlotIndex(cardIndex,currentPlayer)); //todo, qui ho modificato
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
        Point pos = new Point(params.leftMargin,params.topMargin);
        boolean flipCard = currentPlayer == Briscola2PMatchConfig.PLAYER0? false:true;
        //Point pos = new Point(card.getLeft(),card.getTop());
        AnimatorSet putCardOnSurface = AnimationMaster.getTranslationAnimationSet(card,
                //slotCoordinates.get(SlotIndices.getPlayerCardSlotIndex(cardIndex,currentPlayer)),
                pos,
                slotCoordinates.get(destinationSlot),
                this);
        // elevationLevels[1]);
        cards.put(destinationSlot,card);
        if(currentPlayer == Briscola2PMatchConfig.PLAYER1)
            putCardOnSurface.addListener(new FlipCardAnimationListener(card,soundManager));

        if(currentPlayer == Briscola2PMatchConfig.PLAYER0)
            putCardOnSurface.addListener(new GameInteractionEnabler(controller,false)); //false disables
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
        AnimatorSet adjust = adjustCards(cardIndex,currentPlayer);
        if(currentPlayer == Briscola2PMatchConfig.PLAYER1 && destinationSlot == SlotIndices.SurfaceSlot0){ //if play first card first player
            adjust.addListener(new GameInteractionEnabler(controller, true));
        }
        playCard.playSequentially(putCardOnSurface, adjust);
        playCard.setStartDelay(500);
        return playCard;
    }

    public AnimatorSet playFirstCard(int cardIndex, int currentPlayer){
        return playCard(cardIndex,currentPlayer,SlotIndices.SurfaceSlot0);
    }
    public AnimatorSet playSecondCard(int cardIndex, int currentPlayer){
        return  playCard(cardIndex,currentPlayer,SlotIndices.SurfaceSlot1);

    }

    public AnimatorSet playFirstCard(String card, int currentPlayer){
        String cardName = ImageNameBuilder.getFrenchCardImageName(new NeapolitanCard(card.charAt(0),card.charAt(1)));
        Log.d("TAG", "I converted card "+ card + " into "+ cardName);
        int resID = getResources().getIdentifier(cardName, "drawable", activity.getPackageName());
        ImageView cardView = cards.get(SlotIndices.Player1Card0);
        cardView.setTag(resID+";"+true);
        return playCard(0,currentPlayer,SlotIndices.SurfaceSlot0);
    }
    public AnimatorSet playSecondCard(String card, int currentPlayer){
        String cardName = ImageNameBuilder.getFrenchCardImageName(new NeapolitanCard(card.charAt(0),card.charAt(1)));
        Log.d("TAG", "I converted card "+ card + " into "+ cardName);
        int resID = getResources().getIdentifier(cardName, "drawable", activity.getPackageName());
        ImageView cardView = cards.get(SlotIndices.Player1Card0);
        cardView.setTag(resID+";"+true);
        return  playCard(0,currentPlayer,SlotIndices.SurfaceSlot1);

    }



    public AnimatorSet adjustCards(int cardIndex, int currentPlayer){
        AnimatorSet adjust = new AnimatorSet();
        List<Animator> adjustCards = new ArrayList<>();



        for(int j = cardIndex+1; j < 3;j++){

            ImageView card = this.cards.remove(SlotIndices.getPlayerCardSlotIndex(j,currentPlayer));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams(); //todo, qui si scatena lerrore
            Point pos = new Point(params.leftMargin,params.topMargin);

            SlotIndices slot = SlotIndices.getPlayerCardSlotIndex(j-1,currentPlayer);

            adjustCards.add(AnimationMaster.getTranslationAnimationSet(
                    card,
                    pos,
                    slotCoordinates.get(slot),
                    this
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
                slotCoordinates.get(SlotIndices.getPileSlotOfPlayer(winner)),
                this);
        // elevationLevels[0]);
        AnimatorSet cleanSecond = AnimationMaster.getTranslationAnimationSet(card1,
                pos1,
                slotCoordinates.get(SlotIndices.getPileSlotOfPlayer(winner)),
                this);
        //elevationLevels[1]);
        cleanFirst.addListener(new CleanRoundAnimationListener((winner==Briscola2PMatchConfig.PLAYER0)?true:false,soundManager));
        cleanFirst.addListener(new FlipCardAnimationListener(card0,soundManager));
        cleanSecond.addListener(new FlipCardAnimationListener(card1,soundManager));
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

    public AnimatorSet drawCardsNewRound(List<Briscola2PHand> playersHands, int firstPlayerIndex, boolean lastDraw){
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

        ImageView cardSecond;
        ImageView cardFirst = positionCardOnScreen(firstPlayer.getCard(firstHandSize-1),slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[2]);
        if(lastDraw) {
            cardSecond = cards.remove(SlotIndices.BriscolaSlot);
            ((ImageView)slots.get(SlotIndices.DeckSlot)).setImageResource(R.drawable.card_slot_shape);
        }
        else
            cardSecond = positionCardOnScreen(secondPlayer.getCard(secondHandSize-1),slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[1]);

        SlotIndices first = slotIndices[firstHandSize+firstPlayerOffset-1];
        SlotIndices second = slotIndices[secondHandSize+secondPlayerOffset-1];

        cards.put(first,cardFirst);
        cards.put(second,cardSecond);
        AnimatorSet playFirst = AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[firstHandSize+firstPlayerOffset-1]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[firstHandSize+firstPlayerOffset-1]),this);
        AnimatorSet playSecond = AnimationMaster.getTranslationAnimationSet(cards.get(slotIndices[secondHandSize+secondPlayerOffset-1]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[secondHandSize+secondPlayerOffset-1]),this);

        if(firstPlayerIndex == Briscola2PMatchConfig.PLAYER0) {
            playFirst.addListener(new FlipCardAnimationListener(cardFirst,soundManager));
            playFirst.addListener(new GameInteractionEnabler(controller, true));

        }else {
            playSecond.addListener(new FlipCardAnimationListener(cardSecond,soundManager));
            playSecond.addListener(new GameInteractionEnabler(controller, true)); //todo, forse qui c'è il bug SHOULD BE ENABLED ONLY AFTER PLAYER 2 MAKES A MOVE!
        }

        animators.add(playFirst);
        animators.add(playSecond);

        switch(playersHands.get((firstPlayerIndex == Briscola2PMatchConfig.PLAYER0)?firstPlayerIndex:(firstPlayerIndex+1)%2).size()){
            case 3:;cards.get(SlotIndices.Player0Card2).setOnClickListener(new CardAnimationListener(this));
            case 2:cards.get(SlotIndices.Player0Card1).setOnClickListener(new CardAnimationListener(this));
            case 1:cards.get(SlotIndices.Player0Card0).setOnClickListener(new CardAnimationListener(this)); break;
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


    public void initializeFirstPlayer(int currentPlayer){
        //todo, inserisci animazione carina che lancia la moneta
        displayCurrentPlayer(currentPlayer);
    }


    @Override
    public void onResume() { //todo, sistemare onResume e onPause per tutto (animazioni, audio ecc. ecc. ecc.)
        super.onResume();

        // set content view
        //  setContentView(matchView);
        soundManager.resumeBgMusic();

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
       // soundManager.pauseBgMusic();
    }


    @Override
    public void onDetach(){
        super.onDetach();

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
     * @param _message:  get the message text
     * @param _positive: get the positive button text
     * @param _negative: get the negative button text
     * @param exit:      get the exit text
     * @return AlertDialog instance
     */
    public AlertDialog onBuildDialog(String _message, String _positive, String _negative,
                                     final boolean exit, final boolean restart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
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

    public void showToast(String string){
        Toast.makeText(activity,string,Toast.LENGTH_LONG).show();
    }

    private ImageView positionCardOnScreen(NeapolitanCard c, Point initialPosition, int elevation){
        String cardName = ImageNameBuilder.getFrenchCardImageName(c);
        int resID = getResources().getIdentifier(cardName, "drawable", activity.getPackageName());

        ImageView card = new ImageView(activity);
        card.setId(View.generateViewId());
        card.setTag(resID+";"+true);
        card.setImageResource(R.drawable.default_card_background);

        //AnimatorSet in = (AnimatorSet) AnimatorInflater.loadAnimator(this,
        //       R.animator.card_flip_right_in);
        //AnimatorSet out = (AnimatorSet) AnimatorInflater.loadAnimator(this,
        //       R.animator.card_flip_right_out);
        //Animation in = AnimationUtils.loadAnimation(this, R.anim.card_flip_right_in);
        // Animation out = AnimationUtils.loadAnimation(this, R.anim.card_flip_right_out);



       /* ImageView image = new ImageView(this); todo se hai problemi, rimetti questo
        image.setId(View.generateViewId());
        image.setTag(resID+";"+ true); //true indicates the cards is covered by default
        image.setImageResource(R.drawable.default_card_background); //by default, cards are covered
*/



        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) this.getResources().getDimension(R.dimen.card_width),
                (int) this.getResources().getDimension(R.dimen.card_height));
        params.leftMargin = initialPosition.x;
        params.topMargin =  initialPosition.y;
        card.setElevation(elevation);

        layout.addView(card,params);
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

        return card;
    }

    public void waitingToFindOnlinePlayer(){
        progress = new ProgressBar(getActivity());//,null,android.R.attr.indeterminateProgressStyle);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progress,params);
        progress.setVisibility(View.VISIBLE);  //To show ProgressBar

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);



       /* progress=new ProgressDialog(this);
        progress.setMessage("Downloading Music");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();*/
    }

    public void foundOnlinePlayer(){

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progress.setVisibility(View.GONE);     // To Hide ProgressBar

    }

    public void manageError(int errorCode){
        //todo, usare l'errorCode per diversificare la gestione


    }



}
