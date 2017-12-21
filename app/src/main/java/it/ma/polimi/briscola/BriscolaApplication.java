package it.ma.polimi.briscola;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import it.ma.polimi.briscola.audio.SoundService;

/**
 * Created by utente on 21/12/17.
 */

public class BriscolaApplication extends Application {
    private Integer numStarted;
    SoundService soundManager;
    private boolean musicIsBound = false;
    ServiceConnection serviceConnection;


    @Override
    public void onCreate() {
        super.onCreate();
        AppLifecycleTracker tracker = new AppLifecycleTracker();
        registerActivityLifecycleCallbacks(tracker);
        numStarted = tracker.getNumStartedActivities();
        Intent serviceIntent = new Intent(this, SoundService.class);
        serviceConnection = new ServiceConnection(){

            @Override
            public void onServiceConnected(ComponentName name, IBinder
                    binder) {
                soundManager = ((SoundService.ServiceBinder) binder).getService();
                soundManager.resumeBgMusic();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                soundManager = null;
            }
        };

        startService(serviceIntent);
        doBindService(serviceIntent);
    }



    class AppLifecycleTracker implements Application.ActivityLifecycleCallbacks {
        private Integer numStarted = 0;

        //solution proposed at a Google I/O to detect when user sends the app in background (e.g. pressing Home button)
        @Override
        public void onActivityStarted(Activity activity) {
            if (numStarted == 0 && soundManager != null) {
                soundManager.loadMusic();
                soundManager.resumeBgMusic(); // app went to foreground
            }
            numStarted++;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            numStarted--;
            if (numStarted == 0 && soundManager != null) {
                soundManager.pauseBgMusic();// app went to background
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public int getNumStartedActivities(){
            return numStarted;
        }
    }

    public int getNumStartedActivities(){
        return numStarted;
    }


    void doBindService(Intent intent){
        bindService(intent,
                serviceConnection, Context.BIND_AUTO_CREATE);
        musicIsBound = true;
    }

    void doUnbindService()
    {
        if(musicIsBound)
        {
            unbindService(serviceConnection);
            musicIsBound = false;
        }
    }

    public void cleanAudio(){
        doUnbindService();
        stopService(new Intent(this, SoundService.class));
    }

    public SoundService getSoundManager(){
        return soundManager;
    }

}
