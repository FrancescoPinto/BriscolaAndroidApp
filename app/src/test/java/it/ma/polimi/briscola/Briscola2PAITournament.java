package it.ma.polimi.briscola;

import org.junit.Test;

import it.ma.polimi.briscola.controller.Briscola2PMatchNoGUIController;
import it.ma.polimi.briscola.ai.Briscola2PAIDumbGreedyPlayer;
import it.ma.polimi.briscola.ai.Briscola2PAIRandomPlayer;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PFullMatchConfig;

import static junit.framework.Assert.assertTrue;

/**
 * Class thought to check if the AIs behave properly, and to determine what is the best AI
 */

public class Briscola2PAITournament {

    Integer winsPlayer0 = 0;
    Integer winsPlayer1 = 0;

    //IMPORTANT: with some testing
    //In 100 matches, DumbGreedy vs Random, DumbGreedy wins 85 times out of 100, Random wins 13 times out of 100
    @Test
    public void initializeMatchAndResumeMatchTestWithRandomAI(){
        Briscola2PAIRandomPlayer randomPlayer = new Briscola2PAIRandomPlayer();
        Briscola2PAIDumbGreedyPlayer dumbGreedyPlayer = new Briscola2PAIDumbGreedyPlayer();

        for(int j = 0; j < 100; j++)
        {
            Briscola2PMatchNoGUIController controller = new Briscola2PMatchNoGUIController();
            controller.startNewMatch();
            String config = controller.getConfig().toString();
            int move;
            int i = 0;
            while(i < 40){

                Briscola2PFullMatchConfig cfg = new Briscola2PFullMatchConfig(config);
                if(cfg.getCurrentPlayer() == cfg.PLAYER0) {
                    move = randomPlayer.chooseMove(cfg,cfg.PLAYER0);
                    config = MoveTest.moveTest(config, "" + move);
                }else if(cfg.getCurrentPlayer() == cfg.PLAYER1){
                    move = dumbGreedyPlayer.chooseMove(cfg,cfg.PLAYER1);
                    config = MoveTest.moveTest(config, "" + move);
                }
                i++;
            }

            checkMatchEndedCorrectly(config);

        }

        System.out.println("Random wins: "+winsPlayer0 + " vs DumbGreedy "+winsPlayer1);

        winsPlayer0 = winsPlayer1 = 0;

        Briscola2PAIDumbGreedyPlayer smartGreedyPlayer = new Briscola2PAIDumbGreedyPlayer();

        for(int j = 0; j < 100; j++)
        {
            Briscola2PMatchNoGUIController controller = new Briscola2PMatchNoGUIController();
            controller.startNewMatch();
            String config = controller.getConfig().toString();
            int move;
            int i = 0;
            while(i < 40){

                Briscola2PFullMatchConfig cfg = new Briscola2PFullMatchConfig(config);
                if(cfg.getCurrentPlayer() == cfg.PLAYER0) {
                    move = randomPlayer.chooseMove(cfg,cfg.PLAYER0);
                    config = MoveTest.moveTest(config, "" + move);
                }else if(cfg.getCurrentPlayer() == cfg.PLAYER1){
                    move = smartGreedyPlayer.chooseMove(cfg,cfg.PLAYER1);
                    config = MoveTest.moveTest(config, "" + move);
                }
                i++;
            }

            checkMatchEndedCorrectly(config);

        }

        System.out.println("Random wins: "+winsPlayer0 + " vs SmartGreedy "+winsPlayer1);


        //in 10000 matches, SmartGreedy, on average (repeat the 10000 tests many times) performs SLIGHTLY better than DumbGreedy
        float totalPlayer0 = 0f, totalPlayer1 = 0f;
        for(int k = 0; k < 100; k++) { // perform 100 times 100 matches
            winsPlayer0 = winsPlayer1 = 0;

            for (int j = 0; j < 100; j++) {
                Briscola2PMatchNoGUIController controller = new Briscola2PMatchNoGUIController();
                controller.startNewMatch();
                String config = controller.getConfig().toString();
                int move;
                int i = 0;
                while (i < 40) {

                    Briscola2PFullMatchConfig cfg = new Briscola2PFullMatchConfig(config);
                    if (cfg.getCurrentPlayer() == cfg.PLAYER0) {
                        move = dumbGreedyPlayer.chooseMove(cfg, cfg.PLAYER0);
                        config = MoveTest.moveTest(config, "" + move);
                    } else if (cfg.getCurrentPlayer() == cfg.PLAYER1) {
                        move = smartGreedyPlayer.chooseMove(cfg, cfg.PLAYER1);
                        config = MoveTest.moveTest(config, "" + move);
                    }
                    i++;
                }

                checkMatchEndedCorrectly(config);

            }

            totalPlayer0 += winsPlayer0;
            totalPlayer1 += winsPlayer1;
            System.out.println("DumbGreedy wins: " + winsPlayer0 + " vs SmartGreedy " + winsPlayer1);
        }
        System.out.println("Average DumbGreedyWins: "+ totalPlayer0/10000+" vs SmartGreedy: " + totalPlayer1/10000);


    }
    //convenience function
    private void checkMatchEndedCorrectly(String config){
        boolean correctMatch = false;
      //  System.out.println("------------------------------------------------------------------------------------------------\n"+
       //         config+
       //         "\n----------------------------------------------------------------------------------------------");
        if (config.equals("DRAW"))
            correctMatch = true;
        else if (config.substring(0, 7).equals("WINNER0") && Integer.valueOf(config.substring(7)) > 60) {
            correctMatch = true;
            winsPlayer0++;
        }
        else if (config.substring(0, 7).equals("WINNER1") && Integer.valueOf(config.substring(7)) > 60) {
            correctMatch = true;
            winsPlayer1++;
        }
        assertTrue(correctMatch);
    }
    //todo : creare scontri tra AI, in modo da controllare quale è la più intelligente


    //todo: testare passo passo le configurazioni non ha molto senso (anche se si potrebbe fare deterministicamente per le IA più semplici, e probabilisticamente per le più complesse. Ma è nettamente meglio far giocare le IA per controllare che, in linea di massima, si comportino bene e vincano i match.
   /* String outcome = MoveTest.moveTest();
    if(currentPlayer )
    Briscola2PAIDumbGreedyPlayer AI = new Briscola2PAIDumbGreedyPlayer();
    AI.chooseMove(outcome);
    .moveTest(startingConfig1,"00");*/

}
