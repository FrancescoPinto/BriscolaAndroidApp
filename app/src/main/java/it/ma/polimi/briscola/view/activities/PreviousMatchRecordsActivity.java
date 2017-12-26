package it.ma.polimi.briscola.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.model.briscola.statistics.Briscola2PAggregatedData;
import it.ma.polimi.briscola.model.briscola.statistics.Briscola2PMatchRecord;
import it.ma.polimi.briscola.persistency.SQLiteRepositoryImpl;

/**
 * Activity used to show saved match records (statistics) to the user.
 *
 * @author Francesco Pinto
 */
public class PreviousMatchRecordsActivity extends AppCompatActivity {
    private RecyclerView matchRecordRecyclerView;
    private MatchRecordAdapter adapter;
    private TextView numMatchPlayed, numMatchWon, numMatchLost, numMatchDraw, numMatchOnline;
    private SQLiteRepositoryImpl repo;
    private TextView noAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_record_list);

        // init the view and class attributes
        //init recyclerview
        matchRecordRecyclerView = (RecyclerView) findViewById(R.id.match_record_recyclerview);
        matchRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        repo = new SQLiteRepositoryImpl(this);

        //compute and show some statistics
        Briscola2PAggregatedData statistics = new Briscola2PAggregatedData(repo.findAllMatchRecords());

        numMatchPlayed = (TextView) findViewById(R.id.num_match_played);
        numMatchPlayed.setText(""+statistics.getNumberOfMatchesPlayed());

        numMatchWon = (TextView) findViewById(R.id.num_match_won);
        numMatchWon.setText(""+statistics.getNumberOfMatchesWinned());

        numMatchLost = (TextView) findViewById(R.id.num_match_lost);
        numMatchLost.setText(""+(statistics.getNumberOfMatchesPlayed()-statistics.getNumberOfMatchesWinned()-statistics.getNumberOfDraws()));

        numMatchDraw = (TextView) findViewById(R.id.num_match_draw);
        numMatchDraw.setText(""+statistics.getNumberOfDraws());

        numMatchOnline = (TextView) findViewById(R.id.num_match_online);
        numMatchOnline.setText(""+statistics.getNumberOfMatchesOnline());

        noAvailable = (TextView) findViewById(R.id.no_items_match_records);

        //initialize recyclerViewContent
        updateUI();

    }


    private void updateUI(){

        List<Briscola2PMatchRecord> records = repo.findAllMatchRecords();

        if(records.isEmpty()) //if no records available, tell the user
            noAvailable.setVisibility(View.VISIBLE);
        else
            noAvailable.setVisibility(View.GONE);

        //handle recyclerview
        adapter = new MatchRecordAdapter(records);
        matchRecordRecyclerView.setAdapter(adapter);
    }


    //REMARK: since Adapter and ViewHolder should be used ONLY by this activity, they are embedded here for
    //simplicity. They can allways be put in separate classes for reuse. Since it is not the case of this project
    //I preferred to keep them here and don't make the package structure more messy

    //from now on, no extremely detailed documentation is provided (this is VERY STANDARD Android code ... there is no need to explain it step by step)
    private class MatchRecordAdapter extends RecyclerView.Adapter<MatchRecord>{
        private List<Briscola2PMatchRecord> records;

        /**
         * Instantiates a new Match record adapter.
         *
         * @param orderedRankingRows the ordered ranking rows
         */
        public MatchRecordAdapter(List<Briscola2PMatchRecord> orderedRankingRows){
            this.records = orderedRankingRows;
        }

        @Override
        public MatchRecord onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(PreviousMatchRecordsActivity.this);
            //inflate the layout of the list item
            View view = layoutInflater.inflate(R.layout.list_item_match_record, parent, false);
            return new MatchRecord(view);
        }

        @Override
        public void onBindViewHolder(MatchRecord holder, int position){
            Briscola2PMatchRecord data = records.get(position);
            holder.bindRecordRow(data, position);
        }

        @Override
        public int getItemCount(){
            return records.size();
        }

    }

    private class MatchRecord extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{
        //match record data
        Briscola2PMatchRecord data;
        //widgets
        public TextView id, player0Score, namePlayer0, namePlayer1, player1Score;

        /**
         * Instantiates a new Match record.
         *
         * @param view the view
         */
        public MatchRecord(View view){
            super(view);
            id = (TextView) view.findViewById(R.id.idRecord);
            player0Score = (TextView) view.findViewById(R.id.player0Score);
            namePlayer0 = (TextView) view.findViewById(R.id.namePlayer0);
            namePlayer1 = (TextView) view.findViewById(R.id.namePlayer1);
            player1Score = (TextView) view.findViewById(R.id.player1Score);

        }

        /**
         * Bind record row.
         *
         * @param data     the data
         * @param position the position
         */
        public void bindRecordRow(Briscola2PMatchRecord data, int position){
            this.data = data; //perform the binding
            id.setText(""+position);
            player0Score.setText(getString(R.string.record_score,data.getPlayer0Score()));
            player1Score.setText(getString(R.string.record_score,data.getPlayer1Score()));
            namePlayer0.setText(getString(R.string.record_player_name, data.getPlayer0Name()));
            namePlayer1.setText(getString(R.string.record_player_name,data.getPlayer1Name()));
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
