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
 * Fragment representing a Wait Dialog Fragment for the online game. Asks the user to wait while other player is found by the server. Allows the user to stop waiting for another player.
 *
 * @author Francesco Pinto
 */
public class WaitDialogFragment extends DialogFragment
{
    /**
     * EXTRA_STO_WAITING ID
     */
    public static final String EXTRA_STOP_WAITING = "it.ma.polimi.briscola.waitdialog.action";
    private Dialog dialog;

    /**
     * Builds an instance of WaitDialogFragment
     *
     * @return the wait dialog fragment
     */
    public static WaitDialogFragment newInstance(){

        WaitDialogFragment fragment = new WaitDialogFragment();
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        //initialize the Dialog class to be returned, set parameters of the Dialog class
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_wait);

        //retrieve widget references and initialize them (if required)
        Button cancelButton = (Button) dialog.findViewById(R.id.stop_waiting);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tell the hosting activity that should stop waiting
                sendResult(Activity.RESULT_OK, true); //true = cancelled
                dialog.dismiss();
            }
        });

        return dialog;

    }

    //helper method, sends the result of the user interaction with the dialog in an Intent
    private void sendResult(int resultCode, boolean stopWaiting){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_STOP_WAITING,stopWaiting);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}





