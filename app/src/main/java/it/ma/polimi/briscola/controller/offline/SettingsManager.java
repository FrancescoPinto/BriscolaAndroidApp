package it.ma.polimi.briscola.controller.offline;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import it.ma.polimi.briscola.R;

/**
 * Created by utente on 10/12/17.
 */

public class SettingsManager {

    public static final int EASY = 0, MEDIUM = 1, HARD = 2, VERY_HARD = 3;

    public static int getDifficultyPreference(Activity activity){
        return read(activity, R.id.difficulty_settings, EASY);
    }

    public static void setDifficultyPreference(Activity activity, int newValue){
        write(activity, R.id.difficulty_settings, newValue);
    }

    public static boolean getIsAudioOn(Activity activity){
        return read(activity, R.id.toggle_audio, true);
    }

    public static void toggleAudioOn(Activity activity){
        boolean previous = getIsAudioOn(activity);
        write(activity, R.id.toggle_audio, !previous);
    }

    public static boolean getIsSfxOn(Activity activity){
        return read(activity, R.id.toggle_sfx, true);
    }

    public static void toggleSfxOn(Activity activity){
        boolean previous = getIsSfxOn(activity);
        write(activity, R.id.toggle_sfx, !previous);
    }

    private static int read(Activity activity, int id, int defaultValue){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(activity.getString(id), defaultValue);
    }

    private static void write(Activity activity, int id, int newValue ){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(activity.getString(id), newValue);
        editor.commit();
    }

    private static boolean read(Activity activity, int id, boolean defaultValue){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean(activity.getString(id), defaultValue);
    }

    private static void write(Activity activity, int id, boolean newValue ){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(activity.getString(id), newValue);
        editor.commit();
    }

}