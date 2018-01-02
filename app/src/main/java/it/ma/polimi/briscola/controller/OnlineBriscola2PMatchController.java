package it.ma.polimi.briscola.controller;

import android.animation.Animator;
import android.content.Context;

import java.util.List;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PFullMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMinimalMatchConfig;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.rest.client.RESTClient;
import it.ma.polimi.briscola.view.fragments.Briscola2PMatchFragment;

/**
 * Class implementing a controller for online matches human vs remote
 */
public class OnlineBriscola2PMatchController extends AbstractBriscola2PController implements ControllerWithServerResponseManager {

    //whether a fatal error allready occurred (prevents multiple triggering of error-management logic in case multiple errors occur)
    private boolean allreadyOneFatalError = false;
    //to know whether the match started, useful to interrupt the match in case one of the users stopped waiting in the matching fase
    private boolean matchStarted = false;
    //the rest client, encapsulating all the calls to the server
    private RESTClient restClient = new RESTClient(this);

    /**
     * Instantiates a new Online briscola 2 p match controller given the match fragment
     *
     * @param matchFragment the match fragment
     */
    public OnlineBriscola2PMatchController(Briscola2PMatchFragment matchFragment) {
        super();
        this.matchFragment = matchFragment;
    }

    /**
     * Instantiates a new Online briscola 2 p match controller from a given configuration (wrapped in a controller)
     *
     * @param controller    the controller
     * @param matchFragment the match fragment
     */
    public OnlineBriscola2PMatchController(OnlineBriscola2PMatchController controller, Briscola2PMatchFragment matchFragment){
        super();
        this.matchFragment = matchFragment;
        this.config = (Briscola2PMinimalMatchConfig) controller.getConfig();
    }

    @Override
    public void manageNextTurnCard(String card){
        //if the next turn card was specified in the server message, set it to be drawn the next turn
        if(card != null){
            ((Briscola2PMinimalMatchConfig) config).setLocalPlayerNextRoundCard(new NeapolitanCard(card.charAt(0),card.charAt(1)));
        }
    }

    @Override
    public void manageOpponentPlayedCard(String opponent, String card){
        //if card played by the opponent is specified in the server message, play it
        if(opponent != null){
            if(config.countCardsOnSurface() == 0)
                playFirstCard(opponent);
            else
                playSecondCard(opponent);
        }

        //if the next turn card was specified in the server message, set it to be drawn the next turn
        if(card != null){
            ((Briscola2PMinimalMatchConfig) config).setLocalPlayerNextRoundCard(new NeapolitanCard(card.charAt(0), card.charAt(1)));
        }
    }

    @Override
    public Briscola2PMatchFragment getMatchFragment(){
        return matchFragment;
    }

    @Override
    public void startNewMatch() {
        //initialize the client
        restClient.initializeClient();
        //show the user that he/she needs to wait for matching an opponent
        matchFragment.waitingToFindOnlinePlayer();
        //make a startMatch request
        restClient.startMatchCall(matchFragment.getActivity());
    }


    @Override
    public void manageError(String error, String message) {
      if(!allreadyOneFatalError) { //since the controller doesn't immediately stop, other callbacks might cause other manageError calls ... this prevents them to trigger error management logic again
          matchFragment.manageError(error, message);
          allreadyOneFatalError = true;
      }else{
          //do nothing
      }
    }

    @Override
    public void manageStartedMatch(String game, String lastCard,String cards,String yourTurn, String url) {
        //initialize the variables and config with received data
        restClient.setUrl(url);
        this.matchStarted = true;
        config = new Briscola2PMinimalMatchConfig(url, lastCard, cards, Boolean.valueOf(yourTurn));
        //tell the player the match opponent has been found
        matchFragment.foundOnlinePlayer();
        //start the initialization animation sequence
        startInitializationAnimationSequence();
    }

    @Override
    public void playFirstCard(int firstCard) {
        //the Integer firstCard is ALLWAYS generated internally, hence it is associated with the local player
        //make a server request to play a card and generate and start the needed animations
        NeapolitanCard card = ((Briscola2PMinimalMatchConfig) config).getLocalPlayerHand().getCard(firstCard);
        restClient.postCard(card.toString());
        generatePlayFirstCard(firstCard).start();
    }

    /**
     * Play first card. It is a String since it is received in a response from the server
     *
     * @param firstCard the first card
     */
    public void playFirstCard(String firstCard) {
        //play the first card, start associated animations
        generatePlayFirstCard(firstCard).start();
    }


    @Override
    public void playSecondCard(int secondCard) {
        //the Integer secondCard is ALLWAYS generated internally, hence it is associated with the local player
        //make a server request to play a card and generate and start the needed animations
        NeapolitanCard card = ((Briscola2PMinimalMatchConfig) config).getLocalPlayerHand().getCard(secondCard);
        restClient.postCard(card.toString());
        generateAndRunPlaySecondCard(secondCard);
    }

    /**
     * Play second card.It is a String since it is received in a response from the server
     *
     * @param secondCard the second card
     */
    public void playSecondCard(String secondCard){
        //play the second card, start associated animations
        generateAndRunPlaySecondCard(secondCard);
    }


    @Override
    public int getHandSize(int playerIndex) {
        if(playerIndex == Briscola2PMinimalMatchConfig.PLAYER0)
            return ((Briscola2PMinimalMatchConfig) config).getLocalPlayerHand().size();
        else
            return ((Briscola2PMinimalMatchConfig) config).getRemotePlayerCardsCounter();
    }

    @Override
    public void forceMatchEnd(Context context){
        //inform the server
        restClient.forceMatchEnd(context);
    }

    @Override
    public void forceMatchEnd(Context context, String url){
        restClient.setUrl(url);
        forceMatchEnd(context);
    }

    @Override
    public void resumeMatch(){

        //reload all the widgets in the interface
        matchFragment.loadPiles(config.getPile(Briscola2PMatchConfig.PLAYER0).isEmpty(),config.getPile(Briscola2PFullMatchConfig.PLAYER1).isEmpty());
        matchFragment.loadSurface(config.getSurface().getCardList()); //questi li si può fare subito dato che sono molto semplici
        matchFragment.loadHands(config.getHands());
        matchFragment.loadBriscolaIfNeeded(((Briscola2PMinimalMatchConfig)config).inferBriscolaIfInDeck());
        matchFragment.loadCurrentPlayer(config.getCurrentPlayer());

        //restClient.setUrl(((Briscola2PMinimalMatchConfig) config).getMatchURL());
        //reinitialize the client
        // restClient.initializeClient();
    }

    /**
     * Whether the match is started.
     *
     * @return true if match is started (received the server response to a start request), false otherwise
     */
    public boolean matchIsStarted(){
        return matchStarted;
    }

    @Override
    public void callPlayer1(List<Animator> animators){
        restClient.opponentPlayedCardCall();
    }

    @Override
    public void stopMatch(){restClient.stopMatchCall(matchFragment.getActivity());
    }

    @Override
    public Animator.AnimatorListener getPlayer1Listener(){
        return new RemotePlayer1AnimationListener((Briscola2PMinimalMatchConfig) config, this);
    }

    /**
     * Stop waiting for a matching opponent.
     *
     * @param stop True if should stop waiting, false otherwise
     */
    public void stopWaiting(boolean stop){
        restClient.stopWaiting(stop);
    }

    /**
     * Get rest client.
     *
     * @return the rest client
     */
    public RESTClient getRestClient(){
        return restClient;
    }

}

/**
 * The Player 1 animation listener used to let the remote player play the game.
 */
//helper class
class RemotePlayer1AnimationListener implements Animator.AnimatorListener {

    /**
     * The Config.
     */
    Briscola2PMinimalMatchConfig config;
    /**
     * The Controller.
     */
    OnlineBriscola2PMatchController controller;

    /**
     * Instantiates a new Remote player 1 animation listener.
     *
     * @param config     the config
     * @param controller the controller
     */
    RemotePlayer1AnimationListener(Briscola2PMinimalMatchConfig config, OnlineBriscola2PMatchController controller){
        this.config = config;
        this.controller = controller;
    }

    @Override
    public void onAnimationStart(Animator animator) {}
    @Override
    public void onAnimationEnd(Animator animator) {
        if (config.getCurrentPlayer() == config.PLAYER1) {
            controller.getRestClient().opponentPlayedCardCall(); //ask for opponent card
        }
    }
    @Override
    public void onAnimationCancel(Animator animator) {}
    @Override
    public void onAnimationRepeat(Animator animator) {}

}