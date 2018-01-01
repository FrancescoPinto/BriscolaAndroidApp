package it.ma.polimi.briscola.rest.client.callbacks;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import it.ma.polimi.briscola.controller.ControllerWithServerResponseManager;
import it.ma.polimi.briscola.controller.OnlineBriscola2PMatchController;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static java.lang.Thread.sleep;

/**
 * Class implementing the callback to be called after a start request has been answered
 */
public class StartMatchCallback extends CallbackWithRetry<ResponseBody>{

    private boolean stopWaitingOccurred;
    private Context context;

    /**
     * Instantiates a new Start match callback.
     *
     * @param controller the controller
     * @param context   the context
     */
    public StartMatchCallback(ControllerWithServerResponseManager controller, Context context) {
        super();
        this.controller = controller;
        this.context = context;

    }



    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            //important: checking isVisible() prevents the controller from calling the fragment when it is detached! (e.g. because the player left, but the callback still had to be called)
            //if fragment isVisible() and the call has not been cancelled and a shouldStop signal has not been sent
            if(controller.getMatchFragment().isVisible() && !call.isCanceled()&& !shouldStop) {
                success = true;
                shouldStop = true;
                if (response.isSuccessful()) { //HTTP 200, OK
                    //extract data from the response body
                    JSONObject body = new JSONObject(response.body().string());
                    String game = body.getString("game");
                    String lastCard = body.getString("last_card");
                    String cards = body.getString("cards");
                    String yourTurn = body.getString("your_turn");
                    String url = body.getString("url");

                    //remark: since the callback is called in another thread, the user might have decided to stop waiting while executing this part of the callback
                    //hence, we check it
                    if (!stopWaitingOccurred) //if no need to stop waiting, then start the match
                        controller.manageStartedMatch(game, lastCard, cards, yourTurn, url);
                    else //else, force the match to end
                        controller.forceMatchEnd(context, url);

                } else {
                    //manage the error
                    manageFatalError(response.errorBody());
                }
            }else if(stopWaitingOccurred){ //if user stopped waiting
                //IMPORTANT REMARK: given the email sent by Bernaschina, the only way to "stop waiting" for a matching is that of starting the match and immediately closing it
                //if success
                if(response.isSuccessful()) { //extract the url, and use it to force the match end
                    JSONObject body = new JSONObject(response.body().string());
                    String url = body.getString("url");
                    controller.forceMatchEnd(context, url);
                } //no need to handle a failure, because we don't want to start a new match
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop waiting, to be called by other classes to signal the callback that should stop waiting the user
     *
     * @param stop the stop
     */
    public void stopWaiting(boolean stop){
        stopWaitingOccurred = stop;
    }
}
