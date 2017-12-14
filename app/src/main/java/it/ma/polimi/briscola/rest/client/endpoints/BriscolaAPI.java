package it.ma.polimi.briscola.rest.client.endpoints;

import it.ma.polimi.briscola.rest.client.dto.NextTurnCardDTO;
import it.ma.polimi.briscola.rest.client.dto.OpponentCardDTO;
import it.ma.polimi.briscola.rest.client.dto.StartedMatchDTO;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by utente on 09/12/17.
 */

public interface BriscolaAPI {
        @GET("room/{room}")
        Call<StartedMatchDTO> startMatch(@Header("Authorization") String credentials,
                                               @Path("room") String room);
        @GET
        Call<OpponentCardDTO> getOpponentPlayedCard(@Url String url, @Header("Authorization") String credentials);

    //TODO: attento, è proprio url a distinguere tra i due giocatori chi sia quello giusto! quindi due giocatori avranno due Url di gioco diversi

    //todo, attento, devi mandare text/plain e dentro i due caratteri della carta, segui questa guida https://futurestud.io/tutorials/retrofit-2-how-to-send-plain-text-request-body

        @POST
        Call<NextTurnCardDTO> playCard(@Url String url, @Header("Authorization") String credentials, @Header("Content-Type") String type, @Body RequestBody playedCard);

    /*
    esempio di come dovrai usare playCard (attento, puoi fare anche l'enqueue con callback, che è DECISAMENTE MEGLIO
    String body = "plain text request body";
Call<String> call = service.getStringScalar(body);

Response<String> response = call.execute();
String value = response.body();
     */
        @HTTP(method = "DELETE", hasBody = true)
        Call<StartedMatchDTO> stopMatch(@Url String url, @Header("Authorization") String credentials,@Header("Content-Type") String type,
                                        @Body RequestBody motivation);

}