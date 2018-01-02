package it.ma.polimi.briscola.controller.listeners;

import android.app.Activity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import it.ma.polimi.briscola.persistency.SettingsManager;

/**
 * Created by utente on 01/01/18.
 */

public abstract class RadioGroupCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

    final Activity activity;
    public RadioGroupCheckedChangeListener(Activity activity) {
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
            saveSetting(tag); //save
        }
    }

    public abstract void saveSetting(int tag);
}
