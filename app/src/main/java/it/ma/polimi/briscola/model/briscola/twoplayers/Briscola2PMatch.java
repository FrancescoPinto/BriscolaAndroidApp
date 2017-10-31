package it.ma.polimi.briscola.model.briscola.twoplayers;

/**
 * Created by utente on 19/10/17.
 */

public class Briscola2PMatch {


    private Briscola2PMatchConfig config;
    private MatchStatus matchStatus; //todo necessario? credo di sì! E' per forzare il seguire le regole


    public enum MatchStatus {
        ROUNDSTARTED,
        FIRSTCARDPLAYED,
        SECONDCARDPLAYED;
    };
    private Briscola2PMatch(){

        config = new Briscola2PMatchConfig();
        config.initializeNewDeck();
        config.initializeFirstPlayer();
        config.initializePlayersHands();
        config.initializeBriscola();

    }

    private Briscola2PMatch(String configuration){ //resume from saved configuration
        config = new Briscola2PMatchConfig(configuration);
    }

    public Briscola2PMatch startNewMatch(){
        return new Briscola2PMatch();
    }

    public Briscola2PMatch resumeMatch(String config){ //da un salvataggio
        return new Briscola2PMatch(config);
    }

    public static String makeMove(String configuration, int move){
        Briscola2PMatchConfig config = new Briscola2PMatchConfig(configuration);

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


    public static String moveTest(String configuration, String moveSequence){
        String config = configuration;
        try{
            for(int i = 0; i < moveSequence.length() -1; i++){
                config = makeMove(config, Integer.valueOf(String.valueOf(moveSequence.charAt(i))));
            }
            return makeMove(config,Integer.valueOf(String.valueOf(moveSequence.charAt(moveSequence.length()-1))));
        }catch (Exception e){
            e.printStackTrace();
            return "ERROR:" +e.getMessage();
        }

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

}
