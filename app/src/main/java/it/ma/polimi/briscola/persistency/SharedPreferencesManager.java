package it.ma.polimi.briscola.persistency;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import it.ma.polimi.briscola.R;

/**
 * Created by utente on 27/11/17.
 */

public class SharedPreferencesManager {

    public static void saveHidePoints(Activity activity, boolean shouldHide){
        saveBooleanData(activity, R.string.show_points_preference, shouldHide);
    }

    public static boolean getHidePoints(Activity activity){
        return getBooleanData(activity,R.string.show_points_preference,R.string.show_points_default);
    }



    private static void saveBooleanData(Activity activity, int keyStringID, boolean value){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(activity.getString(keyStringID), value);
        editor.commit();
    }

    private static boolean getBooleanData(Activity activity, int keyStringID, int defaultId){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        boolean defaultValue = activity.getResources().getBoolean(defaultId);
        return sharedPref.getBoolean(activity.getString(keyStringID), defaultValue);
    }


}
