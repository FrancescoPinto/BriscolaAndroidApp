package it.ma.polimi.briscola;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import it.ma.polimi.briscola.audio.SoundManager;
import it.ma.polimi.briscola.controller.offline.DifficultyRadioGroupListener;
import it.ma.polimi.briscola.controller.offline.VelocityRadioGroupListener;
import it.ma.polimi.briscola.persistency.SettingsManager;

/**
 * Created by utente on 28/11/17.
 */

public class SettingsActivity extends AppCompatActivity{


    private Switch audio, sfx;
    private RadioGroup difficultyRadioGroup;
    private RadioButton easy, medium, hard, veryHard;

    private RadioGroup velocityRadioGroup;
    private RadioButton normal, fast, veryFast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the content view
        setContentView(R.layout.activity_main_settings);

        // init the elements
        /* layout elements */

        difficultyRadioGroup = (RadioGroup) findViewById(R.id.difficulty_radio_group);
        easy = (RadioButton) findViewById(R.id.difficulty_easy);
        medium = (RadioButton) findViewById(R.id.difficutly_intermediate);
        hard = (RadioButton) findViewById(R.id.difficulty_hard);
        veryHard = (RadioButton) findViewById(R.id.difficulty_very_hard);

        audio = (Switch) findViewById(R.id.toggle_audio_switch);
        sfx = (Switch) findViewById(R.id.toggle_sfx_switch);


        Log.d("TAG", "Sono dai settings e ho audio "+SoundManager.getInstance(getApplicationContext()).getMusicStatus() + " e sfx "+
                SoundManager.getInstance(getApplicationContext()).getSoundStatus());
       // Button saveButton = (Button) findViewById(R.id.id_save_button);
       // saveButton.setOnClickListener(new UpdateColorListener(this, this));

       // resultPreview = (TextView) findViewById(R.id.id_result_preview);

        // get the intent from the caller activity
        Intent intent = getIntent();
       // result = intent.getStringExtra("result");

        SettingsManager settingsManager = new SettingsManager(getApplicationContext());

        switch(settingsManager.getDifficultyPreference()){
            case SettingsManager.EASY:easy.setChecked(true);break;
            case SettingsManager.MEDIUM:medium.setChecked(true);break;
            case SettingsManager.HARD:hard.setChecked(true);break;
            case SettingsManager.VERY_HARD:veryHard.setChecked(true);break;
            default: easy.setChecked(true);

        }
        // set the listeners for each checkboxes
        for (int i = 0; i < difficultyRadioGroup.getChildCount(); i++) {
            View view = difficultyRadioGroup.getChildAt(i);
            RadioButton radioButton = (RadioButton) view;
            switch(i){
                case SettingsManager.EASY: radioButton.setTag(0); break;
                case SettingsManager.MEDIUM: radioButton.setTag(1);break;
                case SettingsManager.HARD: radioButton.setTag(2);break;
                case SettingsManager.VERY_HARD: radioButton.setTag(3);break;
                default:
            }
        }

        difficultyRadioGroup.setOnCheckedChangeListener(new DifficultyRadioGroupListener(this));

        velocityRadioGroup = (RadioGroup) findViewById(R.id.velocity_radio_group);
        normal = (RadioButton) findViewById(R.id.velocity_normal);
        fast = (RadioButton) findViewById(R.id.velocity_fast);
        veryFast = (RadioButton) findViewById(R.id.velocity_very_fast);


        switch(settingsManager.getVelocityPreference()){
            case SettingsManager.NORMAL:normal.setChecked(true);break;
            case SettingsManager.FAST:fast.setChecked(true);break;
            case SettingsManager.VERYFAST:veryFast.setChecked(true);break;
            default: normal.setChecked(true);

        }
        // set the listeners for each checkboxes
        for (int i = 0; i < velocityRadioGroup.getChildCount(); i++) {
            View view = velocityRadioGroup.getChildAt(i);
            RadioButton radioButton = (RadioButton) view;
            switch(i){
                case SettingsManager.NORMAL: radioButton.setTag(0); break;
                case SettingsManager.FAST: radioButton.setTag(1);break;
                case SettingsManager.VERYFAST:radioButton.setTag(2);break;
                default: radioButton.setTag(0);
            }
        }

        Toast.makeText(this,R.string.change_settings_warning,Toast.LENGTH_LONG).show();

        velocityRadioGroup.setOnCheckedChangeListener(new VelocityRadioGroupListener(this));

        audio.setChecked(SoundManager.getInstance(getApplicationContext()).getMusicStatus());
        sfx.setChecked(SoundManager.getInstance(getApplicationContext()).getSoundStatus());

        audio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SoundManager.getInstance(getApplicationContext()).toggleMusicStatus();
                Log.d("TAG", "Sono dai settings e ho audio "+SoundManager.getInstance(getApplicationContext()).getMusicStatus() + " e sfx "+
                        SoundManager.getInstance(getApplicationContext()).getSoundStatus());
            }
        });

        sfx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               SoundManager.getInstance(getApplicationContext()).toggleSoundStatus();
                Log.d("TAG", "Sono dai settings e ho audio "+SoundManager.getInstance(getApplicationContext()).getMusicStatus() + " e sfx "+
                        SoundManager.getInstance(getApplicationContext()).getSoundStatus());
           }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        // update result
       // resultPreview.setText(result);
    }

    /*@Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.back_button_settings))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }*/

   /* public void onCallIntent(String color) {
        // call an intent to pass the result to the activity caller
        Intent intent = new Intent(MainSettingsActivity.this, MainCalculatorActivity.class);
        intent.putExtra("theme_color", color);
        setResult(RESULT_OK, intent);
        // saveMatchRecord the current
        finish();
    }*/



}
