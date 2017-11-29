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
import it.ma.polimi.briscola.persistency.SQLiteBriscola2PMatchRecordRepositoryImpl;

/**
 * Created by utente on 28/11/17.
 */

public class RankingActivity extends AppCompatActivity {

    private RecyclerView rankingRecyclerView;
    private RankingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ranking);
        // init the view
        rankingRecyclerView = (RecyclerView) findViewById(R.id.ranking_recyclerview);
        rankingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateUI();

    }


    private void updateUI(){

        //todo riabilita
        SQLiteBriscola2PMatchRecordRepositoryImpl repo = new SQLiteBriscola2PMatchRecordRepositoryImpl(this); //todo, sposta questo o nel controller o nel model (non dovresti fare operazioni logiche così complicate in una view!)
        List<Briscola2PAggregatedData> aggregatedData = repo.getRanking();

        //List<Briscola2PAggregatedData> aggregatedData = new ArrayList<>();

        adapter = new RankingAdapter(aggregatedData);
        rankingRecyclerView.setAdapter(adapter);

        //todo, vedi quella cosa che DOVRESTI fare con il notify suli libro
    }


    private class RankingAdapter extends RecyclerView.Adapter<RankingRow>{
        private List<Briscola2PAggregatedData> rankingRows; //todo RankingAggregatedData contiene la sintesi dei dati salvati sull'utente (ricorda, gli utenti sono identificati solo dal nome)

        public RankingAdapter(List<Briscola2PAggregatedData> orderedRankingRows){
            //Collections.sort(rankingRows);
            //Collections.reverse(rankingRows);
            this.rankingRows = orderedRankingRows;
        }

        @Override
        public RankingRow onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(RankingActivity.this);
            View view = layoutInflater.inflate(R.layout.ranking_list_item_layout, parent, false);
            return new RankingRow(view);
        }

        @Override
        public void onBindViewHolder(RankingRow holder, int position){
            Briscola2PAggregatedData data = rankingRows.get(position);
            holder.bindRankingRow(data, position);
        }

        @Override
        public int getItemCount(){
            return rankingRows.size();
        }

    }

    private class RankingRow extends RecyclerView.ViewHolder /*implements View.OnClickListener*/{
        Briscola2PAggregatedData data;
        public TextView name, totalScore,numberPlayedMatches, totalWins, totalDraws, rankPos;

        public RankingRow(View view){
            super(view);
            //view.setOnClickListener(this); <- not needed

            name = (TextView) view.findViewById(R.id.namePlayer);
            totalScore = (TextView) view.findViewById(R.id.totalScore);
            numberPlayedMatches = (TextView) view.findViewById(R.id.numPlayed);
            totalWins = (TextView) view.findViewById(R.id.numWon);
            totalDraws = (TextView) view.findViewById(R.id.numDraws);
            rankPos = (TextView) view.findViewById(R.id.rankPosition);

        }

        public void bindRankingRow(Briscola2PAggregatedData data, int position){
            this.data = data;
            rankPos.setText(position);
            name.setText(data.getPlayerName());
            totalScore.setText(data.getTotalScore());
            numberPlayedMatches.setText(data.getNumberOfMatchesPlayed());
            totalDraws.setText(data.getNumberOfDraws());
            totalWins.setText(data.getNumberOfMatchesWinned());
        }

       /* @Override
        public void onClick(View v){
            Toast.makeText(getActivity(), "Mi hai cliccato bene", Toast.LENGTH_LONG).show();
        }*/
    }
}