package it.ma.polimi.briscola.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.ma.polimi.briscola.MatchMenuActivity;
import it.ma.polimi.briscola.R;

/**
 * Created by utente on 28/11/17.
 */

public class OfflineMenuFragment extends Fragment {

    private MatchMenuActivity activity;

    public static OfflineMenuFragment newInstance(){
        return new OfflineMenuFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // init the view
        View mainView = inflater.inflate(R.layout.fragment_offline_menu, container, false);
        return mainView;

    }


    public void setActivity(MatchMenuActivity activity) {
        this.activity = activity;
    }
}