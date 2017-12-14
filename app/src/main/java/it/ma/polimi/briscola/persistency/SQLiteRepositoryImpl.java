package it.ma.polimi.briscola.persistency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.statistics.Briscola2PMatchRecord;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;

/**
 * Created by utente on 25/10/17.
 */

public class SQLiteRepositoryImpl extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "recordRepository";

    private static final String TABLE_MATCH_RECORDS = "briscola2pmatchrecords";
    private static final String COLUMN_PLAYER0_NAME = "player0Name";
    private static final String COLUMN_PLAYER1_NAME = "player1Name";
    private static final String COLUMN_PLAYER0_SCORE = "player0Score";
    private static final String COLUMN_PLAYER1_SCORE = "player1Score";
    private static final String COLUMN_KEY_ID_MATCH_RECORDS = "id";

    private static final String TABLE_CONFIG_SAVED = "briscola2psavedconfig";
    private static final String COLUMN_KEY_ID_CONFIG_SAVED = "id";
    private static final String COLUMN_SAVE_SLOT_NAME = "slotName";
    private static final String COLUMN_CONFIG = "config";



    private static SQLiteRepositoryImpl repo = null;


    public SQLiteRepositoryImpl(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String query1 = "CREATE TABLE "+TABLE_MATCH_RECORDS+"("+
                COLUMN_KEY_ID_MATCH_RECORDS+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAYER0_NAME+ " VARCHAR(25),"+
                COLUMN_PLAYER1_NAME+" VARCHAR(25), " +
                COLUMN_PLAYER0_SCORE+" INTEGER," +
                COLUMN_PLAYER1_SCORE+" INTEGER)";
        String query2 = "CREATE TABLE "+TABLE_CONFIG_SAVED+"("+
                COLUMN_KEY_ID_CONFIG_SAVED+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SAVE_SLOT_NAME+ " VARCHAR(15),"+
                COLUMN_CONFIG+" VARCHAR(50));";

        db.execSQL(query1);
        db.execSQL(query2);

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

    public Briscola2PMatchRecord saveMatchRecord(Briscola2PMatchRecord matchRecord) {
        SQLiteDatabase db = this.getWritableDatabase();

        //todo, check che non è vuota, perché SQLite non consente di infilare tuple vuote ...
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER0_NAME, matchRecord.getPlayer0Name());
        values.put(COLUMN_PLAYER1_NAME, matchRecord.getPlayer1Name());
        values.put(COLUMN_PLAYER0_SCORE, matchRecord.getPlayer0Score());
        values.put(COLUMN_PLAYER1_SCORE, matchRecord.getPlayer1Score());


        long id = db.insert(TABLE_MATCH_RECORDS, null, values);
        matchRecord.setId((int) id);

        return matchRecord;
    }

    public List<Briscola2PMatchRecord> findAllMatchRecords() {
        String selectQuery = "SELECT * FROM " + TABLE_MATCH_RECORDS;
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

    public Briscola2PMatchRecord findMatchRecordsByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] ids = {""+id};
        Cursor result = db.query(TABLE_MATCH_RECORDS,null,COLUMN_KEY_ID_MATCH_RECORDS+" = ?",ids,null,null,null); //i parametri sono: nomeTabella, cosavuoiselezionare (null = select*), condizione, parametri condizioni, group by, having, order by

        if(result.moveToFirst()){
            do{
                 return new Briscola2PMatchRecord(result.getString(1),result.getString(2), result.getInt(3), result.getInt(4));
            } while(result.moveToNext());
        }
        return null;
    }

    public void deleteMatchRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MATCH_RECORDS,COLUMN_KEY_ID_MATCH_RECORDS+" = ?", new String[] {""+id});
    }

    public int updateMatchRecord(Briscola2PMatchRecord matchRecord) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER0_NAME, matchRecord.getPlayer0Name());
        values.put(COLUMN_PLAYER1_NAME, matchRecord.getPlayer1Name());
        values.put(COLUMN_PLAYER0_SCORE, matchRecord.getPlayer0Score());
        values.put(COLUMN_PLAYER1_SCORE, matchRecord.getPlayer1Score());

        // updating row
        return db.update(TABLE_MATCH_RECORDS, values, COLUMN_KEY_ID_MATCH_RECORDS+" = ?",
                new String[] { String.valueOf(matchRecord.getId()) });
    }


    public Briscola2PMatchConfig saveMatchConfig(Briscola2PMatchConfig config, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        //todo, check che non è vuota, perché SQLite non consente di infilare tuple vuote ...
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONFIG, config.toString());
        values.put(COLUMN_SAVE_SLOT_NAME, name);



        long id = db.insert(TABLE_CONFIG_SAVED, null, values);
        config.setId((int) id);

        return config;
    }

    public List<Briscola2PMatchConfig> findAllMatchConfig() {
        String selectQuery = "SELECT * FROM " + TABLE_CONFIG_SAVED;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null); //null è per i parametri, ma per ora non ne hai todo parametrizzare la query sull'utente se il ranking è personale
        List<Briscola2PMatchConfig> result = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                result.add(new Briscola2PMatchConfig(cursor.getString(2),cursor.getInt(0),cursor.getString(1)));
            } while(cursor.moveToNext());
        }

        return result;
    }

    public Briscola2PMatchConfig findMatchConfigByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] ids = {""+id};
        Cursor result = db.query(TABLE_CONFIG_SAVED,null,COLUMN_KEY_ID_CONFIG_SAVED+" = ?",ids,null,null,null); //i parametri sono: nomeTabella, cosavuoiselezionare (null = select*), condizione, parametri condizioni, group by, having, order by

        if(result.moveToFirst()){
            do{
                return new Briscola2PMatchConfig(result.getString(2),result.getInt(0), result.getString(1));
            } while(result.moveToNext());
        }
        return null;
    }

    public void deleteMatchConfig(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONFIG_SAVED,COLUMN_KEY_ID_CONFIG_SAVED+" = ?", new String[] {""+id});
    }

    public int updateMatchConfig(Briscola2PMatchConfig config, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONFIG, config.toString());
        values.put(COLUMN_SAVE_SLOT_NAME, name);

        // updating row
        return db.update(TABLE_CONFIG_SAVED, values, COLUMN_KEY_ID_CONFIG_SAVED+" = ?",
                new String[] { String.valueOf(config.getId()) });
    }

    /*public List<Briscola2PAggregatedData> getRanking(){
        List<Briscola2PMatchRecord> matchRecords = findAllMatchRecords();
        HashSet<String> players = new HashSet<>();

        for(Briscola2PMatchRecord mr : matchRecords){
            players.add(mr.getPlayer0Name());
        }

        List<Briscola2PAggregatedData> aggregatedData = new ArrayList<>();
        for(String player : players){
            List<Briscola2PMatchRecord> temp = new ArrayList<>();
            for(Briscola2PMatchRecord mr : matchRecords){
                if(player.equals(mr.getPlayer0Name())){
                    temp.add(mr);
                }
            }
            aggregatedData.add(new Briscola2PAggregatedData(temp));
        }
        Collections.sort(aggregatedData);
        Collections.reverse(aggregatedData);
        return aggregatedData;
    }*/



}
