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
 * Class implementing the callback to be called after a stop match request has been answered
 */
public class StopMatchCallback extends CallbackWithRetry<ResponseBody> {

    /**
     * Instantiates a new Stop match callback.
     */
    public StopMatchCallback() {
            super();
        }


        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            try {
                //Could also occur while the fragment is not visible
                if(/*(controller.getMatchFragment().isVisible() &&*/ !call.isCanceled() && !shouldStop) {
                success = true;
                shouldStop = true;
                    if (response.code() == 200) { //HTTP 200, OK
                        Log.d("TAG", "Match Cancelled"); //the human player is not concerned with the outcome
                    } else {
                        //even though the error might be fatal, there's no need to handle it as fatal ... the match should allready be closed!
                        JSONObject error = new JSONObject(response.errorBody().string()); //the human player is not concerned with the outcome
                        Log.d("TAG", "STOPMATCHCALLBACK: error " +error.getString("error") + " , you received " + error.getString("message"));
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }


}
