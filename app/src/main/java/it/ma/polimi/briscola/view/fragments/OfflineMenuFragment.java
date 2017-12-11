package it.ma.polimi.briscola.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import it.ma.polimi.briscola.MatchMenuActivity;
import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.SettingsActivity;
import it.ma.polimi.briscola.controller.offline.SettingsManager;

/**
 * Created by utente on 28/11/17.
 */

public class OfflineMenuFragment extends Fragment {

    private Button startOfflineMatch, resumeOfflineMatch;

    public static OfflineMenuFragment newInstance(){
        return new OfflineMenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // init the view
        View mainView = inflater.inflate(R.layout.fragment_offline_menu, container, false);

        startOfflineMatch = (Button) mainView.findViewById(R.id.start_offline_match);
        startOfflineMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = Briscola2PMatchFragment.newInstance(false, SettingsManager.getDifficultyPreference(getActivity())); //false = not online
                fragment.setEnterTransition(new Slide(Gravity.RIGHT));
                fragment.setExitTransition(new Slide(Gravity.LEFT));

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = OfflineMenuFragment.this.getActivity().getSupportFragmentManager();
                Fragment oldMatch = fragmentManager.findFragmentByTag("offline_match");
                if(oldMatch != null)
                    fragmentManager.beginTransaction().remove(oldMatch).commit();

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment,"offline_match") //todo, oppure replace?
                        .commit();
            }
        });

        resumeOfflineMatch = (Button) mainView.findViewById(R.id.resume_from_saved);
        resumeOfflineMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MatchMenuActivity) getActivity()).onBuildDialog(
                        "Sorry, no time to implement this",//getString(R.string.exit_message),
                        "Ok",//getString(R.string.yes),
                        null,//getString(R.string.no),
                        true,
                        false
                ).show();
            }

        });
        return mainView;

    }

}