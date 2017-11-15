package it.ma.polimi.briscola;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by utente on 11/11/17.
 */

@RunWith(Suite.class)

@Suite.SuiteClasses({
        Briscola2PMatchNoGUIControllerTest.class,
        Briscola2PMatchNoGUIControllerConfigTest.class,
        Briscola2PHandTest.class,
        Briscola2PPileTest.class,
        Briscola2PSurfaceTest.class,
        EnumsTest.class,
        NeapolitanCardTest.class,
        NeapolitanDeckTest.class
})
public class TestSuit {
}
