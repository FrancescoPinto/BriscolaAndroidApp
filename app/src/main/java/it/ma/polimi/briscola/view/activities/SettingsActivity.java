package it.ma.polimi.briscola.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import it.ma.polimi.briscola.BriscolaApplication;
import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.audio.SoundService;
import it.ma.polimi.briscola.controller.listeners.DifficultyRadioGroupListener;
import it.ma.polimi.briscola.controller.listeners.VelocityRadioGroupListener;
import it.ma.polimi.briscola.persistency.SettingsManager;

/**
 * Activity used to show settings to the user .
 *
 * @author Francesco Pinto
 */
public class SettingsActivity extends AppCompatActivity{


    private Switch audio, sfx;
    private RadioGroup difficultyRadioGroup;
    private RadioButton easy, medium, hard, veryHard;
    private RadioGroup velocityRadioGroup;
    private RadioButton normal, fast, veryFast;
    private SoundService soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //VERY IMPORTANT REMARK: SETTINGS ARE IMMEDIATELY SAVED AS THE USER INTERACTS WITH THEM
        super.onCreate(savedInstanceState);

        // set the content view
        setContentView(R.layout.activity_main_settings);

        // init the elements

        difficultyRadioGroup = (RadioGroup) findViewById(R.id.difficulty_radio_group);
        easy = (RadioButton) findViewById(R.id.difficulty_easy);
        medium = (RadioButton) findViewById(R.id.difficutly_intermediate);
        hard = (RadioButton) findViewById(R.id.difficulty_hard);
        //veryHard = (RadioButton) findViewById(R.id.difficulty_very_hard); //todo, se ho tempo per fare un altro livello di difficoltà
        audio = (Switch) findViewById(R.id.toggle_audio_switch);
        sfx = (Switch) findViewById(R.id.toggle_sfx_switch);
        velocityRadioGroup = (RadioGroup) findViewById(R.id.velocity_radio_group);
        normal = (RadioButton) findViewById(R.id.velocity_normal);
        fast = (RadioButton) findViewById(R.id.velocity_fast);
        veryFast = (RadioButton) findViewById(R.id.velocity_very_fast);

        //initialize the toggles/radiogroups interface based on the saved settings
        SettingsManager settingsManager = new SettingsManager(getApplicationContext());

        switch(settingsManager.getDifficultyPreference()){
            case SettingsManager.DIFFICULTY_EASY:easy.setChecked(true);break;
            case SettingsManager.DIFFICULTY_MEDIUM:medium.setChecked(true);break;
            case SettingsManager.DIFFICULTY_HARD:hard.setChecked(true);break;
           // case SettingsManager.DIFFICULTY_VERY_HARD:veryHard.setChecked(true);break;
            default: easy.setChecked(true);

        }

        //set tags containing the difficulty id in the radiobuttons
        for (int i = 0; i < difficultyRadioGroup.getChildCount(); i++) {
            View view = difficultyRadioGroup.getChildAt(i);
            RadioButton radioButton = (RadioButton) view;
            switch(i){
                case SettingsManager.DIFFICULTY_EASY: radioButton.setTag(SettingsManager.DIFFICULTY_EASY); break;
                case SettingsManager.DIFFICULTY_MEDIUM: radioButton.setTag(SettingsManager.DIFFICULTY_MEDIUM);break;
                case SettingsManager.DIFFICULTY_HARD: radioButton.setTag(SettingsManager.DIFFICULTY_HARD);break;
              //  case SettingsManager.DIFFICULTY_VERY_HARD: radioButton.setTag(3);break;
                default:
            }
        }

        //set listener
        difficultyRadioGroup.setOnCheckedChangeListener(new DifficultyRadioGroupListener(this));

        //initialize the toggles/radiogroups interface based on the saved settings
        switch(settingsManager.getVelocityPreference()){
            case SettingsManager.VELOCITY_NORMAL:normal.setChecked(true);break;
            case SettingsManager.VELOCITY_FAST:fast.setChecked(true);break;
            case SettingsManager.VELOCITY_VERY_FAST:veryFast.setChecked(true);break;
            default: normal.setChecked(true);

        }
        //set tags containing the velocity id in the radiobuttons
        for (int i = 0; i < velocityRadioGroup.getChildCount(); i++) {
            View view = velocityRadioGroup.getChildAt(i);
            RadioButton radioButton = (RadioButton) view;
            switch(i){
                case SettingsManager.VELOCITY_NORMAL: radioButton.setTag(SettingsManager.VELOCITY_NORMAL); break;
                case SettingsManager.VELOCITY_FAST: radioButton.setTag(SettingsManager.VELOCITY_FAST);break;
                case SettingsManager.VELOCITY_VERY_FAST:radioButton.setTag(SettingsManager.VELOCITY_VERY_FAST);break;
                default: radioButton.setTag(SettingsManager.VELOCITY_NORMAL);
            }
        }
        //set listener
        velocityRadioGroup.setOnCheckedChangeListener(new VelocityRadioGroupListener(this));

        //warn the user that changing the difficulty will only affect next match
        Toast.makeText(this,R.string.change_settings_warning,Toast.LENGTH_LONG).show();

        //get soundManager
        soundManager = ((BriscolaApplication) getApplication()).getSoundManager();

        //initialize switches based on music/sound status
        audio.setChecked(soundManager.getMusicStatus());
        sfx.setChecked(soundManager.getSoundStatus());

        //set listeners
        audio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                soundManager.toggleMusicStatus();
            }
        });

        sfx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                soundManager.toggleSoundStatus();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true; //todo, è qui che si scatena il problema del chekced ... ma se metti false la musica non parte
        }
        return super.onOptionsItemSelected(item);
    }




}
