package it.ma.polimi.briscola.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import it.ma.polimi.briscola.model.briscola.BriscolaCardPointsAndRankingRules;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.model.deck.NeapolitanCard;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;
import it.ma.polimi.briscola.model.deck.NeapolitanDeck;
import it.ma.polimi.briscola.model.deck.UniformProbabilityShuffler;

/**
 * Class that allows to play a Briscola 2-players match by manipulating a Briscola 2 player match configuration. It is a class that collects convenience methods that handle the match game logic, so that clients of this class can play a match without a GUI and do not need to be concerned with direct manipulation of the configuration in order to play the match.
 * This class exposes methods for starting a new match, resuming a saved match (based on a string configuration), let the players make moves.
 *
 * @author Francesco Pinto
 */

public class Briscola2PMatchNoGUIController {


    private Briscola2PMatchConfig config;
   // private MatchStatus matchStatus; //todo necessario? credo di sì! E' per forzare il seguire le regole <- invece no, perché faccio un controller per la GUI e uno non per la gui

    public Briscola2PMatchNoGUIController(){

    }

    public Briscola2PMatchNoGUIController(String configuration){ //resume from saved configuration
        config = new Briscola2PMatchConfig(configuration);
    }

    public void startNewMatch(){
        config = new Briscola2PMatchConfig();
        config.initializeNewDeck();
        config.initializeFirstPlayer();
        config.initializePlayersHands();
        config.initializeBriscola();
    }

    public void resumeMatch(String config){ //da un salvataggio
        this.config = new Briscola2PMatchConfig(config);
    }

    public String makeMove(String configuration, int move){
         config = new Briscola2PMatchConfig(configuration);

        if(config.countCardsOnSurface() == 0) {
            config.playCard(move);
            config.toggleCurrentPlayer();

        }else if (config.countCardsOnSurface() == 1){
            config.playCard(move);

        }

        if (config.countCardsOnSurface() == 2){
            int roundWinner = config.chooseRoundWinner();
            config.clearSurface(roundWinner);
            config.setCurrentPlayer(roundWinner);

            if(config.arePlayersHandsEmpty() && config.isDeckEmpty()){
                switch(config.chooseMatchWinner()){
                    case Briscola2PMatchConfig.PLAYER0: return "WINNER" + Briscola2PMatchConfig.PLAYER0 + config.computeScore(Briscola2PMatchConfig.PLAYER0);
                    case Briscola2PMatchConfig.PLAYER1: return "WINNER" + Briscola2PMatchConfig.PLAYER1 + config.computeScore(Briscola2PMatchConfig.PLAYER1);
                    case Briscola2PMatchConfig.DRAW: return "DRAW";
                    default: throw new RuntimeException("Error while computing the winner");
                }
                //  }else if (config.arePlayersHandsEmpty() && !config.isDeckEmpty()) { //todo non è compito di questo metodo controllare la consistenza! vedi il metodo che devi fare in matchConfig
                //     throw new RuntimeException("Players Hands are Empty but Deck is not Empty: inconsistent configuration");
            }else if (!config.arePlayersHandsEmpty() && config.isDeckEmpty()){
                return config.toString();
            }else if(!config.isDeckEmpty()){
                config.drawCardsNewRound();
                return config.toString();  //cards have been colleted from surface, new cards have been drawn
            }

        }

        return config.toString();

    }


    public String makeMoveSequence(String configuration, String moveSequence){
        String config = configuration;
        try{
            for(int i = 0; i < moveSequence.length() -1; i++){
                config = makeMove(config, Integer.valueOf(String.valueOf(moveSequence.charAt(i))));
            }
            return makeMove(config,Integer.valueOf(String.valueOf(moveSequence.charAt(moveSequence.length()-1))));
        }catch (Exception e){
            //e.printStackTrace();
            return "ERROR:" +e.getMessage();
        }

    }

    public Briscola2PMatchConfig getConfig() {
        return config;
    }




    //  QUI DEVI SEGNARTI TUTTI GLI STATI (ma proprio tutti! anche quelli di pesca ecc, e in ognuno far chiamare i vari metodi di config )



    //macchina a stati che rappresenta il match, reagisce alle mosse
    //in base alle regole, alle mosse e allo stato corrente, mantenendo
    //una conta del punteggio

    //Inizializza e mescola deck
    //scegli chi è il primo a giocare
    //distribuisci le carte
    //estrai la briscola
    //-->//gioca il primo giocatore di turno: guarda le carte, sceglie, butta a terra
    //gioca il secondo giocatore di turno: guarda le carte, sceglie, butta a terra
    //determina chi ha vinto il round
    //mette le carte nel mucchio del giocatore che ha vinto (aggiorna punteggio)
    //determina il primo a giocare al prossimo turno
    //fa pescare a turno i due giocatori (prima chi deve giocare per primo?), MA PRIMA CONTROLLA il deck (se ci sono abbastanza carte ok procedi, se ce n'è solo una fa pescare al giocatore che resta senza la briscola, se sono finite chiudi il match)
    //ritorna allo stato indicato con -->


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
