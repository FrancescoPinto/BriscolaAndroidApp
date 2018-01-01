package it.ma.polimi.briscola.model.briscola.twoplayers;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;


/**
 * Created by utente on 10/12/17.
 */

public class Briscola2PMinimalMatchConfig extends AbstractBriscola2PMatchConfig implements Serializable, Briscola2PMatchConfig {

    private NeapolitanCard briscola;
    private boolean deckIsEmpty;
    private Briscola2PHand hand;
    private int remotePlayerCardsCounter;
    private String matchURL;
    private NeapolitanCard localPlayerNextRoundCard;
    private String briscolaString;

    public NeapolitanCard getLocalPlayerNextRoundCard() {
        return localPlayerNextRoundCard;
    }

    public void setLocalPlayerNextRoundCard(NeapolitanCard localPlayerNextRoundCard) {
        this.localPlayerNextRoundCard = localPlayerNextRoundCard;
    }

    public void drawLocalPlayerCard(){
        if(this.localPlayerNextRoundCard == null)
            return; //due to asynchronism, the match could be closed by the server without initializing the localPlayerNextRoundCard, however the controller will continue (at least for a while) its execution and end up here
        //if the two rows above weren't written, the app would have crashed, because a null card would have been appended to the hand.

        hand.appendCard(localPlayerNextRoundCard);
        this.localPlayerNextRoundCard = null;
    }


    public Briscola2PMinimalMatchConfig(String matchURL, String briscola, String localPlayerHand, boolean localPlayerTurn){
        this.matchURL = matchURL;
        this.briscolaString = briscola;
        this.briscolaSuit = String.valueOf(getBriscolaString().charAt(1));
        this.briscola = new NeapolitanCard(briscola.charAt(0), briscola.charAt(1));
        this.hand = new Briscola2PHand(localPlayerHand);
        this.remotePlayerCardsCounter = 3;
        this.deckIsEmpty = false;
        this.currentPlayer = localPlayerTurn?PLAYER0:PLAYER1;

        this.surface = new Briscola2PSurface("");//initialize surface
        this.piles.add(new Briscola2PPile(""));// pile0
        this.piles.add(new Briscola2PPile("")); // pile1

    }

    public Briscola2PMinimalMatchConfig(Briscola2PMinimalMatchConfig config){
        this.matchURL = config.matchURL;
        this.briscolaString = config.getBriscolaString();
        this.briscolaSuit = String.valueOf(getBriscolaString().charAt(1));
        this.briscola = config.getBriscola();
        this.hand = config.getLocalPlayerHand();
        this.remotePlayerCardsCounter = config.getRemotePlayerCardsCounter();
        this.deckIsEmpty = config.isDeckEmpty();
        this.currentPlayer = config.getCurrentPlayer();
        this.surface = config.getSurface();//initialize surface
        this.piles.add(config.getPile(PLAYER0));// pile0
        this.piles.add(config.getPile(PLAYER1)); // pile1
    }

    public String getMatchURL() {
        return matchURL;
    }

    public void setMatchURL(String matchURL) {
        this.matchURL = matchURL;
    }

    public NeapolitanCard getBriscola() {
        return briscola;
    }

    public List<NeapolitanCard> drawCardsNewRound(){
        drawLocalPlayerCard();
        increaseRemotePlayerCardCounter();
        return null;
    }

    /**
     * Gets briscola suit.
     *
     * @return the briscola suit, among NeapolitanCardSuit enum values
     */
    public String getBriscolaString() {
        return briscolaString;
    }

    /**
     * Sets briscola suit.
     *
     * @param briscolaString the briscola suit, should be among NeapolitanCardSuit enum values
     */
    public void setBriscolaString(String briscolaString) {

        this.briscolaString = new NeapolitanCard(briscolaString.charAt(0),briscolaString.charAt(1)).toString();
    }

    //---------------------------------------------------------------
    //Convenience methods, thought to make the life easier for game controllers
    //---------------------------------------------------------------


    public void toggleCurrentPlayer(){
        if(currentPlayer != null && (currentPlayer == PLAYER1||currentPlayer == PLAYER0))
            this.currentPlayer = (currentPlayer+1)%2; //remember: player0 = 0 and player1 = 1, hence (player0+1)%2 = 1 and (player1 + 1)%2 = 0
        else
            throw new IllegalStateException(noCurrentPlayerInitialized);
    }


    public List<NeapolitanCard> clearSurface(int winner){
        if(surface.size() != surface.getMaxNumCardsAllowedInList())
            throw new IllegalStateException(surfaceNotFilled);

        List<NeapolitanCard> cardsOnSurface = surface.clearCardList();
        piles.get(winner).appendAll(cardsOnSurface);
        Log.d("TAG", "Cleared Cards in configuration");
        return cardsOnSurface;
    }


    public void playCard(Integer card) {
        if(currentPlayer == PLAYER0) {
            NeapolitanCard cardToPlay = hand.removeCard(card);
            surface.appendCard(cardToPlay); //la carta del primo giocatore è sempre in 0
        }
    }

    public void playCard(String card){
        if(currentPlayer == PLAYER1){
            this.remotePlayerCardsCounter--;
            surface.appendCard(new NeapolitanCard(card.charAt(0),card.charAt(1))); //la carta del primo giocatore è sempre in 0
        }
    }
    public Briscola2PHand getLocalPlayerHand() {
        return hand;
    }

    public List<Briscola2PHand> getHands(){
        List<Briscola2PHand> hands = new ArrayList<>();
        hands.add(hand);
        List<NeapolitanCard> cards = new ArrayList<>(); //hand of covered cards
        for(int i = 0; i < remotePlayerCardsCounter; i++)
            cards.add(new NeapolitanCard());
        hands.add(new Briscola2PHand(cards));
        return hands;
    }

    public void increaseRemotePlayerCardCounter(){
        remotePlayerCardsCounter++;
    }

    public int getRemotePlayerCardsCounter() {
        return this.remotePlayerCardsCounter;
    }



    public boolean isDeckEmpty(){
        return deckIsEmpty;
    }


}
