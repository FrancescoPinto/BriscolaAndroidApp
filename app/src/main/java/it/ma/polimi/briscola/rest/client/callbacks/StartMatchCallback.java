package it.ma.polimi.briscola.rest.client.callbacks;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import it.ma.polimi.briscola.controller.OnlineBriscola2PMatchController;
import it.ma.polimi.briscola.rest.client.dto.StartedMatchDTO;
import retrofit2.Call;
import retrofit2.Response;

import static java.lang.Thread.sleep;

/**
 * Created by utente on 10/12/17.
 */

public class StartMatchCallback extends CallbackWithRetry<StartedMatchDTO>{
    private OnlineBriscola2PMatchController controller;

    public StartMatchCallback(OnlineBriscola2PMatchController controller) {
        super();
        this.controller = controller;
    }

    public StartMatchCallback() {
        super();
    }
    @Override
    public void onFailure(Call<StartedMatchDTO> call, Throwable t) {
        super.onFailure(call, t);
    }


    @Override
    public void onResponse(Call<StartedMatchDTO> call, Response<StartedMatchDTO> response) {
        if(response.isSuccessful()){
            Log.d("TAG", "STARTMATCH: Success");
        }else {
            try {
                JSONObject error = new JSONObject(response.errorBody().string());
                controller.manageError(error.getString("error"), error.getString("message"));
                Log.d("TAG", "STARTMATCHCALLBACK: Opponent played " +error.getString("error") + " , you received " + error.getString("message"));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        /* if(controller.getMatchFragment().isVisible() && !call.isCanceled()&& !shouldStop) {

            success = true;
            if (response.isSuccessful()) {
                StartedMatchDTO started = response.body();
                controller.manageStartedMatch(started);
            } else {
                try {
                    JSONObject error = new JSONObject(response.errorBody().string());
                    controller.manageError(error.getString("error"), error.getString("message"));
                    Log.d("TAG", "STARTMATCHCALLBACK: Opponent played " +error.getString("error") + " , you received " + error.getString("message"));

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if(shouldStop) {
            call.cancel();
            Log.d("TAG", "STARTMATCHCALLBACK: shoudlStop");

        }else{
            retry(call);
            Log.d("TAG", "STARTMATCHCALLBACK: retrying");

        }*/
    }
}
