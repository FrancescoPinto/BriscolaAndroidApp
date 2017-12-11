package it.ma.polimi.briscola;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.KeyEvent;
import android.view.MenuItem;

import it.ma.polimi.briscola.audio.SoundManager;
import it.ma.polimi.briscola.view.fragments.OfflineMenuFragment;
import it.ma.polimi.briscola.view.fragments.OnlineMenuFragment;


public class MatchMenuActivity extends AppCompatActivity {

    /** the DrawerLayout */
    private DrawerLayout drawerLayout;
    private NavigationView nvDrawer;
    private Toolbar toolbar;
    private SoundManager soundManager;
    public SoundManager getSoundManager() {
        return soundManager;
    }

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_menu);

        ActionBar actionBar = getSupportActionBar(); //todo, mettere l'hamburger nell'action bar, vedere perché crasha dannatamente!
        // Set a Toolbar to replace the ActionBar.
       // getSupportActionBar().hide(); todo, attenzione, questo ora funziona (ho cambiato il minSdk
        toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
       // getSupportActionBar().setHomeButtonEnabled(true); // for burger icon
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true); // burger icon related
       // getSupportActionBar().setDisplayShowCustomEnabled(true); // CRUCIAL - for displaying your custom actionbar

//todo LEGGI SOTTO
  //DA FARE MOLTO IMPORTANTE, NASCONDI LA ACTION BAR E METTI UN FLOATING BUTTON

        // Find our drawer view
        drawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
        //todo se proprio sarai costretto a fare questo drawer con adapter scritto a mano, tieni bene a mente
        //        che ilt uo vecchio adapter era inutilmente complicato: l'adapter ' deve avere solo una lista di elementi,
        //        prendi a modello l'implementazione' di adapter e holder che hai fatto per le recyclerView in RankingActivity
        //        QUINDI, se vuoi dei SEPARATORI, molto semplicemente salvi nell'adapter ' delle costanti (che rappresentano
        //gli indici di dove si troveranno, nell'UNICA' lista, i nomi dei separatori, e setterai la view in quel modo)
        // Enabling the Drawer button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, 0, 0
        );

        //todo, se vuoi puoi anche modificare il titolo sull'action bar (vedi le slides delle lezioni, slide 28 di Fragments and Navigation
        // add listener to drawer
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        // Setup drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        setupDrawerContent(nvDrawer);


        setVolumeControlStream(AudioManager.STREAM_MUSIC); //todo, spostare questo nell'activity! <------------------------
        //todo DAVVERO IMPORTANTE SPOSTARE LA GESTIONE DEL SUONO NELL'ACTIVITY!!! <-------------------------------------------------
        //todo OPPURE in una superActivity da cui erediteranno tutte le activity, o comunque trova un modo di passare il soundManager
        //todo così che tutte le activity gestiscano l'audio in modo continuo (cioè se vado in performance, l'audio non si ferma)
        soundManager = SoundManager.getInstance(getApplicationContext());
        soundManager.resumeBgMusic();


        Fragment fragment = (Fragment) OfflineMenuFragment.newInstance();
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment) //todo, oppure replace?
                .commit();

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
            case R.id.id_play_offline:
                fragmentClass = OfflineMenuFragment.class;
                startFragment(fragmentClass,menuItem);
                break;
            case R.id.id_play_online:
                fragmentClass = OnlineMenuFragment.class;
                startFragment(fragmentClass,menuItem);
                break;
            case R.id.id_ranking:
                intent = new Intent(MatchMenuActivity.this, RankingActivity.class);
               startActivity(intent);
                break;
            case R.id.id_performance:
                intent = new Intent(MatchMenuActivity.this, PreviousMatchRecordsActivity.class);
                startActivity(intent);
                break;
            case R.id.id_settings: //questa deve diventare un'activity, ma fallo solo dopo che hai finito
                intent = new Intent(MatchMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.id_game_rules: //questo un dialog tooltip
                onBuildDialog(
                        "questa deve diventare un tooltip/tutorial/qualcosa, ma fallo solo dopo che hai finito",//getString(R.string.exit_message),
                        "temporaneo si",//getString(R.string.yes),
                        "temporaneo no",//getString(R.string.no),
                        true,
                        false
                ).show();
                break;
            default:
                fragmentClass = RankingActivity.class;
                startFragment(fragmentClass,menuItem);
        }


        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    private void startFragment(Class fragmentClass, MenuItem menuItem){
        Fragment fragment = null;
        try {
            if(fragmentClass != null) {
                fragment = (Fragment) fragmentClass.newInstance();
                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragment) //todo, oppure replace?
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .commit();
                // Highlight the selected item has been done by NavigationView
                menuItem.setChecked(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   /* @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        //Sync the toggle state after onRestoreInstanceState occurred
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
*/
   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_menu);

        initializeDrawer();

    }


    private void initializeDrawer(){
        // init the drawer layout
        drawerLayout =(DrawerLayout) findViewById(R.id.id_drawerLayout);
        // init the list view
        final ListView drawerList = (ListView) findViewById(R.id.drawer_list);
        // add the header
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.nav_header_main, drawerList, false);
        drawerList.addHeaderView(header);

        // set the customized adapter to the list view
        //Map<Separator,List<Item>>
        Map<String, Map<String, Drawable>> drawerContent = new HashMap<>();

        Map<String, Drawable> sublistContent1 = new HashMap<>();
        sublistContent1.put(getString(R.string.play_offline),ContextCompat.getDrawable(this,R.drawable.ic_play_offline_black_48dp));
        sublistContent1.put(getString(R.string.play_online),ContextCompat.getDrawable(this,R.drawable.ic_play_online_black_48dp));
        drawerContent.put(getString(R.string.game), sublistContent1);

        Map<String, Drawable> sublistContent2 = new HashMap<>();
        sublistContent2.put(getString(R.string.ranking),ContextCompat.getDrawable(this,R.drawable.ic_ranking_black_48dp));
        sublistContent2.put(getString(R.string.performance_summary),ContextCompat.getDrawable(this,R.drawable.ic_performance_summary_black_48dp));
        drawerContent.put(getString(R.string.statistics), sublistContent2);

        Map<String, Drawable> sublistContent3 = new HashMap<>();
        sublistContent3.put(getString(R.string.app_settings),ContextCompat.getDrawable(this,R.drawable.ic_settings_black));
        sublistContent3.put(getString(R.string.game_rules),ContextCompat.getDrawable(this,R.drawable.ic_info));
        drawerContent.put(getString(R.string.general_sublist), sublistContent3);

        drawerList.setAdapter(new DrawerListAdapter(this,drawerContent)); //todo <- THIS IS THE MOST IMPORTANT LINE


        // set the click listener
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick (AdapterView < ? > adapterView, View view,int i, long l){
                switch (i) {
                    case 0: // the header...
                        break;
                    case 1: // the settings item
                        /*Intent intent = new Intent(MainCalculatorActivity.this, MainSettingsActivity.class);
                        intent.putExtra("result", editText.getText().toString());
                        // we want to retrieve the update
                        startActivityForResult(intent, REQUIRE_THEME);*/
       /*                 break;
                }
                // close the drawer
                drawerLayout.closeDrawer(drawerList);
            }
        });

        ActionBar temp = getSupportActionBar();
        // Enabling the Drawer button
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, 0, 0
        );
        // add listener to drawer
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(Gravity.START)) {
                drawerLayout.closeDrawer(Gravity.START);
            }else{
                drawerLayout.openDrawer(Gravity.START);
            }

        }
        return super.onOptionsItemSelected(item);
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
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && getSupportFragmentManager().getBackStackEntryCount() > 1) {

        }
        return super.onKeyDown(keyCode, event);
    }*/


/*    private DrawerLayout drawerLayout;
    private NavigationView nvDrawer;

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_menu);

        //Todo, per il drawer ho consultato questo tutorial, mixato alle lezioni: https://github.com/codepath/android_guides/wiki/Fragment-Navigation-Drawe
        //todo, da quel tutorial, una cosa che potresti aggiungere è far passare il drawer SOPRA la action bar, usando una toolbar




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        /*switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        if (item.getItemId() == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(Gravity.START)) {
                drawerLayout.closeDrawer(Gravity.START);
            }else{
                drawerLayout.openDrawer(Gravity.START);
            }

        }
        return super.onOptionsItemSelected(item);
    }
   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_menu);


        //todo, recupera tutti i riferimenti ai widget e impostane i valori
        //----------------------------------------------------------------------------------------
        //SETTING THE DRAWER
        //----------------------------------------------------------------------------------------


            /*drawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
            final ListView drawerList = (ListView) findViewById(R.id.drawer_list);

            //add the header
            LayoutInflater inflater = getLayoutInflater();
            ViewGroup header = (ViewGroup)inflater.inflate(R.layout.nav_header_main, drawerList, false);
            drawerList.addHeaderView(header);
            // set the customized adapter to the list view
            ArrayList<String> texts = new ArrayList<>();
            ArrayList<Drawable> icons = new ArrayList<>();

            texts.add(getString(R.string.app_settings));
            icons.add(ContextCompat.getDrawable(this,R.drawable.ic_settings_black));


            //texts.add(getString(R.string.show_points));
            //texts.add(getString(R.string.hide_points));
            //texts.add(getString(R.string.choose_difficutly)); //todo, aggiungere 3 sublist elements (for the choice of the difficulty)
            //texts.add(getString(R.string.choose_cards_type)); //todo, aggiungere sublist elements for various card types
            //texts.add(getString(R.string.disable_music)); todo, aggiungere queste voci di menù man mano che vai avanti
            //texts.add(getString(R.string.enable_music));
            //texts.add(getString(R.string.disable_sound_fx));
            //texts.add(getString(R.string.enable_sound_fx));
            //texts.add(getString(R.string.game_rules));
            //icons.add(ContextCompat.getDrawable(this, R.drawable.ic_show_points));//todo, per il dropdown vedi: https://stackoverflow.com/questions/29067696/how-to-create-a-dropdown-within-a-navigation-drawer-in-android
            //icons.add(ContextCompat.getDrawable(this, R.drawable.ic_music));
            //icons.add(ContextCompat.getDrawable(this, R.drawable.ic_sound));
            //icons.add(ContextCompat.getDrawable(this, R.drawable.ic_info));
            //icons.add(ContextCompat.getDrawable(this, R.drawable.ic_disabled));
            //icons.add(ContextCompat.getDrawable(this, R.drawable.ic_dropdown)); //for choose_difficulty, choose_cards_type
            //todo, per ottenere il "disabled" di qualsiasi cosa, ricordati di mettere DUE immagini invece di una: sovrapponi l'icona ic_disabled alle altre icone

            drawerList.setAdapter(new DrawerListAdapter(this, texts, icons));
            // set the click listener
            drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {
                        case 0: // the header...
                            break;
                        case 1: // the settings item
                           /* Intent intent = new Intent(MatchMenuActivity.this, MainSettingsActivity.class);
                            intent.putExtra("result", editText.getText().toString());
                            // we want to retrieve the update
                            startActivityForResult(intent, REQUIRE_THEME);
                            break;
                    }
                    // close the drawer
                    drawerLayout.closeDrawer(drawerList);
                }
            });

            // Enabling the Drawer button
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                    this, drawerLayout, 0, 0
            );
            // add listener to drawer
            drawerLayout.addDrawerListener(actionBarDrawerToggle);*/
   /* }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(Gravity.START)) {
                drawerLayout.closeDrawer(Gravity.START);
            }else{
                drawerLayout.openDrawer(Gravity.START);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        /*onBuildDialog(
                getString(R.string.exit_message),
                getString(R.string.yes),
                getString(R.string.no),
                true,
                false
        ).show();*/
    //}



}
