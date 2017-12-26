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

/**
 * Fragment representing a Save Configuration Dialog. Asks the user whether he/she wants to save the data.
 *
 * @author Francesco Pinto
 */
public class SaveConfigDataDialogFragment extends DialogFragment{

    private Briscola2PMatchConfig config;
    private EditText configNameEditText;
    private int motivation;
    private Dialog dialog;

    private static final String ARG_CONFIG = "Config";
    private static final String ARG_MOTIVATION = "Motivation";

    /**
     * EXTRA_ACTION ID
     */
    public static final String EXTRA_ACTION = "it.ma.polimi.briscola.saveconfigexit.action",
    /**
     * EXTRA_MOTIVATION ID.
     */
    EXTRA_MOTIVATION ="it.ma.polimi.briscola.saveconfigexit.motivation",
    /**
     * EXTRA_LOAD_CONFIG ID
     */
    EXTRA_LOAD_CONFIG = "it.ma.polimi.briscola.saveconfigexit.config";


    /**
     * Builds a new instance of SaveConfigDataDialogFragment given the initialization parameters.
     *
     * @param config     the configuration to be saved
     * @param motivation the motivation
     * @return the save config data dialog fragment
     */
    public static SaveConfigDataDialogFragment newInstance(Briscola2PMatchConfig config, int motivation){
        //put parameters in the bundle
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONFIG, config);
        args.putInt(ARG_MOTIVATION, motivation);
        SaveConfigDataDialogFragment fragment = new SaveConfigDataDialogFragment();
        //set parameters as arguments in the fragment
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //extract parameters from the arguments
        config = (Briscola2PMatchConfig) getArguments().getSerializable(ARG_CONFIG);
        motivation = getArguments().getInt(ARG_MOTIVATION);

        //initialize the Dialog class to be returned, set parameters of the Dialog class
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_save_config_data);

        //retrieve widget references and initialize them (if required)
        Button saveButton = (Button) dialog.findViewById(R.id.save_button);
        configNameEditText = (EditText) dialog.findViewById(R.id.config_name_edit_text);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (configNameEditText.getText().toString().equals("")) { //ensures input is not empty
                    configNameEditText.setError(getString(R.string.mandatory_type_config_name)); //remind the user to fill the form
                } else {
                    //save the match config
                    SQLiteRepositoryImpl repo = new SQLiteRepositoryImpl(getActivity());
                    repo.saveMatchConfig(config, configNameEditText.getText().toString());
                    //tell the hosting activity that should stop the offline match
                    sendResult(Activity.RESULT_OK, MatchMenuActivityActions.STOP_OFFLINE, motivation);
                    dialog.dismiss();
                }
            }
        });

        Button dontSaveButton = (Button) dialog.findViewById(R.id.do_not_save_button);

        dontSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tell the hosting activity that should stop the offline match
                sendResult(Activity.RESULT_OK, MatchMenuActivityActions.STOP_OFFLINE, motivation);
                dialog.dismiss();
            }
        });

        return dialog;
    }

        //helper method, sends the result of the user interaction with the dialog in an Intent
        private void sendResult(int resultCode, MatchMenuActivityActions actionCode, int motivation) {
            if(getTargetFragment()==null)
                return;

            Intent intent=new Intent();
            intent.putExtra(EXTRA_ACTION,actionCode);
            intent.putExtra(EXTRA_MOTIVATION,motivation);
            getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
        }
}



