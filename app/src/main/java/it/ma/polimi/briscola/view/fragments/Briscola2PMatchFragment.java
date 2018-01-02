package it.ma.polimi.briscola.view.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.ma.polimi.briscola.ImageMetadata;
import it.ma.polimi.briscola.audio.SoundService;
import it.ma.polimi.briscola.controller.OfflineBriscola2PMatchController;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PFullMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.view.MatchMenuActivityActions;
import it.ma.polimi.briscola.view.activities.Briscola2PMatchActivity;
import it.ma.polimi.briscola.view.activities.MatchActivity;
import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.controller.Briscola2PController;
import it.ma.polimi.briscola.controller.listeners.CardAnimationListener;
import it.ma.polimi.briscola.controller.listeners.CleanRoundAnimationListener;
import it.ma.polimi.briscola.controller.listeners.FlipCardAnimationListener;
import it.ma.polimi.briscola.controller.listeners.GameInteractionEnabler;
import it.ma.polimi.briscola.controller.listeners.MoveCardAnimationListener;
import it.ma.polimi.briscola.controller.OnlineBriscola2PMatchController;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PHand;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.persistency.SettingsManager;
import it.ma.polimi.briscola.view.dialog.FatalOnlineErrorDialogFragment;
import it.ma.polimi.briscola.view.dialog.SaveConfigDataDialogFragment;
import it.ma.polimi.briscola.view.dialog.SaveFinishedMatchRecordDialogFragment;
import it.ma.polimi.briscola.view.dialog.WaitDialogFragment;
import it.ma.polimi.briscola.view.dialog.WarningExitDialogFragment;
import it.ma.polimi.briscola.view.dialog.WinnerMatchDialogFragment;

/**
 * Class that handles the view of a briscola 2p match
 *
 * @author Francesco Pinto
 */
public class Briscola2PMatchFragment extends Fragment {

    private MatchActivity activity;
    //layout containing all widgets
    private RelativeLayout layout;
    //The interface of the controller
    private Briscola2PController controller;
    //variables to store convenience data about the view (mainly widgets and additional info)
    private final Map<SlotIndices,ImageView> cards = new HashMap<>();//keep mapping between visibile cards and their Image representations
    private final Map<SlotIndices,View> slots = new HashMap<>(); //Keep mapping between the card slots (visible to the player) used as references for card positioning
    private final Map<SlotIndices,Point> slotCoordinates = new HashMap<>(); //coordinates of slots, extracted here for convenience (to avoid the repeated extracton of the coordinates from the previous map)
    private final List<ImageView> pilePlayer0 = new ArrayList<>(); //piles of the two players (if have time, to let the player look at the piles to make decisions ...)
    private final List<ImageView> pilePlayer1 = new ArrayList<>(); //piles of the two players (if have time, to let the player look at the piles to make decisions ...)
    private TextView player0Turn, turnCounterDisplayer;
    //elevation levels used to initialize the cards elevation
    private int[] elevationLevels = new int[] {0,1,2,3,4,5,6};
    private SoundService soundManager;
    //buttons
    private Button exit, menu, save;
    //factor used to make animations faster
    private int animationVelocizationFactor;
    //whether it is online or an offline match that has been resumed
    private boolean isOnline, isResume;
    private int difficulty;
    //whether handling screen rotation (or configuration change)
    private boolean isRetained;

    private int cardViewPreference; //save a copy of the value of the used preference (aim: make the onResume more efficient: no need to refresh card images if a change in preference does not occur)

    //animation velocity constants
    private static final int PLAYER0_TURN_ANIMATION_DURATION = 200, TRANSLATION_DURATION = 1000;
    private static final int LONG_DELAY = 500, SHORT_DELAY = 100, MEDIUM_DELAY = 250;

    //animation velocization factors
    private static final int NORMAL = 2, FAST = 5, VERY_FAST = 15;

    //ids of data passed to the fragment via Bundle
    public static final String MATCH_CONFIG = "it.ma.polimi.briscola.resume.config",
            IS_ONLINE = "it.ma.polimi.briscola.resume.is_online",
            IS_RESUME = "it.ma.polimi.briscola.resume.resume";
    public static final String DIFFICULTY = "it.ma.polimi.briscola.offline.difficulty",
            CARD_VIEW_PREFERENCE = "it.ma.polimi.briscola.cardviewpreference";

    //request codes, to intercept request return values to the activity
    private static final int REQUEST_EXIT = 100, REQUEST_SAVE = 101, REQUEST_STOP_WAITING = 102, REQUEST_SHOW_WINNER = 103, REQUEST_SAVE_RECORD = 104;
    //dynamically generated fragment tags
    private static final String DIALOG_EXIT = "DialogExit", DIALOG_SAVE = "DialogSave", DIALOG_WAIT = "DialogWait", DIALOG_WINNER = "DialogWinner", DIALOG_SAVE_RECORD = "DialogSaveRecord", DIALOG_ONLINE_ERROR = "DialogOnlineError";

    /**
     * Generates a new instance of Briscola2PMatchFragment given isOnline and difficulty.
     *
     * @param isOnline   whether it is an online match
     * @param difficulty the difficulty level
     * @return the briscola 2 p match fragment
     */
    public static Briscola2PMatchFragment newInstance(boolean isOnline, Integer difficulty, Integer cardViewPreference){ //pass null if offline
        //put arguments in a bundle
        Bundle args = new Bundle();
        args.putBoolean(IS_ONLINE,isOnline);
        args.putBoolean(IS_RESUME,false);
        args.putInt(DIFFICULTY, difficulty);
        args.putInt(CARD_VIEW_PREFERENCE, cardViewPreference);
        Briscola2PMatchFragment fragment =  new Briscola2PMatchFragment();
        //pass the arguments to the fragment
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Generates a new instance of Briscola2PMatchFragment given the config to resume from (offline match) and difficulty
     *
     * @param config     the config the game should resume from
     * @param difficulty the difficulty level
     * @return the briscola 2 p match fragment
     */
    public static Briscola2PMatchFragment newInstance(Briscola2PFullMatchConfig config, Integer difficulty, Integer cardViewPreference){ //pass null if offline
        //put arguments in a bundle
        Bundle args = new Bundle();
        args.putSerializable(MATCH_CONFIG,config);
        args.putBoolean(IS_ONLINE,false);
        args.putBoolean(IS_RESUME,true);
        args.putInt(DIFFICULTY, difficulty);
        args.putInt(CARD_VIEW_PREFERENCE, cardViewPreference);

        Briscola2PMatchFragment fragment =  new Briscola2PMatchFragment();
        //pass the arguments to the fragment
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment (for screen rotations or other configuration change)
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initialize view
        View mainView = inflater.inflate(R.layout.fragment_briscola_match, container, false); //todo, refactor del nome

        //get data
        activity = (MatchActivity) this.getActivity();
        soundManager = activity.getSoundManager();
        layout = (RelativeLayout) mainView.findViewById(R.id.layout);
        isOnline = getArguments().getBoolean(IS_ONLINE);
        isResume = getArguments().getBoolean(IS_RESUME);
        difficulty = getArguments().getInt(DIFFICULTY);

        if(controller == null) { //check retained data after configuration change
            if (isOnline) //if nothing retained, create data
                controller = new OnlineBriscola2PMatchController(this);
            else
                controller = new OfflineBriscola2PMatchController(this, difficulty);
        }else{ //if retained, just remember it is retained
            isRetained = true;
        }

        cardViewPreference = getArguments().getInt(CARD_VIEW_PREFERENCE);

        //initialize slots map with the views
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

        //initialize exit button
        exit = mainView.findViewById(R.id.exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager(); //show dialog to request an exit from the match
                WarningExitDialogFragment dialog = WarningExitDialogFragment.newInstance(isOnline,Briscola2PMatchActivity.EXIT_BUTTON);
                dialog.setTargetFragment(Briscola2PMatchFragment.this,REQUEST_EXIT);
                dialog.show(manager, DIALOG_EXIT);
            }
        });

        //initialize the menu button
        menu = mainView.findViewById(R.id.menu_button);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment oldMatches = activity.getMatchesFragments();
                if(oldMatches != null) //if the menu is shown over some running match, then show it as an overlay
                    activity.startMenu(true);
                else //else it is the "start-up" manu, is not superimposed to anything
                    activity.startMenu(false);
            }
        });

        //initialize the save button
        save = mainView.findViewById(R.id.save_button_match);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager(); //warn user the offline match is going to be stopped, might want to save
                SaveConfigDataDialogFragment dialog = SaveConfigDataDialogFragment.newInstance((Briscola2PFullMatchConfig) ((OfflineBriscola2PMatchController) controller).getConfig(), Briscola2PMatchActivity.JUST_SAVE);
                dialog.setTargetFragment(Briscola2PMatchFragment.this, REQUEST_SAVE);
                dialog.show(manager, DIALOG_SAVE);
            }
        });
        if(isOnline) //shouldn't be able to save if it is an online match
            save.setVisibility(View.GONE);

        player0Turn = (TextView) mainView.findViewById(R.id.player0Turn);
        turnCounterDisplayer = (TextView) mainView.findViewById(R.id.turn_displayer);

        //ENSURE THAT THE IMAGEVIEWS HAVE BEEN POSITIONED ON SCREEN BEFORE MANIPULATING IT: DATA IS EXTRACTED ONLY WHEN ALL LAYOUT DATA HAS BEEN INITIALIZED
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                SlotIndices[] indices = SlotIndices.values();
                Point tempCoordinates;

                //extract slot coordinates (for convenience)
                //slots are positioned within relative layouts, HENCE their coordinates are relative to them ... to obtain absolute coordinates
                //one should add the relativeLayout coordinates to them.
                for(int i = 0; i <indices.length; i++ ) {
                    //get relative layouts
                    if (i < SlotIndices.Player0Card0.getIndex()) //slot indices ENSURES other classes that relative layouts will always have indices lower than the Player0Card0 index
                        slotCoordinates.put(indices[i], new Point(slots.get(indices[i]).getLeft(), slots.get(indices[i]).getTop()));
                    else //extract Player0Info (again slot indices ensures Player0Info is between Player0Card0 and Player1Card0
                        if (i >= SlotIndices.Player0Card0.getIndex() && i < SlotIndices.Player1Card0.getIndex()) {
                            tempCoordinates =new Point(slots.get(indices[i]).getLeft(), slots.get(indices[i]).getTop());
                            tempCoordinates.x += slotCoordinates.get(SlotIndices.OffsetLayoutPlayer0).x;
                            tempCoordinates.y += slotCoordinates.get(SlotIndices.OffsetLayoutPlayer0).y;
                            slotCoordinates.put(indices[i],tempCoordinates);
                        } else //extract Player1Info (again slot indices ensures Player1Info is between Player1Card0 and Deckslot
                            if (i >= SlotIndices.Player1Card0.getIndex() && i < SlotIndices.DeckSlot.getIndex()) {
                                tempCoordinates =new Point(slots.get(indices[i]).getLeft(), slots.get(indices[i]).getTop());
                                tempCoordinates.x += slotCoordinates.get(SlotIndices.OffsetLayoutPlayer1).x;
                                tempCoordinates.y += slotCoordinates.get(SlotIndices.OffsetLayoutPlayer1).y;
                                slotCoordinates.put(indices[i],tempCoordinates);

                            } else //extract surface (and deck and briscola) info (slot indices ensures they are between DeckSlot and SurfaceSlot1)
                                if (i >= SlotIndices.DeckSlot.getIndex() && i <= SlotIndices.SurfaceSlot1.getIndex()) {
                                    tempCoordinates =new Point(slots.get(indices[i]).getLeft(), slots.get(indices[i]).getTop());
                                    tempCoordinates.x += slotCoordinates.get(SlotIndices.OffsetLayoutSurface).x;
                                    tempCoordinates.y += slotCoordinates.get(SlotIndices.OffsetLayoutSurface).y;
                                    slotCoordinates.put(indices[i],tempCoordinates);
                                }
                }

                if(!isRetained) { //if is not a retained fragment
                    if (!isResume)  //and a new match (not loaded from saved)
                        controller.startNewMatch();
                    else {
                        //initialize the controller to resume the match from saved config
                        controller = new OfflineBriscola2PMatchController(
                                (Briscola2PFullMatchConfig) getArguments().getSerializable(MATCH_CONFIG),
                                Briscola2PMatchFragment.this, difficulty);
                        controller.resumeMatch(); //resume the match
                    }
                }else{ //if it is retained
                    if(!isOnline) { //if is offline
                        //initialize the controller reloading the config
                        //controller = new OfflineBriscola2PMatchController(
                        //      ((OfflineBriscola2PMatchController) controller).getConfig(),
                        //      Briscola2PMatchFragment.this, difficulty);
                        controller.resumeMatch(); //resume
                    }else{ //is online
                        //initialize the controller
                        //controller = new OnlineBriscola2PMatchController((OnlineBriscola2PMatchController) controller, Briscola2PMatchFragment.this);
                        if(((OnlineBriscola2PMatchController)controller).matchIsStarted()) { //in case user rotates while waiting to find another playing, the match has NOT started and resumeMatch should NOT be called
                            controller.resumeMatch(); //resume
                        }
                    }
                }

                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this); //remove the listener (otherwise it is called again and again and again)
            }
        });
        // init the view
        return mainView;

    }

    /**
     *  Builds an AnimatorSet displaying the first hand dealing animation.
     *
     * @param firstPlayerIndex the first player index (PLAYER0 or PLAYER1)
     * @param playersHands     the players hands
     * @return the animator set
     */
    public AnimatorSet getDealFirstHandAnimatorSet(int firstPlayerIndex, List<Briscola2PHand> playersHands){

        Briscola2PHand firstPlayer = playersHands.get(firstPlayerIndex);
        Briscola2PHand secondPlayer = playersHands.get((firstPlayerIndex+1)%2);

        AnimatorSet dealFirstHand = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();
        SlotIndices[] slotIndices = SlotIndices.values();
        //show turns elapsed
        turnCounterDisplayer.setText(getString(R.string.turns_elapsed,controller.getTurnsElapsed()));

        //index i is for cards (i-th card in hand), index j is for elevation levels
        for(int i = 0,j = 6; i < 3; i++, j-=2){
            //increasing i from 0 to 2 and starting from player0Offset/player1Offset allows to extract
            //the three cards in hand indices of the player0/player1 respectively

            if(firstPlayerIndex == Briscola2PFullMatchConfig.PLAYER0){ //if first player is player0
                //put the at the proper card in hand0 slot the card positioned by positionCardOnScreen initially positioned on the deck slot at highest elevation
                cards.put(slotIndices[i+SlotIndices.player0Offset],positionCardOnScreen(firstPlayer.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j],true));
                //put the at the proper card in hand1 slot the card positioned by positionCardOnScreen initially positioned on the deck slot at highest-1 elevation (while drawing two cards, second card to be drawn should be positioned below)
                cards.put(slotIndices[i+SlotIndices.player1Offset],positionCardOnScreen(secondPlayer.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j-1],true));

                //build the animation                                      get the card, and translate the card from deck to the proper card in hand position
                AnimatorSet player0Animation = getTranslationAnimationSet(cards.get(slotIndices[i+SlotIndices.player0Offset]),slotCoordinates.get(slotIndices[i+SlotIndices.player0Offset]));
                //if the card is of player0 THEN it should also be flipped when arrives in hand (FlipCardAnimationListener will handle it)
                player0Animation.addListener(new FlipCardAnimationListener(cards.get(slotIndices[i+SlotIndices.player0Offset]),soundManager,getActivity().getApplicationContext()));
                //schedule animations, first draws PLAYER0
                animators.add(player0Animation);
                //then PLAYER1           prepare the animation for cards of player1 similarly as done before
                animators.add(getTranslationAnimationSet(cards.get(slotIndices[i+SlotIndices.player1Offset]),slotCoordinates.get(slotIndices[i+SlotIndices.player1Offset])));
            }else if(firstPlayerIndex == Briscola2PFullMatchConfig.PLAYER1) { //if first player is player1

                //put the proper card in hand1 etc. etc. as before
                cards.put(slotIndices[i+SlotIndices.player1Offset],positionCardOnScreen(firstPlayer.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j],true));
                //put the proper card in hand0 etc. etc. as before
                cards.put(slotIndices[i+SlotIndices.player0Offset],positionCardOnScreen(secondPlayer.getCard(i), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[j-1],true));
                //build the animation                                      get the card, and translate the card from deck to the proper card in hand position
                AnimatorSet player0Animation = getTranslationAnimationSet(cards.get(slotIndices[i+SlotIndices.player0Offset]),slotCoordinates.get(slotIndices[i+SlotIndices.player0Offset]));
                //if the card is of player0 THEN it should also be flipped when arrives in hand (FlipCardAnimationListener will handle it)
                player0Animation.addListener(new FlipCardAnimationListener(cards.get(slotIndices[i+SlotIndices.player0Offset]),soundManager,getActivity().getApplicationContext()));

                //schedule the animations: first draws PLAYER1
                animators.add(getTranslationAnimationSet(cards.get(slotIndices[i+SlotIndices.player1Offset]),slotCoordinates.get(slotIndices[i+SlotIndices.player1Offset])));
                //then draws PLAYER0
                animators.add(player0Animation);
            }

        }

        //set listeners on the cards PLAYER0 can play, so that when he/she touches them they are played
        cards.get(SlotIndices.Player0Card0).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));
        cards.get(SlotIndices.Player0Card1).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));
        cards.get(SlotIndices.Player0Card2).setOnClickListener(new CardAnimationListener(Briscola2PMatchFragment.this));

        //schedule animations
        dealFirstHand.playSequentially(animators);
        //even if high-speed animations are selected, allow some time to let the player see what happens
        dealFirstHand.setStartDelay(LONG_DELAY);

        return dealFirstHand;

    }

    /**
     * Build animator set that shows the animation of briscola initialization.
     *
     * @param briscola the briscola card
     * @return the animator set
     */
    public AnimatorSet getInitializeBriscolaAnimatorSet(NeapolitanCard briscola){
        //put briscola on deck slot and update cards data
        cards.put(SlotIndices.BriscolaSlot, positionCardOnScreen(briscola, slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[0],true));
        //move the briscola to the briscola slot position
        AnimatorSet initialize = getTranslationAnimationSet(cards.get(SlotIndices.BriscolaSlot),slotCoordinates.get(SlotIndices.BriscolaSlot));
        //flip it when arrives (using a listener)
        initialize.addListener(new FlipCardAnimationListener(cards.get(SlotIndices.BriscolaSlot),soundManager,getActivity().getApplicationContext()));
        //even if high-speed animations are selected, allow some time to let the player see what happens
        initialize.setStartDelay(SHORT_DELAY);
        return initialize;
    }

    //helper method, generates a playcard animator set
    private AnimatorSet playCard(int cardIndex, int currentPlayer, SlotIndices destinationSlot){

        AnimatorSet playCard = new AnimatorSet();
        //remove card from hand (imageview)
        ImageView card = cards.remove(SlotIndices.getPlayerCardSlotIndex(cardIndex,currentPlayer)); //todo, qui ho modificato
        //extract layout params
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
        Point pos = new Point(params.leftMargin,params.topMargin);

        //translate the card from its position to the destination slot (usually, either surfaceSlot0 or SurfaceSlot1)
        AnimatorSet putCardOnSurface = getTranslationAnimationSet(card,
                slotCoordinates.get(destinationSlot)
        );
        //update card data
        cards.put(destinationSlot,card);
        //if is card of PLAYER1, should be flipped (since cards in hand of player1 are covered)
        if(currentPlayer == Briscola2PFullMatchConfig.PLAYER1)
            putCardOnSurface.addListener(new FlipCardAnimationListener(card,soundManager,getActivity().getApplicationContext()));

        //if is card of PLAYER0 don't flip it, and disable interactions
        if(currentPlayer == Briscola2PFullMatchConfig.PLAYER0)
            controller.setIsPlaying(false); //prevent user from playing other cards

        //adjust the positions of other cards in hand (simply shift them just like in a List structure)
        AnimatorSet adjust = adjustCards(cardIndex,currentPlayer,controller.getHandSize(currentPlayer));
        //schedule animations
        playCard.playSequentially(putCardOnSurface, adjust);
        //even if high-speed animations are selected, allow some time to let the player see what happens
        playCard.setStartDelay(MEDIUM_DELAY);
        return playCard;
    }

    /**
     * Generate an animation for the first card of the turn being played, uses int card index
     *
     * @param cardIndex     the card index
     * @param currentPlayer the current player (PLAYER0 or PLAYER1)
     * @return the animator set
     */
    public AnimatorSet playFirstCard(int cardIndex, int currentPlayer){
        //call the helper method, setting destiantion to SurfaceSlot0
        return playCard(cardIndex,currentPlayer,SlotIndices.SurfaceSlot0);
    }

    /**
     * Generate an animation for the second card of the turn being played, uses int card index
     *
     * @param cardIndex     the card index
     * @param currentPlayer the current player (PLAYER0 or PLAYER1)
     * @return the animator set
     */
    public AnimatorSet playSecondCard(int cardIndex, int currentPlayer){
        //call the helper method, setting destination to SurfaceSlot1
        return  playCard(cardIndex,currentPlayer,SlotIndices.SurfaceSlot1);

    }

    /**
     * Generate an animation for the first card of the turn being played, uses string card representation. This method is called ONLY by the online game opponent card has been initialized as unknown, and now needs to be played.
     *
     * @param card          the card
     * @param currentPlayer the current player
     * @return the animator set
     */
    public AnimatorSet playFirstCard(String card, int currentPlayer){
        //get name of the image representing the card (based on the preference)
        String cardName = ImageMetadata.getCardName(getActivity().getApplicationContext(),card);

        //update card info, FOR SIMPLICITY, in an online match always playe Player1Card0
        ImageView cardView = cards.get(SlotIndices.Player1Card0);
        cardView.setTag(cardName+";"+true); //update the cardName, since it was not initialized
        //call the helper method, setting destination to SurfaceSlot0
        return playCard(0,currentPlayer,SlotIndices.SurfaceSlot0);
    }

    /**
     * Generate an animation for the second card of the turn being played, uses string card representation. This method is called ONLY by the online game opponent card has been initialized as unknown, and now needs to be played. The server passes a string to provide info on the card.
     *
     * @param card          the card
     * @param currentPlayer the current player
     * @return the animator set
     */
    public AnimatorSet playSecondCard(String card, int currentPlayer){
        //get name of the image representing the card (based on the preference)
        String cardName = ImageMetadata.getCardName(getActivity().getApplicationContext(),card);
        //update card info, FOR SIMPLICITY, in an online match always playe Player1Card0
        ImageView cardView = cards.get(SlotIndices.Player1Card0);
        cardView.setTag(cardName+";"+true);//update the cardName, since it was not initialized
        //call the helper method, setting destination to SurfaceSlot1
        return  playCard(0,currentPlayer,SlotIndices.SurfaceSlot1);

    }



    /**
     * Generates an animation that adjustes cards position in hand of player when a card is removed from hand. The adjustment has been implemented using a List-like behaviour (when something is removed, shift everything to occupy the gap)
     *
     * @param cardIndex        the card index
     * @param currentPlayer    the current player
     * @param playerHandLength the player hand length (according to the configuration, not the GUI)
     * @return the animator set
     */
    public AnimatorSet adjustCards(int cardIndex, int currentPlayer, int playerHandLength){
        AnimatorSet adjust = new AnimatorSet();
        List<Animator> adjustCards = new ArrayList<>();

        //for all the cards in hand following the removed one
        //+1 because when adjust is invoked, the card count in hand has been decreased
        for(int j = cardIndex+1; j < playerHandLength+1;j++){
            //update data about the imageView
            ImageView card = this.cards.remove(SlotIndices.getPlayerCardSlotIndex(j,currentPlayer));
            //get parameters of the imageView
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) card.getLayoutParams();
            Point pos = new Point(params.leftMargin, params.topMargin);
            //the "previous" slot, so that the card can shift back
            SlotIndices slot = SlotIndices.getPlayerCardSlotIndex(j - 1, currentPlayer);
            //translate so that it is shifted back
            adjustCards.add(getTranslationAnimationSet(
                    card,
                    slotCoordinates.get(slot)

            ));
            //update view informations
            this.cards.put(SlotIndices.getPlayerCardSlotIndex(j - 1, currentPlayer), card);

        }
        //schedule animation
        adjust.playSequentially(adjustCards);
        return adjust;
    }

    /**
     * Produces a cleaning animation when a round ends
     *
     * @param winner the winner index (PLAYER0 or PLAYER1)
     * @return the animator set
     */
    public AnimatorSet cleanSurface(int winner){
        AnimatorSet cleanSurface = new AnimatorSet();
        //get cards on surface (update info)
        ImageView card0 = cards.remove(SlotIndices.SurfaceSlot0);
        ImageView card1 = cards.remove(SlotIndices.SurfaceSlot1);

        //update view status
        if(winner == Briscola2PMatchConfig.PLAYER0) {
            pilePlayer0.add(card0);
            pilePlayer0.add(card1);
        }else{
            pilePlayer1.add(card0);
            pilePlayer1.add(card1);
        }

        //translate them to the pile of the winner
        AnimatorSet cleanFirst = getTranslationAnimationSet(card0,
                slotCoordinates.get(SlotIndices.getPileSlotOfPlayer(winner))
        );
        AnimatorSet cleanSecond = getTranslationAnimationSet(card1,
                slotCoordinates.get(SlotIndices.getPileSlotOfPlayer(winner))
        );
        //play a sound based on who won when the animation start (via listener)
        cleanFirst.addListener(new CleanRoundAnimationListener((winner== Briscola2PFullMatchConfig.PLAYER0)?true:false,soundManager));
        //flip both cards (ASSUMPTION: Piles are covered)
        cleanFirst.addListener(new FlipCardAnimationListener(card0,soundManager,getActivity().getApplicationContext()));
        cleanSecond.addListener(new FlipCardAnimationListener(card1,soundManager,getActivity().getApplicationContext()));
        //schedule animations
        cleanSurface.playSequentially(cleanFirst,cleanSecond);
        //even if high-speed animations are selected, allow some time to let the player see what happens
        cleanSurface.setStartDelay(SHORT_DELAY);
        return cleanSurface;
    }

    /**
     * Builds an animation that shows the local player whether it is his/her turn and enable him to play
     *
     * @param currentPlayer the current player
     * @return the animator set
     */
    public AnimatorSet displayIsPlayer0Turn(int currentPlayer){
        ObjectAnimator displayCurrentPlayer;
        if(currentPlayer == Briscola2PFullMatchConfig.PLAYER0){ //if is player0 turn
            //show
            displayCurrentPlayer = ObjectAnimator.ofFloat(player0Turn, "alpha", 1f);
            displayCurrentPlayer.setDuration(PLAYER0_TURN_ANIMATION_DURATION); //it is already fast enough, no need to be made faster using velocity factors
            //enable player interactions via listener
            displayCurrentPlayer.addListener(new GameInteractionEnabler(controller,true));
        } else //Shouldn't be called for PLAYER1
            throw new IllegalStateException();

        AnimatorSet animation = new AnimatorSet();
        //schedule
        animation.play(displayCurrentPlayer);
        return animation;
    }

    /**
     * Builds an animation that hides the signal that he/she can play
     *
     * @return the animator set
     */
    public AnimatorSet hideIsPlayer0Turn(){
        ObjectAnimator displayCurrentPlayer;
        //hide
        displayCurrentPlayer= ObjectAnimator.ofFloat(player0Turn, "alpha",0f);//0f,1f);
        displayCurrentPlayer.setDuration(PLAYER0_TURN_ANIMATION_DURATION); //it is already fast enough, no need to be made faster using velocity factors
        AnimatorSet animation = new AnimatorSet();
        animation.play(displayCurrentPlayer);
        return animation;
    }

    /**
     * Builds an animation that draws two cards for a new round (i.e. except the first, where 6 cards are drawn)
     *
     * @param playersHands     the players hands
     * @param firstPlayerIndex the first player index (PLAYER0 or PLAYER1)
     * @param lastDraw         whether it is the last 2 cards being drawn from deck
     * @return the animator set
     */
    public AnimatorSet drawCardsNewRound(List<Briscola2PHand> playersHands, int firstPlayerIndex, boolean lastDraw){

        //prepare some data, first = the first playing in the round, second = the second ...
        Briscola2PHand firstPlayer = playersHands.get(firstPlayerIndex);
        Briscola2PHand secondPlayer = playersHands.get((firstPlayerIndex+1)%2);
        AnimatorSet dealTwoCards = new AnimatorSet();
        List<Animator> animators = new ArrayList<>();
        SlotIndices[] slotIndices = SlotIndices.values();
        int firstHandSize = firstPlayer.size();
        int secondHandSize = secondPlayer.size();
        //offset is used to correctly choose slot indices
        int firstPlayerOffset, secondPlayerOffset;
        if(firstPlayerIndex == Briscola2PFullMatchConfig.PLAYER0) { //correctly initialize offset
            firstPlayerOffset = SlotIndices.player0Offset;
            secondPlayerOffset = SlotIndices.player1Offset;
        }
        else {
            firstPlayerOffset = SlotIndices.player1Offset;
            secondPlayerOffset = SlotIndices.player0Offset;

        }

        //position the two cards on screen
        ImageView cardSecond; //firstCard is positioned on deck
        ImageView cardFirst = positionCardOnScreen(firstPlayer.getCard(firstHandSize-1),slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[2],true);
        if(lastDraw) { //REMARK: if it is the last draw, the briscola SHOULD NOT be positioned, one only needs to update data
            cardSecond = cards.remove(SlotIndices.BriscolaSlot);
            //deck now is empty, show an empty slot
            ((ImageView)slots.get(SlotIndices.DeckSlot)).setImageResource(R.drawable.card_slot_shape);
        }
        else { //if it is not the last draw, simply position the card on deck
            cardSecond = positionCardOnScreen(secondPlayer.getCard(secondHandSize - 1), slotCoordinates.get(SlotIndices.DeckSlot), elevationLevels[1], true);
        }
        //drawn card is ALLWAYS positione as the last one in the list of cards available in hand
        //hence take the slot at playerOffset + HandSize (minus 1 because HandSize is a list length, should be decremented to have 0,1,2 card indices)
        SlotIndices first = slotIndices[firstHandSize+firstPlayerOffset-1];
        SlotIndices second = slotIndices[secondHandSize+secondPlayerOffset-1];

        //update view data
        cards.put(first,cardFirst);
        cards.put(second,cardSecond);
        //properly translate the two cards so that they move to the correct player hand position
        AnimatorSet playFirst = getTranslationAnimationSet(cards.get(slotIndices[firstHandSize+firstPlayerOffset-1]),slotCoordinates.get(slotIndices[firstHandSize+firstPlayerOffset-1]));
        AnimatorSet playSecond;
        if(!lastDraw) //if it is not the last draw, the translation starts from deck
            playSecond = getTranslationAnimationSet(cards.get(slotIndices[secondHandSize+secondPlayerOffset-1]),slotCoordinates.get(slotIndices[secondHandSize+secondPlayerOffset-1]));
        else { //else it starts from the briscola slot! <- AFTER REFACTORING OF CODE, the if()else() IS REDUNDANT, however it is kept (in case roll-back is required)
            playSecond = getTranslationAnimationSet(cards.get(firstPlayerIndex == Briscola2PMatchConfig.PLAYER0 ? SlotIndices.Player1Card2 : SlotIndices.Player0Card2), slotCoordinates.get(firstPlayerIndex == Briscola2PMatchConfig.PLAYER0 ? SlotIndices.Player1Card2 : SlotIndices.Player0Card2));
        }

        //player0 cards should have touch listeners, to let him play
        putPlayer0CardsTouchListeners(playersHands,firstPlayerIndex);
        if(firstPlayerIndex == Briscola2PFullMatchConfig.PLAYER0) { //if first player == PLAYER0
            playFirst.addListener(new FlipCardAnimationListener(cardFirst, soundManager, getActivity().getApplicationContext())); //show to PLAYER0 the drawn card
            if(lastDraw){ //if it is also last draw, briscola is picked up by the other player, should be covered after being picked
                playSecond.addListener(new FlipCardAnimationListener(cardSecond,soundManager,getActivity().getApplicationContext()));
            }
        }else { //if first player == PLAYER1
            if(!lastDraw){ //if not last draw, secondPlayer is card of player0, should be flipped
                playSecond.addListener(new FlipCardAnimationListener(cardSecond,soundManager,getActivity().getApplicationContext())); //flip cards of player0
            }
        }

        //schedule animations
        animators.add(playFirst);
        animators.add(playSecond);

        dealTwoCards.playSequentially(animators);
        //put some delay to let him/her see
        dealTwoCards.setStartDelay(LONG_DELAY);
        return dealTwoCards;

    }

    /**
     * Put listeners on Player0Cards so that he/she can play them and update the turnCounterDisplayer
     *
     * @param playersHands     the players hands
     * @param firstPlayerIndex the first player index
     */
    public void putPlayer0CardsTouchListeners(List<Briscola2PHand> playersHands, int firstPlayerIndex){
        //depending on the number of cards in hand, add listeners to available cards
        switch(playersHands.get((firstPlayerIndex == Briscola2PFullMatchConfig.PLAYER0)?firstPlayerIndex:(firstPlayerIndex+1)%2).size()){
            case 3:;cards.get(SlotIndices.Player0Card2).setOnClickListener(new CardAnimationListener(this));
            case 2:cards.get(SlotIndices.Player0Card1).setOnClickListener(new CardAnimationListener(this));
            case 1:cards.get(SlotIndices.Player0Card0).setOnClickListener(new CardAnimationListener(this)); break;
        }
        //update turnCounterDisplayer (new turn!)
        turnCounterDisplayer.setText(getString(R.string.turns_elapsed,controller.getTurnsElapsed()));
    }
    @Override
    public void onResume() {
        super.onResume();

        //initialize the preferences (on every resume also, because they could have been changed)
        int velocityPreference = new SettingsManager(getActivity().getApplicationContext()).getVelocityPreference();
        switch(velocityPreference){ //map the velocities to the view velocity factors
            case SettingsManager.VELOCITY_NORMAL: animationVelocizationFactor = NORMAL; break;
            case SettingsManager.VELOCITY_FAST: animationVelocizationFactor = FAST; break;
            case SettingsManager.VELOCITY_VERY_FAST: animationVelocizationFactor = VERY_FAST; break;

        }

        //change deckSlot if necessary
        ImageView deck = (ImageView) slots.get(SlotIndices.DeckSlot);
        if(controller != null && controller.getTurnsElapsed() != null && controller.getTurnsElapsed()< 18) { //if briscola is not empty then also the deck should be shown, otherwise deck is empty (should not be refreshed)
        deck.setImageResource(ImageMetadata.getCardBackID(getActivity().getApplicationContext()));
         }


        //refresh images according to preferences on type of cards
        if(cards != null && pilePlayer0 != null && pilePlayer1 != null) {
            SettingsManager settingsManager = new SettingsManager(getActivity().getApplicationContext());
            int newCardViewPreference = settingsManager.getCardViewPreference();

            if (cardViewPreference != newCardViewPreference) {
                //update cards in the piles ... ideally useful in case (in an hypothetical next release, the player will be able to look at the piles while playing)
                for(ImageView c: pilePlayer0){
                    c.setImageResource(ImageMetadata.getCardBackID(getActivity().getApplicationContext()));
                }

                for(ImageView c: pilePlayer1){
                    c.setImageResource(ImageMetadata.getCardBackID(getActivity().getApplicationContext()));
                }
                //change all the other cards
                Collection<ImageView> images = cards.values();
                for (ImageView i : images) {
                    String[] cardTag = ((String) i.getTag()).split(";"); //extract the tag (contains info about whether the card is covered and the uncovered card image name)
                    if (Boolean.valueOf(cardTag[1])) {//is covered, change the card's back
                        int resID = ImageMetadata.getCardBackID(getActivity().getApplicationContext());
                        i.setImageResource(resID);
                    } else { //is not covered
                        String cardName = cardTag[0];
                        //if(cardViewPreference == SettingsManager.FRENCH && newCardViewPreference == SettingsManager.MINIMAL_FRENCH)
                        //    cardName = ImageMetadata.fromFrenchToMinimalFrench(getResources().getResourceEntryName(Integer.valueOf(cardTag[0])));
                       // else if(cardViewPreference == SettingsManager.MINIMAL_FRENCH && newCardViewPreference == SettingsManager.FRENCH)
                        //    cardName = ImageMetadata.fromMinimalFrenchToFrench(getResources().getResourceEntryName(Integer.valueOf(cardTag[0])));
                       // if(cardName.equals(""))
                       //     throw new IllegalArgumentException();
                        //get image id
                        //int resID = getResources().getIdentifier(cardName, "drawable", activity.getPackageName());

                        int resID = ImageMetadata.getCardIDWithCurrentPreference(getActivity().getApplicationContext(),cardName);

                        i.setImageResource(resID);
                        i.setTag(cardName + ";" + cardTag[1]); //update resID, put the isCovered value back to its place
                    }
                }
                cardViewPreference = newCardViewPreference;
            }
        }

    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDetach(){
        super.onDetach();
    }

    //todo DA FARE: METTERE I DIALOG ALL'ONLINE'
    /**
     * Display match winner by using a dialog
     *
     * @param player the player who won
     * @param score  the winner score
     */
    public void displayMatchWinner(int player, int score) {
        FragmentManager manager = getFragmentManager();
        WinnerMatchDialogFragment dialog = WinnerMatchDialogFragment.newInstance(player,score,isOnline);
        dialog.setTargetFragment(Briscola2PMatchFragment.this, REQUEST_SHOW_WINNER);
        dialog.show(manager,DIALOG_WINNER);
    }

    /**
     * Convenience method to show a toast.
     *
     * @param string the string
     */
    public void showToast(String string){
        Toast.makeText(activity,string,Toast.LENGTH_SHORT).show();
    }

    //helper method to position card on screen
    private ImageView positionCardOnScreen(NeapolitanCard c, Point initialPosition, int elevation, boolean isCovered){
        //get name of the image representing the card (based on the preference)
        String cardName = ImageMetadata.getCardName(getActivity().getApplicationContext(),c);;


        //get image id
        int resID = getResources().getIdentifier(cardName, "drawable", activity.getPackageName());


        //build new image
        ImageView card = new ImageView(activity);
        card.setId(View.generateViewId());
        card.setTag(cardName+";"+isCovered); //set the tag as resID;boolean (will be used by listeners)
        if(isCovered) //if card is covered, show card back
            card.setImageResource(ImageMetadata.getCardBackID(getActivity().getApplicationContext()));
        else
            card.setImageResource(resID); //else show card image

        //set layout params based on whether it is landscape/portrait
        //indeed, two scales are used in the two cases
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

        //add imageview to the layout
        layout.addView(card,params);

        return card;
    }

    /**
     * Method that shows the user that the app is waiting to find another online player.
     */
    public void waitingToFindOnlinePlayer(){
        FragmentManager manager = getFragmentManager();
        WaitDialogFragment dialog = WaitDialogFragment.newInstance();
        dialog.setTargetFragment(Briscola2PMatchFragment.this, REQUEST_STOP_WAITING);
        dialog.show(manager, DIALOG_WAIT);
    }

    /**
     * Method to be called when the online player is found, removes the waiting dialog.
     */
    public void foundOnlinePlayer(){
        FragmentManager manager = getFragmentManager();
        //retrieve the dialog
        WaitDialogFragment found =  (WaitDialogFragment) manager.findFragmentByTag(DIALOG_WAIT);
        if(found != null){ //hide it, if present
            found.dismiss();
        }
    }

    /**
     * Method that shows the user a dialog with the occured error
     *
     * @param error the error of the javascript message
     * @param message the message of the javascript message
     */
    public void manageError(String error, String message){

        FragmentManager manager = getFragmentManager();
        FatalOnlineErrorDialogFragment dialog = FatalOnlineErrorDialogFragment.newInstance(error,message, Briscola2PMatchActivity.ONLINE_ERROR);
        dialog.setTargetFragment(Briscola2PMatchFragment.this, REQUEST_EXIT); //the error is fatal, should exit after
        dialog.show(manager,DIALOG_ONLINE_ERROR);
        //todo, usare l'errorCode per diversificare la gestione
        //todo DEVI FARE QUESTO AL POSTO DEL DIALOG CHE HAI CANCELLATO

        //ci sono due tipi di errori:
        // timeout,
        // il giocatore abbandona PRIMA della fine del match,
        // il giocatore abbandona ALLA fine del match

        //i primi due casi vanno gestiti CHIUDENDO IL MATCH dopo il dialog, l'altro invece va gestito con una exit normale



        // PER QUANTO RIGUARDA LE PARTITE INCOMINCIATE E LASCIATE IN ATTESA: nel callback di startMatch
        //       PASSA la BriscolaAPI e una variabile "StoppedWaiting" che se è true implica
        //        che fa una call invisibile a BriscolaAPI.closeMatch()
    }


    /**
     * Method that shows the user a dialog to save data about the match
     *
     * @param player0Points the player 0 points
     */
    public void saveFinishedMatchRecordData(int player0Points){
        FragmentManager manager = getFragmentManager();
        SaveFinishedMatchRecordDialogFragment dialog = SaveFinishedMatchRecordDialogFragment.newInstance(player0Points,isOnline);
        dialog.setTargetFragment(Briscola2PMatchFragment.this, REQUEST_SAVE_RECORD);
        dialog.show(manager,DIALOG_SAVE_RECORD);

    }

    //helper method that defines a translation animation of a targetView between origin and target
    private AnimatorSet getTranslationAnimationSet(ImageView targetView, Point target) {

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) targetView.getLayoutParams();
        Point origin = new Point(params.leftMargin,params.topMargin);

        AnimatorSet translation = new AnimatorSet();
        //translate along x
        ObjectAnimator translationX = new ObjectAnimator().ofFloat(targetView, "translationX", target.x - origin.x);
        translationX.setDuration(TRANSLATION_DURATION/animationVelocizationFactor);
        //set interpolator, to have nice animation
        translationX.setInterpolator(new AccelerateDecelerateInterpolator());
        //translate along y
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(targetView, "translationY", target.y - origin.y);
        translationY.setDuration(TRANSLATION_DURATION/animationVelocizationFactor);
        //set interpolator to have niece animation
        translationY.setInterpolator(new AccelerateDecelerateInterpolator());
        //play them together
        translation.playTogether(translationX, translationY);
        //put a MoveCardAnimationListener for audio
        translation.addListener(new MoveCardAnimationListener(getSoundManager()));
        return translation;
    }

    /**
     * In a match load, load the piles
     *
     * @param pile0IsEmpty the pile 0 is empty
     * @param pile1IsEmpty the pile 1 is empty
     */
    public void loadPiles(boolean pile0IsEmpty, boolean pile1IsEmpty){
        //since piles are covered, no need of info about the pile apart from whether they are empty or not
        //if they are not empty, convert the pile slot into the back of a card
        if(!pile0IsEmpty)
            cards.put(SlotIndices.Player0PileSlot,positionCardOnScreen(new NeapolitanCard(), slotCoordinates.get(SlotIndices.Player0PileSlot),elevationLevels[elevationLevels.length-1],true));
        if(!pile1IsEmpty)
            cards.put(SlotIndices.Player1PileSlot,positionCardOnScreen(new NeapolitanCard(), slotCoordinates.get(SlotIndices.Player1PileSlot),elevationLevels[elevationLevels.length-1],true));
    }

    /**
     * In a match load, load the surface Cards.
     *
     * @param surfaceCards the surface cards
     */
    public void loadSurface(List<NeapolitanCard> surfaceCards){
        int size = surfaceCards.size();
        if(size >= 1){ //if at least 1 card on surface, position the first card on SurfaceSlot0
            ImageView firstCard = positionCardOnScreen(surfaceCards.get(0),slotCoordinates.get(SlotIndices.SurfaceSlot0),elevationLevels[0],false);
            cards.put(SlotIndices.SurfaceSlot0, firstCard);
        }
        if(size >= 2){ //if at least 2 cards on surface, position the second card on SurfaceSlot1
            ImageView secondCard = positionCardOnScreen(surfaceCards.get(1),slotCoordinates.get(SlotIndices.SurfaceSlot1),elevationLevels[0],false);
            cards.put(SlotIndices.SurfaceSlot1, secondCard);
        }
    }

    /**
     * In a match load, load the players hands and load the number of turns elapsed
     *
     * @param playersHands       the players hands
     */
    public void loadHands(List<Briscola2PHand> playersHands){

        Briscola2PHand player0 = playersHands.get(Briscola2PFullMatchConfig.PLAYER0);
        Briscola2PHand player1 = playersHands.get(Briscola2PFullMatchConfig.PLAYER1);

        //update turns elapsed
        turnCounterDisplayer.setText(getString(R.string.turns_elapsed,controller.getTurnsElapsed()));

        switch(player0.size()){ //for all the cards in hand of player0, position the card, update card data in the view, set listener on that card so that it can be played
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

        switch(player1.size()){ //for player1 instead just position cards and update data in the view
            case 3:cards.put(SlotIndices.Player1Card2,positionCardOnScreen(player1.getCard(2), slotCoordinates.get(SlotIndices.Player1Card2), elevationLevels[1],true));
            case 2:cards.put(SlotIndices.Player1Card1,positionCardOnScreen(player1.getCard(1), slotCoordinates.get(SlotIndices.Player1Card1), elevationLevels[1],true));
            case 1:cards.put(SlotIndices.Player1Card0,positionCardOnScreen(player1.getCard(0), slotCoordinates.get(SlotIndices.Player1Card0), elevationLevels[1],true));
            default: break;
        }

    }

    /**
     * In a match load, load the briscola (if needed)
     *
     * @param briscola the briscola
     */
    public void loadBriscolaIfNeeded(NeapolitanCard briscola){
        if(briscola != null) //birscola still on surface, not in players hands (if in hands, loadHands will take care of that)
            cards.put(SlotIndices.BriscolaSlot,positionCardOnScreen(briscola,slotCoordinates.get(SlotIndices.BriscolaSlot),elevationLevels[0],false));

        if(briscola == null) //if last 2 cards have been drawn, show deck empty
            ((ImageView) slots.get(SlotIndices.DeckSlot)).setImageResource(R.drawable.card_slot_shape);

        //turnCounterDisplayer.setText(getString(R.string.turns_elapsed,controller.getTurnsElapsed()));
    }

    /**
     * In a match load, show the user whether it was his/her turn
     *
     * @param currentPlayer the current player
     */
    public void loadCurrentPlayer(int currentPlayer){
        if(currentPlayer == Briscola2PFullMatchConfig.PLAYER0) {
            displayIsPlayer0Turn(currentPlayer).start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_EXIT){ //button exit pressed

            //retrieve data from the Intent returned
            MatchMenuActivityActions action = (MatchMenuActivityActions) data.getSerializableExtra(WarningExitDialogFragment.EXTRA_ACTION);
            int motivation = data.getIntExtra(WarningExitDialogFragment.EXTRA_MOTIVATION, Briscola2PMatchActivity.EXIT_BUTTON);

            //based on the suggested action, make a choice
            if(action == MatchMenuActivityActions.STOP_ONLINE){
                controller.forceMatchEnd(getActivity()); //invoca rest api, terminate match
                handleExit(motivation);

            }else if(action == MatchMenuActivityActions.WARN_STOP_OFFLINE){
                FragmentManager manager = getFragmentManager(); //warn user the offline match is going to be stopped, might want to save
                SaveConfigDataDialogFragment dialog = SaveConfigDataDialogFragment.newInstance((Briscola2PFullMatchConfig)((OfflineBriscola2PMatchController) controller).getConfig(),motivation);
                dialog.setTargetFragment(Briscola2PMatchFragment.this, REQUEST_SAVE);
                dialog.show(manager,DIALOG_SAVE);
            }

        }else if(requestCode == REQUEST_SAVE){ //offline match to be saved has been saved, now continue handling exit

            //extract data from intent
            MatchMenuActivityActions action = (MatchMenuActivityActions) data.getSerializableExtra(SaveConfigDataDialogFragment.EXTRA_ACTION);
            int motivation = data.getIntExtra(SaveConfigDataDialogFragment.EXTRA_MOTIVATION, Briscola2PMatchActivity.EXIT_BUTTON);

            if(action == MatchMenuActivityActions.STOP_OFFLINE){ //
                handleExit(motivation); //stop the match
            }

        }else if(requestCode == REQUEST_STOP_WAITING){
            //extract data from intent
            boolean stopWaiting = data.getBooleanExtra(WaitDialogFragment.EXTRA_STOP_WAITING,false);

            if(stopWaiting){//if the online wants to stop waiting, stop the callbacks and return to menu
                ((OnlineBriscola2PMatchController) controller).stopWaiting(true);
                ((MatchActivity)getActivity()).startMenu(false);
            }

        }else if(requestCode ==REQUEST_SHOW_WINNER){
            //allow user to save match record data
            int player0Score = data.getIntExtra(WinnerMatchDialogFragment.EXTRA_PLAYER0_SCORE,0);
            saveFinishedMatchRecordData(player0Score);
        }else if(requestCode == REQUEST_SAVE_RECORD){
            //once saved match record data, just exit and return to menu
            handleExit(Briscola2PMatchActivity.EXIT_BUTTON);
        }
    }


    /**
     * Method that performs some action based on the motivation a match exited.
     *
     * @param motivation the motivation
     */
    public void handleExit(int motivation){
        switch(motivation){
            //return to menu
            case Briscola2PMatchActivity.EXIT_BUTTON: ((Briscola2PMatchActivity) getActivity()).startMenu(false); break;
            //start other matches
            case Briscola2PMatchActivity.START_NEW_OFFLINE:  ((Briscola2PMatchActivity) getActivity()).startOfflineMatch();  break;
            case Briscola2PMatchActivity.START_NEW_ONLINE:  ((Briscola2PMatchActivity) getActivity()).startOnlineMatch(); break;
            //show saved matches
            case Briscola2PMatchActivity.LOAD_OLD_MATCH :
                // if(loadConfig == null) throw new IllegalArgumentException();
                ((Briscola2PMatchActivity) getActivity()).showSavedMatches();break;
            //return to menu
            case Briscola2PMatchActivity.BACK_PRESSED: ((Briscola2PMatchActivity) getActivity()).startMenu(false); break;
            case Briscola2PMatchActivity.ONLINE_ERROR: ((Briscola2PMatchActivity) getActivity()).startMenu(false); break;
            default: return;
        }
    }

    /**
     * Handle match interrupt, shows a dialog that warns the player that the match will be interrupted by that action.
     *
     * @param motivation the motivation
     */
    public void handleMatchInterrupt(int motivation){
        FragmentManager manager = getFragmentManager();
        WarningExitDialogFragment dialog = WarningExitDialogFragment.newInstance(isOnline, motivation);
        dialog.setTargetFragment(Briscola2PMatchFragment.this, REQUEST_EXIT);
        dialog.show(manager, DIALOG_EXIT);

    }


    /**
     * Gets sound manager.
     *
     * @return the sound manager
     */
    public SoundService getSoundManager() {
        return soundManager;
    }

    /**
     * Sets activity.
     *
     * @param activity the activity
     */
    public void setActivity(MatchActivity activity) {
        this.activity = activity;
    }

    /**
     * Gets controller.
     *
     * @return the controller
     */
    public Briscola2PController getController() {
        return controller;
    }

    /**
     * Gets cards.
     *
     * @return the cards
     */
    public Map<SlotIndices, ImageView> getCards() {
        return cards;
    }
}
