package it.ma.polimi.briscola;

import org.junit.Test;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatch;

import static junit.framework.Assert.assertTrue;

/**
 * Created by utente on 31/10/17.
 */

public class Biscola2PMatchTest {

    String startingConfig1 = "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B..JCKG2B.1CKS3G..";
    String startingConfig1_0 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.JC.KG2B.1CKS3G..";
    String startingConfig1_00 = "1B6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B..KG2B4G.KS3G5S..JC1C";
    String startingConfig1_001 = "0B6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.3G.KG2B4G.KS5S..JC1C";
    String startingConfig1_0011 = "0B5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B..KG4G6S.KS5S2C.3G2B.JC1C";
    String startingConfig1_00110 = "1B5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.KG.4G6S.KS5S2C.3G2B.JC1C";

    //todo testa stati inconsistenti

    @Test
    public void moveTest(){
        String outcome = Briscola2PMatch.moveTest(startingConfig1,"0");
        assertTrue(outcome.equals(startingConfig1_0));
        outcome = Briscola2PMatch.moveTest(startingConfig1,"00");
        System.out.println(outcome);
        assertTrue(outcome.equals(startingConfig1_00));
        outcome = Briscola2PMatch.moveTest(startingConfig1,"001");
        assertTrue(outcome.equals(startingConfig1_001));
        outcome = Briscola2PMatch.moveTest(startingConfig1,"0011");
        assertTrue(outcome.equals(startingConfig1_0011));
        outcome = Briscola2PMatch.moveTest(startingConfig1,"00110");
        assertTrue(outcome.equals(startingConfig1_00110));

        //todo test per i casi limite (i.e. inizializzazione, configurazioni sbagliate, stati diversi ecc. ecc., finite le carte in deck, nelle mani, chiusura match, WINNER, DRAW E SCATENARE TUTTI GLI ERRORI etc. etc.)

    }
}
