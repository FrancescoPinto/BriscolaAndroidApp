package it.ma.polimi.briscola.rest.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import it.ma.polimi.briscola.rest.client.endpoints.BriscolaAPI;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by utente on 09/12/17.
 */

public class NetworkController {//implements Callback<> {

    //todo: questo link ha un esempio di uso android dell'api
    //todo: http://www.vogella.com/tutorials/Retrofit/article.html
    /*todo: segui questo pattern
    in sintesi consiste in: metti una private BriscolaAPI nell'activity,
    nei listener delle interazioni metti le chiamate all'api e accodale direttamente
    (ESEMPIO: BriscolaAPI.faiUnaCosa(parametri).enqueue(nomeCallbackCheDeveGestireRitornoDllaChiamata)
    cioè lui ISTANZA DELLE CLASSI ANONIME CHE IMPLEMENTANO Callback<> in variabili dell'activity, queste classi fano
    ovverride di onResponse e onFailure ... ce n'è una per ogni tipo di query fattibile
    POI Nell'onCreate metti praticamente il contenuto di start()
     per inizializzare Gson e Retrofit e BriscolaAPI
     */
    //TODO IMPORTANTE: capire come fare a rifare la richiesta se c'è timeout
    /*static final String BASE_URL = "http://mobile17.ifmledit.org/api/";

    public void start() {

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

        BriscolaAPI briscolaAPI = retrofit.create(BriscolaAPI.class);

       // Call<List<Change>> call = gerritAPI.loadChanges("status:open");
       // call.enqueue(this);

    }

    public void startNewMatch(){
        // Call<List<Change>> call = gerritAPI.loadChanges("status:open");
        // call.enqueue(this);
    }

    @Override //todo <- nell'implementazione finale creerai classi separate di Callback, ciascuna implementando questi due metodi, una per ciascun metodo
    public void onResponse(Call<List<Change>> call, Response<List<Change>> response) {
        if(response.isSuccessful()) {
            List<Change> changesList = response.body();
            changesList.forEach(change -> System.out.println(change.subject));
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<List<Change>> call, Throwable t) {
        t.printStackTrace();
    }
*/
}