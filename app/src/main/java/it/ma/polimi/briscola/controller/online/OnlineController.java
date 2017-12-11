package it.ma.polimi.briscola.controller.online;

/**
 * Created by utente on 09/12/17.
 */

public class OnlineController {
   /* Ogni richiesta deve avere authorization header del tipo:
    APIKey key;

    se per qualsiasi ragione il messaggio è malformato, 401 Unauthorized

    TUTTE le get usano long polling: setta long timeout (30000 ms):
        se i dati sono disponibili, la richiesta si completa correttamente
        altrimenti, la richiesta scade E DOVRESTI FARNE UN'ALTRA'

    Se success -> ritorna 200 OK
    Se errore -> {"error":"..."}, "message":"..."}

    PER CHIEDERE DI GIOCARE CON QUALCUNO
        GET http://mobile17.ifmledit.org/api/room/:roomname
        con il nome della stanza, nel tuo caso Group01

            se timeout -> prova di nuovo
            se 401 -> stanza che non sei autorizzato a usare
            se 200 -> la partita può iniziare, lui ti ritorna:
                game (id della partita)
                last_card (la briscola, formato "KG", cioè sia numero che suit)
                cards (le carte della tua mano)
                url (url da usare nelle prossime chiamate)

        GET url (quello ritornato prima)
        RItorna opponent Card
        Se sei firstPlayer ti dà opponentCard e la carta per il prossimo round (se sono disponibili)
        Se non sei firstPlayer
        IL GIOCO TERMINA SE UNO DEI DUE GIOCATORI NON FA MOSSE IN 60secondi

            se timeout -> riprova
            se 401 -> non sei autorizzato a giocare quella partita
            se 410 -> il gioco è terminato
            se 200 -> {"opponent":"carta avversaria", opzionalmente anche ->"card":"carta prossimo turno")

        POST: url (quello tornato prima prima)
        Mandi carta che vuoi buttare all'avversario.
        Se sei second player ti dà la tua carta del prossimo turno.
        IL BODY DEVE CONTENERE la carta (e.g. 7B) <- impacchettato in json???
            se timeout -> riprova
            se 401 -> non sei autorizzato a giocare la partita
            se 403 -> non è tuo turno
            se 409 -> non hai quella carta in mano
            se 200 -> magari ti ritorna la carta orissuni turno
                     s {"card":"prossimacarata"}

        DELETE: url (quello ottenuto prima prima prima)
        Termini il gioco <- //Todo, quindi devi implementare un modo per terminare il gioco
        IL BODY DEVE AVERE LA MOTIVAZIONE PER CUI E' TERMIANTA' (e.g. "Going Away")
        Suggerimento del prof: usa o terminated o abandon.


        LEGGI IL SUO ESEMPIO DI USO PER CAPIRE COSA FARE
    */

}
