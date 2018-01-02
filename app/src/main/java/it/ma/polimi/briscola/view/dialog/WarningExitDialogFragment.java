package it.ma.polimi.briscola.view.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.view.MatchMenuActivityActions;
import it.ma.polimi.briscola.view.activities.Briscola2PMatchActivity;

/**
 * Fragment representing a WarningExitDialogFragment. Warns the user that the action he desires to perform will interrupt the current match
 *
 * @author Francesco Pinto
 */
public class WarningExitDialogFragment extends DialogFragment {

    private static final String ARG_IS_ONLINE = "is_online", ARG_MOTIVATION = "motivation";

    /**
     * EXTRA_ACTION ID
     */
    public static final String EXTRA_ACTION = "it.ma.polimi.briscola.warningexit.action",
    /**
     * EXTRA_MOTIVATION ID
     */
    EXTRA_MOTIVATION ="it.ma.polimi.briscola.warningexit.motivation",
    /**
     * EXTRA_LOAD_CONFIG ID
     */
    EXTRA_LOAD_CONFIG = "it.ma.polimi.briscola.warningexit.config";

    private boolean isOnline;
    private int motivation;
    private Dialog dialog;

    /**
     * Builds a new WarningExitDialogFragment instance given the parameters.
     *
     * @param isOnline   whether it is an online match
     * @param motivation the motivation
     * @return the warning exit dialog fragment
     */
    public static WarningExitDialogFragment newInstance(boolean isOnline, int motivation){
        //put parameters in the bundle
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_ONLINE, isOnline);
        args.putInt(ARG_MOTIVATION,motivation);
        WarningExitDialogFragment fragment = new WarningExitDialogFragment();
        //set parameters as arguments in the fragment
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //extract parameters from the arguments
        isOnline = getArguments().getBoolean(ARG_IS_ONLINE);
        motivation = getArguments().getInt(ARG_MOTIVATION);

        //initialize the Dialog class to be returned, set parameters of the Dialog class
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_alert_interrupt_match_no_save);


        //retrieve widget references and initialize them (if required)
        Button yesButton = (Button) dialog.findViewById(R.id.yes_continue);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline){ //tell the activity to start the stopping procedure for an online match
                    sendResult(Activity.RESULT_OK, MatchMenuActivityActions.STOP_ONLINE, motivation); //chiudi la partita online
                }else{ //tell the activity to handle a warning
                    sendResult(Activity.RESULT_OK,MatchMenuActivityActions.WARN_STOP_OFFLINE, motivation); //mostra la richiesta di salvataggio
                }
                dialog.dismiss();
            }
        });

        Button noButton = (Button) dialog.findViewById(R.id.no_continue);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //no action needed
                sendResult(Activity.RESULT_CANCELED, null, Briscola2PMatchActivity.NO_MOTIVATION); //dici all'activity di non far niente e continuare a giocare
                dialog.dismiss();
            }
        });

        return dialog;

    }

    //helper method, sends the result of the user interaction with the dialog in an Intent
    private void sendResult(int resultCode, MatchMenuActivityActions actionCode, int motivation){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_ACTION,actionCode);
        intent.putExtra(EXTRA_MOTIVATION, motivation);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

}







