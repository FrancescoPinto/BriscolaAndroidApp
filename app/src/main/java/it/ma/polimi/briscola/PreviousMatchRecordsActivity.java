package it.ma.polimi.briscola;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.ma.polimi.briscola.model.briscola.statistics.Briscola2PAggregatedData;
import it.ma.polimi.briscola.model.briscola.statistics.Briscola2PMatchRecord;
import it.ma.polimi.briscola.persistency.SQLiteBriscola2PMatchRecordRepositoryImpl;

/**
 * Created by utente on 28/11/17.
 */

public class PreviousMatchRecordsActivity extends AppCompatActivity {
    private RecyclerView matchRecordRecyclerView;
    private MatchRecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_record_list);
        // init the view
        matchRecordRecyclerView = (RecyclerView) findViewById(R.id.match_record_recyclerview);
        matchRecordRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateUI();

    }


    private void updateUI(){

        //todo, riabilita
        SQLiteBriscola2PMatchRecordRepositoryImpl repo = new SQLiteBriscola2PMatchRecordRepositoryImpl(this); //todo, sposta questo o nel controller o nel model (non dovresti fare operazioni logiche così complicate in una view!)
        List<Briscola2PMatchRecord> records = repo.findAll();
        //List<Briscola2PMatchRecord> records = new ArrayList<>();

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
            View view = layoutInflater.inflate(R.layout.match_record_list_item_layout, parent, false);
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


}
