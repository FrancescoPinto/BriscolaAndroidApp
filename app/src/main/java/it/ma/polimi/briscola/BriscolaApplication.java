package it.ma.polimi.briscola;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;

import it.ma.polimi.briscola.audio.SoundService;

/**
 * Created by utente on 21/12/17.
 */
public class BriscolaApplication extends Application {
    //number representing the number of activities started (see Google I/O about Firebase)
    private Integer numStarted;
    /**
     * The Sound manager, instance of Sound Service
     */
    SoundService soundManager;
    private boolean musicIsBound = false;
    /**
     * The Service connection, used to get a reference to Sound Service
     */
    ServiceConnection serviceConnection;


    @Override
    public void onCreate() {
        super.onCreate();
        //register an ActivityLifecycleCallback so that can monitor when the app goes background/foreground using a counter on the number of started activities
        AppLifecycleTracker tracker = new AppLifecycleTracker();
        registerActivityLifecycleCallbacks(tracker);
        numStarted = tracker.getNumStartedActivities();

        //play the audio using the Sound Service
        playAudio();

    }

    /**
     * The App lifecycle tracker, used to detect whether the app goes background/foreground
     */
    class AppLifecycleTracker implements Application.ActivityLifecycleCallbacks {
        private Integer numStarted = 0;

        //solution proposed at a Google I/O to detect when user sends the app in background (e.g. pressing Home button)
        @Override
        public void onActivityStarted(Activity activity) {
            if (numStarted == 0 && soundManager != null) {
                 // app went to foreground
                playAudio();
            }
            numStarted++;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            numStarted--;
            if (numStarted == 0 && soundManager != null) {
            // app went to background
                cleanAudio();
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

        /**
         * Get num started activities int.
         *
         * @return the int
         */
        public int getNumStartedActivities(){
            return numStarted;
        }
    }

    /**
     * Get num started activities int.
     *
     * @return the int
     */
    public int getNumStartedActivities(){
        return numStarted;
    }


    //helper method, binds the service using intent and service connection
    private void doBindService(Intent intent){
        bindService(intent,
                serviceConnection, Context.BIND_AUTO_CREATE);
        musicIsBound = true;
    }

    //helper method, unbinds the service using serviceConnection
    private void doUnbindService()
    {
        if(musicIsBound)
        {
            unbindService(serviceConnection);
            musicIsBound = false;
        }
    }

    /**
     * Clean audio, to be called when the Sound Service should be unbound and stopped
     */
    public void cleanAudio(){
        doUnbindService();
        stopService(new Intent(this, SoundService.class));
    }

    /**
     * Play audio, to be called when the Sound Service should be bound and started (background music)
     */
    private void playAudio(){
        //prepare the intent and the connection
        Intent serviceIntent = new Intent(this, SoundService.class);
        serviceConnection = new ServiceConnection(){

            @Override
            public void onServiceConnected(ComponentName name, IBinder
                    binder) {
                //retrieve the reference to the service
                soundManager = ((SoundService.ServiceBinder) binder).getService();
                soundManager.resumeBgMusic(); //resume playing background music
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                soundManager = null; //clean
            }
        };

        startService(serviceIntent); //start the service
        doBindService(serviceIntent); //bind it
    }

    /**
     * Get sound manager sound service.
     *
     * @return the sound service
     */
    public SoundService getSoundManager(){
        return soundManager;
    }

}
