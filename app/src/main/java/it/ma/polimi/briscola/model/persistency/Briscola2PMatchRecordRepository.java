package it.ma.polimi.briscola.model.persistency;

import java.util.List;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchRecord;

/**
 * Created by utente on 04/11/17.
 */

public interface Briscola2PMatchRecordRepository {

    public Briscola2PMatchRecord save(Briscola2PMatchRecord matchRecord);
    public List<Briscola2PMatchRecord> findAll();
    public void delete(int id);
    public Briscola2PMatchRecord findById(int id);
}
