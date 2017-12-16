package it.ma.polimi.briscola.rest.client;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import it.ma.polimi.briscola.controller.online.OnlineBriscola2PMatchController;
import retrofit2.Call;
import retrofit2.Response;

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


        @Override
        public void onFailedAfterRetry(Throwable t) {

        }

        @Override
        public void onResponse(Call call, Response response) {
            if(response.isSuccessful()) {
                Log.d("TAG","Match Cancelled");
                //todo
              //  controller.endMatch();
            } else {
                try {
                    JSONObject error = new JSONObject(response.errorBody().string());
                    Log.d("TAG","Error on Match Cancelled");
                    controller.manageError(error.getString("error"), error.getString("message"));
                }catch(IOException |JSONException e){
                    e.printStackTrace();
                }
            }
        }
}
