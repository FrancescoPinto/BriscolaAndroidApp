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
 * Class implementing the callback to be called after a request has been sent to the server to ask for the card played by the opponent.
 */
public class OpponentPlayedCardCallback extends CallbackWithRetry<ResponseBody>{

    /**
     * Instantiates a new Opponent played card callback.
     *
     * @param controller the controller
     */
    public OpponentPlayedCardCallback(OnlineBriscola2PMatchController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
                //important: the fragment
                if(controller.getMatchFragment().isVisible() && !call.isCanceled() && !shouldStop) {
                    success = true;
                    shouldStop = true;

                    if (response.isSuccessful()) {
                        JSONObject body = new JSONObject(response.body().string());
                        String opponent = body.getString("opponent");
                        String card = (body.has("card")) ? body.getString("card") : null;

                        Log.d("TAG", "OPPONENTPLAYEDCARDCALLBACK: Opponent played " + opponent + " , you received " + card);
                        controller.manageOpponentPlayedCard(opponent, card);
                    } else {

                        JSONObject error = new JSONObject(response.errorBody().string());
                        controller.manageError(error.getString("error"), error.getString("message"));
                        Log.d("TAG", "OPPONENTPLAYEDCARDCALLBACK: Opponent played " + error.getString("error") + " , you received " + error.getString("message"));

                    }
                }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


}

