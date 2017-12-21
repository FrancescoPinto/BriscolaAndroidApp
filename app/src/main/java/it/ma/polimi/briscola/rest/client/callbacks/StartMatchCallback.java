package it.ma.polimi.briscola.rest.client.callbacks;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import it.ma.polimi.briscola.controller.online.OnlineBriscola2PMatchController;
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

    @Override
    public void onFailure(Call<StartedMatchDTO> call, Throwable t) {
        super.onFailure(call, t);
    }


    @Override
    public void onResponse(Call<StartedMatchDTO> call, Response<StartedMatchDTO> response) {
        if(controller.getMatchFragment().isVisible() && !call.isCanceled()) {

            success = true;
            if (response.isSuccessful()) {
                StartedMatchDTO started = response.body();
                controller.manageStartedMatch(started);
            } else {
                try {
                    JSONObject error = new JSONObject(response.errorBody().string());
                    controller.manageError(error.getString("error"), error.getString("message"));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            retry(call);

        }
    }
}
