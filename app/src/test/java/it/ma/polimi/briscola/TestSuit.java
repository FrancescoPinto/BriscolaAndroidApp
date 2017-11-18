package it.ma.polimi.briscola;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Class that, if run, performs all the tests produced for the delivery.
 */

@RunWith(Suite.class)

@Suite.SuiteClasses({
        Briscola2PMatchNoGUIControllerTest.class,
        Briscola2PMatchConfigTest.class,
        Briscola2PHandTest.class,
        Briscola2PPileTest.class,
        Briscola2PSurfaceTest.class,
        EnumsTest.class,
        NeapolitanCardTest.class,
        NeapolitanDeckTest.class
})
public class TestSuit {
}
