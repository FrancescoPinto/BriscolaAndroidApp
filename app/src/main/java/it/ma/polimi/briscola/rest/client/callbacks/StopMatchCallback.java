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

//todo, eliminarlo? tanto non te ne frega se finisce bene o male lo stop?
public class StopMatchCallback extends CallbackWithRetry<ResponseBody> {

        public StopMatchCallback() {
            super();
        }


        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            Log.d("TAG","STO CHIUDENDO");
            try {

                    if(/*(controller.getMatchFragment().isVisible() &&*/ !call.isCanceled() && !shouldStop) {
                    success = true;
                    shouldStop = true;

                    if (response.code() == 200) {
                        Log.d("TAG", "Match Cancelled");
                        //todo
                        //  controller.endMatch();
                    } else {
                            JSONObject error = new JSONObject(response.errorBody().string());
                            Log.d("TAG", "Error on Match Cancelled");
                            Log.d("TAG", "STOPMATCHCALLBACK: " +error.getString("error") + " , you received " + error.getString("message"));

                    }
                    // }
                    //}
                    }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }


}
