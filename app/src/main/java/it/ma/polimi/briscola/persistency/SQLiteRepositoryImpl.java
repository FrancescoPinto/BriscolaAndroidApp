package it.ma.polimi.briscola.persistency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.statistics.Briscola2PMatchRecord;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PFullMatchConfig;

/**
 * Class that handles SQLite databases within the app.
 *
 * @author Francesco Pinto
 */
public class SQLiteRepositoryImpl extends SQLiteOpenHelper {

    //version and name
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "recordRepository";

    //table briscola2pmatchrecords(id,player0Name,player1Name,player0Score,player1Score)
    private static final String TABLE_MATCH_RECORDS = "briscola2pmatchrecords";
    private static final String COLUMN_PLAYER0_NAME = "player0Name";
    private static final String COLUMN_PLAYER1_NAME = "player1Name";
    private static final String COLUMN_PLAYER0_SCORE = "player0Score";
    private static final String COLUMN_PLAYER1_SCORE = "player1Score";
    private static final String COLUMN_KEY_ID_MATCH_RECORDS = "id";

    //table briscola2psavedconfig(id,slotName,config)
    private static final String TABLE_CONFIG_SAVED = "briscola2psavedconfig";
    private static final String COLUMN_KEY_ID_CONFIG_SAVED = "id";
    private static final String COLUMN_SAVE_SLOT_NAME = "slotName";
    private static final String COLUMN_CONFIG = "config";


    /**
     * Instantiates a new SQLite repository.
     *
     * @param context the context
     */
    public SQLiteRepositoryImpl(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //query to create briscola2pmatchrecords table
        String query1 = "CREATE TABLE "+TABLE_MATCH_RECORDS+"("+
                COLUMN_KEY_ID_MATCH_RECORDS+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAYER0_NAME+ " VARCHAR(25),"+
                COLUMN_PLAYER1_NAME+" VARCHAR(25), " +
                COLUMN_PLAYER0_SCORE+" INTEGER," +
                COLUMN_PLAYER1_SCORE+" INTEGER)";
        //query to create briscola2psavedconfig table
        String query2 = "CREATE TABLE "+TABLE_CONFIG_SAVED+"("+
                COLUMN_KEY_ID_CONFIG_SAVED+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SAVE_SLOT_NAME+ " VARCHAR(15),"+
                COLUMN_CONFIG+" VARCHAR(50));";

        //create the tables
        db.execSQL(query1);
        db.execSQL(query2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //this method is not implemented (there's been no need to update the database)
        switch(oldVersion){
            case 1:; //incremental update
            case 2:;
            default:;

        }
    }

    /**
     * Save match record briscola 2 Players match record.
     *
     * @param matchRecord the match record to be saved
     * @return the briscola 2 p match record (contains the automatically generated id)
     */
    public Briscola2PMatchRecord saveMatchRecord(Briscola2PMatchRecord matchRecord) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER0_NAME, matchRecord.getPlayer0Name());
        values.put(COLUMN_PLAYER1_NAME, matchRecord.getPlayer1Name());
        values.put(COLUMN_PLAYER0_SCORE, matchRecord.getPlayer0Score());
        values.put(COLUMN_PLAYER1_SCORE, matchRecord.getPlayer1Score());

        long id = db.insert(TABLE_MATCH_RECORDS, null, values);
        //update the id in the input
        matchRecord.setId((int) id);
        //return it for convenience
        return matchRecord;
    }

    /**
     * Find all match records list.
     *
     * @return the list containing all match records
     */
    public List<Briscola2PMatchRecord> findAllMatchRecords() {
        //query
        String selectQuery = "SELECT * FROM " + TABLE_MATCH_RECORDS;
        //perform the query
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        List<Briscola2PMatchRecord> result = new ArrayList<>();
        if(cursor.moveToFirst()){ //fill the result list
            do{                                    //extract the columns corresponding to the constructor arguments
                result.add(new Briscola2PMatchRecord(cursor.getString(1),cursor.getString(2), cursor.getInt(3), cursor.getInt(4)));
            } while(cursor.moveToNext());
        }
        //return results
        return result;
    }

    /**
     * Find match records by id briscola 2 p match record.
     *
     * @param id the id
     * @return the briscola 2 p match record, null if no match is found
     */
    public Briscola2PMatchRecord findMatchRecordsByID(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        //parameter
        String[] ids = {""+id};
        //perform the query that finds records based on id
        Cursor result = db.query(TABLE_MATCH_RECORDS,null,COLUMN_KEY_ID_MATCH_RECORDS+" = ?",ids,null,null,null);

        if(result.moveToFirst()){ //if results are available
            do{                              //extract the columns corresponding to the constructor arguments
                 return new Briscola2PMatchRecord(result.getString(1),result.getString(2), result.getInt(3), result.getInt(4));
            } while(result.moveToNext());
        }
        //if no match, return null
        return null;
    }

    /**
     * Delete match record.
     *
     * @param id the id
     */
    public void deleteMatchRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //delete the record with that id
        db.delete(TABLE_MATCH_RECORDS,COLUMN_KEY_ID_MATCH_RECORDS+" = ?", new String[] {""+id});
    }

    /**
     * Update match record.
     *
     * @param matchRecord the match record
     * @return the int
     */
    public int updateMatchRecord(Briscola2PMatchRecord matchRecord) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER0_NAME, matchRecord.getPlayer0Name());
        values.put(COLUMN_PLAYER1_NAME, matchRecord.getPlayer1Name());
        values.put(COLUMN_PLAYER0_SCORE, matchRecord.getPlayer0Score());
        values.put(COLUMN_PLAYER1_SCORE, matchRecord.getPlayer1Score());

        // updating row having the same id
        return db.update(TABLE_MATCH_RECORDS, values, COLUMN_KEY_ID_MATCH_RECORDS+" = ?",
                new String[] { String.valueOf(matchRecord.getId()) }); //same id
    }


    /**
     * Save match config briscola 2 p match config.
     *
     * @param config the config to be saved
     * @param name   the name of the slot save
     * @return the briscola 2 p match config
     */
    public Briscola2PFullMatchConfig saveMatchConfig(Briscola2PFullMatchConfig config, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONFIG, config.toString());
        values.put(COLUMN_SAVE_SLOT_NAME, name);

        long id = db.insert(TABLE_CONFIG_SAVED, null, values);
        //update the id in the return
        config.setId((int) id);
        //return the config for convenience
        return config;
    }

    /**
     * Find all match config list.
     *
     * @return the list containing all match configs
     */
    public List<Briscola2PFullMatchConfig> findAllMatchConfig() {
        //find all query
        String selectQuery = "SELECT * FROM " + TABLE_CONFIG_SAVED;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        List<Briscola2PFullMatchConfig> result = new ArrayList<>();
        //fill result list
        if(cursor.moveToFirst()){
            do{                                     //extract constructor arguments
                result.add(new Briscola2PFullMatchConfig(cursor.getString(2),cursor.getInt(0),cursor.getString(1)));
            } while(cursor.moveToNext());
        }
        //return results
        return result;
    }

    /**
     * Find match config by id of briscola 2 p match config.
     *
     * @param id the id
     * @return the briscola 2 p match config, null if no match is found
     */
    public Briscola2PFullMatchConfig findMatchConfigByID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] ids = {""+id}; //query parameter
        //perform query
        Cursor result = db.query(TABLE_CONFIG_SAVED,null,COLUMN_KEY_ID_CONFIG_SAVED+" = ?",ids,null,null,null); //i parametri sono: nomeTabella, cosavuoiselezionare (null = select*), condizione, parametri condizioni, group by, having, order by
        //get result
        if(result.moveToFirst()){
            do{                                 //extract constructor arguments
                return new Briscola2PFullMatchConfig(result.getString(2),result.getInt(0), result.getString(1));
            } while(result.moveToNext());
        }
        //if no match, return null
        return null;
    }

    /**
     * Delete match config.
     *
     * @param id the id of the match to be deleted
     */
    public void deleteMatchConfig(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //delete match config with that id
        db.delete(TABLE_CONFIG_SAVED,COLUMN_KEY_ID_CONFIG_SAVED+" = ?", new String[] {""+id});
    }

    /**
     * Update match config
     *
     * @param config the config
     * @param name   the name
     * @return the int
     */
    public int updateMatchConfig(Briscola2PFullMatchConfig config, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONFIG, config.toString());
        values.put(COLUMN_SAVE_SLOT_NAME, name);

        // updating row
        return db.update(TABLE_CONFIG_SAVED, values, COLUMN_KEY_ID_CONFIG_SAVED+" = ?",
                new String[] { String.valueOf(config.getId()) });
    }

    //removed ... the server doesn't provide enough information to allow a meaningful ranking
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
