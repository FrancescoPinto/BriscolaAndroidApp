package it.ma.polimi.briscola.forfullrelease.ai;

import java.util.HashMap;
import java.util.Map;

import it.ma.polimi.briscola.controller.daFare.Briscola2PMatchNoGUIController;

/**
 * Created by utente on 07/11/17.
 */

public class Briscola2PAICardCounterGreedyPlayer {

    Map<String,Double> probabilityEstimates = new HashMap<>();

    //CONSIDERAZIONI MIE PER RAGIONARE SULL'AI:

    //Obiettivo dell'AI: raggiungere un punteggio superiore a 60 (cioè, VINCERE, non per forza massimizzare), EVITANDO che l'avversario faccia più di 60 punti

    //Obiettivo greedy: vincere questo round facendo più punti possibile
    //obiettivo greedy migliore: fare più punti possibile in questo round, minimizzando il numero di punti fatti dall'avversario (credo implementerò questo qui in questa classe)
    //Obiettivo greedy alternativo a migliore: VINCERE questo round se faccio punti
    //Obiettivo non greedy euristico: stabilire se vale la pena di vincere il round (cioè se l'avversario ha giocato carte con pochi punti, butto qualcosa senza valore e magari lo faccio pure vincere, perrché mi tengo le carte buone per dopo ... però il problema è che con questo approccio potresti tenerti delle carte "buone" ma non abbastanza da battere l'avversario ...è delicato, ragionaci bene su
        /*
            Come si guadagnano i punti?
                1) io gioco una briscola
                    1.1) l'avversario ha giocato una briscola di più basso valore
                    1.2) l'avversario non ha giocato una briscola
                        1.2.1) l'avversario non ha giocato una briscola ma una carta che dà molti punti
                        1.2.2) l'avversario non ha giocato una briscola né una carta con molti punti, ma io gioco una briscola da molti punti
                        1.2.3) entrambi giochiamo una carta di gran valore, ma l'avversario non ha giocato una briscola
                2) io gioco una carta non briscola
                    2.1) IO SONO IL PRIMO GIOCATORE
                        2.1.2) l'avversario ha giocato una carta non briscola né con lo stesso seme del mio
                            2.1.2.1) guadagno perché l'avversario ha giocato una carta di valore
                            2.1.2.2) guadagno perché io ho giocato una carta di valore ma l'avversario non l'ha giocata
                            2.1.2.3) guadagno perché entrambi abbiamo giocato una carta di valore
                        2.1.3) l'avversario gioca stesso seme, ma più basso valore del mio
                            2.1.3.1) guadagno perché l'avversario ha giocato gran valore
                            2.1.3.2) " io giocato gran valore
                            2.1.3.3) " entrambi gran valore
                    2.2) IO SONO IL SECONDO GIOCATORE
                        2.2.1) io gioco carta con stesso seme dell'avversario, ma di valore più grande

                NOTA BENE: se io gioco una briscola non importa che sia stato il primo o il secondo giocatore

          */
    //IDEA FACILE PER IMPLEMENTARE L'AI: crei fondamentalmente un'AI "stupida" che sceglie cosa giocare in base alle regole su dette E INTANTO usa la conta delle carte per stimare le
    //probabilità

    //L'AI PIU' STUPIDA AGISCE IN MODO GREEDY: cerco di ottimizzare ORA E SUBITO (cioè round per round) IL PUNTEGGIO, E CERCO DI LIMITARE ORA E SUBITO I PUNTI DEL NEMICO (ma in questo round)
    //NOTA ANCHE CHE UNA IA DI QUESTO TIPO LA PUOI FAR COMPORTARE IN DUE MODI DIVERSI IN BASE A SE E' O MENO IL PRIMO GIOCATORE: se è il secondo giocatore, la scelta è reattiva alla carta giocata dal primo
    //IN CASO DI PURO GREEDY, LA SCELTA se è secondo giocatore è praticamente "deterministica", mentre se è primo va condizionata con le probabilità (che vengono aggiornate quando chooseMove è chiamato in base allo stato della config)

    //SE NON VUOI FARLA GREEDY, ALLORA PRESUMO CHE LE PROBABILITA' DEBBANO CONDIZIONARE ANCHE IL CASO IN CUI E' secondo giocatore, o ancora di più, devono anche condizionare se vincere/perdere un round per diventare o meno primo/secondo giocatore del prossimo (ad esempio potrei voler perdere questo round perché credo il giocatore opposto non abbia briscole, ma carte di alto valore in mano, mentre io ho briscole, quindi posso papparmele senza sforzo)
    //TODO: per ora implementane una greedy, poi se hai tempo da perdere ne implementi anche una non greedy


    //INIZIO PRIMO ROUND: io guardo le mie carte, non conosco niente di quello che ha l'altro giocatore in mano (solo delle probabilità molto vaghe, dato che non conosco la pila)

    //TODO: prima di iniziare a scrivere questo, guarda tutti i todo che hai messo in Dumb greedy!


    public void updateProbabilityEstimates(Briscola2PMatchNoGUIController match){ //todo questo metodo va chiamato prima di iniziare i conti, serve ad aggiornare le probabiltà, chiamalo come updateProbabilityEstimates(match);
        //todo scandisci la configurazione per fare delle stime sulle probabilità

    }
}
