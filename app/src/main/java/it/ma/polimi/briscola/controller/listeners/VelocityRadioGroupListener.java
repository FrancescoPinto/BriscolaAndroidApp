package it.ma.polimi.briscola.controller.listeners;

import android.app.Activity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import it.ma.polimi.briscola.persistency.SettingsManager;

/**
 * Created by utente on 10/12/17.
 */

public class VelocityRadioGroupListener implements RadioGroup.OnCheckedChangeListener {

    //implements CompoundButton.OnCheckedChangeListener {

      private final Activity activity;
      public VelocityRadioGroupListener(Activity activity) {
          this.activity = activity;
      }
     /* @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
          new SettingsManager(activity).setDifficultyPreference(difficulty);
      }
*/
    // This overrides the radiogroup onCheckListene
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // This will get the radiobutton that has changed in its check state
        RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
        // This puts the value (true/false) into the variable
        boolean isChecked = checkedRadioButton.isChecked();
        // If the radiobutton that has changed in check state is now checked...
        if (isChecked) {
            int tag = (Integer) checkedRadioButton.getTag();
            // Changes the textview's text to "Checked: example radiobutton text"
            new SettingsManager(activity).setVelocityPreference(tag);
        }
    }
}

