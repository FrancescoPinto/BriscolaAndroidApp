package it.ma.polimi.briscola.model.briscola.twoplayers;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;


/**
 * Class representing a Minimal Match Config (i.e. a match configuration that can let the player play a match with only the player's informations (the config is not "omniscient"). It is used when only partial configuration information is known (e.g. playing with the server).
 */
public class Briscola2PMinimalMatchConfig extends AbstractBriscola2PMatchConfig implements Serializable, Briscola2PMatchConfig {

    private NeapolitanCard briscola;
    private Briscola2PHand hand; //local player hand
    private int remotePlayerCardsCounter; //counter, keeps track of the cards the remote player has in hand
    private NeapolitanCard localPlayerNextRoundCard; //holds the value (passed from the server) of the next card to be drawn by the local player
    private String briscolaString;

    /**
     * Gets local player next round card.
     *
     * @return the local player next round card
     */
    public NeapolitanCard getLocalPlayerNextRoundCard() {
        return localPlayerNextRoundCard;
    }

    /**
     * Sets local player next round card.
     *
     * @param localPlayerNextRoundCard the local player next round card
     */
    public void setLocalPlayerNextRoundCard(NeapolitanCard localPlayerNextRoundCard) {
        this.localPlayerNextRoundCard = localPlayerNextRoundCard;
    }

    /**
     * Draw local player card.
     */
    public void drawLocalPlayerCard(){
        if(this.localPlayerNextRoundCard == null)
            return; //due to asynchronism, the match could be closed by the server without initializing the localPlayerNextRoundCard, however the controller will continue (at least for a while) its execution and end up here
        //if the two rows above weren't written, the app would have crashed, because a null card would have been appended to the hand.

        hand.appendCard(localPlayerNextRoundCard);
        this.localPlayerNextRoundCard = null;
    }


    /**
     * Instantiates a new Briscola 2 p minimal match config.
     *
     * @param briscola        the briscola
     * @param localPlayerHand the local player hand
     * @param localPlayerTurn the local player turn
     */
    public Briscola2PMinimalMatchConfig(String briscola, String localPlayerHand, boolean localPlayerTurn){
        this.briscolaString = briscola;
        this.briscolaSuit = String.valueOf(getBriscolaString().charAt(1));
        this.briscola = new NeapolitanCard(briscola.charAt(0), briscola.charAt(1));
        this.hand = new Briscola2PHand(localPlayerHand);
        this.remotePlayerCardsCounter = 3;
        this.currentPlayer = localPlayerTurn?PLAYER0:PLAYER1;
        this.surface = new Briscola2PSurface("");//initialize surface
        this.piles.add(new Briscola2PPile(""));// pile0
        this.piles.add(new Briscola2PPile("")); // pile1

    }

    /**
     * Instantiates a new Briscola 2 p minimal match config.
     *
     * @param config the config
     */
    public Briscola2PMinimalMatchConfig(Briscola2PMinimalMatchConfig config){
        this.briscolaString = config.getBriscolaString();
        this.briscolaSuit = String.valueOf(getBriscolaString().charAt(1));
        this.briscola = config.getBriscola();
        this.hand = config.getLocalPlayerHand();
        this.remotePlayerCardsCounter = config.getRemotePlayerCardsCounter();
        this.currentPlayer = config.getCurrentPlayer();
        this.surface = config.getSurface();//initialize surface
        this.piles.add(config.getPile(PLAYER0));// pile0
        this.piles.add(config.getPile(PLAYER1)); // pile1
    }

    @Override
    public NeapolitanCard getBriscola() {
        return briscola;
    }

    @Override
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

    @Override
    public void playCard(Integer card) {
        if(currentPlayer == PLAYER0) { //should only be called internally (server provides Strings, not integer)
            NeapolitanCard cardToPlay = hand.removeCard(card);
            surface.appendCard(cardToPlay); //la carta del primo giocatore è sempre in 0
        }
    }
    @Override
    public void playCard(String card){
        if(currentPlayer == PLAYER1){ //should only be called as a reaction to a server response (server provides Strings)
            this.remotePlayerCardsCounter--;
            surface.appendCard(new NeapolitanCard(card.charAt(0),card.charAt(1)));
        }
    }
    @Override
    public List<Briscola2PHand> getHands(){
        List<Briscola2PHand> hands = new ArrayList<>();
        hands.add(hand);

        List<NeapolitanCard> cards = new ArrayList<>(); //create a (mock) hand of covered cards
        for(int i = 0; i < remotePlayerCardsCounter; i++)
            cards.add(new NeapolitanCard());
        hands.add(new Briscola2PHand(cards));

        return hands;
    }

    /**
     * Increase remote player card counter.
     */
    public void increaseRemotePlayerCardCounter(){
        remotePlayerCardsCounter++;
    }

    /**
     * Gets remote player cards counter.
     *
     * @return the number of cards currently in remote player hand
     */
    public int getRemotePlayerCardsCounter() {
        return this.remotePlayerCardsCounter;
    }


    /**
     * Infer briscola, if it is present in deck.
     *
     * @return the neapolitan card representing the briscola
     */
    public NeapolitanCard inferBriscolaIfInDeck(){
        if(getNumberTurnsElapsed() < 18)
            return getBriscola();
        else
            return null; //briscola is in players hands
    }

    /**
     * Gets local player hand.
     *
     * @return the local player hand
     */
    public Briscola2PHand getLocalPlayerHand() {
        return hand;
    }

}
