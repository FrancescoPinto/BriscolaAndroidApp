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

import it.ma.polimi.briscola.audio.SoundManager;
import it.ma.polimi.briscola.controller.offline.DifficultyRadioGroupListener;
import it.ma.polimi.briscola.persistency.SettingsManager;

/**
 * Created by utente on 28/11/17.
 */

public class SettingsActivity extends AppCompatActivity{


    private Switch audio, sfx;
    private RadioGroup difficultyRadioGroup;
    private RadioButton easy, medium, hard, veryHard;


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


        switch(new SettingsManager(getApplicationContext()).getDifficultyPreference()){
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
            int difficulty;
            switch(i){
                case SettingsManager.EASY:difficulty = 0; radioButton.setTag(0); break;
                case SettingsManager.MEDIUM:difficulty = 1; radioButton.setTag(1);break;
                case SettingsManager.HARD:difficulty = 2; radioButton.setTag(2);break;
                case SettingsManager.VERY_HARD:difficulty = 3; radioButton.setTag(3);break;
                default: difficulty= 0;
            }
        }

        difficultyRadioGroup.setOnCheckedChangeListener(new DifficultyRadioGroupListener(this));

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
        // save the current
        finish();
    }*/



}
