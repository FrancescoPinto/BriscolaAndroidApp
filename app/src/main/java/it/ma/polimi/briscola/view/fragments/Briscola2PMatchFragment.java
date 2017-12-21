package it.ma.polimi.briscola.view.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.ma.polimi.briscola.audio.SoundService;
import it.ma.polimi.briscola.view.MatchMenuActivityActions;
import it.ma.polimi.briscola.view.activities.Briscola2PMatchActivity;
import it.ma.polimi.briscola.view.activities.MatchActivity;
import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.controller.Briscola2PController;
import it.ma.polimi.briscola.controller.Briscola2PMatchController;
import it.ma.polimi.briscola.controller.listeners.CardAnimationListener;
import it.ma.polimi.briscola.controller.listeners.CleanRoundAnimationListener;
import it.ma.polimi.briscola.controller.listeners.FlipCardAnimationListener;
import it.ma.polimi.briscola.controller.listeners.GameInteractionEnabler;
import it.ma.polimi.briscola.ImageNameBuilder;
import it.ma.polimi.briscola.controller.listeners.MoveCardAnimationListener;
import it.ma.polimi.briscola.controller.OnlineBriscola2PMatchController;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PHand;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.persistency.SettingsManager;
import it.ma.polimi.briscola.view.dialog.SaveConfigDataDialogFragment;
import it.ma.polimi.briscola.view.dialog.SaveFinishedMatchRecordDialogFragment;
import it.ma.polimi.briscola.view.dialog.WaitDialogFragment;
import it.ma.polimi.briscola.view.dialog.WarningExitDialogFragment;
import it.ma.polimi.briscola.view.dialog.WinnerMatchDialogFragment;

/**
 * Created by utente on 09/12/17.
 */

public class Briscola2PMatchFragment extends Fragment {

    private MatchActivity activity;
    private RelativeLayout layout;
    private Briscola2PController controller;
    private final Map<SlotIndices,ImageView> cards = new HashMap<>();
    private TextView player0Turn, turnCounterDisplayer;
    private final Map<SlotIndices,View> slots = new HashMap<>();
    private final Map<SlotIndices,Point> slotCoordinates = new HashMap<>();
    private int[] elevationLevels = new int[] {0,1,2,3,4,5,6};
    private SoundService soundManager;
    private Button exit, menu;
    private int animationVelocizationFactor;
    private int turnCounter;
    private boolean isOnline, isResume;
    private int difficulty;
    private boolean isRetained;

    public static final String MATCH_CONFIG = "it.ma.polimi.briscola.resume.config",
                               IS_ONLINE = "it.ma.polimi.briscola.resume.is_online",
                                IS_RESUME = "it.ma.polimi.briscola.resume.resume";

    public static final String DIFFICULTY = "it.ma.polimi.briscola.offline.difficulty";

    private static final int REQUEST_EXIT = 100, REQUEST_SAVE = 101, REQUEST_STOP_WAITING = 102, REQUEST_SHOW_WINNER = 103, REQUEST_SAVE_RECORD = 104;
    private static final String DIALOG_EXIT = "DialogExit", DIALOG_SAVE = "DialogSave", DIALOG_WAIT = "DialogWait", DIALOG_WINNER = "DialogWinner", DIALOG_SAVE_RECORD = "DialogSaveRecord";

    public SoundService getSoundManager() {
        return soundManager;
    }

    public void setActivity(MatchActivity activity) {
        this.activity = activity;
    }
    public Briscola2PController getController() {
        return controller;
    }
    public Map<SlotIndices, ImageView> getCards() {
        return cards;
    }

    public static Briscola2PMatchFragment newInstance(boolean isOnline, Integer difficulty){ //pass null if offline
        Bundle args = new Bundle();
        args.putBoolean(IS_ONLINE,isOnline);
        args.putBoolean(IS_RESUME,false);
        args.putInt(DIFFICULTY, difficulty);
        Briscola2PMatchFragment fragment =  new Briscola2PMatchFragment();
        fragment.setArguments(args);

        return fragment;
    }
    public static Briscola2PMatchFragment newInstance(Briscola2PMatchConfig config, Integer difficulty){ //pass null if offline
        Bundle args = new Bundle();
        args.putSerializable(MATCH_CONFIG,config);
        args.putBoolean(IS_ONLINE,false);
        args.putBoolean(IS_RESUME,true);
        args.putInt(DIFFICULTY, difficulty);
        Briscola2PMatchFragment fragment =  new Briscola2PMatchFragment();
        fragment.setArguments(args);
        return fragment;

       /* fragment.controller = new Briscola2PMatchController(config, fragment, difficulty);
        fragment.isOnline = false;
        fragment.isResume = true;
        return fragment;*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_briscola_match, container, false); //todo, refactor del nome

        activity = (MatchActivity) this.getActivity();

        soundManager = activity.getSoundManager();

        layout = (RelativeLayout) mainView.findViewById(R.id.layout);

        isOnline = getArguments().getBoolean(IS_ONLINE);
        isResume = getArguments().getBoolean(IS_RESUME);
        difficulty = getArguments().getInt(DIFFICULTY);

        if(controller == null) { //check retained data after configuration change
            if (isOnline) //if nothing retained, create
                controller = new OnlineBriscola2PMatchController(this);
            else
                controller = new Briscola2PMatchController(this, difficulty);
        }else{
            isRetained = true;
        }


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
                FragmentManager manager = getFragmentManager();
                WarningExitDialogFragment dialog = WarningExitDialogFragment.newInstance(isOnline,Briscola2PMatchActivity.EXIT_BUTTON);
                dialog.setTargetFragment(Briscola2PMatchFragment.this,REQUEST_EXIT);
                dialog.show(manager, DIALOG_EXIT);
            }
        });

        menu = mainView.findViewById(R.id.menu_button);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment oldMatches = activity.getMatchesFragments();
                if(oldMatches != null)
                    activity.startMenu(true);
                else
                    activity.startMenu(false);
            }
        });
        player0Turn = (TextView) mainView.findViewById(R.id.player0Turn);
        turnCounterDisplayer = (TextView) mainView.findViewById(R.id.turn_displayer);

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

                if(!isRetained) { //if is not a retained fragment
                    if (!isResume)  //new match
                        controller.startNewMatch(); //controller = new Briscola2PMatchController(Briscola2PMatchFragment.this);
                        //todo, uyna volta che il layout è stato inizializzato, fai "start game" (porta qui dentro il controller)
                    else {
                        controller = new Briscola2PMatchController(
                                (Briscola2PMatchConfig) getArguments().getSerializable(MATCH_CONFIG),
                                Briscola2PMatchFragment.this, difficulty);
                        controller.resumeMatch();
                    }
                }else{
                    if(!isOnline) {
                        controller = new Briscola2PMatchController(
                                ((Briscola2PMatchController) controller).getConfig(),
                                Briscola2PMatchFragment.this, difficulty);
                        controller.resumeMatch();
                    }else{
                        controller = new OnlineBriscola2PMatchController((OnlineBriscola2PMatchController) controller, Briscola2PMatchFragment.this);
                        controller.resumeMatch();
                               // ricordati di TOGLIERE il retained in OnPause nell'activity' hostante TODO <--------------------
                             //   ANZI, in realtà tu lo devi togliere a fine match solo (o comunque Todo <-------------------
                        //quando la partita viene interrotta da un exit!) TODO <----------------------------
                    }
                }

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
        turnCounterDisplayer.setText(getString(R.string.turns_elapsed,controller.getTurnsElapsed()));
        for(int i = 0,j = 6; i < 3; i++, j-=2){

           /* TODO MOLTO IMPORTANTE: qui c'è un bug, metti la roba del firstPlayer sempre come se fosse Player0

                    ALTRI BUG OSSERVATI: PUOI GIOCARE CARTE ANCHE SE NON E' IL ' TUO TURNO ...
                                    LE CARTE VENGONO DISTRIBUITE MALE AL PLAYER1 (mentre sempre bene al player 0)
                                    IL GIOCO CRASHA QUANDO ARRIVI QUASI ALLA FINE
                                    I FADE-IN/FADE-OUT DEI TURNI SONO SBALLATI

*/
            if(firstPlayerIndex == Briscola2PMatchConfig.PLAYER0){
                cards.put(slotIndices[i+3],positionCardOnScreen(firstPlayer.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j],true));
                cards.put(slotIndices[i+7],positionCardOnScreen(secondPlayer.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j-1],true));
                AnimatorSet player0Animation = getTranslationAnimationSet(cards.get(slotIndices[i+3]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+3]));
                player0Animation.addListener(new FlipCardAnimationListener(cards.get(slotIndices[i+3]),soundManager));
                animators.add(player0Animation);
                animators.add(getTranslationAnimationSet(cards.get(slotIndices[i+7]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+7])));
            }else if(firstPlayerIndex == Briscola2PMatchConfig.PLAYER1) {
                cards.put(slotIndices[i+7],positionCardOnScreen(firstPlayer.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j],true));
                cards.put(slotIndices[i+3],positionCardOnScreen(secondPlayer.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j-1],true));
                AnimatorSet player0Animation = getTranslationAnimationSet(cards.get(slotIndices[i+3]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+3]));
                player0Animation.addListener(new FlipCardAnimationListener(cards.get(slotIndices[i+3]),soundManager));
                animators.add(getTranslationAnimationSet(cards.get(slotIndices[i+7]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[i+7])));
                animators.add(player0Animation);
            }

        }

        cards.get(SlotIndices.Player0Card0).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));
        cards.get(SlotIndices.Player0Card1).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));
        cards.get(SlotIndices.Player0Card2).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));

        dealFirstHand.playSequentially(animators);
        dealFirstHand.setStartDelay(1000);

        return dealFirstHand;

    }
    public AnimatorSet getInitializeBriscolaAnimatorSet(NeapolitanCard briscola){
        cards.put(SlotIndices.BriscolaSlot, positionCardOnScreen(briscola, slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[0],true));
        AnimatorSet initialize = getTranslationAnimationSet(cards.get(SlotIndices.BriscolaSlot),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(SlotIndices.BriscolaSlot));
        initialize.addListener(new FlipCardAnimationListener(cards.get(SlotIndices.BriscolaSlot),soundManager));
        //initialize.addListener(new GameInteractionEnabler(controller, true)); //true enables
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
        AnimatorSet putCardOnSurface = getTranslationAnimationSet(card,
                //slotCoordinates.get(SlotIndices.getPlayerCardSlotIndex(cardIndex,currentPlayer)),
                pos,
                slotCoordinates.get(destinationSlot)
                );
        // elevationLevels[1]);
        cards.put(destinationSlot,card);
        if(currentPlayer == Briscola2PMatchConfig.PLAYER1)
            putCardOnSurface.addListener(new FlipCardAnimationListener(card,soundManager));

        if(currentPlayer == Briscola2PMatchConfig.PLAYER0)
            controller.setIsPlaying(false); //prevent user from playing other cards
           // putCardOnSurface.addListener(new GameInteractionEnabler(controller,false)); //false disables, OK, dopo che hai giocato, non puoi giocare più

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
        AnimatorSet adjust = adjustCards(cardIndex,currentPlayer,controller.getHandSize(currentPlayer));
        if(currentPlayer == Briscola2PMatchConfig.PLAYER1 && destinationSlot == SlotIndices.SurfaceSlot0){ //if play first card first player
            //adjust.addListener(new GameInteractionEnabler(controller, true)); todo, rimuovi proprio questa if ... perché è displayCurrentPlayer a occuparsi di abilitare
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
    public AnimatorSet adjustCards(int cardIndex, int currentPlayer, int playerHandLength){
        AnimatorSet adjust = new AnimatorSet();
        List<Animator> adjustCards = new ArrayList<>();


                                    //+1 because when adjust is invoked, the card count in hand has been decreased
        for(int j = cardIndex+1; j < playerHandLength+1;j++){

            ImageView card = this.cards.remove(SlotIndices.getPlayerCardSlotIndex(j,currentPlayer));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams(); //todo, qui si scatena lerrore
                Point pos = new Point(params.leftMargin, params.topMargin);

                SlotIndices slot = SlotIndices.getPlayerCardSlotIndex(j - 1, currentPlayer);

                adjustCards.add(getTranslationAnimationSet(
                        card,
                        pos,
                        slotCoordinates.get(slot)

                ));
                this.cards.put(SlotIndices.getPlayerCardSlotIndex(j - 1, currentPlayer), card);

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
        AnimatorSet cleanFirst = getTranslationAnimationSet(card0,
                pos0,
                slotCoordinates.get(SlotIndices.getPileSlotOfPlayer(winner))
                );
        // elevationLevels[0]);
        AnimatorSet cleanSecond = getTranslationAnimationSet(card1,
                pos1,
                slotCoordinates.get(SlotIndices.getPileSlotOfPlayer(winner))
                );
        //elevationLevels[1]);
        cleanFirst.addListener(new CleanRoundAnimationListener((winner==Briscola2PMatchConfig.PLAYER0)?true:false,soundManager));
        cleanFirst.addListener(new FlipCardAnimationListener(card0,soundManager));
        cleanSecond.addListener(new FlipCardAnimationListener(card1,soundManager));
        ObjectAnimator hideCurrentPlayer; //todo, nascondere qui il player0
        cleanSurface.playSequentially(cleanFirst,cleanSecond);
        cleanSurface.setStartDelay(300);
        return cleanSurface;
    }
    public AnimatorSet displayIsPlayer0Turn(int currentPlayer){
        ObjectAnimator displayCurrentPlayer;
        if(currentPlayer ==Briscola2PMatchConfig.PLAYER0){
            displayCurrentPlayer = ObjectAnimator.ofFloat(player0Turn, "alpha", 1f);//0f, 1f);
            displayCurrentPlayer.setDuration(200);
            displayCurrentPlayer.addListener(new GameInteractionEnabler(controller,true));
        //}else if(currentPlayer == Briscola2PMatchConfig.PLAYER1){
        //    displayCurrentPlayer= ObjectAnimator.ofFloat(player0Turn, "alpha",0f);//0f,1f);
       //     displayCurrentPlayer.setDuration(200);
        } else
            throw new IllegalStateException();
        AnimatorSet animation = new AnimatorSet();
        animation.play(displayCurrentPlayer);
        return animation;
    }

    public AnimatorSet hidIsPlayer0Turn(){
        ObjectAnimator displayCurrentPlayer;
        // if(currentPlayer == Briscola2PMatchConfig.PLAYER1){
               displayCurrentPlayer= ObjectAnimator.ofFloat(player0Turn, "alpha",0f);//0f,1f);
                displayCurrentPlayer.setDuration(200);
       // } else
       //     throw new IllegalStateException();
        AnimatorSet animation = new AnimatorSet();
        animation.play(displayCurrentPlayer);
        return animation;
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
            ImageView cardFirst = positionCardOnScreen(firstPlayer.getCard(firstHandSize-1),slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[2],true);
            if(lastDraw) {
                cardSecond = cards.remove(SlotIndices.BriscolaSlot);
                ((ImageView)slots.get(SlotIndices.DeckSlot)).setImageResource(R.drawable.card_slot_shape);
            }
            else
                cardSecond = positionCardOnScreen(secondPlayer.getCard(secondHandSize-1),slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[1],true);

            SlotIndices first = slotIndices[firstHandSize+firstPlayerOffset-1];
            SlotIndices second = slotIndices[secondHandSize+secondPlayerOffset-1];

            cards.put(first,cardFirst);
            cards.put(second,cardSecond);
            AnimatorSet playFirst = getTranslationAnimationSet(cards.get(slotIndices[firstHandSize+firstPlayerOffset-1]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[firstHandSize+firstPlayerOffset-1]));
            AnimatorSet playSecond = getTranslationAnimationSet(cards.get(slotIndices[secondHandSize+secondPlayerOffset-1]),slotCoordinates.get(SlotIndices.DeckSlot),slotCoordinates.get(slotIndices[secondHandSize+secondPlayerOffset-1]));

            putPlayer0CardsTouchListeners(playersHands,firstPlayerIndex); //todo, SPOSTARE QUESTO A DOPO CHE LE CARTE SONO STATE POSIZIONATE
        //TODO QUESTA E' UNA POSSIBILE SORGENTE DEI BUGS!
            if(firstPlayerIndex == Briscola2PMatchConfig.PLAYER0) {
                playFirst.addListener(new FlipCardAnimationListener(cardFirst, soundManager)); //mostra  Player0 la carta pescata
                //playFirst.addListener(new GameInteractionEnabler(controller, true)); //l'umano è abilitato a giocare SOLO dopo che l'animazione displayCurrentPlayer è eseguita, e l'abilitazione
                //viene SEMPRE fatta dal metodo displayCurrentPlayer, PUNTO ... il bloccaggio invece è fatto non appena ha finito di giocare

                if(lastDraw){ //briscola is picked up by the other player, should be covered after being picked
                    Log.d("TAG","The image resource id of briscola is "+cardSecond.getTag());
                    playSecond.addListener(new FlipCardAnimationListener(cardSecond,soundManager));

                }
            }else {
                if(!lastDraw){ //briscola is already face-up on surface, should not flip it
                    playSecond.addListener(new FlipCardAnimationListener(cardSecond,soundManager)); //flip cards of player0
                }

                //il player0 gioca per secondo, quindi NON deve poter giocare
                //playSecond.addListener(new GameInteractionEnabler(controller, false)); //todo, forse qui c'è il bug SHOULD BE ENABLED ONLY AFTER PLAYER 2 MAKES A MOVE!
            }

            animators.add(playFirst);
            animators.add(playSecond);



            dealTwoCards.playSequentially(animators);
            dealTwoCards.setStartDelay(1000);
            return dealTwoCards;

    }
    public void putPlayer0CardsTouchListeners(List<Briscola2PHand> playersHands, int firstPlayerIndex){
        switch(playersHands.get((firstPlayerIndex == Briscola2PMatchConfig.PLAYER0)?firstPlayerIndex:(firstPlayerIndex+1)%2).size()){
            case 3:;cards.get(SlotIndices.Player0Card2).setOnClickListener(new CardAnimationListener(this));
            case 2:cards.get(SlotIndices.Player0Card1).setOnClickListener(new CardAnimationListener(this));
            case 1:cards.get(SlotIndices.Player0Card0).setOnClickListener(new CardAnimationListener(this)); break;
        }
        turnCounterDisplayer.setText(getString(R.string.turns_elapsed,controller.getTurnsElapsed()));
    }
    @Override
    public void onResume() { //todo, sistemare onResume e onPause per tutto (animazioni, audio ecc. ecc. ecc.)
        super.onResume();

        // set content view
        //  setContentView(matchView);
       // soundManager.resumeBgMusic();

        int velocityPreference = new SettingsManager(getActivity().getApplicationContext()).getVelocityPreference();
        switch(velocityPreference){
            case SettingsManager.NORMAL: animationVelocizationFactor = 1; break;
            case SettingsManager.FAST: animationVelocizationFactor = 5; break;
            case SettingsManager.VERYFAST: animationVelocizationFactor = 10; break;

        }

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
        //soundManager.pauseBgMusic();
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
    public void displayMatchWinner(int player, int score) {
        FragmentManager manager = getFragmentManager();
        WinnerMatchDialogFragment dialog = WinnerMatchDialogFragment.newInstance(player,score,isOnline);
        dialog.setTargetFragment(Briscola2PMatchFragment.this, REQUEST_SHOW_WINNER);
        dialog.show(manager,DIALOG_WINNER);

        //WinnerMatchDialog dialog = new WinnerMatchDialog();
        //dialog.buildDialog(getActivity(),player,score,this);
        //dialog.show();

    }
    public void showToast(String string){
        Toast.makeText(activity,string,Toast.LENGTH_SHORT).show();
    }
    private ImageView positionCardOnScreen(NeapolitanCard c, Point initialPosition, int elevation, boolean isCovered){
        String cardName = ImageNameBuilder.getFrenchCardImageName(c);
        int resID = getResources().getIdentifier(cardName, "drawable", activity.getPackageName());

        ImageView card = new ImageView(activity);
        card.setId(View.generateViewId());
        card.setTag(resID+";"+isCovered);
        if(isCovered)
            card.setImageResource(R.drawable.default_card_background);
        else
            card.setImageResource(resID);

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

        RelativeLayout.LayoutParams params;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            params = new RelativeLayout.LayoutParams(
                    (int) this.getResources().getDimension(R.dimen.card_width_landscape),
                    (int) this.getResources().getDimension(R.dimen.card_height_landscape));
        }else {
            params = new RelativeLayout.LayoutParams(
                    (int) this.getResources().getDimension(R.dimen.card_width),
                    (int) this.getResources().getDimension(R.dimen.card_height));
        }
        params.leftMargin = initialPosition.x;
        params.topMargin =  initialPosition.y;
        card.setElevation(elevation);

        layout.addView(card,params);

        return card;
    }
    public void waitingToFindOnlinePlayer(){
        FragmentManager manager = getFragmentManager();
        WaitDialogFragment dialog = WaitDialogFragment.newInstance();
        dialog.setTargetFragment(Briscola2PMatchFragment.this, REQUEST_STOP_WAITING);
        dialog.show(manager, DIALOG_WAIT);

       /* progress = new ProgressBar(getActivity());//,null,android.R.attr.indeterminateProgressStyle);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progress,params);
        progress.setVisibility(View.VISIBLE);  //To show ProgressBar

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/

    }
    public void foundOnlinePlayer(){
        FragmentManager manager = getFragmentManager();
       WaitDialogFragment found =  (WaitDialogFragment) manager.findFragmentByTag(DIALOG_WAIT);
        if(found != null){
            found.dismiss();
        }
        //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        //progress.setVisibility(View.GONE);     // To Hide ProgressBar

    }
    public void manageError(int errorCode){
        //todo, usare l'errorCode per diversificare la gestione


    }
    public void saveFinishedMatchRecordData(int player0Points){
        FragmentManager manager = getFragmentManager();
        SaveFinishedMatchRecordDialogFragment dialog = SaveFinishedMatchRecordDialogFragment.newInstance(player0Points,isOnline);
        dialog.setTargetFragment(Briscola2PMatchFragment.this, REQUEST_SAVE_RECORD);
        dialog.show(manager,DIALOG_SAVE_RECORD);

    }
    private AnimatorSet getTranslationAnimationSet(ImageView targetView, Point origin, Point target) {


        AnimatorSet translation = new AnimatorSet();
        ObjectAnimator translationX = new ObjectAnimator().ofFloat(targetView, "translationX", target.x - origin.x);
        translationX.setDuration(1000/animationVelocizationFactor);
        translationX.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(targetView, "translationY", target.y - origin.y);
        translationY.setDuration(1000/animationVelocizationFactor);
        translationY.setInterpolator(new AccelerateDecelerateInterpolator());
        translation.playTogether(translationX, translationY);
        translation.addListener(new MoveCardAnimationListener(getSoundManager()));

        return translation;
    }
    public void loadPiles(boolean pile0IsEmpty, boolean pile1IsEmpty){
        if(!pile0IsEmpty)
        positionCardOnScreen(new NeapolitanCard(), slotCoordinates.get(SlotIndices.Player0PileSlot),elevationLevels[elevationLevels.length-1],true);
        if(!pile1IsEmpty)
        positionCardOnScreen(new NeapolitanCard(), slotCoordinates.get(SlotIndices.Player1PileSlot),elevationLevels[elevationLevels.length-1],true);
    }
    public void loadSurface(List<NeapolitanCard> surfaceCards){
        int size = surfaceCards.size();
        if(size >= 1){
            ImageView firstCard = positionCardOnScreen(surfaceCards.get(0),slotCoordinates.get(SlotIndices.SurfaceSlot0),elevationLevels[0],false);
            cards.put(SlotIndices.SurfaceSlot0, firstCard);
        }
        if(size >= 2){
            ImageView secondCard = positionCardOnScreen(surfaceCards.get(1),slotCoordinates.get(SlotIndices.SurfaceSlot1),elevationLevels[0],false);
            cards.put(SlotIndices.SurfaceSlot1, secondCard);
        }
    }
    public void loadHands(List<Briscola2PHand> playersHands, int numberTurnsElapsed){

        Briscola2PHand player0 = playersHands.get(Briscola2PMatchConfig.PLAYER0);
        Briscola2PHand player1 = playersHands.get(Briscola2PMatchConfig.PLAYER1);

        turnCounterDisplayer.setText(getString(R.string.turns_elapsed,controller.getTurnsElapsed()));

        switch(player0.size()){
            case 3:
                cards.put(SlotIndices.Player0Card2,positionCardOnScreen(player0.getCard(2), slotCoordinates.get(SlotIndices.Player0Card2), elevationLevels[1],false));
                cards.get(SlotIndices.Player0Card2).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));
            case 2:
                cards.put(SlotIndices.Player0Card1,positionCardOnScreen(player0.getCard(1), slotCoordinates.get(SlotIndices.Player0Card1), elevationLevels[1],false));
                cards.get(SlotIndices.Player0Card1).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));
            case 1:
                cards.put(SlotIndices.Player0Card0,positionCardOnScreen(player0.getCard(0), slotCoordinates.get(SlotIndices.Player0Card0), elevationLevels[1],false));
                cards.get(SlotIndices.Player0Card0).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));
            default: break;
        }

        switch(player1.size()){
            case 3:cards.put(SlotIndices.Player1Card2,positionCardOnScreen(player1.getCard(2), slotCoordinates.get(SlotIndices.Player1Card2), elevationLevels[1],true));
            case 2:cards.put(SlotIndices.Player1Card1,positionCardOnScreen(player1.getCard(1), slotCoordinates.get(SlotIndices.Player1Card1), elevationLevels[1],true));
            case 1:cards.put(SlotIndices.Player1Card0,positionCardOnScreen(player1.getCard(0), slotCoordinates.get(SlotIndices.Player1Card0), elevationLevels[1],true));
            default: break;
        }

    }
    public void loadBriscolaIfNeeded(NeapolitanCard briscola){
        if(briscola != null) //birscola still on surface, not in players hands (if in hands, loadHands will take care of that
        cards.put(SlotIndices.BriscolaSlot,positionCardOnScreen(briscola,slotCoordinates.get(SlotIndices.BriscolaSlot),elevationLevels[0],false));

        if(briscola == null) //if last 2 cards have been drawn
            ((ImageView) slots.get(SlotIndices.DeckSlot)).setImageResource(R.drawable.card_slot_shape);

        turnCounterDisplayer.setText(getString(R.string.turns_elapsed,controller.getTurnsElapsed()));
    }
    public void loadCurrentPlayer(int currentPlayer){
        if(currentPlayer ==Briscola2PMatchConfig.PLAYER0) {
            displayIsPlayer0Turn(currentPlayer).start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_EXIT){ //button exit pressed

            MatchMenuActivityActions action = (MatchMenuActivityActions) data.getSerializableExtra(WarningExitDialogFragment.EXTRA_ACTION);
            int motivation = data.getIntExtra(WarningExitDialogFragment.EXTRA_MOTIVATION, Briscola2PMatchActivity.EXIT_BUTTON);
            Briscola2PMatchConfig loadConfig = (Briscola2PMatchConfig) data.getSerializableExtra(WarningExitDialogFragment.EXTRA_LOAD_CONFIG);

            if(action == MatchMenuActivityActions.STOP_ONLINE){
                controller.forceMatchEnd(); //invoca rest api
                handleExit(motivation);

               // ((Briscola2PMatchActivity) getActivity()).exitMatch(,motivation); //true = isOnline
            }else if(action == MatchMenuActivityActions.WARN_STOP_OFFLINE){
                FragmentManager manager = getFragmentManager();
                SaveConfigDataDialogFragment dialog = SaveConfigDataDialogFragment.newInstance(((Briscola2PMatchController) controller).getConfig(),motivation);
                dialog.setTargetFragment(Briscola2PMatchFragment.this, REQUEST_SAVE);
                dialog.show(manager,DIALOG_SAVE);
            }

        }else if(requestCode == REQUEST_SAVE){ //offline match to be saved has been saved, now continue handling exit

            MatchMenuActivityActions action = (MatchMenuActivityActions) data.getSerializableExtra(SaveConfigDataDialogFragment.EXTRA_ACTION);
            int motivation = data.getIntExtra(SaveConfigDataDialogFragment.EXTRA_MOTIVATION, Briscola2PMatchActivity.EXIT_BUTTON);
            Briscola2PMatchConfig loadConfig = (Briscola2PMatchConfig) data.getSerializableExtra(SaveConfigDataDialogFragment.EXTRA_LOAD_CONFIG);

            if(action == MatchMenuActivityActions.STOP_OFFLINE){
                handleExit(motivation);
            }

        }else if(requestCode == REQUEST_STOP_WAITING){
            boolean stopWaiting = data.getBooleanExtra(WaitDialogFragment.EXTRA_STOP_WAITING,false);
            if(stopWaiting){
                ((OnlineBriscola2PMatchController) controller).stopCallbacks();
                ((MatchActivity)getActivity()).startMenu(false);
            }
        }else if(requestCode ==REQUEST_SHOW_WINNER){
            int player0Score = data.getIntExtra(WinnerMatchDialogFragment.EXTRA_PLAYER0_SCORE,0);
            saveFinishedMatchRecordData(player0Score);
        }else if(requestCode == REQUEST_SAVE_RECORD){
            handleExit(Briscola2PMatchActivity.EXIT_BUTTON);
        }
    }


    public void handleExit(int motivation){
        switch(motivation){
            case Briscola2PMatchActivity.EXIT_BUTTON: ((Briscola2PMatchActivity) getActivity()).startMenu(false); break;
            case Briscola2PMatchActivity.START_NEW_OFFLINE:  ((Briscola2PMatchActivity) getActivity()).startOfflineMatch();  break;
            case Briscola2PMatchActivity.START_NEW_ONLINE:  ((Briscola2PMatchActivity) getActivity()).startOnlineMatch(); break;
            case Briscola2PMatchActivity.LOAD_OLD_MATCH :
               // if(loadConfig == null) throw new IllegalArgumentException();
                ((Briscola2PMatchActivity) getActivity()).showSavedMatches();break;
            case Briscola2PMatchActivity.BACK_PRESSED: ((Briscola2PMatchActivity) getActivity()).startMenu(false); break;
            default: return;
        }
    }

    public void handleMatchInterrupt(int motivation){
        FragmentManager manager = getFragmentManager();
        WarningExitDialogFragment dialog = WarningExitDialogFragment.newInstance(isOnline, motivation);
        dialog.setTargetFragment(Briscola2PMatchFragment.this, REQUEST_EXIT);
      //  manager.beginTransaction().add(R.id.fragment_container,dialog).commitAllowingStateLoss();
        dialog.show(manager, DIALOG_EXIT); //todo, riprisinta
        //todo, quando farai la versione orizzontale, guarda: https://stackoverflow.com/questions/30424319/commitallowingstateloss-on-dialogfragment
        //todo, per ora per evitare la illgalStateException faccio commitAllowingStateLoss
    }


}
