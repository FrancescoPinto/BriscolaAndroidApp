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
 * Class that represents a Briscola 2 Players Match Configuration and provides methods to manipulate it. This class provides only atomic operations on the configuration, and thus DOES NOT HANDLE THE MATCH EXECUTION FLOW. The execution flow is handled by another class (that in turn should be invoked in a controller). This class just provides means to manipulate the configuration (consistently) to other classes.
 *
 * @author Francesco Pinto
 */
public class Briscola2PMatchConfig {
//TODO ATTENTO QUESTA CLASSE NON CONTIENE COMPORTAMENTO, E' SOLO UN AGGREGATORE DI DATI -> POI CI SARA' UNA CLASSE CHE INBASE ALLA CONFIG E ALLE REGOLE DEL GIOCO MODIFICA LO STATO
//TODO problema, come risolvo chi è il player (esempio: io salvo 0, ma chi dei due è 0?? la CPU o il giocatore? e nel multiplayer?

    /**
     * The constant PLAYER0 is an index used to represent the Local Player (i.e. the human player on the local device)
     */
    public static final int PLAYER0 = 0, /**
     * The Player 1 is an index used to represent the "other player" (i.e. either a local/remote AI or another remote human player)
     */
    PLAYER1 = 1, /**
     * The Draw, index used to represent a Draw in the match
     */
    DRAW = -1; //PLAYER0 is the Local player, PLAYER1 is the other player (i.e. AI or remote player)

    private int currentPlayer;
    private String briscolaSuit;
    private NeapolitanDeck deck;
    private Briscola2PSurface surface;
    private List<Briscola2PHand> hands = new ArrayList<>();
    private List<Briscola2PPile> piles = new ArrayList<>();


    //todo inserisci un metodo checkConfigConsistency per controllare la consistenza di una stringa, da chiamare nel costruttore


    /**
     * Instantiates a new empty Briscola 2 Players Match Configuration. Before the configuration can be used, it must be brought to a consistent state by using the class' initialization methods.
     */
    public Briscola2PMatchConfig(){}

    /**
     * Instantiates a new Briscola 2 p match config.
     *
     * @param configuration the configuration
     */
    public Briscola2PMatchConfig(String configuration){
        this.currentPlayer = Integer.valueOf(""+configuration.charAt(0));
        String[] tokens = configuration.split("\\.",6);
        String deck = "";
        if(tokens[0].length() > 2)
            deck = tokens[0].substring(2);
        NeapolitanDeck temp = new NeapolitanDeck(deck); //deck and briscola
        //todo: devi chiedere al prof se la configurazione con tutte le carte pescate dal deck ha ancora la briscola appesa alla fine ... se non è così, ovviamente non puoi estrarre tutta la briscola!
        if(tokens[0].length() >= 2) {
            /*this.briscola = (NeapolitanCard) temp.drawCardFromBottom();
            temp.putCardToBottom(this.briscola);*/
        //}else{
            this.briscolaSuit = NeapolitanCardSuit.getCardSuit(""+tokens[0].charAt(1)).getSuit(); //usa getCardSuit per fare il check di valore corretto!
        } else
            throw new IllegalArgumentException("No current player or briscola properly specified");
        this.deck = temp;
        this.surface = new Briscola2PSurface(tokens[1]);
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


//todo devi ricontrollare la javadoc, che non l'hai fatta bene!
    //Initialization methods

    /**
     * Initialize new deck.
     */
    public void initializeNewDeck(){
        NeapolitanDeck deck = new NeapolitanDeck();
        deck.shuffleDeck(new UniformProbabilityShuffler());
        this.deck = deck;
    }

    /**
     * Initialize first player.
     */
//Select first player TODO metti qualcosa di carino tipo carta/sasso/forbice ... per ora semplifica con lancio di moneta
    public void initializeFirstPlayer(){
        int coinTossResult = ThreadLocalRandom.current().nextInt(0, 2);
        if(coinTossResult == 0){
            currentPlayer = PLAYER0;
        }else{
            currentPlayer = PLAYER1;
        }
    }

    /**
     * Initialize players hands.
     */
    public void initializePlayersHands(){ //il FirstPlayer (currentPlayer determinato dall'inizializzazione) gioca per primo
        List<NeapolitanCard> hand0 = new ArrayList<>();
        List<NeapolitanCard> hand1 = new ArrayList<>();

        for(int i = 0; i < 3; i++) {

            hand0.add((NeapolitanCard)deck.drawCardFromTop());
            hand1.add((NeapolitanCard)deck.drawCardFromTop());

        }

        hands.add(new Briscola2PHand(currentPlayer == PLAYER0?hand0:hand1));
        hands.add(new Briscola2PHand(currentPlayer == PLAYER0?hand1:hand0));

    }

    /**
     * Initialize briscola.
     */
    public void initializeBriscola(){
        NeapolitanCard briscola = (NeapolitanCard) deck.drawCardFromTop();
        deck.putCardToBottom(briscola);
        this.briscolaSuit = NeapolitanCardSuit.getCardSuit(briscola.getCardSuit()).getSuit(); //check correttezza fatto da getCardSuit
    }



    //Cards methods

    /**
     * Draw cards new round.
     */
    public void drawCardsNewRound(){
        if(deck.isEmpty())
            return;


        hands.get(currentPlayer).putCardInHand((NeapolitanCard) deck.drawCardFromTop());  //TODO assunzione che il first player è stato settato a fine turno precedente
        hands.get((currentPlayer+1)%2).putCardInHand((NeapolitanCard) deck.drawCardFromTop());

       /* if(deck.getCurrentDeckSize() == 0) {
            hands.get((currentPlayer+1)%2).putCardInHand(briscola); //not the current player
        }else {
            hands.get((currentPlayer+1)%2).putCardInHand((NeapolitanCard) deck.drawCardFromTop());
        }*/

        }

    /**
     * Play card.
     *
     * @param i the
     */
    public void playCard(int i) {
        NeapolitanCard cardToPlay = hands.get(currentPlayer).removeCardFromHand(i);
        if(cardToPlay == null)
            throw new IllegalStateException();
        else
            surface.putCardOnSurface(cardToPlay); //la carta del primo giocatore è sempre in 0
    }

    /**
     * Count cards on surface int.
     *
     * @return the int
     */
    public int countCardsOnSurface(){
        return surface.countCardsOnSurface();
    }

    /**
     * Are players hands empty boolean.
     *
     * @return the boolean
     */
    public boolean arePlayersHandsEmpty(){
        return hands.get(PLAYER0).getHand().isEmpty() && hands.get(PLAYER1).getHand().isEmpty();
    }

    /**
     * Is deck empty boolean.
     *
     * @return the boolean
     */
    public boolean isDeckEmpty(){
        return deck.isEmpty();
    }

    /**
     * Clear surface.
     *
     * @param winner the winner
     */
    public void clearSurface(int winner){
        if(surface.countCardsOnSurface() != 2)
            throw new IllegalStateException();

        List<NeapolitanCard> cardsOnSurface = surface.clearSurface();
        piles.get(winner).pushOnPile(cardsOnSurface);

    }



    //Player methods

    /**
     * Toggle current player.
     */
    public void toggleCurrentPlayer(){
        this.currentPlayer = (currentPlayer+1)%2;
    }

    /**
     * Choose round winner int.
     *
     * @return the int
     */
    public int chooseRoundWinner(){
        if(surface.countCardsOnSurface() != 2)
            throw new IllegalStateException();

        NeapolitanCard cardFirstPlayer = surface.getCard(0),
                cardCurrentPlayer = surface.getCard(1);
        String suitFirstPlayer = cardFirstPlayer.getCardSuit(),
                suitCurrentPlayer = cardCurrentPlayer.getCardSuit();

        String numberFirstPlayer = cardFirstPlayer.getCardNumber(),
                numberCurrentPlayer = cardCurrentPlayer.getCardNumber();

        int rankFirstPlayer = BriscolaCardPointsAndRankingRules.getRank(numberFirstPlayer),
                rankCurrentPlayer = BriscolaCardPointsAndRankingRules.getRank(numberCurrentPlayer);

       // int valueFirstPlayer = BriscolaCardPointsAndRankingRules.getPointValue(numberFirstPlayer),
          //      valueCurrentPlayer = BriscolaCardPointsAndRankingRules.getPointValue(numberCurrentPlayer);

        if(rankFirstPlayer == -1 || rankCurrentPlayer == -1)
            throw new IllegalStateException();

        if(suitFirstPlayer != suitCurrentPlayer){ //if different suit
            //if there is a trump, the trump wins
            if(suitFirstPlayer.equals(briscolaSuit)){
                return (currentPlayer+1)%2; //the first player wins
            }else if(suitCurrentPlayer.equals(briscolaSuit)){
                return currentPlayer;
            }

            //if no trump, first player wins
            return (currentPlayer+1)%2; //the first player wins

        }else if ((suitFirstPlayer.equals(briscolaSuit)) &&  (briscolaSuit.equals(suitCurrentPlayer))) { //both trumps suit

         //   System.out.println("first " + cardFirstPlayer + " current "+ cardCurrentPlayer);
          //  System.out.println(valueFirstPlayer + "primo" + (currentPlayer+1)%2 + ", " + valueCurrentPlayer + "+current" + currentPlayer);
            if(rankCurrentPlayer < rankFirstPlayer)
                return currentPlayer;
            else
                return (currentPlayer+1)%2; //the first player wins

        } else if (suitFirstPlayer.equals(suitCurrentPlayer)){ //ridondante, potresti collassare tutto con quella di sopra cambiando condizione, comunque questo è ilc aso non trump ma uguali

           // System.out.println("first " + cardFirstPlayer + " current "+ cardCurrentPlayer);
          //  System.out.println(rankFirstPlayer + "primo" + (currentPlayer+1)%2 + ", " + rankCurrentPlayer + "+current" + currentPlayer);
            if(rankFirstPlayer > rankCurrentPlayer)
                return currentPlayer;
            else
                return (currentPlayer+1)%2; //the first player wins
        }

        throw new IllegalStateException();
    }

    /**
     * Choose match winner int.
     *
     * @return the int
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
     * Compute score int.
     *
     * @param player the player
     * @return the int
     */
    public int computeScore(int player){
        List<NeapolitanCard> pile = piles.get(player).getPile();
        int score = 0;

        for(NeapolitanCard c: pile){
            score += BriscolaCardPointsAndRankingRules.getPointValue(c.getCardNumber());
        }

        return score;
    }


    /**
     * Gets current player.
     *
     * @return the current player
     */
//Setters and getters
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets current player.
     *
     * @param currentPlayer the current player
     */
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Gets briscola suit.
     *
     * @return the briscola suit
     */
    public String getBriscolaSuit() {
        return briscolaSuit;
    }

    /**
     * Sets briscola suit.
     *
     * @param briscolaSuit the briscola suit
     */
    public void setBriscolaSuit(String briscolaSuit) {
        this.briscolaSuit = briscolaSuit;
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
     * Gets hand.
     *
     * @param i the
     * @return the hand
     */
    public Briscola2PHand getHand(int i) {
        return hands.get(i);
    }

    /**
     * Gets pile.
     *
     * @param i the
     * @return the pile
     */
    public Briscola2PPile getPile(int i) {
        return piles.get(i);
    }

    /**
     * Equal to boolean.
     *
     * @param config the config
     * @return the boolean
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
     * Set hand.
     *
     * @param i    the
     * @param hand the hand
     */
    public void setHand(int i, String hand){

            this.hands.add(i,new Briscola2PHand(hand));
    }

    /**
     * Set pile.
     *
     * @param i    the
     * @param pile the pile
     */
    public void setPile(int i, String pile){
        this.piles.add(i,new Briscola2PPile(pile));
    }

    @Override
    public String toString(){

        return ""+ currentPlayer + briscolaSuit + deck /*+ briscola*/ + "."+ surface +
                //surface.get(0) == null?"":surface.get(0)+((surface.get(1) == null)?"":surface.get(1)) +
            "."+hands.get(PLAYER0)+"."+hands.get(PLAYER1)+"."+piles.get(PLAYER0)+"."+piles.get(PLAYER1);

    }

}
