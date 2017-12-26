package it.ma.polimi.briscola.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import it.ma.polimi.briscola.view.activities.Briscola2PMatchActivity;
import it.ma.polimi.briscola.R;

/**
 * Class that handles the menu of the game
 *
 * @author Francesco Pinto
 */
public class MenuFragment extends Fragment {

    //image buttons of the view
    private ImageButton startOfflineMatch, loadOfflineMatch, startOnlineMatch, closeMenuOverlay;
    private Briscola2PMatchActivity activity;

    /**
     * ID used to pass the isOverlay value to the fragment
     */
    public static final String IS_OVERLAY = "it.ma.polimi.briscola.matchmenu.isoverlay";

    //whether the menu is Overlay or not
    private boolean isOverlay = false;

    /**
     * Generates a new instance of MenuFragment given the parameters.
     *
     * @param isOverlay the is overlay
     * @return the menu fragment
     */
    public static MenuFragment newInstance(boolean isOverlay){
        //put data in bundle
        Bundle args = new Bundle();
        args.putBoolean(IS_OVERLAY, isOverlay);
        MenuFragment fragment =  new MenuFragment();
        //provide data to fragment
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //extract data from arguments
        isOverlay = getArguments().getBoolean(IS_OVERLAY);

        // init the view
        View mainView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        activity = (Briscola2PMatchActivity) getActivity();

        startOfflineMatch = (ImageButton) mainView.findViewById(R.id.start_offline_match);
        startOfflineMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Briscola2PMatchFragment match = activity.getMatchesFragments();
                if(match == null) { //if there is no previously running match, then start new
                    activity.startOfflineMatch();
                }else{ //else handle interrupt current match
                    match.handleMatchInterrupt(Briscola2PMatchActivity.START_NEW_OFFLINE);
                }
            }
        });

        loadOfflineMatch = (ImageButton) mainView.findViewById(R.id.load_from_saved);
        loadOfflineMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Briscola2PMatchFragment match = activity.getMatchesFragments();
                if(match == null) { //if there is no previously running match, then show saved matches
                    activity.showSavedMatches();
                }else{ //else handle interrupt current match
                    match.handleMatchInterrupt(Briscola2PMatchActivity.LOAD_OLD_MATCH);
                }
            }

        });


        startOnlineMatch = (ImageButton) mainView.findViewById(R.id.start_online_match);
        startOnlineMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Briscola2PMatchFragment match = activity.getMatchesFragments();
                if(match == null) { //if there is no previously running match, then start new
                    activity.startOnlineMatch();
                }else{//else handle interrupt current match
                    match.handleMatchInterrupt(Briscola2PMatchActivity.START_NEW_ONLINE);
                }

            }
        });

        closeMenuOverlay = (ImageButton) mainView.findViewById(R.id.close_menu_button);
        if(!isOverlay) //if menu is not an overlay you shouldn't close it with the close button
            closeMenuOverlay.setVisibility(View.GONE); //hide the button
        else{
            closeMenuOverlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.hideOverlayMenu(); //hide on click
                }
            });
        }

        return mainView;

    }

    /**
     * Whether the menu is an overlay.
     *
     * @return True if the menu is an overlay, false otherwise
     */
    public boolean getIsOverlay(){
        return isOverlay;
    }

    /**
     * Set is overlay.
     *
     * @param isOverlay True if the menu is an overlay, false otherwise
     */
    public void setIsOverlay(boolean isOverlay){
        this.isOverlay = isOverlay;
    }
}
