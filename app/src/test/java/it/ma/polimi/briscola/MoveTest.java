package it.ma.polimi.briscola;

import it.ma.polimi.briscola.controller.Briscola2PMatchNoGUIController;

/**
 * Class created appositely for the Prof. evaluation.
 *
 * @author Francesco Pinto
 */
public class MoveTest {


    /**
     * Move test string. This calss behaves as required by the specfications: received an input string representing the configuration and another string representing a sequence of moves, returns either the new configuration or the winner or the draw or the error string
     *
     * @param configuration The match configuration, whose format has been specified in the slides
     * @param moveSequence  The sequence of moves, whose format has been specified in the slides
     * @return A string representing the new configuration if the match didn't end, otherwise either the winner/draw string (formatted as specified in the slides) or a string representing the error occurred (formatted as specified in the slides)
     */
    public static String moveTest(String configuration, String moveSequence){
        Briscola2PMatchNoGUIController match = new Briscola2PMatchNoGUIController();
        return match.makeMoveSequence(configuration, moveSequence);
    }
}
