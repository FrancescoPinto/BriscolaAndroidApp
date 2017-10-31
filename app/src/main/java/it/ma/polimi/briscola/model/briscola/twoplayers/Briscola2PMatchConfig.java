package it.ma.polimi.briscola.model.briscola.twoplayers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import it.ma.polimi.briscola.model.briscola.BriscolaCardRanking;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanDeck;
import it.ma.polimi.briscola.model.deck.UniformProbabilityShuffler;

/**
 * Created by utente on 21/10/17.
 */

public class Briscola2PMatchConfig {
//TODO ATTENTO QUESTA CLASSE NON CONTIENE COMPORTAMENTO, E' SOLO UN AGGREGATORE DI DATI -> POI CI SARA' UNA CLASSE CHE INBASE ALLA CONFIG E ALLE REGOLE DEL GIOCO MODIFICA LO STATO
//TODO problema, come risolvo chi è il player (esempio: io salvo 0, ma chi dei due è 0?? la CPU o il giocatore? e nel multiplayer?

    public static final int PLAYER0 = 0, PLAYER1 = 1, DRAW = -1;

    private int currentPlayer;// = ThreadLocalRandom.current().nextInt(0,2) == 0?CPUPlayer:HumanPlayer; //estremo escluso, quindi tra 0 e 1
    private NeapolitanCard briscola;
    private NeapolitanDeck deck;
    private Briscola2PSurface surface; //DECIDI SE DEVI FARLO COME UN OGGETTO O MENO
    private List<Briscola2PHand> hands = new ArrayList<>(); //TODO DECIDI SE DEVI FARLO CON UN OGGETTO O MENO
    private List<Briscola2PPile> piles = new ArrayList<>(); //TODO decidi se questi li devi fare con un oggetto o meno


    //todo inserisci un metodo checkConfigConsistency per controllare la consistenza di una stringa, da chiamare nel costruttore

    //TODO decidi se devi spostare questi in MatchConfig o stateMachine ...
    public void initializeNewDeck(){
        NeapolitanDeck deck = new NeapolitanDeck();
        deck.shuffleDeck(new UniformProbabilityShuffler());
        this.deck = deck;
    }

    //Select first player TODO metti qualcosa di carino tipo carta/sasso/forbice ... per ora semplifica con lancio di moneta
    public void initializeFirstPlayer(){
        int coinTossResult = ThreadLocalRandom.current().nextInt(0, 2);
        if(coinTossResult == 0){
            currentPlayer = PLAYER0;
        }else{
            currentPlayer = PLAYER1;
        }
    }

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

    public void initializeBriscola(){
        NeapolitanCard briscola = (NeapolitanCard) deck.drawCardFromTop();
        //deck.putCardToBottom(briscola);
        this.briscola = briscola;
    }

    public void drawCardsNewRound(){
        if(deck.isEmpty())
            return;


        hands.get(currentPlayer).putCardInHand((NeapolitanCard) deck.drawCardFromTop());  //TODO assunzione che il first player è stato settato a fine turno precedente

        if(deck.getCurrentDeckSize() == 0) {
            hands.get((currentPlayer+1)%2).putCardInHand(briscola); //not the current player
        }else {
            hands.get((currentPlayer+1)%2).putCardInHand((NeapolitanCard) deck.drawCardFromTop());
        }

       /* if(currentPlayer == PLAYER0) {
            hand0.putCardInHand((NeapolitanCard) deck.drawCardFromTop());

                if(deck.getCurrentDeckSize() == 1) {
                    hand1.putCardInHand(briscola);
                }else {
                    hand1.putCardInHand((NeapolitanCard) deck.drawCardFromTop());
                }

        }else if (currentPlayer == PLAYER1){
            hand1.putCardInHand((NeapolitanCard) deck.drawCardFromTop());

                if(deck.getCurrentDeckSize() == 1) {
                    hand0.putCardInHand(briscola);
                }else {
                    hand0.putCardInHand((NeapolitanCard) deck.drawCardFromTop());
                }
            }*/

        }

    public void playCard(int i) {
        NeapolitanCard cardToPlay = hands.get(currentPlayer).removeCardFromHand(i);
        if(cardToPlay == null)
            throw new IllegalStateException();
        else
            surface.putCardOnSurface(cardToPlay); //la carta del primo giocatore è sempre in 0
    }

    public void toggleCurrentPlayer(){
        this.currentPlayer = (currentPlayer+1)%2;
    }

    public int chooseRoundWinner(){
        if(surface.countCardsOnSurface() != 2)
            throw new IllegalStateException();

        NeapolitanCard cardFirstPlayer = surface.getCard(0),
                cardCurrentPlayer = surface.getCard(1);
        String suitFirstPlayer = cardFirstPlayer.getCardSuit(),
                suitCurrentPlayer = cardCurrentPlayer.getCardSuit();

        String numberFirstPlayer = cardFirstPlayer.getCardNumber(),
                numberCurrentPlayer = cardCurrentPlayer.getCardNumber();

        int rankFirstPlayer = BriscolaCardRanking.getRank(numberFirstPlayer),
                rankCurrentPlayer = BriscolaCardRanking.getRank(numberCurrentPlayer);

        int valueFirstPlayer = BriscolaCardRanking.getPointValue(numberFirstPlayer),
                valueCurrentPlayer = BriscolaCardRanking.getPointValue(numberCurrentPlayer);

        if(rankFirstPlayer == -1 || rankCurrentPlayer == -1)
            throw new IllegalStateException();

        if(suitFirstPlayer != suitCurrentPlayer){ //if different suit
            //if there is a trump, the trump wins
            if(suitFirstPlayer.equals(briscola.getCardSuit())){
                return (currentPlayer+1)%2; //the first player wins
            }else if(suitCurrentPlayer.equals(briscola.getCardSuit())){
                return currentPlayer;
            }

            //if no trump, first player wins
            return (currentPlayer+1)%2; //the first player wins

        }else if ((suitFirstPlayer.equals(briscola.getCardSuit())) &&  (briscola.getCardSuit().equals(suitCurrentPlayer))) { //both trumps suit

         //   System.out.println("first " + cardFirstPlayer + " current "+ cardCurrentPlayer);
          //  System.out.println(valueFirstPlayer + "primo" + (currentPlayer+1)%2 + ", " + valueCurrentPlayer + "+current" + currentPlayer);
            if(valueFirstPlayer < valueCurrentPlayer)
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

    public int countCardsOnSurface(){
        return surface.countCardsOnSurface();
    }

    public boolean arePlayersHandsEmpty(){
        return hands.get(PLAYER0).getHand().isEmpty() && hands.get(PLAYER1).getHand().isEmpty();
    }

    public boolean isDeckEmpty(){
        return deck.isEmpty();
    }

    public void clearSurface(int winner){
        if(surface.countCardsOnSurface() != 2)
            throw new IllegalStateException();

        List<NeapolitanCard> cardsOnSurface = surface.clearSurface();
        piles.get(winner).pushOnPile(cardsOnSurface);

    }

    public int computeScore(int player){
        List<NeapolitanCard> pile = piles.get(player).getPile();
        int score = 0;

        for(NeapolitanCard c: pile){
            score += BriscolaCardRanking.getPointValue(c.getCardNumber());
        }

        return score;
    }

   // public String swapPlayerRoles() TODO in realtà questo è già fatto da setCurrentPlayer (quando finisce il turno scegli
    // todo chi vince, e lo setti come giocatore corrente (NON LO FAI IN CHOOSE WINNER, MA NEL METODO CHE LO CHIAMA (e che riceve l'intero che indica il vincitore, quindi ti permette di scegliere chi è il prossimo first (o di recuperare il current e continuare ad alternare) inverti chi è il primo a giocare al prossimo turno, altrimenti fai la solita alternanza

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public NeapolitanCard getBriscola() {
        return briscola;
    }

    public void setBriscola(NeapolitanCard briscola) {
        this.briscola = briscola;
    }

    public NeapolitanDeck getDeck() {
        return deck;
    }

    public void setDeck(NeapolitanDeck deck) {
        this.deck = deck;
    }

    public Briscola2PSurface getSurface() {
        return surface;
    }

    public void setSurface(Briscola2PSurface surface) {
        this.surface = surface;
    }

    public Briscola2PHand getHand(int i) {
        return hands.get(i);
    }

    public Briscola2PPile getPile(int i) {
        return piles.get(i);
    }



    public Briscola2PMatchConfig(){}

    public Briscola2PMatchConfig(String configuration){
        this.currentPlayer = Integer.valueOf(""+configuration.charAt(0));
        String[] tokens = configuration.split("\\.",6);
        String deck = tokens[0].substring(2);
        NeapolitanDeck temp = new NeapolitanDeck(deck); //deck and briscola
        this.briscola = (NeapolitanCard) temp.drawCardFromBottom();
        this.deck = temp;
        this.surface = new Briscola2PSurface(tokens[1]);
        this.hands.add(new Briscola2PHand(tokens[2])); //hands0
        this.hands.add(new Briscola2PHand(tokens[3])); //hands1
        this.piles.add(new Briscola2PPile(tokens[4]));// pile0
        this.piles.add(new Briscola2PPile(tokens[5])); // pile1
    }


    public Briscola2PMatchConfig(int currentPlayer, NeapolitanCard briscola, NeapolitanDeck deck, Briscola2PSurface surface, Briscola2PHand hand0, Briscola2PHand hand1, Briscola2PPile pile0, Briscola2PPile pile1) {
        this.currentPlayer = currentPlayer;
        this.briscola = briscola;
        this.deck = deck;
        this.surface = surface;
        this.hands.add(hand0);
        this.hands.add(hand1);
        this.piles.add(pile0);
        this.piles.add(pile1);

    }

    public Briscola2PMatchConfig(Briscola2PMatchConfig config){
        this.currentPlayer = config.getCurrentPlayer();
        this.briscola = config.getBriscola();
        this.deck = config.getDeck();
        this.surface = config.getSurface();
        this.hands.add(config.getHand(PLAYER0));
        this.hands.add(config.getHand(PLAYER1));
        this.piles.add(config.getPile(PLAYER0));
        this.piles.add(config.getPile(PLAYER1));
    }


    public boolean equalTo(Briscola2PMatchConfig config){
        return  this.currentPlayer == config.getCurrentPlayer()&&
                this.briscola.equalTo(config.getBriscola()) &&
                this.deck.equalTo(config.getDeck()) &&
                this.surface.equalTo(config.getSurface()) &&
                this.hands.get(PLAYER0).equalTo(config.getHand(PLAYER0)) &&
                this.hands.get(PLAYER1).equalTo(config.getHand(PLAYER1)) &&
                this.piles.get(PLAYER0).equalTo(config.getPile(PLAYER0)) &&
                this.piles.get(PLAYER1).equalTo(config.getPile(PLAYER1));

    }

    public void setHand(int i, String hand){

            this.hands.add(i,new Briscola2PHand(hand));
    }

    public void setPile(int i, String pile){
        this.piles.add(i,new Briscola2PPile(pile));
    }

    @Override
    public String toString(){
        //TODO QUELLI DELLA SURFACE DEVONOE SSERE IN ORDINE IN CUI SON COMPARSI
        return ""+ currentPlayer + briscola.getCardSuit() + deck + briscola + "."+ surface +
                //surface.get(0) == null?"":surface.get(0)+((surface.get(1) == null)?"":surface.get(1)) +
            "."+hands.get(PLAYER0)+"."+hands.get(PLAYER1)+"."+piles.get(PLAYER0)+"."+piles.get(PLAYER1);

    }

}
