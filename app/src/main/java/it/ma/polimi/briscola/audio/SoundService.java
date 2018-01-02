package it.ma.polimi.briscola.audio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import it.ma.polimi.briscola.persistency.SettingsManager;

/**
 * Service that handles the audio/sound aspects
 *
 * @author Francesco Pinto
 */
public class SoundService extends Service{


     // The Binder, used to bind the Service (used by ServiceConnection objects)
    private final IBinder binder = new ServiceBinder();

    private static final int MAX_STREAMS = 10; //max number of streams
    private static final float DEFAULT_MUSIC_VOLUME = 0.6f;

    private HashMap<GameEvent, Integer> soundsMap; //hashmap mapping game events on resources id

    private Context context;
    private SoundPool soundPool;

    private boolean soundEnabled; //whether sound/music are enabled
    private boolean musicEnabled;

    private boolean loadedSound, loadedMusic; //whether sound/music have been loaded (to prevent getting in Illegal states)

    private MediaPlayer backgroundPlayer;
    private SettingsManager settings;

    private UUID serviceID = UUID.randomUUID();

    @Override
    public IBinder onBind(Intent arg0){return binder;}

    @Override
    public void onCreate(){
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //generate settings manager and extract preferences
        settings = new SettingsManager(this);
        soundEnabled = settings.getSoundPreference();
        musicEnabled =settings.getMusicPreference();

        Log.d("TAG","Creato SERVICE ID:"+serviceID);

        this.context = this;

        loadIfNeeded();
        //resumeBgMusic();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        unloadMusic();
        stopSelf();
        Log.d("TAG","Distrutto SERVICE ID:"+serviceID);

        super.onDestroy();
    }

    /**
     * The Service binder class, used by ServiceConnection to to return a reference to the service.
     */
    public class ServiceBinder extends Binder {
        /**
         * Gets service.
         *
         * @return the service
         */
        public SoundService getService()
        {
            return SoundService.this;
        }
    }

    private void loadEventSound(Context context, GameEvent event, String... filename) {
        try {
            //retrieve sound descriptor
            AssetFileDescriptor descriptor = context.getAssets().openFd("sfx/" + filename[0]);
            //load it
            int soundId = soundPool.load(descriptor, 1);
            //save the mapping between event and soundId
            soundsMap.put(event, soundId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Play sound for game event.
     *
     * @param event the event
     */
    public void playSoundForGameEvent(GameEvent event) {
        if (!soundEnabled) { //check if should play sound
            return;
        }

        Integer soundId = soundsMap.get(event); //get soundId corresponding to event
        if (soundId != null) {
            // Left Volume, Right Volume, priority (0 == lowest), loop (0 == no) and rate (1.0 normal playback rate)
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    private void loadIfNeeded () { //load sounds/music only if they are enabled
        if (soundEnabled) {
            loadSounds();
        }
        if (musicEnabled) {
            loadMusic();
        }
    }

    private void loadSounds() {
        //initialize pool object
        createSoundPool();
        soundsMap = new HashMap<GameEvent, Integer>();
        //initialize soundsMap
        loadEventSound(context, GameEvent.MoveCard, "240777__f4ngy__dealing-card.wav");
        loadEventSound(context, GameEvent.FlipCard, "240776__f4ngy__card-flip.wav");
        loadEventSound(context, GameEvent.WinRound, "341985__unadamlar__goodresult.wav");
        loadEventSound(context, GameEvent.LoseRound, "362204__taranp__horn-fail-wahwah-3.wav");
        loadEventSound(context, GameEvent.WinMatch, "353546__maxmakessounds__success.wav");
        loadEventSound(context, GameEvent.LoseMatch, "371451__cabled-mess__lose-funny-retro-video-game.wav");

        loadedSound = true;

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

    /**
     * Load music.
     */
    public void loadMusic() {
        try {
            //since the state machine of MediaPlayer is quite complex, I'll create a new one every time it is needed to reload the music
            //to simplify the management of MediaPlayer state ... for the purposes of this app it doesn't produce low performance
            backgroundPlayer = new MediaPlayer();
            //find data about asset
            AssetFileDescriptor afd = context.getAssets().openFd("sfx/UnexpectedBackgroundMusic.mp3");
            //initialize backgroundPlayer
            backgroundPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            backgroundPlayer.setLooping(true);
            backgroundPlayer.setVolume(DEFAULT_MUSIC_VOLUME, DEFAULT_MUSIC_VOLUME);
            backgroundPlayer.prepare();
            loadedMusic = true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pause background music.
     */
    public void pauseBgMusic() {
        if (musicEnabled) {
            backgroundPlayer.pause();
            Log.d("TAG","Paused SERVICE ID:"+serviceID);

        }
    }

    /**
     * Resume background music.
     */
    public void resumeBgMusic() {
        if (musicEnabled) {
            backgroundPlayer.start();
            Log.d("TAG","Resumed SERVICE ID:"+serviceID);

        }
    }

    /**
     * Unload music.
     */
    public void unloadMusic() {
        if(loadedMusic) {
            backgroundPlayer.stop();
            backgroundPlayer.release();
            Log.d("TAG","Unloaded SERVICE ID:"+serviceID);

            loadedMusic = false;
        }
    }

    private void unloadSounds() {
        if(loadedSound) {
            soundPool.release();
            soundPool = null;
            soundsMap.clear();
            loadedSound = false;
        }
    }

    /**
     * Toggle sound status.
     */
    public void toggleSoundStatus() {
        soundEnabled = !soundEnabled;
        if (soundEnabled) {
            loadSounds();
        }
        else {
            unloadSounds();
        }
        // Save it to preferences
        settings.setSoundPreference(soundEnabled);

    }

    /**
     * Toggle music status.
     */
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
        settings.setMusicPreference(musicEnabled);

    }

    /**
     * Gets music status.
     *
     * @return the music status
     */
    public boolean getMusicStatus() {
        return musicEnabled;
    }


    /**
     * Gets sound status.
     *
     * @return the sound status
     */
    public boolean getSoundStatus() {
        return soundEnabled;
    }
}
