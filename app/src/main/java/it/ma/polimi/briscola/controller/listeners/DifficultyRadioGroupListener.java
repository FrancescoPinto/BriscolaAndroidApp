package it.ma.polimi.briscola.controller.listeners;

import android.app.Activity;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import it.ma.polimi.briscola.persistency.SettingsManager;

/**
 * The DifficultyRadioGroupListener is a listener that saves the difficulty preference of the player when it interacts with the RadioGroup
 *
 * @author Francesco Pinto
 */
public class DifficultyRadioGroupListener implements RadioGroup.OnCheckedChangeListener {

      private final Activity activity;

    /**
     * Instantiates a new Difficulty radio group listener.
     *
     * @param activity the activity
     */
    public DifficultyRadioGroupListener(Activity activity) {
          this.activity = activity;
      }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // This will get the radiobutton that has changed in its check state
        RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);

        boolean isChecked = checkedRadioButton.isChecked();
        // If the radiobutton that has changed in check state is now checked, save the info
        if (isChecked) {
            int tag = (Integer) checkedRadioButton.getTag(); //tag has been set to contain the difficulty id
            new SettingsManager(activity).setDifficultyPreference(tag);
        }
    }
}

