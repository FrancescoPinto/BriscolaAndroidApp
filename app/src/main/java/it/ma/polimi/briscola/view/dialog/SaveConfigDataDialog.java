package it.ma.polimi.briscola.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import it.ma.polimi.briscola.MatchMenuActivity;
import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.model.briscola.statistics.Briscola2PMatchRecord;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.persistency.SQLiteRepositoryImpl;

/**
 * Created by utente on 13/12/17.
 */

public class SaveConfigDataDialog {

    private final MatchMenuActivity activity;
    private Briscola2PMatchConfig config;
    private EditText configNameEditText;
    private int motivation;
    //motivations of restart
    public static final int EXIT_BUTTON = 0, START_NEW = 1;

    public SaveConfigDataDialog(MatchMenuActivity activity, Briscola2PMatchConfig config, int motivation){
        this.activity = activity;
        this.config = config;
        this.motivation = motivation;

    }

    public void showDialog(){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_save_config_data);


        Button saveButton = (Button) dialog.findViewById(R.id.save_button);
        configNameEditText = (EditText) dialog.findViewById(R.id.config_name_edit_text);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(configNameEditText.getText().toString().equals("")) {
                    //userName.setHint(R.string.mandatory_type_your_name);
                    //userName.setHintTextColor(Color.RED);
                    configNameEditText.setError(activity.getString(R.string.mandatory_type_config_name));
                }else{
                    SQLiteRepositoryImpl repo = new SQLiteRepositoryImpl(activity);
                    repo.saveMatchConfig(config, configNameEditText.getText().toString());
                    activity.exitMatch(false);
                    dialog.dismiss();
                }
            }
        });

        Button dontSaveButton = (Button) dialog.findViewById(R.id.do_not_save_button);

        dontSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //se esci dal match mediante pulsante exit
                //se avvii un nuovo match (sia online che offline) mentre ce n'è uno in corso
                    switch(motivation) {
                        case EXIT_BUTTON:
                            activity.exitMatch(false);
                            break;
                        //sposta l'exit button nel menù
                    }
                    dialog.dismiss();
                }
            });


        /*dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                Utils.showShortToast("Back button pressed?");
            }
        });*/
        dialog.show();

    }

}
