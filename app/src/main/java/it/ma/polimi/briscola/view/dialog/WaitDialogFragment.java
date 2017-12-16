package it.ma.polimi.briscola.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
 * Created by utente on 15/12/17.
 */



public class WaitDialogFragment extends DialogFragment
{
    public static final String EXTRA_STOP_WAITING = "it.ma.polimi.briscola.waitdialog.action";
    private Dialog dialog;

    public static WaitDialogFragment newInstance(){

        WaitDialogFragment fragment = new WaitDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_wait);

        Button cancelButton = (Button) dialog.findViewById(R.id.stop_waiting);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResult(Activity.RESULT_OK, true); //true = cancelled
                dialog.dismiss();
            }
        });

        return dialog;

    }

    private void sendResult(int resultCode, boolean stopWaiting){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_STOP_WAITING,stopWaiting);
        // intent.putExtra(EXTRA_LOAD_CONFIG,config);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}





