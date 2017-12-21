package it.ma.polimi.briscola.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Created by utente on 28/11/17.
 */

public class PreviousMatchRecordsActivity extends AppCompatActivity {
    private RecyclerView matchRecordRecyclerView;
    private MatchRecordAdapter adapter;
    private TextView numMatchPlayed, numMatchWon, numMatchLost, numMatchDraw, numMatchOnline;
    SQLiteRepositoryImpl repo;
    private TextView noAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_record_list);
        // init the view
        matchRecordRecyclerView = (RecyclerView) findViewById(R.id.match_record_recyclerview);
        matchRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        repo = new SQLiteRepositoryImpl(this);

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

        updateUI();

    }


    private void updateUI(){

        //todo, riabilita
         //todo, sposta questo o nel controller o nel model (non dovresti fare operazioni logiche così complicate in una view!)
        List<Briscola2PMatchRecord> records = repo.findAllMatchRecords();
        //List<Briscola2PMatchRecord> records = new ArrayList<>();
        if(records.isEmpty())
            noAvailable.setVisibility(View.VISIBLE);
        else
            noAvailable.setVisibility(View.GONE);



        adapter = new MatchRecordAdapter(records);
        matchRecordRecyclerView.setAdapter(adapter);

        //todo, vedi quella cosa che DOVRESTI fare con il notify suli libro
    }


    private class MatchRecordAdapter extends RecyclerView.Adapter<MatchRecord>{
        private List<Briscola2PMatchRecord> records; //todo RankingAggregatedData contiene la sintesi dei dati salvati sull'utente (ricorda, gli utenti sono identificati solo dal nome)

        public MatchRecordAdapter(List<Briscola2PMatchRecord> orderedRankingRows){
            //Collections.sort(rankingRows);
            //Collections.reverse(rankingRows);
            this.records = orderedRankingRows;
        }

        @Override
        public MatchRecord onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(PreviousMatchRecordsActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item_match_record, parent, false);
            return new MatchRecord(view);
        }

        @Override
        public void onBindViewHolder(MatchRecord holder, int position){
            Briscola2PMatchRecord data = records.get(position);
            holder.bindRankingRow(data, position);
        }

        @Override
        public int getItemCount(){
            return records.size();
        }

    }

    private class MatchRecord extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{
        Briscola2PMatchRecord data;
        public TextView id, player0Score, namePlayer0, namePlayer1, player1Score;

        public MatchRecord(View view){
            super(view);
            //view.setOnClickListener(this); <- not needed

            id = (TextView) view.findViewById(R.id.idRecord);
            player0Score = (TextView) view.findViewById(R.id.player0Score);
            namePlayer0 = (TextView) view.findViewById(R.id.namePlayer0);
            namePlayer1 = (TextView) view.findViewById(R.id.namePlayer1);
            player1Score = (TextView) view.findViewById(R.id.player1Score);

        }

        public void bindRankingRow(Briscola2PMatchRecord data, int position){
            this.data = data;
            id.setText("#"+position);
            Log.d("TAG",data.toString());
            player0Score.setText("Score: "+ data.getPlayer0Score());
            player1Score.setText("Score: "+ data.getPlayer1Score());
            namePlayer0.setText("Player: "+ data.getPlayer0Name());
            namePlayer1.setText("Player: "+ data.getPlayer1Name());
        }

       /* @Override
        public void onClick(View v){
            Toast.makeText(getActivity(), "Mi hai cliccato bene", Toast.LENGTH_LONG).show();
        }*/
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
