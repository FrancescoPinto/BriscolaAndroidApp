package it.ma.polimi.briscola.controller;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PFullMatchConfig;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PPile;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PSurface;

/**
 * Class that allows to play a Briscola 2-players match by manipulating a Briscola 2 player match configuration. It is a class that collects convenience methods that handle the match game logic, so that clients of this class can play a match without a GUI and do not need to be concerned with direct manipulation of the configuration in order to play the match.
 * This class exposes methods for starting a new match, resuming a saved match (based on a string configuration), let the players make moves.
 *
 * @author Francesco Pinto
 */

public class Briscola2PMatchNoGUIController {
    private Briscola2PFullMatchConfig config;

    /**
     * Instantiates a new Briscola 2 p match no gui controller. If this constructor is used, then should be initialized programmatically by invoking either startNewmatch or resumeMatch
     */
    public Briscola2PMatchNoGUIController(){

    }

    /**
     * Instantiates a new Briscola 2 p match no gui controller starting from a string configuration.
     *
     * @param configuration the configuration, format as specified in the slides.
     */
    public Briscola2PMatchNoGUIController(String configuration){ //resume from saved configuration
        config = new Briscola2PFullMatchConfig(configuration);
    }

    /**
     * Start new match (initializes the deck, chooses first player, initializes player's hands and the briscola)
     */
    public void startNewMatch(){
        config = new Briscola2PFullMatchConfig();
        config.initializeNewDeck();
        config.initializeFirstPlayer();
        config.initializePlayersHands();
        config.initializeBriscola();
        config.setSurface( new Briscola2PSurface(""));
        config.setPile(config.PLAYER0, new Briscola2PPile(""));
        config.setPile(config.PLAYER1, new Briscola2PPile(""));

    }

    /**
     * Resume match from a given configuration.
     *
     * @param config the configuration, format as specified in the slides
     */
    public void resumeMatch(String config){ //da un salvataggio
        this.config = new Briscola2PFullMatchConfig(config);
    }
    /**
     * Make a single move specified by an input.
     *
     * @param configuration the configuration on which the move should be performed
     * @param move          the move (shoudld be a valid index of card in hand of the current player
     * @return the string representing either the new configuration (if the match is not finished), the outcome of the match (if the match is finished), an error message (if a fatal error occurs)
     */
    private String makeMove(String configuration, int move){
        config = new Briscola2PFullMatchConfig(configuration);

        if(config.countCardsOnSurface() == 0) { //if the roud has just begun
            config.playCard(move); //play first card
            config.toggleCurrentPlayer(); //toggle player

        }else if (config.countCardsOnSurface() == 1){ //if the first card has already been played
            config.playCard(move); //play second card

        }

        if (config.countCardsOnSurface() == 2){ //if two cards are on surface, close the round
            int roundWinner = config.chooseRoundWinner(); //choose round winner
            config.clearSurface(roundWinner); //put cards on surface to the winner's pile
            config.setCurrentPlayer(roundWinner); //choose the next round winner

            if(config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the match is finished, choose winner
                switch(config.chooseMatchWinner()){
                    case Briscola2PFullMatchConfig.PLAYER0: return "WINNER" + Briscola2PFullMatchConfig.PLAYER0 + config.computeScore(Briscola2PFullMatchConfig.PLAYER0);
                    case Briscola2PFullMatchConfig.PLAYER1: return "WINNER" + Briscola2PFullMatchConfig.PLAYER1 + config.computeScore(Briscola2PFullMatchConfig.PLAYER1);
                    case Briscola2PFullMatchConfig.DRAW: return "DRAW";
                    default: throw new RuntimeException("Error while computing the winner");
                }
            }else if (!config.arePlayersHandsEmpty() && config.isDeckEmpty()){ //if the deck is empty, but players have cards in hand, return new config
                return config.toString();
            }else if(!config.isDeckEmpty()){ //if deck is not empty, return new config
                config.drawCardsNewRound();
                return config.toString();  //cards have been collected from surface, new cards have been drawn
            }

        }

        return config.toString();

    }
    /**
     * Make the moves specified in a sequence.
     *
     * @param configuration the configuration on which the move sequence should be performed
     * @param moveSequence  the move sequence, left-most moves are executed first (single moves should be valid indexes of cards in hand of the current player)
     * @return the string representing either the new configuration (if the match is not finished), the outcome of the match (if the match is finished), an error message (if a fatal error occurs)
     */
    public String makeMoveSequence(String configuration, String moveSequence){
        String config = configuration;
        try{
            for(int i = 0; i < moveSequence.length() -1; i++){ //parse the input string and execute alle the moves in the string
                config = makeMove(config, Integer.valueOf(String.valueOf(moveSequence.charAt(i))));
            }
            return makeMove(config,Integer.valueOf(String.valueOf(moveSequence.charAt(moveSequence.length()-1))));
        }catch (Exception e){
            //e.printStackTrace();
            return "ERROR:" +e.getMessage();
        }

    }

    /**
     * Gets config.
     *
     * @return the config
     */
    public Briscola2PFullMatchConfig getConfig() {
        return config;
    }

}
