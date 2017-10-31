package it.ma.polimi.briscola.model.buttare;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;

/**
 * Created by utente on 22/10/17.
 */

//TODO questo credo sarà in pratica un controller
public class Briscola2PStateMachine {

    private Briscola2PMatchConfig config;

    public void startNewMatch(){
        //Deck shuffle
        config = new Briscola2PMatchConfig();
        config.initializeNewDeck();
        config.initializeFirstPlayer();
        config.initializePlayersHands();
        config.initializeBriscola();

    }

    public void resumeMatch(String config){ //da un salvataggio

        //TODO

    }

    public void playRound(int roundNumber){

        //TODO

    }




    /*
    LISTA DEGLI STATI:
        Inizializza partita
            Deck shuffle            FATTO
            Scegli primo a giocare (magari carta sasso forbice?) FATTO (prototipato)
            PESCA LE CARTE (primo player, secondo, primo, secondo, primo, secondo) (FATTO)
            estrai la briscola  FATTO
            metti il deck a posto <- è una cosa che fa la GUI, non fatto quindi ...
        Primo round
            il primo player ha controllo, sceglie carta, butta a terra <- i primi due sono gestiti da GUI e controller, l'altra
            setsso per secondo giocatore
            determina il vincitore del round (se una briscola e una non briscola sono giocate, vince chi gioca la briscola)
                                                (se due briscole sono giocate, quello con la briscola di più alto valore vince)
                                                 (se non si giocano briscole, il primo giocatore butta una suit a terra e il secondo
                                                   se gioca con la stessa suit si comparano e vince il più grande, altrimenti vince il primo giocatore
           pulisci la superfici e mettile sulla pila del vincitore (faccia in giù)
       SECONDO ... Quartultimo ROUND
            il vincitore del round precedente diventa primo giocatore
            entrambi i giocatori pescano una carda (prima il primo giocatore)
                SE IL DECK E' VUOTO, NESSUNA CARTA SI PESCA
  inizio    il primo player ha controllo, sceglie carta, butta a terra
            setsso per secondo giocatore
            determina il vincitore del round
  fine      pulisci la superfici e mettile sulla pila del vincitore (faccia in giù)

        Terzultimo round
            il vincitore del round precedente diventa primo
            entrambi pescano, il primo dal deck, il secondo raccoglie la briscola
            RIPETI i passi da inizio a fine di prima
        PENULTIMO E ULTIMO:
            RIPETI i passi da inizio a fine di due stati prima
            Calcola il punteggio (non lo puoi fare in itinere? così lo mostri sul display ... ma così il giocatore umano può contare ...)
            Determina il vincitore
            TERMINA


     */
}
