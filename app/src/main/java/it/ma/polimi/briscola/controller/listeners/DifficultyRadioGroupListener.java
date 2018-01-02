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
public class DifficultyRadioGroupListener extends RadioGroupCheckedChangeListener {


    public DifficultyRadioGroupListener(Activity activity) {
        super(activity);
    }

    @Override
    public void saveSetting(int tag){
        new SettingsManager(activity).setDifficultyPreference(tag);
    };
}

