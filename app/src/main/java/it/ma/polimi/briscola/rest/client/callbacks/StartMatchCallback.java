package it.ma.polimi.briscola.rest.client.callbacks;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import it.ma.polimi.briscola.controller.OnlineBriscola2PMatchController;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static java.lang.Thread.sleep;

/**
 * Created by utente on 10/12/17.
 */

public class StartMatchCallback extends CallbackWithRetry<ResponseBody>{
    private OnlineBriscola2PMatchController controller;
    private boolean stopWaitingOccurred;
    private Activity activity;

    public StartMatchCallback(OnlineBriscola2PMatchController controller, Activity activity) {
        super();
        this.controller = controller;
        this.activity = activity;

    }



    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            if(controller.getMatchFragment().isVisible() && !call.isCanceled()&& !shouldStop) {
                success = true;
                shouldStop = true;
                if (response.isSuccessful()) {
                    JSONObject body = new JSONObject(response.body().string());
                    String game = body.getString("game");
                    String lastCard = body.getString("last_card");
                    String cards = body.getString("cards");
                    String yourTurn = body.getString("your_turn");
                    String url = body.getString("url");

                    Log.d("TAG", "STARTED: Success, " + game + "|" + lastCard + "|" + cards + "|" + yourTurn + "|" + url);
                    if (!stopWaitingOccurred)
                        controller.manageStartedMatch(game, lastCard, cards, yourTurn, url);
                    else
                        controller.forceMatchEnd(activity, url); //todo, capisci se questo else lo raggiungi

                } else {

                    JSONObject error = new JSONObject(response.errorBody().string());
                    controller.manageError(error.getString("error"), error.getString("message"));
                    Log.d("TAG", "STARTMATCHCALLBACK: Opponent played " + error.getString("error") + " , you received " + error.getString("message"));


                }
            }else if(stopWaitingOccurred){
                JSONObject body = new JSONObject(response.body().string());
                String url = body.getString("url");
                controller.forceMatchEnd(activity,url);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void stopWaiting(boolean stop){
        stopWaitingOccurred = stop;
    }
}
