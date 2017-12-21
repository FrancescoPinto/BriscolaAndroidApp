package it.ma.polimi.briscola.audio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by utente on 17/12/17.
 */

public class BackgroundMusicManager {

    private static final float DEFAULT_MUSIC_VOLUME = 0.6f;

    private static final String MUSIC_PREF_KEY = "it.ma.polimi.briscola.music.boolean";

    public static final String EXTRA_SOUND_MANAGER = "it.ma.polimi.briscola.soundmanager";

    private static BackgroundMusicManager instance;

    //	private HashMap<GameEvent, SoundInfo> mSoundsMap;
    private HashMap<GameEvent, Integer> soundsMap;

    private Context context;

    private boolean musicEnabled;

    private MediaPlayer backgroundPlayer;


    private BackgroundMusicManager(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        musicEnabled = prefs.getBoolean(MUSIC_PREF_KEY, true);
        // I should use SoundPool.Builder on API 21 http://developer.android.com/reference/android/media/SoundPool.Builder.html
        this.context = context;
        loadIfNeeded();
    }

    /* public void refreshEnabledFlags(){
         SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
         soundEnabled = prefs.getBoolean(SOUNDS_PREF_KEY, true);
         musicEnabled = prefs.getBoolean(MUSIC_PREF_KEY, true);
     }*/
    public static BackgroundMusicManager getInstance(Context context){
        if(instance == null)
        {
            instance = new BackgroundMusicManager(context);
            return instance;
        }
        else
            return instance;
    }



    private void loadIfNeeded () {
        if (musicEnabled) {
            loadMusic();
        }
    }

    private void loadMusic() {
        try {
            // Important to not reuse it. It can be on a strange state
            backgroundPlayer = new MediaPlayer();
            AssetFileDescriptor afd = context.getAssets().openFd("sfx/UnexpectedBackgroundMusic.mp3"); //todo, cambia musichetta
            backgroundPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            backgroundPlayer.setLooping(true);
            backgroundPlayer.setVolume(DEFAULT_MUSIC_VOLUME, DEFAULT_MUSIC_VOLUME);
            backgroundPlayer.prepare();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseBgMusic() {
        if (musicEnabled) {
            backgroundPlayer.pause();
        }
    }

    public void resumeBgMusic() {
        if (musicEnabled) {
            backgroundPlayer.start();
        }
    }

    public void unloadMusic() {
        backgroundPlayer.stop();
        backgroundPlayer.release();
    }


    public void toggleMusicStatus() {
        musicEnabled = !musicEnabled;
        if (musicEnabled) {
            loadMusic();
            resumeBgMusic();
        }
        else {
            unloadMusic();
        }
        // Save it to preferences
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(MUSIC_PREF_KEY, musicEnabled)
                .commit();
    }

    public boolean getMusicStatus() {
        return musicEnabled;
    }

}
