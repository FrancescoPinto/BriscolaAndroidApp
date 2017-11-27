package it.ma.polimi.briscola;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import it.ma.polimi.briscola.view.DrawerListAdapter;


public class MatchMenuActivity extends AppCompatActivity {

    /** the DrawerLayout */
    private DrawerLayout drawerLayout;
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
        // Find our drawer view
        drawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        /*switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }*/

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
