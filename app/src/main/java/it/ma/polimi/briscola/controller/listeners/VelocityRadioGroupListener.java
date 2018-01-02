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
public class VelocityRadioGroupListener extends RadioGroupCheckedChangeListener {

     public VelocityRadioGroupListener(Activity activity) {
          super(activity);
      }

    @Override
    public void saveSetting(int tag){
        new SettingsManager(activity).setVelocityPreference(tag); //save
    };

}

