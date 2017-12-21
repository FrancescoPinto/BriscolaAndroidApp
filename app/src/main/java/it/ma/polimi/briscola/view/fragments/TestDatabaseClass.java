package it.ma.polimi.briscola.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.ma.polimi.briscola.R;

/**
 * Created by utente on 12/12/17.
 */

public class TestDatabaseClass extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_briscola_match, container, false); //todo, refactor del nome

        //SaveFinishedMatchRecordDialogFragment dialog = new SaveFinishedMatchRecordDialogFragment(getActivity(),68,true);
       // dialog.showDialog();

       // WinnerMatchDialog dialog = new WinnerMatchDialog();
       // dialog.buildDialog(getActivity(), 1, 67, this);
       // dialog.show();
        return mainView;

    }

    public void saveMatchData(int player0Points){

    }
}
