package it.ma.polimi.briscola.rest.client;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import it.ma.polimi.briscola.controller.online.OnlineBriscola2PMatchController;
import it.ma.polimi.briscola.rest.client.dto.OpponentCardDTO;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by utente on 10/12/17.
 */

public class OpponentPlayedCardCallback extends CallbackWithRetry<OpponentCardDTO>{
    private OnlineBriscola2PMatchController controller;

    public OpponentPlayedCardCallback(OnlineBriscola2PMatchController controller) {
        super();
        this.controller = controller;
    }

    @Override
    public void onFailure(Call<OpponentCardDTO> call, Throwable t) {
        super.onFailure(call, t);
    }

    @Override
    public void onFailedAfterRetry(Throwable t) {

    }

    @Override
    public void onResponse(Call<OpponentCardDTO> call, Response<OpponentCardDTO> response) {
        success = true;
        if(response.isSuccessful()) {
            OpponentCardDTO opponent = response.body();
            Log.d("TAG", "Opponent played "+ opponent.getOpponent() + " , you received "+ opponent.getCard());
           controller.manageOpponentPlayedCard(opponent);
        } else {
            try {
                JSONObject error = new JSONObject(response.errorBody().string());
                controller.manageError(error.getString("error"), error.getString("message"));
            }catch(IOException |JSONException e){
                e.printStackTrace();
            }
        }
    }
}

