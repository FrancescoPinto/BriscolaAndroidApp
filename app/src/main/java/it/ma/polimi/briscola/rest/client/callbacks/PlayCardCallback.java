package it.ma.polimi.briscola.rest.client.callbacks;

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

public class PlayCardCallback extends CallbackWithRetry<ResponseBody>{
    private OnlineBriscola2PMatchController controller;

    public PlayCardCallback(OnlineBriscola2PMatchController controller) {
        super();
        this.controller = controller;

    }



    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
                if(controller.getMatchFragment().isVisible() && !call.isCanceled() && !shouldStop) {
                success = true;
                shouldStop = true;

                if (response.isSuccessful()) {
                    JSONObject body = new JSONObject(response.body().string());
                    String card = (body.has("card"))?body.getString("card"):null;
                    Log.d("TAG", "PLAYCARDCALLBACK: Success playCardRequest, next turn card is" + card);
                    controller.manageNextTurnCard(card);
                } else {

                        JSONObject error = new JSONObject(response.errorBody().string());
                        controller.manageError(error.getString("error"), error.getString("message"));
                        Log.d("TAG", "PLAYCARDCALLBACK: Opponent played " +error.getString("error") + " , you received " + error.getString("message"));


                }
            }else if(shouldStop) {
                call.cancel();
                Log.d("TAG", "PLAYCARDCALLBACK: shoudlStop");

            }else{
                retry(call);
                Log.d("TAG", "PLAYCARDCALLBACK: retrying");

            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}
