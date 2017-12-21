package it.ma.polimi.briscola.rest.client.callbacks;

import android.os.Handler;

import it.ma.polimi.briscola.controller.OnlineBriscola2PMatchController;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by utente on 10/12/17.
 */

public abstract class CallbackWithRetry<T> implements Callback<T> {
    private static final int RETRY_COUNT = 3;
    /**
     * Base retry delay for exponential backoff, in Milliseconds
     */
    private static final double RETRY_DELAY = 30000;
    private int retryCount = 0;
    OnlineBriscola2PMatchController controller;

    boolean success = false;
    boolean shouldStop = false;
    Call<T> call;

    @Override
    public void onFailure(final Call<T> call, Throwable t) {
        // retryCount++;
        // if (retryCount <= RETRY_COUNT) {
        if(shouldStop)
            call.cancel();
        else
            retry(call);

            /*int expDelay = (int) (RETRY_DELAY * Math.pow(2, Math.max(0, retryCount - 1)));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    retry(call);
                }
            }, expDelay);*/
        //} else { //todo, il prof. ha detto che se succede qualcosa ci manda errore, quindi continua a chiedere ... prima o poi il server risponderà

        // }
    }

    public void shouldStopRetrying(boolean shouldStop) {
        this.shouldStop = shouldStop;
    }

    public void retry(final Call<T> call) {
        if (!success) {
            new Handler().postDelayed(new Runnable() {
                                          @Override
                                          public void run() {
                                              if (!shouldStop)
                                                  call.clone().enqueue(CallbackWithRetry.this);

                                          }
                                      }

                    , (int) RETRY_DELAY);

        }
    }
}
