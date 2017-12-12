package it.ma.polimi.briscola.persistency;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import it.ma.polimi.briscola.R;

/**
 * Created by utente on 10/12/17.
 */

public class SettingsManager {

    public static final int EASY = 0, MEDIUM = 1, HARD = 2, VERY_HARD = 3;
    private static final String DIFFICULTY_SETTINGS = "it.ma.polimi.briscola.difficulty.int";

    private Context context;
    public SettingsManager(Context context) {
        this.context = context;
    }

    public  int getDifficultyPreference(){
        return read(DIFFICULTY_SETTINGS, EASY);
    }

    public  void setDifficultyPreference( int newValue){
        write(DIFFICULTY_SETTINGS, newValue);
    }

    private  int read( String id, int defaultValue){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt(id, defaultValue);
    }

    private  void write( String id, int newValue ){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(id, newValue);
        editor.commit();
    }



}