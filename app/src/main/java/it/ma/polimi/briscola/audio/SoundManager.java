package it.ma.polimi.briscola.audio;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by utente on 07/12/17.
 */

public class SoundManager {
    private static final int MAX_STREAMS = 10;
    private static final float DEFAULT_MUSIC_VOLUME = 0.6f;

    private static final String SOUNDS_PREF_KEY = "it.ma.polimi.briscola.sounds.boolean";
    private static final String MUSIC_PREF_KEY = "it.ma.polimi.briscola.music.boolean";

    private static SoundManager sInstance;

    //	private HashMap<GameEvent, SoundInfo> mSoundsMap;
    private HashMap<GameEvent, Integer> soundsMap;

    private Context context;
    private SoundPool soundPool;

    private boolean soundEnabled;
    private boolean musicEnabled;

    private MediaPlayer backgroundPlayer;

    public SoundManager(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        soundEnabled = prefs.getBoolean(SOUNDS_PREF_KEY, true);
        musicEnabled = prefs.getBoolean(MUSIC_PREF_KEY, true);
        // I should use SoundPool.Builder on API 21 http://developer.android.com/reference/android/media/SoundPool.Builder.html
        this.context = context;
        loadIfNeeded();
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
        if (musicEnabled) {
            loadMusic();
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

    private void unloadMusic() {
        backgroundPlayer.stop();
        backgroundPlayer.release();
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

    public boolean getSoundStatus() {
        return soundEnabled;
    }
}
