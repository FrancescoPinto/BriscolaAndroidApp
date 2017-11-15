package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import it.ma.polimi.briscola.model.briscola.BriscolaCardPointsAndRankingRules;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;
import it.ma.polimi.briscola.model.deck.NeapolitanDeck;
import it.ma.polimi.briscola.model.deck.UniformProbabilityShuffler;

/**
 * Class that represents a Briscola 2 Players Match Configuration and provides methods to manipulate it. This class provides basic and complex (based on the game rules) operations that can be performed on the configuration, however DOES NOT HANDLE THE MATCH EXECUTION FLOW. The execution flow is handled by another class (a match controller). The methods provided by this class simplify the management of the execution flow, implementing the allowed configuration changes. (If client code needs to violate them, it can directly modify the configuration using setters and getters; NOT RECOMMENDED)
 *
 * @author Francesco Pinto
 */
public class Briscola2PMatchConfig {

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

    private int currentPlayer;
    private String briscolaSuit;
    private NeapolitanDeck deck;
    private Briscola2PSurface surface;
    private List<Briscola2PHand> hands = new ArrayList<>();
    private List<Briscola2PPile> piles = new ArrayList<>();

    /**
     * Instantiates a new empty Briscola 2 Players Match Configuration. Before the configuration can be used, it must be brought programmatically to a consistent state by using the class' initialization methods.
     */
    public Briscola2PMatchConfig(){}

    /**
     * Instantiates a new Briscola 2 p match config from its string representation.
     *
     * @param configuration String representation of the match configuration (format as specified in the slides)
     */
    public Briscola2PMatchConfig(String configuration){
        //Parse the configuration string


        String[] tokens = configuration.split("\\.",6); //get the tokens

        String deck = "";//initialize the deck
        if(tokens[0].length() > 2) //there's more than current player and briscola suit in the first token
            deck = tokens[0].substring(2);
        NeapolitanDeck temp = new NeapolitanDeck(deck); //deck and briscola

        if(tokens[0].length() >= 2) { //initialize the briscola and the currentPlayer
            this.currentPlayer = Integer.valueOf(""+tokens[0].charAt(0)); //first char is current player
            this.briscolaSuit = NeapolitanCardSuit.getCardSuit(""+tokens[0].charAt(1)).getSuit(); //uses getCardSuit in order to make the check that the suit value is valid
        } else
            throw new IllegalArgumentException("No current player or briscola properly specified");

        this.deck = temp;
        this.surface = new Briscola2PSurface(tokens[1]);//initialize surface
        this.hands.add(new Briscola2PHand(tokens[2])); //hands0
        this.hands.add(new Briscola2PHand(tokens[3])); //hands1
        this.piles.add(new Briscola2PPile(tokens[4]));// pile0
        this.piles.add(new Briscola2PPile(tokens[5])); // pile1
    }


    /**
     * Instantiates a new Briscola 2 p match config.
     *
     * @param currentPlayer the current player
     * @param briscolaSuit  the briscola suit
     * @param deck          the deck
     * @param surface       the surface
     * @param hand0         the hand 0
     * @param hand1         the hand 1
     * @param pile0         the pile 0
     * @param pile1         the pile 1
     */
    public Briscola2PMatchConfig(int currentPlayer, String briscolaSuit, NeapolitanDeck deck, Briscola2PSurface surface, Briscola2PHand hand0, Briscola2PHand hand1, Briscola2PPile pile0, Briscola2PPile pile1) {
        this.currentPlayer = currentPlayer;
        this.briscolaSuit = briscolaSuit;
        this.deck = deck;
        this.surface = surface;
        this.hands.add(hand0);
        this.hands.add(hand1);
        this.piles.add(pile0);
        this.piles.add(pile1);

    }

    /**
     * Instantiates a new Briscola 2 p match config.
     *
     * @param config the config
     */
    public Briscola2PMatchConfig(Briscola2PMatchConfig config){
        this.currentPlayer = config.getCurrentPlayer();
        this.briscolaSuit = config.getBriscolaSuit();
        this.deck = config.getDeck();
        this.surface = config.getSurface();
        this.hands.add(config.getHand(PLAYER0));
        this.hands.add(config.getHand(PLAYER1));
        this.piles.add(config.getPile(PLAYER0));
        this.piles.add(config.getPile(PLAYER1));
    }

    /**
     * Set hand of the i-th player from String
     *
     * @param i    the player's index, should be the public final int PLAYER0 or PLAYER1 provided by this class
     * @param hand the hand
     */
    public void setHand(int i, String hand){
        this.hands.add(i,new Briscola2PHand(hand));
    }

    /**
     * Set hand of the i-th player from Briscola2PHand
     *
     * @param i    the player's index, should be the public final int PLAYER0 or PLAYER1 provided by this class
     * @param hand the hand
     */
    public void setHand(int i, Briscola2PHand hand){
        this.hands.add(i,hand);
    }

    /**
     * Set pile of the i-th player from String
     *
     * @param i    the player's index, should be the public final int PLAYER0 or PLAYER1 provided by this class
     * @param pile the pile
     */
    public void setPile(int i, String pile){
        this.piles.add(i,new Briscola2PPile(pile));
    }

    /**
     * Set pile of the i-th player from Briscola2PPile
     *
     * @param i    the player's index, should be the public final int PLAYER0 or PLAYER1 provided by this class
     * @param pile the pile
     */
    public void setPile(int i, Briscola2PPile pile){
        this.piles.add(i,pile);
    }

    /**
     * Gets current player.
     *
     * @return the current player
     */

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets current player.
     *
     * @param currentPlayer the current player, should be the public final int PLAYER0 or PLAYER1 provided by this class
     * @throws IllegalArgumentException if invalid player is specified.
     */
    public void setCurrentPlayer(int currentPlayer) {
        if(currentPlayer == PLAYER0 || currentPlayer == PLAYER1)
            this.currentPlayer = currentPlayer;
        else throw new IllegalArgumentException("Wrong Current Player. Current Player should either be "+ PLAYER0 + " or " + PLAYER1);
    }

    /**
     * Gets briscola suit.
     *
     * @return the briscola suit, among NeapolitanCardSuit enum values
     */
    public String getBriscolaSuit() {
        return briscolaSuit;
    }

    /**
     * Sets briscola suit.
     *
     * @param briscolaSuit the briscola suit, should be among NeapolitanCardSuit enum values
     */
    public void setBriscolaSuit(String briscolaSuit) {

        this.briscolaSuit = NeapolitanCardSuit.getCardSuit(briscolaSuit).getSuit(); //the invocation of getCardSuit performs the validity check of the input argument
    }

    /**
     * Gets deck.
     *
     * @return the deck
     */
    public NeapolitanDeck getDeck() {
        return deck;
    }

    /**
     * Sets deck.
     *
     * @param deck the deck
     */
    public void setDeck(NeapolitanDeck deck) {
        this.deck = deck;
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
     * Gets hand of the i-th player.
     *
     * @param i the player index, should be the public final int PLAYER0 or PLAYER1 provided by this class
     * @return the hand
     */
    public Briscola2PHand getHand(int i) {
        return hands.get(i);
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


    /**
     * Initialize new deck. Creates a neapolitan deck and shuffles it.
     */
    public void initializeNewDeck(){
        NeapolitanDeck deck = new NeapolitanDeck();
        deck.shuffleDeck(new UniformProbabilityShuffler());
        this.deck = deck;
    }

    /**
     * Initialize first player. Initialization is made at random (coin toss).
     */
    public void initializeFirstPlayer(){
        int coinTossResult = ThreadLocalRandom.current().nextInt(0, 2); //coin toss, Bernoulli random variable
        if(coinTossResult == 0){
            currentPlayer = PLAYER0;
        }else{
            currentPlayer = PLAYER1;
        }
    }

    /**
     * Initialize players hands. To be called after initializeNewDeck and initializeFirstPlayer. Alternates a card draw from the deck for first and second player of the first round. Each player draws 3 cards.
     */
    public void initializePlayersHands(){
        List<NeapolitanCard> firstPlayerHand = new ArrayList<>();
        List<NeapolitanCard> secondPlayerHand = new ArrayList<>();

        for(int i = 0; i < 3; i++) {

            firstPlayerHand.add(deck.drawCardFromTop());  // the first player (current player) draws a card
            secondPlayerHand.add(deck.drawCardFromTop()); //then the second player

        }

        hands.add(new Briscola2PHand(currentPlayer == PLAYER0?firstPlayerHand:secondPlayerHand)); //the currentPlayer is the first to draw a card
        hands.add(new Briscola2PHand(currentPlayer == PLAYER0?secondPlayerHand:firstPlayerHand));

    }

    /**
     * Initialize briscola. To be called after initializePlayersHands. Draws the briscola from the top of the deck, and puts it to the bottom.
     */
    public void initializeBriscola(){
        NeapolitanCard briscola = deck.drawCardFromTop();
        deck.putCardToBottom(briscola);
        this.briscolaSuit = briscola.getCardSuit();
    }


    /**
     * Toggle current player.
     */
    public void toggleCurrentPlayer(){
        this.currentPlayer = (currentPlayer+1)%2; //remember: player0 = 0 and player1 = 1, hence (player0+1)%2 = 1 and (player1 + 1)%2 = 0
    }

    /**
     * Clear surface. Clears the surface and appends the cards on the surface to the pile of the winner.
     *
     * @param winner the winner
     */
    public void clearSurface(int winner){
        if(surface.size() != surface.getMaxNumCardsAllowedInList())
            throw new IllegalStateException();

        List<NeapolitanCard> cardsOnSurface = surface.clearCardList();
        piles.get(winner).appendAll(cardsOnSurface);

    }

    /**
     * Play card. Removes the i-th card from the hand of the current player and puts it onto the surface.
     *
     * @param i the index of the card to be played (should be either CARD1,CARD2 or CARD3 of the public final ints provided by the Briscola2PHand class)
     */
    public void playCard(int i) {
        NeapolitanCard cardToPlay = hands.get(currentPlayer).removeCard(i);
        surface.appendCard(cardToPlay); //la carta del primo giocatore è sempre in 0
    }

    /**
     * Draw cards for the new round. Should be called at the beginning of the round, after the current player has been set. If the deck is empty, the configuration is unaffected.
     */
    public void drawCardsNewRound(){
        if(deck.isEmpty()) //if deck is empty, don't do anything.
            return;

        hands.get(currentPlayer).appendCard(deck.drawCardFromTop());
        hands.get((currentPlayer+1)%2).appendCard(deck.drawCardFromTop());

    }

    /**
     * Choose match winner, should be called after the end of the whole match.
     *
     * @return the int representing the player who won the match (PLAYER0,PLAYER1 or DRAW in case of draw)
     */
    public int chooseMatchWinner(){
        int score0 = computeScore(PLAYER0),
                score1 = computeScore(PLAYER1);

        if(score0 > score1)
            return PLAYER0;
        else if (score0 < score1)
            return PLAYER1;
        else
            return DRAW;
    }


    /**
     * Choose round winner int, should be called at the end of the round, after clearSurface has been called.
     *
     * @return the int representing the round winner (either PLAYER0 or PLAYER1)
     */
    public int chooseRoundWinner(){
        if(surface.size() != 2) //if round is not finished, throw exception
            throw new IllegalStateException();

        NeapolitanCard cardFirstPlayer = surface.getCard(surface.FIRSTCARD),
                cardCurrentPlayer = surface.getCard(surface.SECONDCARD); //current player is the second player (since when the second card has been played, the current player is the second)

        String suitFirstPlayer = cardFirstPlayer.getCardSuit(),
                suitCurrentPlayer = cardCurrentPlayer.getCardSuit();

        String numberFirstPlayer = cardFirstPlayer.getCardNumber(),
                numberCurrentPlayer = cardCurrentPlayer.getCardNumber();

        int rankFirstPlayer = BriscolaCardPointsAndRankingRules.getRank(numberFirstPlayer),
                rankCurrentPlayer = BriscolaCardPointsAndRankingRules.getRank(numberCurrentPlayer);

        if(suitFirstPlayer != suitCurrentPlayer){ //if different suit

                //if there is a trump, the trump wins
                if(suitFirstPlayer.equals(briscolaSuit)){
                    return (currentPlayer+1)%2; //the first player wins (remember: when the second card has been played, the current player is the second player)
                }else if(suitCurrentPlayer.equals(briscolaSuit)){
                    return currentPlayer;
                }

                //if no trump, first player wins
                return (currentPlayer+1)%2; //the first player wins

        }else if ((suitFirstPlayer.equals(briscolaSuit)) &&  (briscolaSuit.equals(suitCurrentPlayer))) { //both trumps suit

            //REMARK: AFTER UPDATE OF SPECIFICATIONS, THIS BRANCH IS REDUNDANT AND COULD BE COLLAPSED WITH THE ONE BELOW, HOWEVER IS KEPT TO BETTER EMPHASIZE THE GAME RULES (previously, this branch chose the winner based on point values, not rank)
                if(rankCurrentPlayer < rankFirstPlayer)
                    return currentPlayer;
                else
                    return (currentPlayer+1)%2; //the first player wins

        } else if (suitFirstPlayer.equals(suitCurrentPlayer)){ //not trumps, but same suit

                if(rankFirstPlayer > rankCurrentPlayer)
                    return currentPlayer;
                else
                    return (currentPlayer+1)%2; //the first player wins
        }

        throw new IllegalStateException();
    }

    /**
     * Compute score examining the piles of the specified player
     *
     * @param player the player index, should be either PLAYER0 or PLAYER1
     * @return the int representing the score, computed summing the point values of the cards in the player's pile
     */
    public int computeScore(int player){
        List<NeapolitanCard> pile = piles.get(player).getCardList();
        int score = 0;

        for(NeapolitanCard c: pile){
            score += BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber());
        }

        return score;
    }


    /**
     * Count the number of cards currently on surface.
     *
     * @return the int representing the number of cards currently on surface.
     */
    public int countCardsOnSurface(){
        return surface.size();
    }

    /**
     * Determine whether both the players hands are empty, convenience method.
     *
     * @return true if both players hands are empty, false otherwise.
     */
    public boolean arePlayersHandsEmpty(){
        return hands.get(PLAYER0).isEmpty() && hands.get(PLAYER1).isEmpty();
    }

    /**
     * Determine whether the deck is empty, convenience method
     *
     * @return True if the deck is empty, false otherwise
     */
    public boolean isDeckEmpty(){
        return deck.isEmpty();
    }


    /**
     * Checks if two configurations are in the same state.
     *
     * @param config the configuration to be used for the comparison
     * @return true if the configurations are in the same state, false otherwise
     */
    public boolean equalTo(Briscola2PMatchConfig config){
        return  this.currentPlayer == config.getCurrentPlayer()&&
                this.briscolaSuit == config.getBriscolaSuit() &&
                this.deck.equalTo(config.getDeck()) &&
                this.surface.equalTo(config.getSurface()) &&
                this.hands.get(PLAYER0).equalTo(config.getHand(PLAYER0)) &&
                this.hands.get(PLAYER1).equalTo(config.getHand(PLAYER1)) &&
                this.piles.get(PLAYER0).equalTo(config.getPile(PLAYER0)) &&
                this.piles.get(PLAYER1).equalTo(config.getPile(PLAYER1));

    }


    /**
     * Provides the string representation of the string.
     *
     * @return String representing the configuration (format as specified in the slides)
     */
    @Override
    public String toString(){

        return ""+ currentPlayer + briscolaSuit + deck + "."+ surface + "."+hands.get(PLAYER0)+"."+hands.get(PLAYER1)+"."+piles.get(PLAYER0)+"."+piles.get(PLAYER1);

    }

}
