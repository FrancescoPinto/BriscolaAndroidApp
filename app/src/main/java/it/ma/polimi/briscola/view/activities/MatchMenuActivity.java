package it.ma.polimi.briscola.view.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.audio.SoundManager;
import it.ma.polimi.briscola.audio.SoundService;
import it.ma.polimi.briscola.controller.offline.Briscola2PController;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.persistency.SettingsManager;
import it.ma.polimi.briscola.view.dialog.WarningExitDialogFragment;
import it.ma.polimi.briscola.view.fragments.Briscola2PMatchFragment;
import it.ma.polimi.briscola.view.fragments.MenuFragment;


public class MatchMenuActivity extends AppCompatActivity implements Briscola2PMatchActivity{

    /** the DrawerLayout */
    private DrawerLayout drawerLayout;
    private NavigationView nvDrawer;
    private Menu menu;
    private Toolbar toolbar;
    private SoundManager soundManager;
    private SettingsManager settingsManager;
    public static final int PICK_MATCH_TO_LOAD = 1;


    public SoundManager getSoundManager() {
        return soundManager;
    }

    private final String menuFragmentTag ="game_menu", offlineMatchTag = "offline_match", onlineMatchTag = "online_match";

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_menu);


        settingsManager = new SettingsManager(getApplicationContext());
       // ActionBar actionBar = getSupportActionBar(); //todo, mettere l'hamburger nell'action bar, vedere perché crasha dannatamente!
        // Set a Toolbar to replace the ActionBar.
       // getSupportActionBar().hide(); todo, attenzione, questo ora funziona (ho cambiato il minSdk
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);

        // add listener to drawer
        drawerLayout.addDrawerListener(drawerToggle);

        // Setup drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0,0)
            {

                public void onDrawerClosed(View view)
                {
                    supportInvalidateOptionsMenu();

                    //drawerOpened = false;
                }

                public void onDrawerOpened(View drawerView)
                {
                    supportInvalidateOptionsMenu();
                    //drawerOpened = true;
                }
            };
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerToggle.syncState();
        }
        setupDrawerContent(nvDrawer);

        menu = nvDrawer.getMenu();


        setVolumeControlStream(AudioManager.STREAM_MUSIC); //todo, spostare questo nell'activity! <------------------------
        //todo DAVVERO IMPORTANTE SPOSTARE LA GESTIONE DEL SUONO NELL'ACTIVITY!!! <-------------------------------------------------
        //todo OPPURE in una superActivity da cui erediteranno tutte le activity, o comunque trova un modo di passare il soundManager
        //todo così che tutte le activity gestiscano l'audio in modo continuo (cioè se vado in performance, l'audio non si ferma)

        //soundManager.resumeBgMusic(); todo <----- questo è il vecchio musica
        startService(new Intent(MatchMenuActivity.this, SoundManager.class));
        soundManager = SoundManager.getInstance(getApplicationContext()); //todo <----- questo è il vecchio musica
        soundManager.resumeBgMusic();
      //  Intent serviceIntent = new Intent(MatchMenuActivity.this, SoundService.class);
       // Bundle bundle = new Bundle();
       // bundle.putSerializable(SoundManager.EXTRA_SOUND_MANAGER, soundManager);
       // serviceIntent.putExtras(bundle);
        //serviceIntent.putExtra("UserID", "123456");
     //   startService(serviceIntent);

        Log.d("TAG","Grabbed the sound manager "+soundManager);
        // find the retained fragment on activity restarts
        FragmentManager fm = getSupportFragmentManager();
        Briscola2PMatchFragment wasPlayingOfflineMatchFragment = (Briscola2PMatchFragment) fm.findFragmentByTag(offlineMatchTag);
        Briscola2PMatchFragment wasPlayingOnlineMatchFragment = (Briscola2PMatchFragment) fm.findFragmentByTag(onlineMatchTag);

        if(wasPlayingOfflineMatchFragment != null){
            //do nothing, fragment is already available
        }else if(wasPlayingOnlineMatchFragment != null){
            //do nothing, fragment is already available

        }else if (wasPlayingOfflineMatchFragment == null) {
        // create the fragment and data the first time
            startMenu(false);
            // add the fragment
           // mRetainedFragment = new RetainedFragment();
            //fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();
            // load data from a data source or perform any calculation
           // mRetainedFragment.setData(loadMyData());
        }else if(wasPlayingOnlineMatchFragment==null){
            startMenu(false);
        }
        //if there was a match running, restart the match


    }


    private void unCheckAllMenuItems( Menu menu) {
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            final MenuItem item = menu.getItem(i);
            if(item.hasSubMenu()) {
                // Un check sub menu items
                unCheckAllMenuItems(item.getSubMenu());
            } else {
                item.setChecked(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(MatchMenuActivity.this, SoundManager.class));
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
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Intent intent;
        Class fragmentClass;
        switch(menuItem.getItemId()) {

            case R.id.id_performance:
                intent = new Intent(MatchMenuActivity.this, PreviousMatchRecordsActivity.class);
                startActivity(intent);
                break;
            case R.id.id_settings: //questa deve diventare un'activity, ma fallo solo dopo che hai finito
                intent = new Intent(MatchMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                return;
        }

        //since they are handled through activities and drawer is not necessary in those activities
        //when the activity are closed the menuItem should not be checked
        drawerLayout.closeDrawers();
    }


    /**
     * Build and AlertDialog instance
     *
     * @param _message:     get the message text
     * @param _positive:    get the positive button text
     * @param _negative:    get the negative button text
     * @param exit:         get the exit text
     * @return AlertDialog instance
     */
    public AlertDialog onBuildDialog(String _message, String _positive, String _negative,
                                     final boolean exit, final boolean restart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(_message);
        builder.setPositiveButton(_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (exit) {
                    /*if (restart)
                        startActivity(new Intent(MainCalculatorActivity.this, MainCalculatorActivity.class));
                    else
                        new UpdateAppSettings(MainCalculatorActivity.this).onResetSavedText();
                    finish();*/
                }
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
        if (_negative != null)
            builder.setNegativeButton(_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // dismiss dialog
                    dialogInterface.dismiss();
                }
            });
        builder.setCancelable(false);
        return builder.create();
    }

    @Override
    public void onBackPressed() {


        MenuFragment menu = (MenuFragment) getSupportFragmentManager().findFragmentByTag(menuFragmentTag);
        if(menu != null && menu.getIsOverlay()){
            hideOverlayMenu();
            menu.setIsOverlay(false);
        }else { //if there is an overlay, priority to overlay closure rather than closing the match
            Briscola2PMatchFragment match = getOldMatches();
            if (match != null) {
                match.handleMatchInterrupt(EXIT_BUTTON);
            }
        }



        super.onBackPressed(); //todo <- questo lo chiami dopo che l'utente dice' sì voglio uscire nel dialog
        // matchMenu.getIsOverlay() if yes fai una cosa altrimenti un'altra'
        if(getSupportFragmentManager().getBackStackEntryCount() > 1){
            getSupportFragmentManager().popBackStack();
        }else {
            onBuildDialog(
                    "temporaneo, vuoi uscire",//getString(R.string.exit_message),
                    "temporaneo si",//getString(R.string.yes),
                    "temporaneo no",//getString(R.string.no),
                    true,
                    false
            ).show();
        }
    }

    public void exitMatch(int motivation, Briscola2PMatchConfig loadConfig){
        /*Fragment oldMatch;
        Fragment newMatch;
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(isOnline) {
            oldMatch = fragmentManager.findFragmentByTag(onlineMatchTag);
        }
        else {
            oldMatch = fragmentManager.findFragmentByTag(offlineMatchTag);
        }*/

        //se fai exit, allora c'è lo scorso match in cima allo stack (il menù è rimasto sotto)
        //se fai START_NEW_OFFLINE allora c'è il menu overlay, il vecchio match
        //se fai START_NEW_ONLINE allora c'è il menu overlay, il vecchio match
        //se fai LOAD_OLD_MATCH allora c'è il menu overlay almeno ...

        //todo: ricorda: replace = rimuovi tutto quello che è stato messo su
        //todo: invece add = metti su
        //todo: addToBackStack = metti su e ricordatene gestendolo con una pila
        //todo: QUINDI l'unica cosa in overlay è il menu su un match, solo per quello farai il pop e l'addToBackStack
        //todo: il resto pulisce tutto ciò che ha sotto
        switch(motivation){
            case EXIT_BUTTON: startMenu(false); break;
            case START_NEW_OFFLINE:  startOfflineMatch();  break;
            case START_NEW_ONLINE:  startOnlineMatch(); break;
            case LOAD_OLD_MATCH : loadMatch(loadConfig);break;
            default: break;

        }

      /*  if(oldMatch != null)
            fragmentManager.beginTransaction().remove(oldMatch).commit();

        if(newFragment!= null)
            startFragment(newFragment, isOnline?onlineMenuItem:offlineMenuItem, isOnline?"online_match":"offline_match");*/

    }

    @Override
    public void startMenu(boolean isOverlay){
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(!isOverlay){ //delete everything below, and put menu
            MenuFragment fragment = MenuFragment.newInstance(false); //false = not overlay
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, menuFragmentTag)
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .commit();
        }else { //put the menu in overlay over the current fragment
            Fragment fragment;
            Fragment oldFragment = fragmentManager.findFragmentByTag(menuFragmentTag);

            if (oldFragment != null) {
                fragmentManager.beginTransaction().remove(oldFragment).commit();
            }
            fragment = MenuFragment.newInstance(true); //true = overlay


            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment, menuFragmentTag)
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .addToBackStack(menuFragmentTag)
                    .commit();
        }
    }
    @Override
    public void startOfflineMatch(){
        Briscola2PMatchFragment fragment = Briscola2PMatchFragment.newInstance(false,settingsManager.getDifficultyPreference());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, offlineMatchTag)
                .commit();
    }
    @Override
    public void startOnlineMatch(){
        Briscola2PMatchFragment fragment = Briscola2PMatchFragment.newInstance(true,0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, onlineMatchTag)
                .commit();
    }
    @Override
    public void loadOfflineMatch(Briscola2PMatchConfig config){
        if(config == null) throw new IllegalArgumentException();
        Briscola2PMatchFragment fragment = Briscola2PMatchFragment.newInstance(config, settingsManager.getDifficultyPreference());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, offlineMatchTag)
                .commitAllowingStateLoss(); //because this is invoked when the matchMenuActivity is in background ... avoids IllegalStateException
    }

  //  @Override
    //protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). To solve a bug on API Level > 11 that prevents correct execution of fragment transaction after onResult
   // }

    @Override
    public void showSavedMatches(){
        startActivityForResult(new Intent(MatchMenuActivity.this,SavedConfigActivity.class),PICK_MATCH_TO_LOAD);
    }

    public Briscola2PMatchFragment getOldMatches(){ //todo, credo sia inutile ... causa replace
        FragmentManager fragmentManager = getSupportFragmentManager();
        Briscola2PMatchFragment oldFragment = (Briscola2PMatchFragment) fragmentManager.findFragmentByTag(offlineMatchTag);
        if(oldFragment != null)
            return oldFragment;
            //fragmentManager.beginTransaction().remove(oldFragment).commit();

        oldFragment = (Briscola2PMatchFragment) fragmentManager.findFragmentByTag(onlineMatchTag);
        if(oldFragment != null)
            return oldFragment;

        return null;

    }

    public void hideOverlayMenu(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment oldFragment = fragmentManager.findFragmentByTag(menuFragmentTag);
        fragmentManager.beginTransaction().remove(oldFragment).commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == PICK_MATCH_TO_LOAD) {
            // Make sure the request was successful

                Bundle bundle = data.getExtras();
                Briscola2PMatchConfig config = (Briscola2PMatchConfig) bundle.getSerializable(SavedConfigActivity.EXTRA_LOAD_CONFIG);

                Briscola2PMatchFragment match = getOldMatches();
                if(match != null){ //poiché load è chiamato solo dal menù principale, ci deve essere un MenuFragment
                    loadOfflineMatch(config);
                    //FragmentManager manager = getFragmentManager();
                   // WarningExitDialogFragment dialog = WarningExitDialogFragment.newInstance(isOnline, Briscola2PMatchActivity.LOAD_OLD_MATCH, loadConfig);
                   // dialog.setTargetFragment(Briscola2PMatchFragment.this, REQUEST_EXIT);
                   // manager.beginTransaction().add(R.id.fragment_container,dialog).commitAllowingStateLoss();
                    //dialog.show(manager, DIALOG_EXIT);
                   // match.handleLoadConfig(config);
                    //FragmentManager manager = getSupportFragmentManager();
                    //MenuFragment menu = (MenuFragment) manager.findFragmentByTag(menuFragmentTag);
                    //menu.handleLoadConfig(config);
                }else{ //altrimenti non c'è nulla da salvare, quindi fai direttamente il load
                    loadOfflineMatch(config);
                }

            }



            //todo, considera se è necessario per settings <- ma io vorrei salvare il tutto già dentro SettingsActivity
            //poi se uno vuole i dati salvati si prende il settingsmanager e amen
            //invece il previous match records non ritorna un bel niente, quindi inutile fare


        }


    }
    public void loadMatch(Briscola2PMatchConfig config) {

      /*  Briscola2PMatchFragment fragment = Briscola2PMatchFragment.newInstance(config, new SettingsManager(getApplicationContext()).getDifficultyPreference());
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment oldOfflineMatch = fragmentManager.findFragmentByTag(offlineMatchTag);
        Fragment oldOnlineMatch = fragmentManager.findFragmentByTag(onlineMatchTag);
        if(oldOfflineMatch != null || oldOnlineMatch != null){
            WarningExitDialogFragment warningDialog = WarningExitDialogFragment.newInstance(false,WarningExitDialogFragment.LOAD_OLD_MATCH);
            warningDialog.show(fragmentManager, WARNING_DIALOG);
            devi trovare un modo di dire "hey, ti sto chiedendo di spegnere il match corrente per avviarne uno nuovo!"
                    TROVATO: gestisci la motivation!
        }

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, offlineMatchTag)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .commit();

       /* FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment oldMatch = fragmentManager.findFragmentByTag("offline_match");
        if(oldMatch != null)
            fragmentManager.beginTransaction().remove(oldMatch).commit();

        oldMatch = fragmentManager.findFragmentByTag("online_match");
        if(oldMatch != null)
            fragmentManager.beginTransaction().remove(oldMatch).commit();

        Fragment oldFragment = fragmentManager.findFragmentByTag("saved_config");
        if(oldFragment != null)
            fragmentManager.beginTransaction().remove(oldFragment).commit();

        Briscola2PMatchFragment fragment = Briscola2PMatchFragment.newInstance(config,new SettingsManager(getApplicationContext()).getDifficultyPreference());
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, "offline_match") //todo, oppure replace?
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .commit();*/
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);

    }



}
