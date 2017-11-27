package it.ma.polimi.briscola.persistency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchRecord;

/**
 * Created by utente on 25/10/17.
 */

public class SQLiteBriscola2PMatchRecordRepositoryImpl extends SQLiteOpenHelper implements Briscola2PMatchRecordRepository{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "recordRepository";

    public SQLiteBriscola2PMatchRecordRepositoryImpl(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String query = "CREATE TABLE briscola2pmatchrecords(id INTEGER PRIMARY KEY AUTOINCREMENT, player0Name VARCHAR(25), player1Name VARCHAR(25), player0Score INTEGER, player1Score INTEGER)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        switch(oldVersion){
            case 1:; //in caso serva, fai così: chiama db.execSQL(UPDATE_FROM_1_TO_2) dove l'argomento è la query che fa update del database da versione 1 a 2
            case 2:; //poi fallo da 2 a 3 (NON mettere break PRIMA!)
            default:;

                //OPPURE, ANZICHE' FARE UPDATE INCREMENTALI, DROPPA LA TABLE E CHIAMA TU onCreate(db);
        }
    }

    @Override
    public Briscola2PMatchRecord save(Briscola2PMatchRecord matchRecord) {
        SQLiteDatabase db = this.getWritableDatabase();

        //todo, check che non è vuota, perché SQLite non consente di infilare tuple vuote ...
        ContentValues values = new ContentValues();
        values.put("player0Name", matchRecord.getPlayer0Name()); //todo, usa delle costanti come attributi per rappresentare i nomi delle colonne!
        values.put("player1Name", matchRecord.getPlayer1Name()); //todo, usa delle costanti come attributi per rappresentare i nomi delle colonne!
        values.put("player0Score", matchRecord.getPlayer0Score()); //todo, usa delle costanti come attributi per rappresentare i nomi delle colonne!
        values.put("player1Score", matchRecord.getPlayer1Score()); //todo, usa delle costanti come attributi per rappresentare i nomi delle colonne!


        long id = db.insert("briscola2pmatchrecords", null, values); //todo, anche qui costante al posto di stringa del nome
        matchRecord.setId((int) id);

        return matchRecord;
    }

    @Override
    public List<Briscola2PMatchRecord> findAll() {
        String selectQuery = "SELECT * FROM " + "briscola2pmatchrecords"; //todo constant
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null); //null è per i parametri, ma per ora non ne hai todo parametrizzare la query sull'utente se il ranking è personale
        List<Briscola2PMatchRecord> result = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                result.add(new Briscola2PMatchRecord(cursor.getString(1),cursor.getString(2), cursor.getInt(3), cursor.getInt(4)));
            } while(cursor.moveToNext());
        }

        return result;
    }

    @Override
    public Briscola2PMatchRecord findById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] ids = {""+id};
        Cursor result = db.query("briscola2pmatchrecords",null,"id = ?",ids,null,null,null); //i parametri sono: nomeTabella, cosavuoiselezionare (null = select*), condizione, parametri condizioni, group by, having, order by

        if(result.moveToFirst()){
            do{
                 return new Briscola2PMatchRecord(result.getString(1),result.getString(2), result.getInt(3), result.getInt(4));
            } while(result.moveToNext());
        }
        return null;
    }

    @Override
    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("briscola2pmatchrecords","id = ?", new String[] {""+id});
    }

    /*
    todo: cose da salvare
     config di partite interrotte
        i record delle partite passate
        statistiche
        settings e dati utente (magari non nel database)*/
}
