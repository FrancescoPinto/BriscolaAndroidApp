package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.ma.polimi.briscola.model.briscola.BriscolaCardPointsAndRankingRules;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;
import it.ma.polimi.briscola.model.deck.NeapolitanDeck;
import it.ma.polimi.briscola.model.deck.UniformProbabilityShuffler;

/**
 * Class that represents a Briscola 2 Players Match Configuration and provides methods to manipulate it. It is an "omniscient" class (knows everything that occurs in a match). This class provides basic and complex (based on the game rules) operations that can be performed on the configuration, however DOES NOT HANDLE THE MATCH EXECUTION FLOW. The execution flow is handled by another class (a match controller). The methods provided by this class simplify the management of the execution flow, implementing the allowed configuration changes. (If client code needs to violate them, it can directly modify the configuration using setters and getters; NOT RECOMMENDED)
 *
 * @author Francesco Pinto
 */
public class Briscola2PFullMatchConfig extends AbstractBriscola2PMatchConfig implements Serializable, Briscola2PMatchConfig{



    private NeapolitanDeck deck;
    private List<Briscola2PHand> hands = new ArrayList<>();
    private int id;
    private String name;
    public List<Briscola2PHand> getHands() {
        return hands;
    }

    /**
     * Instantiates a new empty Briscola 2 Players Match Configuration. Before the configuration can be used, it must be brought programmatically to a consistent state by using the class' initialization methods, called in proper order.
     */
    public Briscola2PFullMatchConfig(){}

    /**
     * Instantiates a new Briscola 2 p match config from its string representation.
     *
     * @param configuration String representation of the match configuration (format as specified in the slides)
     * @throws IllegalArgumentException if current player or briscola are not specified
     */
    public Briscola2PFullMatchConfig(String configuration){
        //Parse the configuration string
        String[] tokens = configuration.split("\\.",6); //get the tokens

        String deck = "";//initialize the deck string
        if(tokens[0].length() > 2) //there's more than current player and briscola suit in the first token
            deck = tokens[0].substring(2); //extract the deck string
        NeapolitanDeck temp = new NeapolitanDeck(deck); //deck (remark, if deck is not empty, the briscola is the last last card)


        if(tokens[0].length() >= 2) { //initialize the briscola and the currentPlayer
            int currentPlayer = Integer.valueOf(""+tokens[0].charAt(0)); //first char is current player

            if(currentPlayer == PLAYER1 || currentPlayer == PLAYER0)
                this.currentPlayer = currentPlayer;
            else
                throw new IllegalArgumentException(noCurrentPlayerOrBriscolaSpecified);

            this.briscolaSuit = NeapolitanCardSuit.getCardSuit(""+tokens[0].charAt(1)).getSuit(); //uses getCardSuit in order to make the check that the suit value is valid
        } else
            throw new IllegalArgumentException(noCurrentPlayerOrBriscolaSpecified);

        this.deck = temp;
        this.surface = new Briscola2PSurface(tokens[1]);//initialize surface
        this.hands.add(new Briscola2PHand(tokens[2])); //hands0
        this.hands.add(new Briscola2PHand(tokens[3])); //hands1
        this.piles.add(new Briscola2PPile(tokens[4]));// pile0
        this.piles.add(new Briscola2PPile(tokens[5])); // pile1
    }

    /**
     * Instantiates a new Briscola 2 p full match config.
     *
     * @param configuration the configuration
     * @param id            the id
     * @param name          the name
     */
    public Briscola2PFullMatchConfig(String configuration, int id, String name){
        this(configuration);
        this.id = id;
        this.name = name;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set hand of the i-th player from String
     *
     * @param i    the player's index, should be either the public final int PLAYER0 or PLAYER1 provided by this class
     * @param hand String representing the hand, format as specified in the slides
     */
    public void setHand(int i, String hand){
        setHand(i,new Briscola2PHand(hand));
    }

    /**
     * Set hand of the i-th player from Briscola2PHand
     *
     * @param i    the player's index, should be either the public final int PLAYER0 or PLAYER1 provided by this class
     * @param hand the hand represented as a Briscola2PHand object
     * @throws IllegalArgumentException if invalid index is specified
     */
    public void setHand(int i, Briscola2PHand hand){

        if(i == PLAYER0 || i == PLAYER1)
            this.hands.add(i,hand);
        else throw new IllegalArgumentException(wrongPlayerIndex);
    }

    public NeapolitanCard getBriscola(){
        return getDeck().getCard(getDeck().size()-1);
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
     * Gets hand of the i-th player.
     *
     * @param i the player index, should be the public final int PLAYER0 or PLAYER1 provided by this class
     * @return the hand
     */
    public Briscola2PHand getHand(int i) {
        return hands.get(i);
    }


    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void playCard(String card) {
        //by convention, the offline controller accepts only Integer arguments
        throw new IllegalArgumentException();
    }

    @Override
    public void playCard(Integer i) {
        NeapolitanCard cardToPlay = hands.get(currentPlayer).removeCard(i);
        surface.appendCard(cardToPlay); //la carta del primo giocatore è sempre in 0
    }


    /**
     * Initialize new deck. Creates a neapolitan deck and shuffles it. Puts it in the configuration.
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
        int coinTossResult = new Random().nextInt(2); //coin toss, Bernoulli random variable with p  = 0.5
        if(coinTossResult == 0){
            currentPlayer = PLAYER0;
        }else{
            currentPlayer = PLAYER1;
        }

    }

    /**
     * Initialize players hands. To be called after initializeNewDeck and initializeFirstPlayer. Alternates a card draw from the deck for first and second player of the first round. Each player draws 3 cards.
     *
     * @throws IllegalStateException if either the deck is not filled with 40 cards or the current player has not been initialized
     */
    public void initializePlayersHands(){
        if(deck != null && deck.size() == deck.getMaxNumCardsAllowedInList() && currentPlayer != null) {
            List<NeapolitanCard> firstPlayerHand = new ArrayList<>();
            List<NeapolitanCard> secondPlayerHand = new ArrayList<>();

            for (int i = 0; i < 3; i++) {

                firstPlayerHand.add(deck.drawCardFromTop());  // the first player (current player) draws a card
                secondPlayerHand.add(deck.drawCardFromTop()); //then the second player

            }

            hands.add(new Briscola2PHand(currentPlayer == PLAYER0 ? firstPlayerHand : secondPlayerHand)); //the currentPlayer is the first to draw a card
            hands.add(new Briscola2PHand(currentPlayer == PLAYER0 ? secondPlayerHand : firstPlayerHand));

        }else if(deck == null ||!(deck.size() == deck.getMaxNumCardsAllowedInList()))
            throw new IllegalStateException(startWith40CardDeck);
        else if (currentPlayer == null){
            throw new IllegalStateException(noCurrentPlayerInitialized);
        }
    }

    /**
     * Initialize briscola. To be called after initializePlayersHands. Draws the briscola from the top of the deck, and puts it to the bottom.
     *
     * @throws IllegalStateException if the deck does not contain 34 cards
     */
    public void initializeBriscola(){
        if(deck != null && deck.size() == deck.getMaxNumCardsAllowedInList() - 6) {
            NeapolitanCard briscola = deck.drawCardFromTop();
            deck.putCardToBottom(briscola);
            this.briscolaSuit = briscola.getCardSuit();
        }else
            throw new IllegalStateException(playersHandsNotInitialized);
    }


    @Override
    public List<NeapolitanCard> drawCardsNewRound(){
        if(deck.isEmpty()) //if deck is empty, don't do anything.
            return null;

        NeapolitanCard c1 = deck.drawCardFromTop();
        NeapolitanCard c2 = deck.drawCardFromTop();
        hands.get(currentPlayer).appendCard(c1); //first player draws a card
        hands.get((currentPlayer+1)%2).appendCard(c2); //second player draws a card

        List<NeapolitanCard> newCards =  new ArrayList<NeapolitanCard>();
        newCards.add(c1);
        newCards.add(c2);
        return newCards;
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
     * @return true if the deck is empty, false otherwise
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
    public boolean equalTo(Briscola2PFullMatchConfig config){
        return  this.currentPlayer == config.getCurrentPlayer()&&
                this.briscolaSuit.equals(config.getBriscolaSuit()) &&
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


    /**
     * Infer the briscola, if it is still in deck.
     *
     * @return the neapolitan card representing the briscola
     */
    public NeapolitanCard inferBriscolaIfInDeck(){
        if(deck.size() >= 2)
            return deck.getCard(deck.size()-1);
        else
            return null; //briscola is in players hands
    }

}
