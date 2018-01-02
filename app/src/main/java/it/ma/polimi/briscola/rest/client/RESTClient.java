package it.ma.polimi.briscola.rest.client;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.controller.ControllerWithServerResponseManager;
import it.ma.polimi.briscola.controller.OnlineBriscola2PMatchController;
import it.ma.polimi.briscola.rest.client.callbacks.OpponentPlayedCardCallback;
import it.ma.polimi.briscola.rest.client.callbacks.PlayCardCallback;
import it.ma.polimi.briscola.rest.client.callbacks.StartMatchCallback;
import it.ma.polimi.briscola.rest.client.callbacks.StopMatchCallback;
import it.ma.polimi.briscola.rest.client.endpoints.BriscolaAPI;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * Class encapsulating the RESTClient, that interacts with the server. It can be reused in different controllers.
 */
public class RESTClient {

    //connection data
    private final String authHeader = "APIKey 0c3828b9-b0d6-45c3-aa3b-a6d324561569",
            roomName = "Group01",
            contentTypePlainText = "text/plain";
    private static final String BASE_URL = "http://mobile17.ifmledit.org/api/";
    private String url; //returned by the server

    // callback instances
    private OpponentPlayedCardCallback opponentPlayedCardCallback;
    private StartMatchCallback startMatchCallback;
    private PlayCardCallback playCardCallback;
    private StopMatchCallback stopMatchCallback;

    //the class to be called to make HTTP requests
    private BriscolaAPI briscolaAPI;

    //a controller implementing methods of a Server Response Manager
    private ControllerWithServerResponseManager controller;

    /**
     * Instantiates a new Rest client.
     *
     * @param controller the controller
     */
    public RESTClient(ControllerWithServerResponseManager controller){
        this.controller = controller;
    }


    /**
     * Opponent played card call.
     */
    public void opponentPlayedCardCall(){
        //send it, and set the callback on the request
        opponentPlayedCardCallback = new OpponentPlayedCardCallback(controller);
        briscolaAPI.getOpponentPlayedCard(url, authHeader).enqueue(opponentPlayedCardCallback);
    }

    /**
     * Start match call.
     *
     * @param context the context
     */
    public void startMatchCall(Context context){
        //send it, and set the callback on the request
        startMatchCallback = new StartMatchCallback(controller, context);
        briscolaAPI.startMatch(authHeader, roomName).enqueue(startMatchCallback);
    }

    /**
     * Stop waiting for a match to start
     *
     * @param stop the stop
     */
    public void stopWaiting(boolean stop){
        //update the startMatchCallback status, telling it to stop waiting (so that it can handle the stop correctly)
        startMatchCallback.stopWaiting(stop);
    }

    /**
     * Post card.
     *
     * @param plainText the plain text
     */
    public void postCard(String plainText){
        //prepare the message
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), plainText);
        playCardCallback = new PlayCardCallback(controller);
        //send it, and set the callback on the request
        briscolaAPI.playCard(url, authHeader,contentTypePlainText, body).enqueue(playCardCallback);

    }

    /**
     * Stop match call.
     *
     * @param context the context
     */
    public void stopMatchCall(Context context) {
        stopMatchCallback = new StopMatchCallback();
        //prepare the message
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), context.getString(R.string.terminated));
        //send it, and set the callback on the request
        briscolaAPI.stopMatch(url, authHeader, contentTypePlainText, body).enqueue(stopMatchCallback);
    }

    /**
     * Initialize client (i.e. initialize BriscolaAPI using Retrofit and OkHttpClient).
     */
    public void initializeClient(){

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS) //as specified in the server specification, use 30s timeout
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        //initialize retrofit rest api
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                // .addConverterFactory(GsonConverterFactory.create(gson))
                // .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build();

        briscolaAPI = retrofit.create(BriscolaAPI.class);
    }

    /**
     * Force match end (interrupt it)
     *
     * @param context the context, to retrieve string data
     */
    public void forceMatchEnd(Context context){
        stopMatchCallback = new StopMatchCallback();
        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), context.getString(R.string.abandon));
        briscolaAPI.stopMatch(url,authHeader,contentTypePlainText,body).enqueue(stopMatchCallback);
        if(opponentPlayedCardCallback != null) opponentPlayedCardCallback.shouldStopRetrying(true);
        if(playCardCallback != null) playCardCallback.shouldStopRetrying(true);
        if(startMatchCallback != null) startMatchCallback.shouldStopRetrying(true);
        //stopMatchCallback.shouldStopRetrying(true);
    }

    /**
     * Set url.
     *
     * @param url the url returned by the server
     */
    public void setUrl(String url){
        this.url = url;
    }
}
