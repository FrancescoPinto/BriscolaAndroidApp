package it.ma.polimi.briscola.junk;

import org.junit.Test;

/**
 * Created by utente on 23/10/17.
 */

public class JavaTest {

    @Test
    public void test(){
        String temp = "...";
        String[] tokens = temp.split(".");
        assert(tokens.length == 4);
        for(String s:tokens){
            assert(s.equals(""));
        }
    }

    @Test
    public void substringTest(){
        String gatto = "gatto";
        assert(gatto.substring(2,3).equals("tt"));
        assert(gatto.substring(0,1).equals("g"));
    }

    @Test
    public void splitTest2(){
        String stringaProva = "ba.nan.a";
        assert(stringaProva.split("\\.")[0].charAt(1) == 'a');
        assert(stringaProva.split("\\.")[0].charAt(stringaProva.split("\\.")[0].length()-1) == 'a');
    }

    @Test
    public void splitTest3(){
        String[] tokens = "0B5S4G6S2C.6G7G....".split("\\.",6);
        System.out.println("Simpatia" + tokens.length);
        for(String s:tokens){
            System.out.println("Sono la stringa" + s) ;
        }

    }


}
