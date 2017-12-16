package it.ma.polimi.briscola.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.persistency.SQLiteRepositoryImpl;

/**
 * Created by utente on 28/11/17.
 */

public class SavedConfigActivity extends AppCompatActivity {

    private RecyclerView savedConfigRecyclerView;
    private SavedConfigAdapter adapter;

    public static final String EXTRA_LOAD_CONFIG = "it.ma.polimi.briscola.savedConfig.config";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_saved_config);

        //@Override
        // public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // init the view
        //    View mainView = inflater.inflate(R.layout.fragment_saved_config, container, false);

        savedConfigRecyclerView = (RecyclerView) findViewById(R.id.saved_config_recyclerview);
        savedConfigRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateUI();
        //    return mainView;

        //}

    }

    private void updateUI(){

        SQLiteRepositoryImpl repo = new SQLiteRepositoryImpl(this);
        List<Briscola2PMatchConfig> savedConfig = repo.findAllMatchConfig();

        adapter = new SavedConfigAdapter(savedConfig);
        savedConfigRecyclerView.setAdapter(adapter);

        //todo, vedi quella cosa che DOVRESTI fare con il notify suli libro
    }


    private class SavedConfigAdapter extends RecyclerView.Adapter<SavedConfig>{
        private List<Briscola2PMatchConfig> savedConfigRows;

        public SavedConfigAdapter(List<Briscola2PMatchConfig> savedConfigRows){
            //Collections.sort(rankingRows);
            //Collections.reverse(rankingRows);
            this.savedConfigRows = savedConfigRows;
        }

        @Override
        public SavedConfig onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(SavedConfigActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item_saved_config, parent, false);
            return new SavedConfig(view);
        }

        @Override
        public void onBindViewHolder(SavedConfig holder, int position){
            Briscola2PMatchConfig data = savedConfigRows.get(position);
            holder.bindSavedConfigRow(data, position);
        }

        @Override
        public int getItemCount(){
            return savedConfigRows.size();
        }

    }

    private class SavedConfig extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{
        Briscola2PMatchConfig data;
        public TextView name, position;
        public Button load, cancel;

        public SavedConfig(View view){
            super(view);
            //view.setOnClickListener(this); <- not needed

            name = (TextView) view.findViewById(R.id.name_saved_config);
            position = (TextView) view.findViewById(R.id.identifier_saved_config);
            load = (Button) view.findViewById(R.id.load_button);
            load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();//getIntent();//new Intent(SavedConfigActivity.this,MatchMenuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(EXTRA_LOAD_CONFIG,data);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                   // QUI C'è ' un errore per cui non ritorna e fa load ...
                    //matchMenuActivity.loadMatch(data); //todo ritornare il result dell'activity con il comando "avvia match loadato"
                }
            });

            cancel = (Button) view.findViewById(R.id.cancel_saved_button);
            cancel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    SQLiteRepositoryImpl repo = new SQLiteRepositoryImpl(SavedConfigActivity.this);
                    repo.deleteMatchConfig(data.getId());
                    adapter.savedConfigRows.remove(getAdapterPosition());
                    adapter.notifyDataSetChanged();
                }
            });

        }

        public void bindSavedConfigRow(Briscola2PMatchConfig data, int position){
            this.data = data;
            name.setText(data.getName());
            this.position.setText(""+position);
        }

    }



    //todo, gestire i ritorni indietro non ben fatti
}