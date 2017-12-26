package it.ma.polimi.briscola.rest.client.callbacks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import it.ma.polimi.briscola.rest.client.endpoints.BriscolaAPI;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by utente on 26/12/17.
 */

public class TestAPI {

    private static final String BASE_URL = "http://mobile17.ifmledit.org/api/";
    private static final String  authHeader = "APIKey 0c3828b9-b0d6-45c3-aa3b-a6d324561569",
            roomName = "Group01",
            contentTypePlainText = "text/plain";
    private static OpponentPlayedCardCallback opponentPlayedCardCallback = new OpponentPlayedCardCallback();
    private static PlayCardCallback playCardCallback = new PlayCardCallback();
    private static StartMatchCallback startMatchCallback = new StartMatchCallback();
    private static StopMatchCallback stopMatchCallback = new StopMatchCallback();
    private static BriscolaAPI briscolaAPI;

    public static void main(){
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build();

        briscolaAPI = retrofit.create(BriscolaAPI.class);

        while(true){
            Scanner s = new Scanner(System.in);
            String str = s.nextLine();
            switch(str){
                case "start":        briscolaAPI.startMatch(authHeader, roomName).enqueue(startMatchCallback);
                    ;break;
                case "stop":;break;
                case "play":;break;
                case "oppon":;break;
            }
        }
    }
}
