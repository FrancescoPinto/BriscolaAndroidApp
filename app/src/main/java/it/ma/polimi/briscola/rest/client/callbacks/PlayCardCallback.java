package it.ma.polimi.briscola.rest.client.callbacks;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import it.ma.polimi.briscola.controller.OnlineBriscola2PMatchController;
import it.ma.polimi.briscola.rest.client.dto.NextTurnCardDTO;
import retrofit2.Call;
import retrofit2.Response;

import static java.lang.Thread.sleep;

/**
 * Created by utente on 10/12/17.
 */

public class PlayCardCallback extends CallbackWithRetry<NextTurnCardDTO>{
    private OnlineBriscola2PMatchController controller;

    public PlayCardCallback(OnlineBriscola2PMatchController controller) {
        super();
        this.controller = controller;
    }


    public PlayCardCallback() {
        super();
    }

    @Override
    public void onFailure(Call<NextTurnCardDTO> call, Throwable t) {
        super.onFailure(call, t);
    }



    @Override
    public void onResponse(Call<NextTurnCardDTO> call, Response<NextTurnCardDTO> response) {
        if(response.isSuccessful()){
            Log.d("TAG", "PLAYCARDCALLBACK: Success playCardRequest, next turn card is" + response.body().getCard());
        }else {
            try {
                JSONObject error = new JSONObject(response.errorBody().string());
                controller.manageError(error.getString("error"), error.getString("message"));
                Log.d("TAG", "PLAYCARDCALLBACK: Opponent played " + error.getString("error") + " , you received " + error.getString("message"));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
      /*  if(controller.getMatchFragment().isVisible() && !call.isCanceled() && !shouldStop) {
            success = true;
            if (response.isSuccessful()) {
                Log.d("TAG", "PLAYCARDCALLBACK: Success playCardRequest, next turn card is" + response.body().getCard());
                NextTurnCardDTO nextTurn = response.body();
                controller.manageNextTurnCard(nextTurn);
            } else {
                try {
                    JSONObject error = new JSONObject(response.errorBody().string());
                    controller.manageError(error.getString("error"), error.getString("message"));
                    Log.d("TAG", "PLAYCARDCALLBACK: Opponent played " +error.getString("error") + " , you received " + error.getString("message"));

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if(shouldStop) {
            call.cancel();
            Log.d("TAG", "PLAYCARDCALLBACK: shoudlStop");

        }else{
            retry(call);
            Log.d("TAG", "PLAYCARDCALLBACK: retrying");

        }*/
    }
}
