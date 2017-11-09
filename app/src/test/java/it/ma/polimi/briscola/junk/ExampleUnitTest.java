package it.ma.polimi.briscola.junk;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    //TEST CHE POTRESTI FARE

    //Inizializza il deck (controlla che tutte le carte ci siano e siano a posto (magari contale, fai prima)

    //Mescola il deck (controlla che cambia l'ordine delle carte, scorri le due liste e controlla che
    //abbiano almeno una carta in ordine diverso -> meglio, allo shuffler imponi di rimescolare se le carte
    //non sono mescolate bene, magari fino ad avere una certa soglia di differenza minima

    //Pesca una carta dal deck

    //Metti la briscola
    //Metti una carta sulla superficie
    // continua ...


    //Inizio gioco <- giocatore corrente hand vuota, così anche il concorrente ... fase di pesca iniziale e di scelta dlela briscola -> collections vuote, mani 3 e 3 ciascuno E DEVONO ESSERE PRESI CORRETTAMENTE DAL DECK (si alternano), DECK - le sue 7 carte, briscola settata


    //TEST DI ALTO LIVELLO SULLA CONFIGURAZIONE (test tipo moveTest(config,move))
    //RICORDA che hai entità: (current player, briscola, deck, cardsOnSurface, hand0,hand1,collection0,collection1)
    //controlla anche i casi di dati sbagliati (tipo un giocatore 53 che tenta di giocare, ciò non deve creare problemi!)
    //    ---- PRIMO ROUND ----
    //Gioco inizializzato: INPUT(inizializzata ok,inizializzata ok,40-7 carte, surface vuota, hands piene 3 carte tutte diverse tra loro, collections vuote)->EFFETTO(hand giocatore corrente ridotto, superficie + 1 carta)
    //C'è una carta sulla surface,

    //---- SECONDO ROUND ----

    //---- TERZO ROUND ----

    //---- QUARTO ROUND ----

    //NON FARE TUTTI I ROUND, SOLO GLI ESTREMI, MA ASSICURATI DI FARE TUTTI I MATCH POSSIBILI PER TERRA!

    //---- PENULTIMO ROUND ----
    //---- ULTIMO ROUND ----

    /*@Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }*/
}