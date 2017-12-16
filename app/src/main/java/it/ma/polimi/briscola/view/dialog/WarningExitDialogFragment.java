package it.ma.polimi.briscola.view.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.persistency.SQLiteRepositoryImpl;
import it.ma.polimi.briscola.view.MatchMenuActivityActions;
import it.ma.polimi.briscola.view.activities.Briscola2PMatchActivity;
import it.ma.polimi.briscola.view.activities.MatchMenuActivity;
import android.content.DialogInterface;

/**
 * Created by utente on 14/12/17.
 */

public class WarningExitDialogFragment extends DialogFragment {

    private static final String ARG_IS_ONLINE = "is_online", ARG_MOTIVATION = "motivation",
                                ARG_CONFIG ="loadConfig";


    public static final String EXTRA_ACTION = "it.ma.polimi.briscola.warningexit.action",
                                EXTRA_MOTIVATION ="it.ma.polimi.briscola.warningexit.motivation",
                                 EXTRA_LOAD_CONFIG = "it.ma.polimi.briscola.warningexit.config";

    private boolean isOnline;
    private int motivation;
    private Dialog dialog;

    public static WarningExitDialogFragment newInstance(boolean isOnline, int motivation){
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_ONLINE, isOnline);
        args.putInt(ARG_MOTIVATION,motivation);
       // args.putSerializable(ARG_CONFIG,config);

        WarningExitDialogFragment fragment = new WarningExitDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        isOnline = getArguments().getBoolean(ARG_IS_ONLINE);
        motivation = getArguments().getInt(ARG_MOTIVATION);
        //config = (Briscola2PMatchConfig) getArguments().getSerializable(ARG_CONFIG);
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_alert_interrupt_match_no_save);

        Button yesButton = (Button) dialog.findViewById(R.id.yes_continue);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline){
                    sendResult(Activity.RESULT_OK, MatchMenuActivityActions.STOP_ONLINE, motivation); //chiudi la partita online
                }else{
                    sendResult(Activity.RESULT_OK,MatchMenuActivityActions.WARN_STOP_OFFLINE, motivation); //mostra la richiesta di salvataggio
                }
                dialog.dismiss();
            }
        });

        Button noButton = (Button) dialog.findViewById(R.id.no_continue);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(Activity.RESULT_CANCELED, null, Briscola2PMatchActivity.NO_MOTIVATION); //dici all'activity di non far niente e continuare a giocare
                dialog.dismiss();
            }
        });

        return dialog;

    }

    private void sendResult(int resultCode, MatchMenuActivityActions actionCode, int motivation){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_ACTION,actionCode);
        intent.putExtra(EXTRA_MOTIVATION, motivation);
       // intent.putExtra(EXTRA_LOAD_CONFIG,config);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

}







