package it.ma.polimi.briscola.controller.offline;

import android.app.Activity;
import android.widget.CompoundButton;

/**
 * Created by utente on 10/12/17.
 */

public class DifficultyRadioButtonListener implements CompoundButton.OnCheckedChangeListener {

        private final int difficulty;
        private final Activity activity;
        public DifficultyRadioButtonListener(Activity activity, int difficulty) {
            this.difficulty = difficulty;
            this.activity = activity;
        }
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SettingsManager.setDifficultyPreference(activity,difficulty);
        }
}
