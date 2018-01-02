package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.BriscolaCardPointsAndRankingRules;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

/**
 * Abstract class implementing common methods a Briscola2PMatchConfig should have, and providing some common attributes.
 */

public abstract class AbstractBriscola2PMatchConfig implements Briscola2PMatchConfig, Serializable{

    //error messages
    static final String wrongCurrentPlayer = "Wrong Current Player. Current Player should either be "+ PLAYER0 + " or " + PLAYER1,
                                surfaceNotFilled = "Surface is not completely filled",
                                notFinishedMatch ="Match is not finished, cannot compute choose match winner",
                                inconsistentSurfaceForRound = "The surface is in an inconsistent state, could not evaluate the round winner",
                                wrongPlayerIndex = "Wrong player index, shoudl either be PLAYER0 or PLAYER1 provided by this class",
                                noCurrentPlayerOrBriscolaSpecified = "No current player or briscola properly specified",
                                startWith40CardDeck = "The deck should contain 40 cards to start a match",
                                playersHandsNotInitialized = "Players hands have not been initialized, cards for first round have not been drawn",
                                noCurrentPlayerInitialized = "The currentPlayer has not been initialized";

    Briscola2PSurface surface;
    List<Briscola2PPile> piles = new ArrayList<>();
    Integer currentPlayer;
    String briscolaSuit;

    static final int totPoints = 120; //the total of the points of a deck

    /*public AbstractBriscola2PMatchConfig(){
        this.surface = new Briscola2PSurface("");//initialize surface
        this.piles.add(new Briscola2PPile(""));// pile0
        this.piles.add(new Briscola2PPile("")); // pile1
    }*/

    @Override
    public int countCardsOnSurface(){
        return surface.size();
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void setCurrentPlayer(int currentPlayer) {
        if(currentPlayer == PLAYER0 || currentPlayer == PLAYER1)
            this.currentPlayer = currentPlayer;
        else throw new IllegalArgumentException(wrongCurrentPlayer);
    }

   @Override
    public void toggleCurrentPlayer(){
        if(currentPlayer != null && (currentPlayer == PLAYER1||currentPlayer == PLAYER0))
            this.currentPlayer = (currentPlayer+1)%2; //remember: player0 = 0 and player1 = 1, hence (player0+1)%2 = 1 and (player1 + 1)%2 = 0
        else
            throw new IllegalStateException(noCurrentPlayerInitialized);
    }

   @Override
    public List<NeapolitanCard> clearSurface(int winner){
        if(surface.size() != surface.getMaxNumCardsAllowedInList())
            throw new IllegalStateException(surfaceNotFilled);

        List<NeapolitanCard> cardsOnSurface = surface.clearCardList();
        piles.get(winner).appendAll(cardsOnSurface);
        return cardsOnSurface;
    }

    @Override
    public Briscola2PSurface getSurface() {
        return surface;
    }




    @Override
    public void setSurface(Briscola2PSurface surface) {
        this.surface = surface;
    }

    @Override
    public int chooseRoundWinner(){
        String briscolaSuit = String.valueOf(this.briscolaSuit);
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

    @Override
    public int computeScore(int player){
        List<NeapolitanCard> pile = piles.get(player).getCardList();
        int score = 0;

        for(NeapolitanCard c: pile){
            score += BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber());
        }

        return score;
    }

   @Override
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

    @Override
    public int getNumberTurnsElapsed() {
        int totalPilesSize = piles.get(PLAYER0).size() + piles.get(PLAYER1).size();
        return totalPilesSize/2 + 1; //first turn -> empty piles, second turn -> 2 cards in piles etc.
    }

    @Override
    public void setPile(int i, String pile){
        setPile(i, new Briscola2PPile(pile));
    }

    @Override
    public void setPile(int i, Briscola2PPile pile){
        if(i == PLAYER0 || i == PLAYER1)
            this.piles.add(i,pile);
        else throw new IllegalArgumentException(wrongPlayerIndex);

    }

    @Override
    public Briscola2PPile getPile(int i) {
        return piles.get(i);
    }



    @Override
    public String getBriscolaSuit() {
        return briscolaSuit;
    }


    @Override
    public void setBriscolaSuit(String briscolaSuit) {

        this.briscolaSuit = NeapolitanCardSuit.getCardSuit(briscolaSuit).getSuit(); //the invocation of getCardSuit performs the validity check of the input argument
    }
}
