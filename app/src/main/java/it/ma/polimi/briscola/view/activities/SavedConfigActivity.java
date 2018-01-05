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
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PFullMatchConfig;
import it.ma.polimi.briscola.persistency.SQLiteRepositoryImpl;

/**
 * Activity used to show saved matches to the user.
 *
 * @author Francesco Pinto
 */
public class SavedConfigActivity extends AppCompatActivity {

    private RecyclerView savedConfigRecyclerView;
    private SavedConfigAdapter adapter;

    private TextView noAvailable;

    /**
     * The constant id representing the extra EXTRA_LOAD_CONFIG.
     */
    public static final String EXTRA_LOAD_CONFIG = "it.ma.polimi.briscola.savedConfig.config";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_config);

        noAvailable = (TextView) findViewById(R.id.no_items_saved_matches);

        //initialize the recyclerView
        savedConfigRecyclerView = (RecyclerView) findViewById(R.id.saved_config_recyclerview);
        savedConfigRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateUI();


    }

    private void updateUI(){

        SQLiteRepositoryImpl repo = new SQLiteRepositoryImpl(this);
        List<Briscola2PFullMatchConfig> savedConfig = repo.findAllMatchConfig();

        if(savedConfig.isEmpty()) //if no items available in the list, tell the user
            noAvailable.setVisibility(View.VISIBLE);
        else
            noAvailable.setVisibility(View.GONE);

        adapter = new SavedConfigAdapter(savedConfig);
        savedConfigRecyclerView.setAdapter(adapter);

    }

    //REMARK: since Adapter and ViewHolder should be used ONLY by this activity, they are embedded here for
    //simplicity. They can allways be put in separate classes for reuse. Since it is not the case of this project
    //I preferred to keep them here and don't make the package structure more messy

    //from now on, no extremely detailed documentation is provided (this is VERY STANDARD ANDROID CODE ... there is no need to explain it step by step)
    private class SavedConfigAdapter extends RecyclerView.Adapter<SavedConfig>{
        private List<Briscola2PFullMatchConfig> savedConfigRows;

        /**
         * Instantiates a new Saved Config adapter.
         *
         * @param savedConfigRows the saved config rows
         */
        public SavedConfigAdapter(List<Briscola2PFullMatchConfig> savedConfigRows){
            this.savedConfigRows = savedConfigRows;
        }

        @Override
        public SavedConfig onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(SavedConfigActivity.this);
            //inflate the layout for the list item
            View view = layoutInflater.inflate(R.layout.list_item_saved_config, parent, false);
            return new SavedConfig(view);
        }

        @Override
        public void onBindViewHolder(SavedConfig holder, int position){
            Briscola2PFullMatchConfig data = savedConfigRows.get(position);
            holder.bindSavedConfigRow(data, position);
        }

        @Override
        public int getItemCount(){
            return savedConfigRows.size();
        }

    }

    private class SavedConfig extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{

        //the config data
        Briscola2PFullMatchConfig data;

        //widget references
        public TextView name, position;

        public Button load,cancel;

        /**
         * Instantiates a new Saved config viewHolder.
         *
         * @param view the view
         */
        public SavedConfig(View view){
            super(view);

            name = (TextView) view.findViewById(R.id.name_saved_config);
            position = (TextView) view.findViewById(R.id.identifier_saved_config);
            load = (Button) view.findViewById(R.id.load_button);
            load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(); //send the calling activity the config data to be loaded in a bundle
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(EXTRA_LOAD_CONFIG,data);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent); //return data to the calling activity
                    finish(); //close
                }
            });

            cancel = (Button) view.findViewById(R.id.cancel_saved_button);
            cancel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    SQLiteRepositoryImpl repo = new SQLiteRepositoryImpl(SavedConfigActivity.this);
                    repo.deleteMatchConfig(data.getId()); //delete the row from database
                    adapter.savedConfigRows.remove(getAdapterPosition()); //remove it from the recyclerView
                    adapter.notifyDataSetChanged(); //notify changes
                }
            });

        }

        /**
         * Bind saved config row.
         *
         * @param data     the data
         * @param position the position
         */
        public void bindSavedConfigRow(Briscola2PFullMatchConfig data, int position){
            this.data = data;
            name.setText(data.getName());
            this.position.setText(""+position);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}