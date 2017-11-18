package it.ma.polimi.briscola;

import org.junit.Test;

import it.ma.polimi.briscola.model.briscola.BriscolaCardPointsAndRankingRules;
import it.ma.polimi.briscola.model.deck.NeapolitanCardNumbers;
import it.ma.polimi.briscola.model.deck.NeapolitanCardSuit;

import static junit.framework.Assert.assertTrue;

/**
 * Test class collecting tests for the enums with non-ordinary methods.
 */


public class EnumsTest {
    /**
     * The Neapolitan card numbers.
     */
    String neapolitanCardNumbers = "1234567JHK";
    /**
     * The Neapolitan card suit.
     */
    String neapolitanCardSuit = "BCGS";

    /**
     * Test that the right NeapolitanCardNumber is returned given the input
     */
    @Test
    public void testgetNeapolitanCardNumbers(){
        NeapolitanCardNumbers[] ncn = NeapolitanCardNumbers.values(); //test for all correct values
        for(int i = 0; i < neapolitanCardNumbers.length(); i++){
            assertTrue(ncn[i] == NeapolitanCardNumbers.getCardNumber(""+neapolitanCardNumbers.charAt(i)));

        }

        try{
            NeapolitanCardNumbers.getCardNumber("D"); //test for wrong argument
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    /**
     * Test that the right NeapolitanCardSuit is returned given the input
     */
    @Test
    public void testgetNeapolitanCardSuit(){
        NeapolitanCardSuit[] ncs = NeapolitanCardSuit.values(); //test for all correct values
        for(int i = 0; i < neapolitanCardSuit.length(); i++){
            assertTrue(ncs[i] == NeapolitanCardSuit.getCardSuit(""+neapolitanCardSuit.charAt(i)));
        }

        try{
            NeapolitanCardSuit.getCardSuit("D"); //test for wrong argument
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    /**
     * Test that the right PointValue and Rank is returned given the input
     */
    @Test
    public void testgetBriscolaCardPoinsAndRankingRulesGetPointValueAndGetRank(){
        BriscolaCardPointsAndRankingRules[] rankingRules = BriscolaCardPointsAndRankingRules.values(); //test for all correct values
        for(int i = 0; i < neapolitanCardNumbers.length(); i++){
            assertTrue(BriscolaCardPointsAndRankingRules.getPointValue(rankingRules[i].getCardNumber()) == Integer.valueOf(rankingRules[i].getPointValue()));
            assertTrue(BriscolaCardPointsAndRankingRules.getRank(rankingRules[i].getCardNumber()) == rankingRules[i].getRank());
        }

        try{
            BriscolaCardPointsAndRankingRules.getPointValue("D"); //test for wrong argument
        }catch(Exception e){
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
}
