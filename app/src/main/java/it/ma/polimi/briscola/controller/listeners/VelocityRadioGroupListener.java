package it.ma.polimi.briscola.controller.listeners;

import android.app.Activity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import it.ma.polimi.briscola.persistency.SettingsManager;

/**
 * The VelocityRadioGroupListener is a listener that saves the velocity preference of the player when it interacts with the RadioGroup
 *
 * @author Francesco Pinto
 */
public class VelocityRadioGroupListener implements RadioGroup.OnCheckedChangeListener {


      private final Activity activity;
      public VelocityRadioGroupListener(Activity activity) {
          this.activity = activity;
      }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // This will get the radiobutton that has changed in its check state
        RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
        boolean isChecked = checkedRadioButton.isChecked();
        // If the radiobutton that has changed in check state is now checked...
        if (isChecked) {
            int tag = (Integer) checkedRadioButton.getTag(); //retrieve the velocity id
            new SettingsManager(activity).setVelocityPreference(tag); //save
        }
    }
}

