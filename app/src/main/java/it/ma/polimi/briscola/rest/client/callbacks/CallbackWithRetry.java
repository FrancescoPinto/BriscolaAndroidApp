package it.ma.polimi.briscola.rest.client.callbacks;

import android.os.Handler;
import android.util.Log;

import it.ma.polimi.briscola.controller.OnlineBriscola2PMatchController;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Abstract class implementing methods that automatically retry the call after a certain amount of time if the server hasn't responded within timeout
 *
 * @param <T> the object representing the response
 */
public abstract class CallbackWithRetry<T> implements Callback<T> {

    private static final double RETRY_DELAY = 30000;
    /**
     * The Controller.
     */
    OnlineBriscola2PMatchController controller;
    /**
     * The Success, whether the response status is success.
     */
    boolean success = false;
    /**
     * The Should stop, whether should stop retrying in case of failure.
     */
    boolean shouldStop = false;

    @Override
    public void onFailure(final Call<T> call, Throwable t) {
            Log.d("TAG", "onFailure = " + t.getMessage() + t.getCause());
            if (shouldStop) //if should stop
                call.cancel(); //stop retrying
            else
                retry(call); //else, retry
    }

    /**
     * Should stop retrying.
     *
     * @param shouldStop the should stop
     */
    public void shouldStopRetrying(boolean shouldStop) {
        this.shouldStop = shouldStop;
    }

    //convenience method, performs a retry after a certain amount of time (retry delay) using a Runnable
    //remark: shouldStop is checked again, because it is possible that at the moment the Runnable is run
    //the shouldStop status has changed!
    private void retry(final Call<T> call) {
        Log.d("TAG", "GONNA RETRY" + this.getClass().getName());
        if (!success) {//REMARK: onFailure is called even in the case in which an exception is thrown while during response processing even if it was a success
                        //avoid to retry if the response has been a success -> might lead to undesirable state
            new Handler().postDelayed(new Runnable() {
                                          @Override
                                          public void run() {
                                              if (!shouldStop) //if still shouldn't stop, retry
                                                  call.clone().enqueue(CallbackWithRetry.this);
                                          }
                                      },
                    (int) RETRY_DELAY);

        }
    }
}
