package it.ma.polimi.briscola;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchRecord;

import static junit.framework.Assert.assertTrue;

/**
 * Created by utente on 15/11/17.
 */

public class Briscola2PMatchRecordTest {
    Briscola2PMatchRecord[] correctRanking = new Briscola2PMatchRecord[]{
            new Briscola2PMatchRecord("giocatore1","CPU",115,5),
            new Briscola2PMatchRecord("giocatore2","CPU",110,10),
            new Briscola2PMatchRecord("giocatore1","giocatore2", 100,20),
            new Briscola2PMatchRecord("giocatore2","giocatore1", 50,70),
            new Briscola2PMatchRecord("giocatore2","giocatore1", 60,60),
            new Briscola2PMatchRecord("giocatore2","CPU",0,120)
    };

    Briscola2PMatchRecord[] records = new Briscola2PMatchRecord[]{
            new Briscola2PMatchRecord("giocatore2","CPU",0,120),
            new Briscola2PMatchRecord("giocatore2","giocatore1", 50,70),
            new Briscola2PMatchRecord("giocatore2","CPU",110,10),
            new Briscola2PMatchRecord("giocatore2","giocatore1", 60,60),
            new Briscola2PMatchRecord("giocatore1","giocatore2", 100,20),
            new Briscola2PMatchRecord("giocatore1","CPU",115,5)
    };

    @Test
    public void testCompareTo(){
        List<Briscola2PMatchRecord> correctRankingList = new ArrayList<Briscola2PMatchRecord>();
        for(Briscola2PMatchRecord r : correctRanking){
            correctRankingList.add(r);
        System.out.println(correctRankingList.get(correctRankingList.size()-1).getPlayer0Name());
    }

    System.out.println("---------------");

    List<Briscola2PMatchRecord> recordsList = new ArrayList<Briscola2PMatchRecord>();
        for(Briscola2PMatchRecord r : records)
            recordsList.add(r);

        Collections.sort(recordsList);
        Collections.reverse(recordsList); //todo: ricorda! lui di default ordina dal basso al più alto ... ma il ranking va dal più alto al più basso
        for(int i = 0; i < records.length; i++){
            System.out.println(recordsList.get(i).getPlayer0Name());
            assertTrue(recordsList.get(i).getWinnerScore() == correctRankingList.get(i).getWinnerScore());
            assertTrue(recordsList.get(i).getWinnerName() == correctRankingList.get(i).getWinnerName());
        }

    }
}
