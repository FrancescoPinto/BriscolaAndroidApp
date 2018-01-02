package it.ma.polimi.briscola.persistency;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Class that handles the settings of the application.
 *
 * @author Francesco Pinto
 */
public class SettingsManager {

    /**
     * Constant encoding easy difficulty
     */
    public static final int DIFFICULTY_EASY = 0, /**
      * Constant encoding easy difficulty
     */
    DIFFICULTY_MEDIUM = 1, /**
    * Constant encoding medium difficulty
     */
    DIFFICULTY_HARD = 2, /**
     * Constant encoding hard difficulty
     */
    DIFFICULTY_VERY_HARD = 3;
    private static final String DIFFICULTY_SETTINGS = "it.ma.polimi.briscola.difficulty.int";
    /**
     * The constant representing normal velocity.
     */
    public static final int VELOCITY_NORMAL = 0, /**
     * The constant representing fast velocity.
     */
    VELOCITY_FAST = 1, /**
     * The constant representing very fast velocity.
     */
    VELOCITY_VERY_FAST = 2;
    private static final String VELOCITY_SETTINGS = "it.ma.polimi.briscola.velocity.int";

    public static final int FRENCH = 0, MINIMAL_FRENCH = 1;
    private static final String CARD_VIEW_SETTINGS = "it.ma.polimi.briscola.cardview.int";

    private static final String SOUNDS_PREF_KEY = "it.ma.polimi.briscola.sounds.boolean";
    private static final String MUSIC_PREF_KEY = "it.ma.polimi.briscola.music.boolean";

    private Context context;

    /**
     * Instantiates a new Settings manager.
     *
     * @param context the context
     */
    public SettingsManager(Context context) {
        this.context = context;
    }

    /**
     * Get difficulty preference int.
     *
     * @return the int
     */
    public  int getDifficultyPreference(){
        return read(DIFFICULTY_SETTINGS, DIFFICULTY_EASY);
    }

    /**
     * Set difficulty preference.
     *
     * @param newValue the new value
     */
    public  void setDifficultyPreference( int newValue){
        write(DIFFICULTY_SETTINGS, newValue);
    }


    /**
     * Get velocity preference int.
     *
     * @return the int
     */
    public  int getVelocityPreference(){
        return read(VELOCITY_SETTINGS, VELOCITY_NORMAL);
    }

    /**
     * Set velocity preference.
     *
     * @param newValue the new value
     */
    public  void setVelocityPreference( int newValue){
        write(VELOCITY_SETTINGS, newValue);
    }

    /**
     * Get velocity preference int.
     *
     * @return the int
     */
    public  int getCardViewPreference(){
        return read(CARD_VIEW_SETTINGS, FRENCH);
    }

    /**
     * Set velocity preference.
     *
     * @param newValue the new value
     */
    public  void setCardViewPreference( int newValue){
        write(CARD_VIEW_SETTINGS, newValue);
    }


    //helper method
    private  int read( String id, int defaultValue){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt(id, defaultValue);
    }

    //helper method
    private  void write( String id, int newValue ){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(id, newValue);
        editor.commit();
    }


    //helper method
    private  boolean read( String id, boolean defaultValue){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(id, defaultValue);
    }

    //helper method
    private  void write( String id, boolean newValue ){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(id, newValue);
        editor.commit();
    }

    /**
     * Get sound preference boolean.
     *
     * @return the boolean
     */
    public boolean getSoundPreference(){
        return read(SOUNDS_PREF_KEY,true);
    }

    /**
     * Get music preference boolean.
     *
     * @return the boolean
     */
    public boolean getMusicPreference(){
        return read(MUSIC_PREF_KEY,true);
    }

    /**
     * Set sound preference.
     *
     * @param soundEnabled the sound enabled
     */
    public void setSoundPreference(boolean soundEnabled){
        write(SOUNDS_PREF_KEY,soundEnabled);
    }

    /**
     * Sets music preference.
     *
     * @param musicEnabled the music enabled
     */
    public void setMusicPreference(boolean musicEnabled) {
        write(MUSIC_PREF_KEY,musicEnabled);
    }
}