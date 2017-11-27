package it.ma.polimi.briscola;

import org.junit.Test;

import it.ma.polimi.briscola.controller.daFare.Briscola2PMatchNoGUIController;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.forfullrelease.ai.Briscola2PAIRandomPlayer;

import static junit.framework.Assert.assertTrue;

/**
 * Class containing tests of the Briscola2PMatchNoGUIController. This class performs tests using the MoveTest, that contains the moveTest method specified by the slides,
 * that method in turn instantiates the Briscola2PMatchNoGUIController and controls the flow of the match starting from a string representation of the configuration and a sequence of the moves .
 */
public class Briscola2PMatchNoGUIControllerTest {


    //del prof
    //Here are some configurations. NAMING CONVENTION: stratingConfig + number of config (optionally followed by  _ + movesequence that lead to that config.
    //The following startingConfig1 correspond to the examples presented in the slides by the prof.

    private String startingConfig1 = "0B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B..JCKG2B.1CKS3G..";
    private String startingConfig1_0 = "1B5S4G6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.JC.KG2B.1CKS3G..";
    private String startingConfig1_00 = "1B6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B..KG2B4G.KS3G5S..JC1C";
    private String startingConfig1_001 = "0B6S2C5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.3G.KG2B4G.KS5S..JC1C";
    private String startingConfig1_0011 = "0B5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B..KG4G6S.KS5S2C.3G2B.JC1C";
    private String startingConfig1_00110 = "1B5GKB7B6CHCHB1GKC5C4B1BHG7C6BJS6G7G4C3C7SJBHS2S3S4S1S2G3BJG5B.KG.4G6S.KS5S2C.3G2B.JC1C";


     // Now follows a sequence of configs that describe a complete match. The starting config and the moveSequence are given below. Then, in order to simplify the test writing, an array is defined, that contains all the configs (starting config included) that are obtained applying one at a time the moves in the specified sequence to the starting config.

    private String startingConfig2 = "1GHGKGHS7C2S3B3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..3G1C6C.JC2C5B..";
    private String moveSequenceConfig2 = "2120200110210201120022102122222212221100";
    private String[] Config2Array = {
            "1GHGKGHS7C2S3B3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..3G1C6C.JC2C5B..",
            "0GHGKGHS7C2S3B3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.5B.3G1C6C.JC2C..",//player 1 played 2
            "1GHS7C2S3B3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..3G6CKG.JC2CHG..5B1C",//player 0 played 1
            "0GHS7C2S3B3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.HG.3G6CKG.JC2C..5B1C",//ecc. see the moveSequenceConfig2 string
            "0G2S3B3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..6CKGHS.JC2C7C.HG3G.5B1C",
            "1G2S3B3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.HS.6CKG.JC2C7C.HG3G.5B1C",
            "0G3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..6CKG2S.2C7C3B.HG3GHSJC.5B1C",
            "1G3S4S7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.6C.KG2S.2C7C3B.HG3GHSJC.5B1C",
            "1G7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..KG2S4S.2C3B3S.HG3GHSJC.5B1C6C7C",
            "0G7B4B5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.3B.KG2S4S.2C3S.HG3GHSJC.5B1C6C7C",
            "0G5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..2S4S7B.2C3S4B.HG3GHSJC3BKG.5B1C6C7C",
            "1G5S6GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.7B.2S4S.2C3S4B.HG3GHSJC3BKG.5B1C6C7C",
            "0GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..2S4S5S.2C4B6G.HG3GHSJC3BKG7B3S.5B1C6C7C",
            "1GKSKB6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.2S.4S5S.2C4B6G.HG3GHSJC3BKG7B3S.5B1C6C7C",
            "1G6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..4S5SKB.2C4BKS.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G",
            "0G6SJB1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.2C.4S5SKB.4BKS.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G",
            "1G1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..4SKBJB.4BKS6S.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5S",
            "0G1G5CJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.KS.4SKBJB.4B6S.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5S",
            "1GJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG..4SKB5C.4B6S1G.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB",
            "0GJS7S2G6BHB4CHC4GKC1S7G5G2B3C1BJG.4B.4SKB5C.6S1G.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB",
            "1G2G6BHB4CHC4GKC1S7G5G2B3C1BJG..KB5C7S.6S1GJS.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4S",
            "0G2G6BHB4CHC4GKC1S7G5G2B3C1BJG.JS.KB5C7S.6S1G.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4S",
            "1GHB4CHC4GKC1S7G5G2B3C1BJG..KB5C6B.6S1G2G.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S",
            "0GHB4CHC4GKC1S7G5G2B3C1BJG.1G.KB5C6B.6S2G.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S",
            "1GHC4GKC1S7G5G2B3C1BJG..5C6B4C.6S2GHB.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKB",
            "0GHC4GKC1S7G5G2B3C1BJG.HB.5C6B4C.6S2G.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKB",
            "1GKC1S7G5G2B3C1BJG..5C4C4G.6S2GHC.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "0GKC1S7G5G2B3C1BJG.HC.5C4C4G.6S2G.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "0G7G5G2B3C1BJG..5C4CKC.6S2G1S.HG3GHSJC3BKG7B3SHC4G.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "1G7G5G2B3C1BJG.KC.5C4C.6S2G1S.HG3GHSJC3BKG7B3SHC4G.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "0G2B3C1BJG..5C4C7G.6S2G5G.HG3GHSJC3BKG7B3SHC4GKC1S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "1G2B3C1BJG.7G.5C4C.6S2G5G.HG3GHSJC3BKG7B3SHC4GKC1S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "0G1BJG..5C4C2B.6S2G3C.HG3GHSJC3BKG7B3SHC4GKC1S7G5G.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "1G1BJG.4C.5C2B.6S2G3C.HG3GHSJC3BKG7B3SHC4GKC1S7G5G.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B",
            "1G..5C2BJG.6S2G1B.HG3GHSJC3BKG7B3SHC4GKC1S7G5G.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C",//TODO la briscola va rimossa o meno dal deck? direi che la briscola è "nel deck" e quindi quando la si prende in mano il deck si svuota
            "0G.1B.5C2BJG.6S2G.HG3GHSJC3BKG7B3SHC4GKC1S7G5G.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C",
            "0G..5C2B.6S2G.HG3GHSJC3BKG7B3SHC4GKC1S7G5G1BJG.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C",
            "1G.2B.5C.6S2G.HG3GHSJC3BKG7B3SHC4GKC1S7G5G1BJG.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C",
            "1G..5C.6S.HG3GHSJC3BKG7B3SHC4GKC1S7G5G1BJG.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C2B2G",
            "0G.6S.5C..HG3GHSJC3BKG7B3SHC4GKC1S7G5G1BJG.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C2B2G",
            "WINNER073"
    };

    //to test a draw (obtained modifying the last config in the previous array so that the players have the same score)
    private String[] Config3Array = {
            "0G.6S.5C..HSJC3BKG7B3SHC4GKC1S7G5G1BJG.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C2B2GHG3G",
            "DRAW"
    };


    // used to make player 1 win (in the match of config2Array player0 won!)
    private String[] Config4Array = {
            "0G.6S.5C..3BKG7B3SHC4GKC1S7G5G1BJG.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C2B2GHG3GHSJC",
            "WINNER165"
    };

    //for illegal input moves
    private String[] Config5Array = {
            "1G.2B.5C.6S2G.HG3GHSJC3BKG7B3SHC4GKC1S7G5G1BJG.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B4C3C",
            "WINNER165"
    };


    //another full match
    private String moves6 = "0012200220012122110011202101011111200100";
    private String[] config6Array = {
            "0C3GHG3S6S2S3C1G5S6B4C4B1CJSKC2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C..4S6GKS.HB7S7G..",
            "1C3GHG3S6S2S3C1G5S6B4C4B1CJSKC2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C.4S.6GKS.HB7S7G..",
            "0C3S6S2S3C1G5S6B4C4B1CJSKC2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C..6GKS3G.7S7GHG.4SHB.",
            "1C3S6S2S3C1G5S6B4C4B1CJSKC2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C.KS.6G3G.7S7GHG.4SHB.",
            "0C2S3C1G5S6B4C4B1CJSKC2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C..6G3G3S.7S7G6S.4SHBKSHG.",
            "1C2S3C1G5S6B4C4B1CJSKC2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C.3S.6G3G.7S7G6S.4SHBKSHG.",
            "0C1G5S6B4C4B1CJSKC2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C..6G3G2S.7G6S3C.4SHBKSHG3S7S.",
            "1C1G5S6B4C4B1CJSKC2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C.6G.3G2S.7G6S3C.4SHBKSHG3S7S.",
            "1C6B4C4B1CJSKC2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C..3G2S5S.7G6S1G.4SHBKSHG3S7S.6G3C",
            "0C6B4C4B1CJSKC2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C.1G.3G2S5S.7G6S.4SHBKSHG3S7S.6G3C",
            "1C4B1CJSKC2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C..2S5S4C.7G6S6B.4SHBKSHG3S7S.6G3C1G3G",
            "0C4B1CJSKC2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C.7G.2S5S4C.6S6B.4SHBKSHG3S7S.6G3C1G3G",
            "1CJSKC2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C..2S4C1C.6S6B4B.4SHBKSHG3S7S.6G3C1G3G7G5S",
            "0CJSKC2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C.4B.2S4C1C.6S6B.4SHBKSHG3S7S.6G3C1G3G7G5S",
            "0C2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C..2S1CJS.6S6BKC.4SHBKSHG3S7S4B4C.6G3C1G3G7G5S",
            "1C2C6C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C.JS.2S1C.6S6BKC.4SHBKSHG3S7S4B4C.6G3C1G3G7G5S",
            "1C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C..2S1C6C.6S6B2C.4SHBKSHG3S7S4B4C.6G3C1G3G7G5SJSKC",
            "0C3BKBHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C.6B.2S1C6C.6S2C.4SHBKSHG3S7S4B4C.6G3C1G3G7G5SJSKC",
            "0CHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C..2S6C3B.6S2CKB.4SHBKSHG3S7S4B4C6B1C.6G3C1G3G7G5SJSKC",
            "1CHS2G7BHCJBJGKG5G1S4G5BJC1B2B7C5C.2S.6C3B.6S2CKB.4SHBKSHG3S7S4B4C6B1C.6G3C1G3G7G5SJSKC",
            "1C7BHCJBJGKG5G1S4G5BJC1B2B7C5C..6C3B2G.2CKBHS.4SHBKSHG3S7S4B4C6B1C.6G3C1G3G7G5SJSKC2S6S",
            "0C7BHCJBJGKG5G1S4G5BJC1B2B7C5C.KB.6C3B2G.2CHS.4SHBKSHG3S7S4B4C6B1C.6G3C1G3G7G5SJSKC2S6S",
            "0CJBJGKG5G1S4G5BJC1B2B7C5C..6C2G7B.2CHSHC.4SHBKSHG3S7S4B4C6B1CKB3B.6G3C1G3G7G5SJSKC2S6S",
            "1CJBJGKG5G1S4G5BJC1B2B7C5C.7B.6C2G.2CHSHC.4SHBKSHG3S7S4B4C6B1CKB3B.6G3C1G3G7G5SJSKC2S6S",
            "1CKG5G1S4G5BJC1B2B7C5C..6C2GJG.HSHCJB.4SHBKSHG3S7S4B4C6B1CKB3B.6G3C1G3G7G5SJSKC2S6S7B2C",
            "0CKG5G1S4G5BJC1B2B7C5C.JB.6C2GJG.HSHC.4SHBKSHG3S7S4B4C6B1CKB3B.6G3C1G3G7G5SJSKC2S6S7B2C",
            "1C1S4G5BJC1B2B7C5C..6CJG5G.HSHCKG.4SHBKSHG3S7S4B4C6B1CKB3B.6G3C1G3G7G5SJSKC2S6S7B2CJB2G",
            "0C1S4G5BJC1B2B7C5C.HS.6CJG5G.HCKG.4SHBKSHG3S7S4B4C6B1CKB3B.6G3C1G3G7G5SJSKC2S6S7B2CJB2G",
            "1C5BJC1B2B7C5C..6C5G4G.HCKG1S.4SHBKSHG3S7S4B4C6B1CKB3B.6G3C1G3G7G5SJSKC2S6S7B2CJB2GHSJG",
            "0C5BJC1B2B7C5C.HC.6C5G4G.KG1S.4SHBKSHG3S7S4B4C6B1CKB3B.6G3C1G3G7G5SJSKC2S6S7B2CJB2GHSJG",
            "1C1B2B7C5C..6C4GJC.KG1S5B.4SHBKSHG3S7S4B4C6B1CKB3B.6G3C1G3G7G5SJSKC2S6S7B2CJB2GHSJGHC5G",
            "0C1B2B7C5C.1S.6C4GJC.KG5B.4SHBKSHG3S7S4B4C6B1CKB3B.6G3C1G3G7G5SJSKC2S6S7B2CJB2GHSJGHC5G",
            "1C7C5C..6CJC2B.KG5B1B.4SHBKSHG3S7S4B4C6B1CKB3B.6G3C1G3G7G5SJSKC2S6S7B2CJB2GHSJGHC5G1S4G",
            "0C7C5C.5B.6CJC2B.KG1B.4SHBKSHG3S7S4B4C6B1CKB3B.6G3C1G3G7G5SJSKC2S6S7B2CJB2GHSJGHC5G1S4G",
            "0C..6C2B7C.KG1B5C.4SHBKSHG3S7S4B4C6B1CKB3B5BJC.6G3C1G3G7G5SJSKC2S6S7B2CJB2GHSJGHC5G1S4G",
            "1C.7C.6C2B.KG1B5C.4SHBKSHG3S7S4B4C6B1CKB3B5BJC.6G3C1G3G7G5SJSKC2S6S7B2CJB2GHSJGHC5G1S4G",
            "0C..6C2B.1B5C.4SHBKSHG3S7S4B4C6B1CKB3B5BJC7CKG.6G3C1G3G7G5SJSKC2S6S7B2CJB2GHSJGHC5G1S4G",
            "1C.6C.2B.1B5C.4SHBKSHG3S7S4B4C6B1CKB3B5BJC7CKG.6G3C1G3G7G5SJSKC2S6S7B2CJB2GHSJGHC5G1S4G",
            "0C..2B.1B.4SHBKSHG3S7S4B4C6B1CKB3B5BJC7CKG6C5C.6G3C1G3G7G5SJSKC2S6S7B2CJB2GHSJGHC5G1S4G",
            "1C.2B..1B.4SHBKSHG3S7S4B4C6B1CKB3B5BJC7CKG6C5C.6G3C1G3G7G5SJSKC2S6S7B2CJB2GHSJGHC5G1S4G",
            "WINNER169"
    };


    private String HAND_indexOutOfBoundsErrorMessage1 =  "ERROR:Briscola2PHand: index can not exceed the maximum of ";

    private String HAND_indexOutOfBoundsErrorMessage2 = "ERROR:Briscola2PHand: index can not exceed the current bounds of the list";


    //todo il prof dice che NON testerà configurazioni inconsistenti, MA TESTERA' MOSSE ILLECITE!)

    /**
     * Move test. Performs tests based on data.
     */
    @Test
    public void moveTest(){

        //test del prof
        String outcome = MoveTest.moveTest(startingConfig1,"0");


        assertTrue(outcome.equals(startingConfig1_0));
        outcome = MoveTest.moveTest(startingConfig1,"00");
        assertTrue(outcome.equals(startingConfig1_00));
        outcome = MoveTest.moveTest(startingConfig1,"001");
        assertTrue(outcome.equals(startingConfig1_001));
        outcome = MoveTest.moveTest(startingConfig1,"0011");
        assertTrue(outcome.equals(startingConfig1_0011));
        outcome = MoveTest.moveTest(startingConfig1,"00110");
        assertTrue(outcome.equals(startingConfig1_00110));

        //test match completo, parti da qualunque configurazione nel match e sii in grado di raggiungere tutte le successive (tenendo fissa la sequenza globale di mosse)
        for(int i = 0; i <= moveSequenceConfig2.length()-1; i++){ //dalla config i-esima

            for(int j = i+1; j <= moveSequenceConfig2.length();j++) { //prova a raggiungere tutte le config successive seguendo le mosse
                outcome = MoveTest.moveTest(Config2Array[i], moveSequenceConfig2.substring(i, j));
                assertTrue(outcome.equals(Config2Array[j]));
            }
        }

        //test pareggio
        outcome = MoveTest.moveTest(Config3Array[0], "0");
        assertTrue(outcome.equals(Config3Array[1]));

        //test vince 1
        outcome = MoveTest.moveTest(Config4Array[0], "0");
        assertTrue(outcome.equals(Config4Array[1]));

        //test mossa illecita, partendo dallo stato iniziale di config2
        outcome = MoveTest.moveTest(Config2Array[0], "54");
        assertTrue(outcome.equals(HAND_indexOutOfBoundsErrorMessage1 + 2));

        //test mossa illecita, partendo da uno stato intermedio di config2
        outcome = MoveTest.moveTest(Config2Array[22], "4");
        assertTrue(outcome.equals(HAND_indexOutOfBoundsErrorMessage1 + 2));

        //test mossa illecita partendo da uno stato terminale
        outcome = MoveTest.moveTest(Config3Array[0], "4");
        assertTrue(outcome.equals(HAND_indexOutOfBoundsErrorMessage1 + 2));

        //test mossa illecita partendo da uno stato terminale
        outcome = MoveTest.moveTest(Config3Array[0], "2");
        assertTrue(outcome.equals(HAND_indexOutOfBoundsErrorMessage2));

        //test mossa illecita
        outcome = MoveTest.moveTest(Config5Array[0], "2");
        assertTrue(outcome.equals(String.format(HAND_indexOutOfBoundsErrorMessage2, 2)));

        //test mossa non valida
        outcome = MoveTest.moveTest(Config2Array[0], "-1");
        assertTrue(outcome.equals(String.format("ERROR:For input string: \"-\"", 2)));

        //altro test match completo, raggiungere a partire da qualunque stato tutti i successivi
        for(int i = 0; i <= moves6.length()-1; i++){ //a partire dallo stato i-esimo
            for(int j = i+1; j <= moves6.length();j++) { //prova a raggiungere tutti i successivi
                outcome = MoveTest.moveTest(config6Array[i], moves6.substring(i, j));
                assertTrue(outcome.equals(config6Array[j]));
            }
        }

    }


    /**
     * To generate matches from a random starting config (or resumes a match from a given config), since moves are made at random, one can only check manually the correctness of the moves looking at the System.out
     */
    @Test
    public void initializeMatchAndResumeMatchTestWithRandomAI(){
        Briscola2PAIRandomPlayer randomPlayer = new Briscola2PAIRandomPlayer();
        //use just one player since they are both random ...

        for(int j = 0; j < 20; j++) //play 20 random matches
        {
            Briscola2PMatchNoGUIController controller = new Briscola2PMatchNoGUIController();
            controller.startNewMatch();
            String config = controller.getConfig().toString();
            int move;
            for (int i = 0; i < 40; i++) {
                System.out.println(config);
                move = randomPlayer.chooseMove(new Briscola2PMatchConfig(config));
                System.out.println(move);
                config = MoveTest.moveTest(config, "" + move);
            }

            checkMatchEndedCorrectly(config);

        }
        Briscola2PMatchNoGUIController controller = new Briscola2PMatchNoGUIController();
        controller.resumeMatch("1GKC1S7G5G2B3C1BJG..5C4C4G.6S2GHC.HG3GHSJC3BKG7B3S.5B1C6C7C2S6G2C5SKSJB4B4SJS7S1GKBHB6B");
        String config = controller.getConfig().toString();
        int move;
        while(!config.equals("DRAW") && !config.substring(0, 7).equals("WINNER0") && !config.substring(0, 7).equals("WINNER1")){
            System.out.println(config);
            move = randomPlayer.chooseMove(new Briscola2PMatchConfig(config));
            System.out.println(move);
            config = MoveTest.moveTest(config, "" + move);
        }

        checkMatchEndedCorrectly(config);

    }
    //convenience function
private void checkMatchEndedCorrectly(String config){
    boolean correctMatch = false;
    System.out.println("------------------------------------------------------------------------------------------------\n"+
            config+
            "\n----------------------------------------------------------------------------------------------");
    if (config.equals("DRAW"))
        correctMatch = true;
    else if (config.substring(0, 7).equals("WINNER0") && Integer.valueOf(config.substring(7)) > 60)
        correctMatch = true;
    else if (config.substring(0, 7).equals("WINNER1") && Integer.valueOf(config.substring(7)) > 60)
        correctMatch = true;
    assertTrue(correctMatch);
}

}

