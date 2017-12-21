package it.ma.polimi.briscola.audio;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;

/**
 * Created by utente on 17/12/17.
 */

public class SoundService extends Service{

    SoundManager soundManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // soundManager = (SoundManager) bundle.getSerializable(SoundManager.EXTRA_SOUND_MANAGER);
        soundManager = SoundManager.getInstance(this);
        //return START_STICKY;
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        soundManager.unloadMusic();
        stopSelf();
        super.onDestroy();
    }
}
