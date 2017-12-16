package it.ma.polimi.briscola.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.persistency.SQLiteRepositoryImpl;
import it.ma.polimi.briscola.view.MatchMenuActivityActions;
import it.ma.polimi.briscola.view.activities.MatchMenuActivity;

/**
 * Created by utente on 14/12/17.
 */
public class SaveConfigDataDialogFragment extends DialogFragment{

    private Briscola2PMatchConfig config;
    private EditText configNameEditText;
    private int motivation;
    private Dialog dialog;

    private static final String ARG_CONFIG = "Config";
    private static final String ARG_MOTIVATION = "Motivation";

    public static final String EXTRA_ACTION = "it.ma.polimi.briscola.saveconfigexit.action",
                            EXTRA_MOTIVATION ="it.ma.polimi.briscola.saveconfigexit.motivation",
                             EXTRA_LOAD_CONFIG = "it.ma.polimi.briscola.saveconfigexit.config";


    public static SaveConfigDataDialogFragment newInstance(Briscola2PMatchConfig config, int motivation){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONFIG, config);
        args.putInt(ARG_MOTIVATION, motivation);
        SaveConfigDataDialogFragment fragment = new SaveConfigDataDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        config = (Briscola2PMatchConfig) getArguments().getSerializable(ARG_CONFIG);
        motivation = getArguments().getInt(ARG_MOTIVATION);
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_save_config_data);


        Button saveButton = (Button) dialog.findViewById(R.id.save_button);
        configNameEditText = (EditText) dialog.findViewById(R.id.config_name_edit_text);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (configNameEditText.getText().toString().equals("")) {
                    //userName.setHint(R.string.mandatory_type_your_name);
                    //userName.setHintTextColor(Color.RED);
                    configNameEditText.setError(getString(R.string.mandatory_type_config_name));
                } else {
                    SQLiteRepositoryImpl repo = new SQLiteRepositoryImpl(getActivity());
                    repo.saveMatchConfig(config, configNameEditText.getText().toString());
                    sendResult(Activity.RESULT_OK, MatchMenuActivityActions.STOP_OFFLINE, motivation);
                    //activity.exitMatch(false);
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
                //  switch(motivation) {
                //  case EXIT_BUTTON:
                // activity.exitMatch(false); <- sostituisci
                sendResult(Activity.RESULT_OK, MatchMenuActivityActions.STOP_OFFLINE, motivation);
                //   break;
                //sposta l'exit button nel menù
                //  }
                dialog.dismiss();
            }
        });

        return dialog;
    }

        private void sendResult(int resultCode, MatchMenuActivityActions actionCode, int motivation) {
            if(getTargetFragment()==null)
                return;

            Intent intent=new Intent();
            intent.putExtra(EXTRA_ACTION,actionCode);
            intent.putExtra(EXTRA_MOTIVATION,motivation);
            getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
        }
}



