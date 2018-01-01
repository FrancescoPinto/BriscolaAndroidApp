package it.ma.polimi.briscola.rest.client.endpoints;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
 * Interface implementing a REST Client from annotations, using Retrofit (as suggested by Prof. De Bernardi via email we were allowed to use it).
 * The signatures simply replicate the server-API structure, it is not necessary to explain them (see the available server API for an explaination)
 */

public interface BriscolaAPI {
        @GET("room/{room}")
        Call<ResponseBody> startMatch(@Header("Authorization") String credentials,
                                               @Path("room") String room);
        @GET
        Call<ResponseBody> getOpponentPlayedCard(@Url String url, @Header("Authorization") String credentials);

        @POST
        Call<ResponseBody> playCard(@Url String url, @Header("Authorization") String credentials, @Header("Content-Type") String type, @Body RequestBody playedCard);

        @HTTP(method = "DELETE", hasBody = true)
        Call<ResponseBody> stopMatch(@Url String url, @Header("Authorization") String credentials, @Header("Content-Type") String type,
                                 @Body RequestBody motivation);
}