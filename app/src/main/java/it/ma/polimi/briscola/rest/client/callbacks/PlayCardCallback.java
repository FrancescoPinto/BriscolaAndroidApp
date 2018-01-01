package it.ma.polimi.briscola.rest.client.callbacks;

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
 * Class implementing the callback to be called after a post request has been sent to the server to play a card, and the answer is received
 */
public class PlayCardCallback extends CallbackWithRetry<ResponseBody>{

    /**
     * Instantiates a new Play card callback.
     *
     * @param controller the controller
     */
    public PlayCardCallback(ControllerWithServerResponseManager controller) {
        super();
        this.controller = controller;

    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            //important: checking isVisible() prevents the controller from calling the fragment when it is detached! (e.g. because the player left, but the callback still had to be called)
            //if fragment isVisible() and the call has not been cancelled and a shouldStop signal has not been sent
            if(controller.getMatchFragment().isVisible() && !call.isCanceled() && !shouldStop) {
                success = true;
                shouldStop = true;

                if (response.isSuccessful()) { //HTTP 200 OK
                    JSONObject body = new JSONObject(response.body().string());
                    //extract data (if any) from the response
                    String card = (body.has("card"))?body.getString("card"):null;

                    controller.manageNextTurnCard(card); //tell the controller to react
                } else {
                    //manage the error
                    manageFatalError(response.errorBody());
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}
