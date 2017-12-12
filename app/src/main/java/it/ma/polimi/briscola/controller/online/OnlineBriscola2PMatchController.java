package it.ma.polimi.briscola.controller.online;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import it.ma.polimi.briscola.ai.Briscola2PAIPlayer;
import it.ma.polimi.briscola.ai.Briscola2PAIRandomPlayer;
import it.ma.polimi.briscola.controller.offline.Briscola2PController;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMinimalMatchConfig;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.rest.client.OpponentPlayedCardCallback;
import it.ma.polimi.briscola.rest.client.PlayCardCallback;
import it.ma.polimi.briscola.rest.client.StartMatchCallback;
import it.ma.polimi.briscola.rest.client.StopMatchCallback;
import it.ma.polimi.briscola.rest.client.dto.NextTurnCardDTO;
import it.ma.polimi.briscola.rest.client.dto.OpponentCardDTO;
import it.ma.polimi.briscola.rest.client.dto.StartedMatchDTO;
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
    private Briscola2PAIPlayer player1 = new Briscola2PAIRandomPlayer();
    private Briscola2PMatchFragment matchFragment;
    public boolean playing = false;
    private boolean done = false;

    private String url;
    private static final String BASE_URL = "http://mobile17.ifmledit.org/api/";
    private BriscolaAPI briscolaAPI;

    private static int turnsPlayed = 0;
    private final String authHeader = "APIKey 0c3828b9-b0d6-45c3-aa3b-a6d324561569",
            roomName = "Group01",
            contentTypePlainText = "text/plain";
    private OpponentPlayedCardCallback opponentPlayedCardCallback = new OpponentPlayedCardCallback(this);
    private PlayCardCallback playCardCallback = new PlayCardCallback(this);
    private StartMatchCallback startMatchCallback = new StartMatchCallback(this);
    private StopMatchCallback stopMatchCallback = new StopMatchCallback(this);


    public OnlineBriscola2PMatchController(Briscola2PMatchFragment matchFragment) {
        super();
        this.matchFragment = matchFragment;
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
//todo: questo link ha un esempio di uso android dell'api
    //todo: http://www.vogella.com/tutorials/Retrofit/article.html
    /*todo: segui questo pattern
    in sintesi consiste in: metti una private BriscolaAPI nell'activity,
    nei listener delle interazioni metti le chiamate all'api e accodale direttamente
    (ESEMPIO: BriscolaAPI.faiUnaCosa(parametri).enqueue(nomeCallbackCheDeveGestireRitornoDllaChiamata)
    cioè lui ISTANZA DELLE CLASSI ANONIME CHE IMPLEMENTANO Callback<> in variabili dell'activity, queste classi fano
    ovverride di onResponse e onFailure ... ce n'è una per ogni tipo di query fattibile
    POI Nell'onCreate metti praticamente il contenuto di start()
     per inizializzare Gson e Retrofit e BriscolaAPI
     */
    //TODO IMPORTANTE: capire come fare a rifare la richiesta se c'è timeout

    /*public void start() {

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
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build();

        BriscolaAPI briscolaAPI = retrofit.create(BriscolaAPI.class);

       // Call<List<Change>> call = gerritAPI.loadChanges("status:open");
       // call.enqueue(this);

    }

    public void startNewMatch(){
        // Call<List<Change>> call = gerritAPI.loadChanges("status:open");
        // call.enqueue(this);
    }

    @Override //todo <- nell'implementazione finale creerai classi separate di Callback, ciascuna implementando questi due metodi, una per ciascun metodo
    public void onResponse(Call<List<Change>> call, Response<List<Change>> response) {
        if(response.isSuccessful()) {
            List<Change> changesList = response.body();
            changesList.forEach(change -> System.out.println(change.subject));
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<List<Change>> call, Throwable t) {
        t.printStackTrace();
    }
*/
    @Override
    public void startNewMatch() {

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
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build();

        briscolaAPI = retrofit.create(BriscolaAPI.class);

        matchFragment.waitingToFindOnlinePlayer();
        briscolaAPI.startMatch(authHeader, roomName).enqueue(startMatchCallback);
        //todo PROBABILMENTE QUI DEVI FARE "nomePathVariable: "+pathVariable <- ma anche no
        // Call<List<Change>> call = gerritAPI.loadChanges("status:open");
        // call.enqueue(this);
    }

    public void postCard(String plainText){
        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), plainText);
        briscolaAPI.playCard(url, authHeader,contentTypePlainText, body).enqueue(playCardCallback);

    }
    public void manageError(String error, String message) {
        matchFragment.onBuildDialog("Error: " + error + ". Message: " + message,
                "Ok",
                null,
                false,
                false
        ).show();
    }

    public void manageStartedMatch(StartedMatchDTO started) {
        url = started.getUrl();
        config = new Briscola2PMinimalMatchConfig(started.getGame(), started.getLastCard(), started.getCards(), started.isYourTurn()); //todo, TI CONVIENE CREARE UNA NUOVA CLASSE CHE GESTISCA CONFIGURAZIONI INCOMPLETE

        matchFragment.foundOnlinePlayer();
        Log.d("TAG", started.getGame() + "|"+started.getLastCard() + "|" +  started.getCards() + "|"+ started.isYourTurn());
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
        AnimatorSet initializeBriscola = matchFragment.getInitializeBriscolaAnimatorSet(new NeapolitanCard(config.getBriscolaString().charAt(0), config.getBriscolaString().charAt(1)));
        AnimatorSet displayCurrentPlayer = matchFragment.displayCurrentPlayer(config.getCurrentPlayer());
        //animators.add(dealFirstHand);
        //animators.add(initializeBriscola);
        matchFragment.showToast("Current player is " + config.getCurrentPlayer());

        initializeBriscola.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (config.getCurrentPlayer() == config.PLAYER1) {
                    //playFirstCard(player1.chooseMove(config)).start(); //todo, questo è un metodo annidato in un metodo chiamato dal callback di OpponentCallback
                    briscolaAPI.getOpponentPlayedCard(url, authHeader).enqueue(opponentPlayedCardCallback);

                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        initializationSequence.playSequentially(displayCurrentPlayer, dealFirstHand, initializeBriscola);
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
        AnimatorSet displayCurrentPlayer = matchFragment.displayCurrentPlayer(config.getCurrentPlayer());
        AnimatorSet playFirstCard = new AnimatorSet();
        playFirstCard.playSequentially(playCard, displayCurrentPlayer);
        playFirstCard.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                if(config.getCurrentPlayer() == config.PLAYER1){
                    //playSecondCard(player1.chooseMove(config)).start(); //todo, sarà nel callback che decidi se giocare first o second card!
                    briscolaAPI.getOpponentPlayedCard(url, authHeader).enqueue(opponentPlayedCardCallback);
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        playFirstCard.start();
    }


    public void manageNextTurnCard(NextTurnCardDTO next){
        if(next.getCard() != null){
            config.setLocalPlayerNextRoundCard(new NeapolitanCard(next.getCard().charAt(0), next.getCard().charAt(1)));
        }
    }

    public void manageOpponentPlayedCard(OpponentCardDTO opponent){
        if(opponent.getOpponent() != null){
            if(config.countCardsOnSurface() == 0)
                playFirstCard(opponent.getOpponent());
            else
                playSecondCard(opponent.getOpponent());
        }

        if(opponent.getCard() != null){
            config.setLocalPlayerNextRoundCard(new NeapolitanCard(opponent.getCard().charAt(0), opponent.getCard().charAt(1)));
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
        Log.d("TAG", "prepeared beautiful clean surfacea animation, lasts: " + cleanSurface.getDuration());
        config.setCurrentPlayer(roundWinner);
        matchFragment.showToast("Current player = " + config.getCurrentPlayer() + ", roundWInner = "+roundWinner);
        AnimatorSet displayCurrentPlayer = matchFragment.displayCurrentPlayer(config.getCurrentPlayer());
        displayCurrentPlayer.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                AnimatorSet closeTurnAnimation;
                if(turnsPlayed == 20){ //if the match is finished, choose winner
                    switch(config.chooseMatchWinner()){
                        case Briscola2PMatchConfig.PLAYER0: matchFragment.displayMatchWinner(Briscola2PMatchConfig.PLAYER0, config.computeScore(Briscola2PMatchConfig.PLAYER0)); break;
                        case Briscola2PMatchConfig.PLAYER1: matchFragment.displayMatchWinner(Briscola2PMatchConfig.PLAYER1, config.computeScore(Briscola2PMatchConfig.PLAYER1)); break;
                        case Briscola2PMatchConfig.DRAW: matchFragment.displayMatchWinner(Briscola2PMatchConfig.DRAW, config.computeScore(Briscola2PMatchConfig.PLAYER0)); break;
                        default: throw new RuntimeException("Error while computing the winner");
                    }
                }else if (!config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the deck is empty, but players have cards in hand,don't do anything
                }else if(!config.isDeckEmpty()){ //if deck is not empty, draw cards new round
                    //boolean lastDraw = false;
                    //if(config.getDeck().size() == 2)
                    //   lastDraw = true;
                    turnsPlayed++; //since the API doesn't give info on the deck, count turns played to know whether lastDraw
                    boolean noDraw = false;
                    boolean lastDraw = false;
                    if(turnsPlayed== 17) //total = 20 turns =20 - 3 without drawing new cards
                        lastDraw = true;
                    if(turnsPlayed== 18 || turnsPlayed == 19)
                        noDraw = true;

                    config.drawCardsNewRound();
                    //cards have been collected from surface, new cards have been drawn
                    if(noDraw){
                        matchFragment.enablePlayer0CardsTouch(config.getHands(),config.getCurrentPlayer());
                    }else {
                        AnimatorSet drawCards = matchFragment.drawCardsNewRound(config.getHands(), config.getCurrentPlayer(), lastDraw);
                        animators.add(drawCards);
                    }
                    CREDO IL PROBLEMA SIA CHE ERA FINITA LA PARTITA! QUINDI NON AVEVA CARTE DA PESCARE!
                    //config.drawCardsNewRound();
                    //cards have been collected from surface, new cards have been drawn
                    config.drawLocalPlayerCard();
                    config.increaseRemotePlayerCardCounter();
                    closeTurnAnimation = matchFragment.drawCardsNewRound(config.getHands(), config.getCurrentPlayer(),false); //TODO, sistemare come calcolare il lastdraw in base al comportamento dell'api del prof
                    closeTurnAnimation.start();
                }

                if(config.getCurrentPlayer() == config.PLAYER1){
                    //animators.add(playFirstCard(player1.chooseMove(config)));
                    briscolaAPI.getOpponentPlayedCard(url, authHeader).enqueue(opponentPlayedCardCallback);
                }


            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        playSecondCard.playSequentially(playCard, cleanSurface,displayCurrentPlayer);
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
}
