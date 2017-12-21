package it.ma.polimi.briscola.audio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by utente on 17/12/17.
 */

public class SfxManager {
    private static final int MAX_STREAMS = 10;
    private static final float DEFAULT_MUSIC_VOLUME = 0.6f;

    private static final String SOUNDS_PREF_KEY = "it.ma.polimi.briscola.sounds.boolean";
    private static final String MUSIC_PREF_KEY = "it.ma.polimi.briscola.music.boolean";

    private static SfxManager instance;

    //	private HashMap<GameEvent, SoundInfo> mSoundsMap;
    private HashMap<GameEvent, Integer> soundsMap;

    private Context context;
    private SoundPool soundPool;

    private boolean soundEnabled;
    private boolean musicEnabled;

    private MediaPlayer backgroundPlayer;


    private SfxManager(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        soundEnabled = prefs.getBoolean(SOUNDS_PREF_KEY, true);
        // I should use SoundPool.Builder on API 21 http://developer.android.com/reference/android/media/SoundPool.Builder.html
        this.context = context;
        loadIfNeeded();
    }

    /* public void refreshEnabledFlags(){
         SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
         soundEnabled = prefs.getBoolean(SOUNDS_PREF_KEY, true);
         musicEnabled = prefs.getBoolean(MUSIC_PREF_KEY, true);
     }*/
    public static SfxManager getInstance(Context context){
        if(instance == null)
        {
            instance = new SfxManager(context);
            return instance;
        }
        else
            return instance;
    }

    private void loadEventSound(Context context, GameEvent event, String... filename) {
//		mSoundsMap.put(event,new SoundInfo(context, soundPool, filename));
        try {
            AssetFileDescriptor descriptor = context.getAssets().openFd("sfx/" + filename[0]);
            int soundId = soundPool.load(descriptor, 1);
            soundsMap.put(event, soundId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playSoundForGameEvent(GameEvent event) {
        if (!soundEnabled) {
            return;
        }

        Integer soundId = soundsMap.get(event);
        if (soundId != null) {
            // Left Volume, Right Volume, priority (0 == lowest), loop (0 == no) and rate (1.0 normal playback rate)
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    private void loadIfNeeded () {
        if (soundEnabled) {
            loadSounds();
        }

    }

    private void loadSounds() {
        createSoundPool();
        soundsMap = new HashMap<GameEvent, Integer>();
        loadEventSound(context, GameEvent.MoveCard, "240777__f4ngy__dealing-card.wav");
        loadEventSound(context, GameEvent.FlipCard, "240776__f4ngy__card-flip.wav");
        loadEventSound(context, GameEvent.WinRound, "341985__unadamlar__goodresult.wav");
        loadEventSound(context, GameEvent.LoseRound, "362204__taranp__horn-fail-wahwah-3.wav");
        loadEventSound(context, GameEvent.WinMatch, "341985__unadamlar__goodresult.wav"); //todo AGGIUNGI UN VERO VICTORY THEME
        loadEventSound(context, GameEvent.LoseMatch, "371451__cabled-mess__lose-funny-retro-video-game.wav");


    }

    private void createSoundPool() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }
        else {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(MAX_STREAMS)
                    .build();
        }
    }

    private void unloadSounds() {
        soundPool.release();
        soundPool = null;
        soundsMap.clear();
    }

    public void toggleSoundStatus() {
        soundEnabled = !soundEnabled;
        if (soundEnabled) {
            loadSounds();
        }
        else {
            unloadSounds();
        }
        // Save it to preferences
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putBoolean(SOUNDS_PREF_KEY, soundEnabled)
                .commit();
    }

    public boolean getSoundStatus() {
        return soundEnabled;
    }
}
