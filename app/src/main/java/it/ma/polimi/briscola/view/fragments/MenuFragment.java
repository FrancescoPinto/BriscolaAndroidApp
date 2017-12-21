package it.ma.polimi.briscola.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.view.activities.Briscola2PMatchActivity;
import it.ma.polimi.briscola.view.activities.MatchMenuActivity;
import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.persistency.SettingsManager;
import it.ma.polimi.briscola.view.dialog.WarningExitDialogFragment;

/**
 * Created by utente on 13/12/17.
 */

public class MenuFragment extends Fragment {

    private ImageButton startOfflineMatch, loadOfflineMatch, startOnlineMatch, closeMenuOverlay;
    private Briscola2PMatchActivity activity;
    public static final String IS_OVERLAY = "it.ma.polimi.briscola.matchmenu.isoverlay";


    private boolean isOverlay = false;
    public static MenuFragment newInstance(boolean isOverlay){
        Bundle args = new Bundle();
        args.putBoolean(IS_OVERLAY, isOverlay);

        MenuFragment fragment =  new MenuFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        isOverlay = getArguments().getBoolean(IS_OVERLAY);
        // init the view
        View mainView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        activity = (Briscola2PMatchActivity) getActivity();
        startOfflineMatch = (ImageButton) mainView.findViewById(R.id.start_offline_match);
        startOfflineMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //todo, anziché settare la difficulty qui, fallo nel newInstance, assegnandolo all'oggeto fragment che istanzi!
                Briscola2PMatchFragment match = activity.getOldMatches();
                if(match == null) {
                    activity.startOfflineMatch();
                }else{
                    match.handleMatchInterrupt(Briscola2PMatchActivity.START_NEW_OFFLINE);
                }
            /*    // Pop off everything up to and including the current tab
               FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                // Add the new tab fragment
                fragmentManager.beginTransaction()
                        .replace(R.id.container, TabFragment.newInstance())
                        .addToBackStack(BACK_STACK_ROOT_TAG)
                        .commit();

                DECIDERE QUALE DEI DUE

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
                        .commit();*/
            }
        });

        loadOfflineMatch = (ImageButton) mainView.findViewById(R.id.load_from_saved);
        loadOfflineMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Briscola2PMatchFragment match = activity.getOldMatches();
                if(match == null) {
                    activity.showSavedMatches();
                }else{
                    match.handleMatchInterrupt(Briscola2PMatchActivity.LOAD_OLD_MATCH);
                }
                /*((MatchMenuActivity) getActivity()).onBuildDialog(
                        "Sorry, no time to implement this",//getString(R.string.exit_message),
                        "Ok",//getString(R.string.yes),
                        null,//getString(R.string.no),
                        true,
                        false
                ).show();*/
            }

        });


        startOnlineMatch = (ImageButton) mainView.findViewById(R.id.start_online_match);
        startOnlineMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Briscola2PMatchFragment match = activity.getOldMatches();
                if(match == null) {
                    activity.startOnlineMatch();
                }else{
                    match.handleMatchInterrupt(Briscola2PMatchActivity.START_NEW_ONLINE);
                }
               /* Fragment fragment = Briscola2PMatchFragment.newInstance(true, null); //true = online match
                fragment.setEnterTransition(new Slide(Gravity.RIGHT));
                fragment.setExitTransition(new Slide(Gravity.LEFT));

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = MenuFragment.this.getActivity().getSupportFragmentManager();
                Fragment oldMatch = fragmentManager.findFragmentByTag("online_match");
                if(oldMatch != null)
                    fragmentManager.beginTransaction().remove(oldMatch).commit();

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment,"online_match") //todo, oppure replace?
                        .commit();*/
            }
        });

        closeMenuOverlay = (ImageButton) mainView.findViewById(R.id.close_menu_button);
        if(!isOverlay)
            closeMenuOverlay.setVisibility(View.GONE);
        else{
            closeMenuOverlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.hideOverlayMenu();
                }
            });
        }



        return mainView;

    }

    public boolean getIsOverlay(){
        return isOverlay;
    }

    public void setIsOverlay(boolean isOverlay){
        this.isOverlay = isOverlay;
    }
}
