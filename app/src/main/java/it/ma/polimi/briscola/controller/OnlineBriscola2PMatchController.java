package it.ma.polimi.briscola.controller;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMinimalMatchConfig;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.rest.client.callbacks.CallbackWithRetry;
import it.ma.polimi.briscola.rest.client.callbacks.OpponentPlayedCardCallback;
import it.ma.polimi.briscola.rest.client.callbacks.PlayCardCallback;
import it.ma.polimi.briscola.rest.client.callbacks.StartMatchCallback;
import it.ma.polimi.briscola.rest.client.callbacks.StopMatchCallback;
import it.ma.polimi.briscola.rest.client.endpoints.BriscolaAPI;
import it.ma.polimi.briscola.view.fragments.Briscola2PMatchFragment;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by utente on 10/12/17.
 */

public class OnlineBriscola2PMatchController implements Briscola2PController {

    private Briscola2PMinimalMatchConfig config;
    // private MatchStatus matchStatus; //todo necessario? credo di sì! E' per forzare il seguire le regole <- invece no, perché faccio un controller per la GUI e uno non per la gui
    private Briscola2PMatchFragment matchFragment;
    private Activity activity;
    public boolean playing = false;
    private boolean allreadyOneFatalError = false;

    private String url;
    private static final String BASE_URL = "http://mobile17.ifmledit.org/api/";
    private BriscolaAPI briscolaAPI;

    private int currentRound = 1;
    private final String authHeader = "APIKey 0c3828b9-b0d6-45c3-aa3b-a6d324561569",
            roomName = "Group01",
            contentTypePlainText = "text/plain";
    private OpponentPlayedCardCallback opponentPlayedCardCallback;
    private StartMatchCallback startMatchCallback;
    private PlayCardCallback playCardCallback;
    private StopMatchCallback stopMatchCallback;


    public OnlineBriscola2PMatchController(Briscola2PMatchFragment matchFragment) {
        super();
        this.matchFragment = matchFragment;
        this.activity = matchFragment.getActivity();
    }

    public OnlineBriscola2PMatchController(OnlineBriscola2PMatchController controller, Briscola2PMatchFragment matchFragment){
        super();
        this.matchFragment = matchFragment;
        //this.config = new Briscola2PMinimalMatchConfig(controller.getConfig());
        this.config = controller.getConfig();
        this.activity = matchFragment.getActivity();

    }


    @Override
    public void setIsPlaying(boolean isPlaying) {
        playing = isPlaying;
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public int countCardsOnSurface() {
        return config.countCardsOnSurface();
    }

    @Override
    public int getCurrentPlayer() {
        return config.getCurrentPlayer();
    }

    @Override
    public void startNewMatch() {

        initializeClient();

        matchFragment.waitingToFindOnlinePlayer();
        startMatchCall();
        //todo PROBABILMENTE QUI DEVI FARE "nomePathVariable: "+pathVariable <- ma anche no
        // Call<List<Change>> call = gerritAPI.loadChanges("status:open");
        // call.enqueue(this);
    }

    private void startMatchCall(){
        startMatchCallback = new StartMatchCallback(this, activity);
        briscolaAPI.startMatch(authHeader, roomName).enqueue(startMatchCallback);
    }

    public void stopWaiting(boolean stop){
        startMatchCallback.stopWaiting(stop);
    }
    private void initializeClient(){

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
               // .addConverterFactory(GsonConverterFactory.create(gson))
               // .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build();

        briscolaAPI = retrofit.create(BriscolaAPI.class);
    }

    public void postCard(String plainText){
        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), plainText);
        playCardCallback = new PlayCardCallback(this);
        briscolaAPI.playCard(url, authHeader,contentTypePlainText, body).enqueue(playCardCallback);

    }
    public void manageError(String error, String message) {
        //todo, gestire meglio le cose da qui per gli errori, in particolare mostrare solo messagi d'errore utili (timeout altro giocatore, abbandono ecc.)
      if(!allreadyOneFatalError) { //since the controller doesn't immediately stop, other callbacks might cause other manageError calls
          matchFragment.manageError(error, message);
          allreadyOneFatalError = true;
      }else{
          //do nothing
      }


    }

    public void manageStartedMatch(String game, String lastCard,String cards,String yourTurn, String url) {
        this.url = url;


        config = new Briscola2PMinimalMatchConfig(url, lastCard, cards, Boolean.valueOf(yourTurn)); //todo, TI CONVIENE CREARE UNA NUOVA CLASSE CHE GESTISCA CONFIGURAZIONI INCOMPLETE
        matchFragment.foundOnlinePlayer();
        //config.initializeNewDeck(); non più possibile
        //  config.initializeFirstPlayer(); todo questo fallo nel costruttore (ricorda: se sei tu il firstPlayer allora inizializza con i valori che ti ha ritornato,
        //todo altrimenti inizializzalo con una carta vuota (aggiungi una carta CoveredCard, che non vada a rompere le scatole con il resto che hai fatto finora)
        //config.initializePlayersHands(); todo, anche questo nel costruttore
        //config.initializeBriscola(); todo, anche questo in costruttore
        //config.setSurface( new Briscola2PSurface("")); TODO <-------------------- QUESTA ROBA NON SERVE PIU' PERCHE' LA FACCIO NEL COSTRUTTORE
        //config.setPile(config.PLAYER0, new Briscola2PPile("")); //todo, queste te le devi gestire tu offline aggiungendo carta per carta
        //config.setPile(config.PLAYER1, new Briscola2PPile(""));
        //List<AnimatorSet> animators = new ArrayList<>();


        AnimatorSet initializationSequence = new AnimatorSet();
        //Todo, modificare il display delle carte, così che se passi un nome invalido lui non se ne frega ...
        AnimatorSet dealFirstHand = matchFragment.getDealFirstHandAnimatorSet(config.getCurrentPlayer(), config.getHands());
        AnimatorSet initializeBriscola = matchFragment.getInitializeBriscolaAnimatorSet(config.getBriscola());
       // AnimatorSet displayCurrentPlayer = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
        //animators.add(dealFirstHand);
        //animators.add(initializeBriscola);

        initializeBriscola.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (config.getCurrentPlayer() == config.PLAYER1) {
                    //playFirstCard(player1.chooseMove(config)).start(); //todo, questo è un metodo annidato in un metodo chiamato dal callback di OpponentCallback
                  opponentPlayedCardCall();

                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        if(config.getCurrentPlayer() == config.PLAYER0) {
            AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
            initializationSequence.playSequentially(dealFirstHand, initializeBriscola, displayIsPlayer0Turn);
        }else{
            initializationSequence.playSequentially(dealFirstHand, initializeBriscola);
        }
       // initializationSequence.playSequentially(dealFirstHand, initializeBriscola,displayCurrentPlayer);
        initializationSequence.start();
    }

    //todo, QUESTO è invocato dal listener sulla carta, quindi vale per il locale (fallo valere anche per il remoto!)

    @Override
    public void playFirstCard(int firstCard) {
        AnimatorSet playCard;
        NeapolitanCard card = config.getLocalPlayerHand().getCard(firstCard);
        config.playCard(firstCard);
        Log.d("TAG", "Before POST playing "+card.toString());
        postCard(card.toString());
        playCard = matchFragment.playFirstCard(firstCard, config.getCurrentPlayer());
        auxiliaryPlayFirstCard(playCard);
    }

    public void playFirstCard(String firstCard) {
        AnimatorSet playCard;
        config.playCard(firstCard);
        playCard = matchFragment.playFirstCard(firstCard, config.getCurrentPlayer());
        auxiliaryPlayFirstCard(playCard);
    }

    public void auxiliaryPlayFirstCard(AnimatorSet playCard){
        config.toggleCurrentPlayer();
        //AnimatorSet displayCurrentPlayer = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
        AnimatorSet playFirstCard = new AnimatorSet();
        //AnimatorSet playFirstCard = new AnimatorSet();
        if(config.getCurrentPlayer() == config.PLAYER0) {
            AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
            playFirstCard.playSequentially(playCard, displayIsPlayer0Turn);
        }else{
            AnimatorSet hideIsPlayer0Turn = matchFragment.hideIsPlayer0Turn(/*config.getCurrentPlayer()*/);
            playFirstCard.playSequentially(hideIsPlayer0Turn, playCard);
        }
       // playFirstCard.playSequentially(playCard, displayCurrentPlayer);
        playFirstCard.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                if(config.getCurrentPlayer() == config.PLAYER1){
                    //playSecondCard(player1.chooseMove(config)).start(); //todo, sarà nel callback che decidi se giocare first o second card!
                    opponentPlayedCardCall();
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        playFirstCard.start();
    }


    public void manageNextTurnCard(String card){
        if(card != null){
            config.setLocalPlayerNextRoundCard(new NeapolitanCard(card.charAt(0),card.charAt(1)));
        }
    }

    public void manageOpponentPlayedCard(String opponent, String card){
        if(opponent != null){
            if(config.countCardsOnSurface() == 0)
                playFirstCard(opponent);
            else
                playSecondCard(opponent);
        }

        if(card != null){
            config.setLocalPlayerNextRoundCard(new NeapolitanCard(card.charAt(0), card.charAt(1)));
        }
    }

    @Override
    public void playSecondCard(int secondCard) {
        AnimatorSet playCard;
        NeapolitanCard card = config.getLocalPlayerHand().getCard(secondCard);
        config.playCard(secondCard);
        postCard(card.toString());
        playCard = matchFragment.playSecondCard(secondCard, config.getCurrentPlayer());
        auxiliaryPlaySecondCard(playCard);
    }

    public void playSecondCard(String secondCard){
        AnimatorSet playCard;
        config.playCard( secondCard);
        playCard = matchFragment.playSecondCard(secondCard, config.getCurrentPlayer());
        auxiliaryPlaySecondCard(playCard);
    }

    public void auxiliaryPlaySecondCard(AnimatorSet playCard){

        AnimatorSet playSecondCard = new AnimatorSet();
        int roundWinner = config.chooseRoundWinner(); //choose round winner
        config.clearSurface(roundWinner);
        AnimatorSet cleanSurface = matchFragment.cleanSurface(roundWinner);
        config.setCurrentPlayer(roundWinner);
        AnimatorSet hideIsPlayer0Turn = matchFragment.hideIsPlayer0Turn();

        //   final AnimatorSet displayCurrentPlayer = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
        cleanSurface.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                AnimatorSet closeTurnAnimation = new AnimatorSet();
                List<Animator> animators = new ArrayList<Animator>();

                if(currentRound == 20){ //if the match is finished, choose winner
                    stopMatchCall();
                    switch(config.chooseMatchWinner()){
                        case Briscola2PMatchConfig.PLAYER0: matchFragment.displayMatchWinner(Briscola2PMatchConfig.PLAYER0, config.computeScore(Briscola2PMatchConfig.PLAYER0)); break;
                        case Briscola2PMatchConfig.PLAYER1: matchFragment.displayMatchWinner(Briscola2PMatchConfig.PLAYER1, config.computeScore(Briscola2PMatchConfig.PLAYER1)); break;
                        case Briscola2PMatchConfig.DRAW: matchFragment.displayMatchWinner(Briscola2PMatchConfig.DRAW, config.computeScore(Briscola2PMatchConfig.PLAYER0)); break;
                        default: throw new RuntimeException("Error while computing the winner");
                    }
                }else if (currentRound == 19 || currentRound == 18){ //if the deck is empty, but players have cards in hand,don't do anything
                    currentRound++;
                    config.setCurrentRound(currentRound);
                    matchFragment.putPlayer0CardsTouchListeners(config.getHands(),config.getCurrentPlayer());
                    if(config.getCurrentPlayer() == config.PLAYER1){
                        //animators.add(playFirstCard(player1.chooseMove(config)));
                        opponentPlayedCardCall();
                    }  else{
                        AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
                        animators.add(displayIsPlayer0Turn);
                    }

                }else if(currentRound <= 17){ //if deck is not empty, draw cards new round
                    //boolean lastDraw = false;
                    //if(config.getDeck().size() == 2)
                    //   lastDraw = true;
                    config.setCurrentRound(currentRound);
                    currentRound++; //since the API doesn't give info on the deck, count turns played to know whether lastDraw
                    boolean lastDraw = false;
                    if(currentRound == 18) // 20th turn (1card -> 0 card, no draw at turn start), 19th turn (2cards->1card, no draw), 18 turn (3cards->2cards, last draw)
                        lastDraw = true;

                    //config.drawCardsNewRound();
                    //cards have been collected from surface, new cards have been drawn
                    config.drawLocalPlayerCard();
                    config.increaseRemotePlayerCardCounter();
                    AnimatorSet drawCards = matchFragment.drawCardsNewRound(config.getHands(), config.getCurrentPlayer(),lastDraw); //TODO, sistemare come calcolare il lastdraw in base al comportamento dell'api del prof
                    animators.add(drawCards);
                    if(config.getCurrentPlayer() == config.PLAYER1){
                        //animators.add(playFirstCard(player1.chooseMove(config)));
                        opponentPlayedCardCall();
                    }else{
                        AnimatorSet displayIsPlayer0Turn = matchFragment.displayIsPlayer0Turn(config.getCurrentPlayer());
                        animators.add(displayIsPlayer0Turn);
                    }
                }

                //animators.add(displayCurrentPlayer);
                closeTurnAnimation.playSequentially(animators);
                closeTurnAnimation.start();


            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        playSecondCard.playSequentially(playCard,hideIsPlayer0Turn, cleanSurface);
        playSecondCard.start();
    }

    public Briscola2PMinimalMatchConfig getConfig() {
        return config;
    }

    @Override
    public int getHandSize(int playerIndex) {
        if(playerIndex == Briscola2PMinimalMatchConfig.PLAYER0)
            return config.getLocalPlayerHand().size();
        else
            return config.getRemotePlayerCardsCounter();
    }

    private void stopMatchCall() {
        stopMatchCallback = new StopMatchCallback();
        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), matchFragment.getString(R.string.terminated));
        briscolaAPI.stopMatch(url, authHeader, contentTypePlainText, body).enqueue(stopMatchCallback);
    }
    public void forceMatchEnd(Context context){
        stopMatchCallback = new StopMatchCallback();
        RequestBody body =
        RequestBody.create(MediaType.parse("text/plain"), context.getString(R.string.abandon));
        briscolaAPI.stopMatch(url,authHeader,contentTypePlainText,body).enqueue(stopMatchCallback);
        if(opponentPlayedCardCallback != null) opponentPlayedCardCallback.shouldStopRetrying(true);
        if(playCardCallback != null) playCardCallback.shouldStopRetrying(true);
        if(startMatchCallback != null) startMatchCallback.shouldStopRetrying(true);
        //stopMatchCallback.shouldStopRetrying(true);

    }

    public void forceMatchEnd(Context context, String url){
        this.url = url;
        forceMatchEnd(context);
    }

    @Override
    public void resumeMatch(){
        initializeClient();
        matchFragment.loadPiles(config.getPile(Briscola2PMatchConfig.PLAYER0).isEmpty(),config.getPile(Briscola2PMatchConfig.PLAYER1).isEmpty());
        matchFragment.loadSurface(config.getSurface().getCardList()); //questi li si può fare subito dato che sono molto semplici
        matchFragment.loadHands(config.getHands());
        matchFragment.loadBriscolaIfNeeded(config.getBriscola());
        matchFragment.loadCurrentPlayer(config.getCurrentPlayer());
        this.url = config.getMatchURL();
    }


    public int getTurnsElapsed(){
        return currentRound;
    }

    public Briscola2PMatchFragment getMatchFragment() {
        return matchFragment;
    }

    private void opponentPlayedCardCall(){
        opponentPlayedCardCallback = new OpponentPlayedCardCallback(OnlineBriscola2PMatchController.this);
        briscolaAPI.getOpponentPlayedCard(url, authHeader).enqueue(opponentPlayedCardCallback);
    }

    public String getUrl(){
        return url;
    }



   /* public void performCallWithUrl(int typeOfCall, String url, String plainTextParmeter){ //in case of rotations, url becomes null, but the callbacks save their urls, and call this method to reperform the call with a correct url
        if(briscolaAPI == null)
            initializeClient();
        if(url == null)
            this.url = url;

        switch(typeOfCall){
            case CallbackWithRetry.OPPONENT_PLAYED:opponentPlayedCardCall();break;
            case CallbackWithRetry.PLAY_CARD:postCard(plainTextParmeter);break;
            case CallbackWithRetry.START_MATCH:;break;
            case CallbackWithRetry.STOP_MATCH:stopMatchCall();break;
        }
    }*/
}
