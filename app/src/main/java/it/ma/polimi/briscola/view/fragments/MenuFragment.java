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
import android.widget.ImageButton;

import it.ma.polimi.briscola.MatchMenuActivity;
import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.persistency.SettingsManager;

/**
 * Created by utente on 13/12/17.
 */

public class MenuFragment extends Fragment {

    private ImageButton startOfflineMatch, loadOfflineMatch, startOnlineMatch;

    public static MenuFragment newInstance(){
        return new MenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // init the view
        View mainView = inflater.inflate(R.layout.fragment_main_menu, container, false);

        startOfflineMatch = (ImageButton) mainView.findViewById(R.id.start_offline_match);
        startOfflineMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //todo, anziché settare la difficulty qui, fallo nel newInstance, assegnandolo all'oggeto fragment che istanzi!

                // Pop off everything up to and including the current tab
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                // Add the new tab fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.container, TabFragment.newInstance())
                        .addToBackStack(BACK_STACK_ROOT_TAG)
                        .commit();
            }





            Fragment fragment = Briscola2PMatchFragment.newInstance(false, new SettingsManager(getActivity().getApplicationContext()).getDifficultyPreference()); //false = not online
                fragment.setEnterTransition(new Slide(Gravity.RIGHT));
                fragment.setExitTransition(new Slide(Gravity.LEFT));

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = MenuFragment.this.getActivity().getSupportFragmentManager();
                Fragment oldMatch = fragmentManager.findFragmentByTag("offline_match");
                if(oldMatch != null)
                    fragmentManager.beginTransaction().remove(oldMatch).commit();

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment,"offline_match") //todo, oppure replace?
                        .commit();
            }
        });

        loadOfflineMatch = (ImageButton) mainView.findViewById(R.id.load_from_saved);
        loadOfflineMatch.setOnClickListener(new View.OnClickListener() {
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


        startOnlineMatch = (ImageButton) mainView.findViewById(R.id.start_online_match);
        startOnlineMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = Briscola2PMatchFragment.newInstance(true, null); //true = online match
                fragment.setEnterTransition(new Slide(Gravity.RIGHT));
                fragment.setExitTransition(new Slide(Gravity.LEFT));

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = MenuFragment.this.getActivity().getSupportFragmentManager();
                Fragment oldMatch = fragmentManager.findFragmentByTag("online_match");
                if(oldMatch != null)
                    fragmentManager.beginTransaction().remove(oldMatch).commit();

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment,"online_match") //todo, oppure replace?
                        .commit();
            }
        });


        return mainView;

    }

    public void warnStopMatchIfNeeded(){

    }
}