package it.ma.polimi.briscola.model.briscola.twoplayers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.BriscolaCardPointsAndRankingRules;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;
import it.ma.polimi.briscola.model.deck.NeapolitanDeck;
import it.ma.polimi.briscola.rest.client.dto.StartedMatchDTO;

/**
 * Created by utente on 10/12/17.
 */

public class Briscola2PMinimalMatchConfig {
    /**
     * The constant PLAYER0 is an index used to represent the Local Player (i.e. the human player on the local device)
     */
    public static final int PLAYER0 = 0, /**
     * The Player 1 is an index used to represent the "other player" (i.e. either a local/remote AI or another remote human player)
     */
    PLAYER1 = 1, /**
     * The Draw, index used to represent a Draw in the match
     */
    DRAW = -1;

    private Integer currentPlayer;
    private String briscolaString;
    private NeapolitanCard briscola;
    private boolean deckIsEmpty;
    private Briscola2PSurface surface;
    private Briscola2PHand hand;
    private List<Briscola2PPile> piles = new ArrayList<>();
    private int remotePlayerCardsCounter;
    private boolean isLocalPlayerTurn;
    private String matchId;
    private NeapolitanCard localPlayerNextRoundCard;

    public NeapolitanCard getLocalPlayerNextRoundCard() {
        return localPlayerNextRoundCard;
    }

    public void setLocalPlayerNextRoundCard(NeapolitanCard localPlayerNextRoundCard) {
        this.localPlayerNextRoundCard = localPlayerNextRoundCard;
    }

    public void drawLocalPlayerCard(){
        hand.appendCard(localPlayerNextRoundCard);
        this.localPlayerNextRoundCard = null;
    }
    private static final int totPoints = 120; //the total of the points of a deck

    //error messages
    private static final String wrongCurrentPlayer = "Wrong Current Player. Current Player should either be "+ PLAYER0 + " or " + PLAYER1,
            noCurrentPlayerInitialized = "The currentPlayer has not been initialized",
            surfaceNotFilled = "Surface is not completely filled",
            notFinishedMatch ="Match is not finished, cannot compute choose match winner",
            inconsistentSurfaceForRound = "The surface is in an inconsistent state, could not evaluate the round winner";


    public Briscola2PMinimalMatchConfig(String matchId, String briscola, String localPlayerHand, boolean localPlayerTurn){
        this.matchId = matchId;
        this.briscolaString = briscola;
        this.hand = new Briscola2PHand(localPlayerHand);
        this.isLocalPlayerTurn = localPlayerTurn;
        this.remotePlayerCardsCounter = 3;
        this.deckIsEmpty = false;
        this.currentPlayer = localPlayerTurn?PLAYER0:PLAYER1;

        this.surface = new Briscola2PSurface("");//initialize surface
        this.piles.add(new Briscola2PPile(""));// pile0
        this.piles.add(new Briscola2PPile("")); // pile1

    }


    /**
     * Gets current player.
     *
     * @return Index of the current player, is either the public final int PLAYER0 or PLAYER1 provided by this class
     */

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets current player.
     *
     * @param currentPlayer the current player, should be either the public final int PLAYER0 or PLAYER1 provided by this class
     * @throws IllegalArgumentException if invalid player is specified.
     */
    public void setCurrentPlayer(int currentPlayer) {
        if(currentPlayer == PLAYER0 || currentPlayer == PLAYER1)
            this.currentPlayer = currentPlayer;
        else throw new IllegalArgumentException(wrongCurrentPlayer);
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

        this.briscolaString = NeapolitanCardSuit.getCardSuit(briscolaString).getSuit(); //the invocation of getCardSuit performs the validity check of the input argument
    }

    /**
     * Gets surface.
     *
     * @return the surface
     */
    public Briscola2PSurface getSurface() {
        return surface;
    }

    /**
     * Sets surface.
     *
     * @param surface the surface
     */
    public void setSurface(Briscola2PSurface surface) {
        this.surface = surface;
    }



    /**
     * Gets pile of the i-th player
     *
     * @param i the player index, should be the public final int PLAYER0 or PLAYER1 provided by this class
     * @return the pile
     */
    public Briscola2PPile getPile(int i) {
        return piles.get(i);
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


    public void playCard(int card) {
        if(currentPlayer == PLAYER0) {
            NeapolitanCard cardToPlay = hand.removeCard(card);
            surface.appendCard(cardToPlay); //la carta del primo giocatore è sempre in 0
        }
    }

    public void playCard(String card){
        if(currentPlayer == PLAYER1){
            remotePlayerCardsCounter--;
            surface.appendCard(new NeapolitanCard(card.charAt(0),card.charAt(1))); //la carta del primo giocatore è sempre in 0
        }
    }
    public Briscola2PHand getLocalPlayerHand() {
        return hand;
    }


    public int  chooseMatchWinner(){
        int score0 = computeScore(PLAYER0),
                score1 = computeScore(PLAYER1);

        if(score0 + score1 != totPoints)
            throw new IllegalStateException(notFinishedMatch);

        if(score0 > score1)
            return PLAYER0;
        else if (score0 < score1)
            return PLAYER1;
        else
            return DRAW;
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
    public int chooseRoundWinner(){
        String briscolaSuit = String.valueOf(briscolaString.charAt(1));
        if(surface.size() != 2) //if round is not finished, throw exception
            throw new IllegalStateException(surfaceNotFilled);

        //get data
        NeapolitanCard cardFirstPlayer = surface.getCard(surface.FIRSTCARD),
                cardCurrentPlayer = surface.getCard(surface.SECONDCARD); //current player is the second player (since when the second card has been played, the current player is the second)

        String suitFirstPlayer = cardFirstPlayer.getCardSuit(),
                suitCurrentPlayer = cardCurrentPlayer.getCardSuit();

        String numberFirstPlayer = cardFirstPlayer.getCardNumber(),
                numberCurrentPlayer = cardCurrentPlayer.getCardNumber();

        int rankFirstPlayer = BriscolaCardPointsAndRankingRules.getRank(numberFirstPlayer),
                rankCurrentPlayer = BriscolaCardPointsAndRankingRules.getRank(numberCurrentPlayer);

        //reason about data gathered
        if(!suitFirstPlayer.equals(suitCurrentPlayer)){ //if different suit

            //if there is a trump, the trump wins
            if(suitFirstPlayer.equals(briscolaSuit)){
                return (currentPlayer+1)%2; //the first player wins (remember: when the second card has been played, the current player is the second player)
            }else if(suitCurrentPlayer.equals(briscolaSuit)){
                return currentPlayer;
            }

            //if no trump, first player wins
            return (currentPlayer+1)%2; //the first player wins (remember: when the second card has been played, the current player is the second player)

        }else if ((suitFirstPlayer.equals(briscolaSuit)) &&  (briscolaSuit.equals(suitCurrentPlayer))) { //both trumps suit

            //REMARK: AFTER UPDATE OF SPECIFICATIONS, THIS BRANCH IS REDUNDANT AND COULD BE COLLAPSED WITH THE ONE BELOW, HOWEVER IS KEPT TO BETTER EMPHASIZE THE GAME RULES (previously, this branch chose the winner based on point values, not rank)
            if(rankCurrentPlayer < rankFirstPlayer)
                return currentPlayer;
            else
                return (currentPlayer+1)%2; //the first player wins (remember: when the second card has been played, the current player is the second player)

        } else if (suitFirstPlayer.equals(suitCurrentPlayer)){ // there are no trumps, but both cards have same suit

            if(rankFirstPlayer > rankCurrentPlayer)
                return currentPlayer;
            else
                return (currentPlayer+1)%2; //the first player wins (remember: when the second card has been played, the current player is the second player)
        }

        throw new IllegalStateException(inconsistentSurfaceForRound);
    }


    public int computeScore(int player){
        List<NeapolitanCard> pile = piles.get(player).getCardList();
        int score = 0;

        for(NeapolitanCard c: pile){
            score += BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber());
        }

        return score;
    }


    public int countCardsOnSurface(){
        return surface.size();
    }


    public boolean arePlayersHandsEmpty(){
        return hand.isEmpty() && remotePlayerCardsCounter == 0;
    }

    public boolean isDeckEmpty(){
        return deckIsEmpty;
    }

    public void setDeckIsEmpty(boolean deckIsEmpty){
        this.deckIsEmpty = deckIsEmpty;
    }


    /**
     * Checks if two configurations are in the same state.
     *
     * @param config the configuration to be used for the comparison
     * @return true if the configurations are in the same state, false otherwise
     */
   /* public boolean equalTo(Briscola2PMatchConfig config){
        return  this.currentPlayer == config.getCurrentPlayer()&&
                this.briscolaString.equals(config.getBriscolaSuit()) &&
                this.deck.equalTo(config.getDeck()) &&
                this.surface.equalTo(config.getSurface()) &&
                this.hands.get(PLAYER0).equalTo(config.getHand(PLAYER0)) &&
                this.hands.get(PLAYER1).equalTo(config.getHand(PLAYER1)) &&
                this.piles.get(PLAYER0).equalTo(config.getPile(PLAYER0)) &&
                this.piles.get(PLAYER1).equalTo(config.getPile(PLAYER1));

    }*/


    /**
     * Provides the string representation of the string.
     *
     * @return String representing the configuration (format as specified in the slides)
     */
   /* @Override
    public String toString(){

        return ""+ currentPlayer + briscolaString + deck + "."+ surface + "."+hands.get(PLAYER0)+"."+hands.get(PLAYER1)+"."+piles.get(PLAYER0)+"."+piles.get(PLAYER1);

    }*/
}
