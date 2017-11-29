package it.ma.polimi.briscola;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.statistics.Briscola2PMatchRecord;

import static junit.framework.Assert.assertTrue;

/**
 * Test class containing test for Briscola2PMatchRecord
 */
public class Briscola2PMatchRecordTest {

    private Briscola2PMatchRecord[] correctRanking = new Briscola2PMatchRecord[]{
            new Briscola2PMatchRecord("giocatore1","CPU",115,5),
            new Briscola2PMatchRecord("giocatore2","CPU",110,10),
            new Briscola2PMatchRecord("giocatore1","giocatore2", 100,20),
            new Briscola2PMatchRecord("giocatore2","giocatore1", 50,70),
            new Briscola2PMatchRecord("giocatore2","giocatore1", 60,60),
            new Briscola2PMatchRecord("giocatore2","CPU",0,120)
    };


    private Briscola2PMatchRecord[] records = new Briscola2PMatchRecord[]{
            new Briscola2PMatchRecord("giocatore2","CPU",0,120),
            new Briscola2PMatchRecord("giocatore2","giocatore1", 50,70),
            new Briscola2PMatchRecord("giocatore2","CPU",110,10),
            new Briscola2PMatchRecord("giocatore2","giocatore1", 60,60),
            new Briscola2PMatchRecord("giocatore1","giocatore2", 100,20),
            new Briscola2PMatchRecord("giocatore1","CPU",115,5)
    };

    /**
     * Test compare to. In this case we use the compare to to build a correct ranking starting from an unordered array of records
     */
    @Test
    public void testCompareTo(){

        //prepare data for testing
        List<Briscola2PMatchRecord> correctRankingList = new ArrayList<>();
        for(Briscola2PMatchRecord r : correctRanking){
            correctRankingList.add(r);
        }

        List<Briscola2PMatchRecord> recordsList = new ArrayList<>();
            for(Briscola2PMatchRecord r : records)
                recordsList.add(r);


        Collections.sort(recordsList);
        Collections.reverse(recordsList); //IMPORTANT REMARK: sort orders from bottom to top, but a ranking is from top score to bottom!
        for(int i = 0; i < records.length; i++){ //check they contain the same records in the same order
            System.out.println(recordsList.get(i).getWinnerName());
            System.out.println(correctRankingList.get(i).getWinnerName());
            assertTrue(recordsList.get(i).getWinnerScore() == correctRankingList.get(i).getWinnerScore());
            assertTrue(recordsList.get(i).getWinnerName() == (correctRankingList.get(i).getWinnerName()));
        }

    }
}
