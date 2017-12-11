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

/**
 * Created by utente on 10/12/17.
 */

public class OnlineMenuFragment extends Fragment {
    private Button startOnlineMatch, resumeOfflineMatch;

    public static OnlineMenuFragment newInstance(){
        return new OnlineMenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // init the view
        View mainView = inflater.inflate(R.layout.fragment_online_menu, container, false);

        startOnlineMatch = (Button) mainView.findViewById(R.id.start_online_match);
        startOnlineMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = Briscola2PMatchFragment.newInstance(true, null); //true = online match
                fragment.setEnterTransition(new Slide(Gravity.RIGHT));
                fragment.setExitTransition(new Slide(Gravity.LEFT));

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = OnlineMenuFragment.this.getActivity().getSupportFragmentManager();
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
}
