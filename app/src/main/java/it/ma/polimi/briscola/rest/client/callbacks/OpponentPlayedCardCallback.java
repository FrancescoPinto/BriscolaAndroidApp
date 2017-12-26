package it.ma.polimi.briscola.rest.client.callbacks;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import it.ma.polimi.briscola.controller.OnlineBriscola2PMatchController;
import it.ma.polimi.briscola.rest.client.dto.OpponentCardDTO;
import retrofit2.Call;
import retrofit2.Response;

import static java.lang.Thread.sleep;

/**
 * Created by utente on 10/12/17.
 */

public class OpponentPlayedCardCallback extends CallbackWithRetry<OpponentCardDTO>{

    public OpponentPlayedCardCallback(OnlineBriscola2PMatchController controller) {
        super();
        this.controller = controller;
    }

    public OpponentPlayedCardCallback() {
        super();
    }

    @Override
    public void onFailure(Call<OpponentCardDTO> call, Throwable t) {
        super.onFailure(call, t);
    }



    @Override
    public void onResponse(Call<OpponentCardDTO> call, Response<OpponentCardDTO> response) {
        if(response.isSuccessful()){
            OpponentCardDTO opponent = response.body();
            Log.d("TAG", "OPPONENTPLAYEDCARDCALLBACK: Opponent played " + opponent.getOpponent() + " , you received " + opponent.getCard());
        }else {
            try {
                JSONObject error = new JSONObject(response.errorBody().string());
                controller.manageError(error.getString("error"), error.getString("message"));
                Log.d("TAG", "OPPONENTPLAYEDCARDCALLBACK: Opponent played " +error.getString("error") + " , you received " + error.getString("message"));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
       /* if(controller.getMatchFragment().isVisible() && !call.isCanceled() && !shouldStop) {
            success = true;
            if (response.isSuccessful()) {
                OpponentCardDTO opponent = response.body();
                Log.d("TAG", "OPPONENTPLAYEDCARDCALLBACK: Opponent played " + opponent.getOpponent() + " , you received " + opponent.getCard());
                controller.manageOpponentPlayedCard(opponent);
            } else {
                try {
                    JSONObject error = new JSONObject(response.errorBody().string());
                    controller.manageError(error.getString("error"), error.getString("message"));
                    Log.d("TAG", "OPPONENTPLAYEDCARDCALLBACK: Opponent played " +error.getString("error") + " , you received " + error.getString("message"));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if(shouldStop) {
            call.cancel();
            Log.d("TAG", "OPPONENTPLAYEDCARDCALLBACK: shoudlStop");

        }else{
            retry(call);
            Log.d("TAG", "OPPONENTPLAYEDCARDCALLBACK: retrying");

        }*/

    }


}

