package it.ma.polimi.briscola.rest.client.callbacks;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import it.ma.polimi.briscola.controller.OnlineBriscola2PMatchController;
import retrofit2.Call;
import retrofit2.Response;

import static java.lang.Thread.sleep;

/**
 * Created by utente on 10/12/17.
 */

//todo, eliminarlo? tanto non te ne frega se finisce bene o male lo stop?
public class StopMatchCallback extends CallbackWithRetry{
    private OnlineBriscola2PMatchController controller;

        public StopMatchCallback(OnlineBriscola2PMatchController controller) {
            super();
            this.controller = controller;
        }

    public StopMatchCallback() {
        super();
    }
        @Override
        public void onResponse(Call call, Response response) {
            if(response.isSuccessful()){
                Log.d("TAG", "Match Cancelled correctly");
            }else {
                try {
                    JSONObject error = new JSONObject(response.errorBody().string());
                    controller.manageError(error.getString("error"), error.getString("message"));
                    Log.d("TAG", "STOPMATCHCALLBACK: Opponent played " +error.getString("error") + " , you received " + error.getString("message"));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            /*if(controller.getMatchFragment().isVisible() && !call.isCanceled() && !shouldStop) {
                success = true;

                if (response.isSuccessful()) {
                    Log.d("TAG", "Match Cancelled");
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Log.d("TAG", "Error on Match Cancelled");
                        Log.d("TAG", "STOPMATCHCALLBACK: Opponent played " +error.getString("error") + " , you received " + error.getString("message"));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

            }else if(shouldStop) {
                call.cancel();
                Log.d("TAG", "STOPMATCALLBACK: shoudlStop");

            }else{
                retry(call);
                Log.d("TAG", "STOPMATCALLBACK: retrying");

            }*/
        }
}
