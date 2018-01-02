package it.ma.polimi.briscola.view.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import it.ma.polimi.briscola.BriscolaApplication;
import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.audio.SoundService;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PFullMatchConfig;
import it.ma.polimi.briscola.persistency.SettingsManager;
import it.ma.polimi.briscola.view.fragments.Briscola2PMatchFragment;
import it.ma.polimi.briscola.view.fragments.MenuFragment;


/**
 * Simple implementation of an activity that is able to host a match .
 *
 * @author Francesco Pinto
 */
public class MatchActivity extends AppCompatActivity implements Briscola2PMatchActivity{

    private DrawerLayout drawerLayout;
    private NavigationView nvDrawer;
    private Menu menu;
    private Toolbar toolbar;
    private SettingsManager settingsManager;
    private boolean backButtonPressedOnce = false;
    /**
     * The request code used to pick a match config to load when the SavedConfigActivity returns it, PICK_MATCH_TO_LOAD.
     */
    public static final int PICK_MATCH_TO_LOAD = 1;


    /**
     * Gets sound manager.
     *
     * @return the sound manager
     */
    public SoundService getSoundManager() {
        return ((BriscolaApplication)getApplication()).getSoundManager();
    }

    private final String menuFragmentTag ="game_menu", offlineMatchTag = "offline_match", onlineMatchTag = "online_match";

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_menu);

        //save an instance of SettingsManager
        settingsManager = new SettingsManager(getApplicationContext());

        //set up the drawer (and consistently handle toolbar/actionbar)
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
        drawerLayout.addDrawerListener(drawerToggle); // add listener to drawer
        nvDrawer = (NavigationView) findViewById(R.id.nvView); // Setup drawer view

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0,0)
            {
                public void onDrawerClosed(View view)
                {
                    supportInvalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView)
                {
                    supportInvalidateOptionsMenu();
                }
            };
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.syncState();
        }
        setupDrawerContent(nvDrawer);

        menu = nvDrawer.getMenu();


        setVolumeControlStream(AudioManager.STREAM_MUSIC); //set that music is controlled via the +/- physical buttons


        // find the retained fragment on activity restarts (e.g. retained because of screen rotation!)
        FragmentManager fm = getSupportFragmentManager();
        Briscola2PMatchFragment wasPlayingOfflineMatchFragment = (Briscola2PMatchFragment) fm.findFragmentByTag(offlineMatchTag);
        Briscola2PMatchFragment wasPlayingOnlineMatchFragment = (Briscola2PMatchFragment) fm.findFragmentByTag(onlineMatchTag);

        if(wasPlayingOfflineMatchFragment != null){
            //do nothing, fragment is already available
        }else if(wasPlayingOnlineMatchFragment != null){
            //do nothing, fragment is already available
        }else if (wasPlayingOfflineMatchFragment == null && wasPlayingOnlineMatchFragment == null) {
            // create the fragment and data the first time
            startMenu(false);
        } //todo, qui ci potrebbe essere un bug legato ai menu ...
        //}else if(wasPlayingOnlineMatchFragment==null){
            // create the fragment and data the first time
        //    startMenu(false);
        //}
        //if there was a match running, restart the match


    }

    @Override
    protected void onResume(){
        super.onResume();
        // since the drawer calls activities and not fragments, they should NOT be selected (as they would by default) when the drawer is opened again
        unCheckAllMenuItems(menu);

    }


    //helper method, uncheks all items in menu and its submenus
    private void unCheckAllMenuItems( Menu menu) {
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            final MenuItem item = menu.getItem(i);
            if(item.hasSubMenu()) {
                // recursively uncheck sub menu items
                unCheckAllMenuItems(item.getSubMenu());
            } else {
                item.setChecked(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        ((BriscolaApplication) getApplication()).cleanAudio(); //perform cleaning for audio resources
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (item.getItemId() == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(Gravity.START)) {
                drawerLayout.closeDrawer(Gravity.START);
            }else{
                drawerLayout.openDrawer(Gravity.START);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    /**
     * Select drawer item.
     *
     * @param menuItem the menu item
     */
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Intent intent;
        Class fragmentClass;
        switch(menuItem.getItemId()) {

            case R.id.id_performance:
                //show performance screen
                intent = new Intent(MatchActivity.this, PreviousMatchRecordsActivity.class);
                startActivity(intent);
                break;
            case R.id.id_settings:
                //show settings screen
                intent = new Intent(MatchActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                return;
        }

        //since they are handled through activities and drawer is not necessary in those activities
        //when the activity are closed the menuItem should not be checked
        drawerLayout.closeDrawers();
    }


    @Override
    public void onBackPressed() {

        MenuFragment menu = (MenuFragment) getSupportFragmentManager().findFragmentByTag(menuFragmentTag);
        Briscola2PMatchFragment match = getMatchesFragments();
        if(menu != null && menu.getIsOverlay()){ //if there is a menu overlay, hide it
            hideOverlayMenu();
            menu.setIsOverlay(false);//if there is an overlay, priority to overlay closure rather than closing the match
        }else if(match!=null) { //if there is a match
                match.handleMatchInterrupt(BACK_PRESSED); //handle the interrupt (i.e. tell user "hey, you're going to stop the match!"

        }else{
            if(!backButtonPressedOnce) { //warn the user if it is the first time he presses the back button
                Toast.makeText(this,R.string.warn_back_button,Toast.LENGTH_SHORT).show();
                backButtonPressedOnce = !backButtonPressedOnce;
            }
            else //if i already warned the user he would have closed
                super.onBackPressed(); //close
        }

    }


    @Override
    public void startMenu(boolean isOverlay){
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(!isOverlay){ //delete everything below, and put menu
            //create fragment instance
            MenuFragment fragment = MenuFragment.newInstance(false); //false = not overlay
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.fragment_container, fragment, menuFragmentTag) //replace, no overlay
                    .commit();
        }else { //put the menu in overlay over the current fragment

            //remove menus, if any
            Fragment oldFragment = fragmentManager.findFragmentByTag(menuFragmentTag);
            if (oldFragment != null) {
                fragmentManager.beginTransaction().remove(oldFragment).commit(); //clean
            }

            Fragment newFragment;
            //create fragment instance
            newFragment = MenuFragment.newInstance(true); //true = overlay

            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .add(R.id.fragment_container, newFragment, menuFragmentTag) //add it, overlay
                    .commit();
        }
    }

    @Override
    public void startOfflineMatch(){
        //prepare fragment
        Briscola2PMatchFragment fragment = Briscola2PMatchFragment.newInstance(false,settingsManager.getDifficultyPreference(), settingsManager.getCardViewPreference());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragment_container, fragment, offlineMatchTag)
                .commit();
    }
    @Override
    public void startOnlineMatch(){
        //prepare fragment
        Briscola2PMatchFragment fragment = Briscola2PMatchFragment.newInstance(true,0, settingsManager.getCardViewPreference());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragment_container, fragment, onlineMatchTag)
                .commit();
    }
    @Override
    public void loadOfflineMatch(Briscola2PFullMatchConfig config){
        if(config == null) throw new IllegalArgumentException(); //shouldn't be called with no config to be loaded!
        //prepare fragment
        Briscola2PMatchFragment fragment = Briscola2PMatchFragment.newInstance(config, settingsManager.getDifficultyPreference(),settingsManager.getCardViewPreference());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragment_container, fragment, offlineMatchTag)
                .commitAllowingStateLoss(); //because this is invoked when the matchMenuActivity is in background ... avoids IllegalStateException
    }

    @Override
    public void showSavedMatches(){
        startActivityForResult(new Intent(MatchActivity.this,SavedConfigActivity.class),PICK_MATCH_TO_LOAD);
    }

    @Override
    public Briscola2PMatchFragment getMatchesFragments(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        //check for offline matches
        Briscola2PMatchFragment oldFragment = (Briscola2PMatchFragment) fragmentManager.findFragmentByTag(offlineMatchTag);
        if(oldFragment != null) //if found return it
            return oldFragment;

        //check for online matches
        oldFragment = (Briscola2PMatchFragment) fragmentManager.findFragmentByTag(onlineMatchTag);
        if(oldFragment != null)//if found, return it
            return oldFragment;

        return null;

    }

    @Override
    public void hideOverlayMenu(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment oldFragment = fragmentManager.findFragmentByTag(menuFragmentTag);
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                .remove(oldFragment).commit(); //removes the overlay menu

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_MATCH_TO_LOAD) {
            // Make sure the request was successful

                Bundle bundle = data.getExtras();
                Briscola2PFullMatchConfig config = (Briscola2PFullMatchConfig) bundle.getSerializable(SavedConfigActivity.EXTRA_LOAD_CONFIG);

                Briscola2PMatchFragment match = getMatchesFragments();
                if(match != null){ //questa diramazione è diventata ridondante dopo il refactor del codice, la mantengo nel caso in cui cambiassi idea dopo
                    loadOfflineMatch(config);
                }else{ //altrimenti non c'è nulla da salvare, quindi fai direttamente il load
                    loadOfflineMatch(config);
                }
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState(); //correctly handle drawer

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);  //correctly handle drawer

    }



}
