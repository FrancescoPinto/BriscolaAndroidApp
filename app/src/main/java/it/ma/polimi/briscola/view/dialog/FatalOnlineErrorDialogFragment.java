package it.ma.polimi.briscola.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.view.MatchMenuActivityActions;

/**
 * Fragment representing a Fatal Online ErrorDialog. Tells the user that a fatal error occured, its motivation, and warns him/her that hence the match will be closed.
 *
 * @author Francesco Pinto
 */
public class FatalOnlineErrorDialogFragment extends DialogFragment {

    private int motivation;
    private Dialog dialog;

    private static final String ARG_ERROR = "error";
    private static final String ARG_MESSAGE = "message";
    private static final String ARG_MOTIVATION = "motivation";


    /**
     * EXTRA_ACTION ID
     */
    public static final String EXTRA_ACTION = "it.ma.polimi.briscola.warningexit.action",
    /**
     * EXTRA_MOTIVATION ID.
     */
    EXTRA_MOTIVATION ="it.ma.polimi.briscola.warningexit.motivation";


    /**
     * Build a new instance of fatal online error dialog fragment given the parameters.
     *
     * @param error      the error that occurred
     * @param message    the message explaining the motivation
     * @param motivation the motivation the dialog has been invoked (i.e. an error occurred), REMARK: it is NOT the motivation of the error
     * @return the fatal online error dialog fragment
     */
    public static FatalOnlineErrorDialogFragment newInstance(String error, String message, int motivation){
        //put parameters in the bundle
        Bundle args = new Bundle();
        args.putString(ARG_ERROR, error);
        args.putString(ARG_MESSAGE, message);
        args.putInt(ARG_MOTIVATION,motivation);
        FatalOnlineErrorDialogFragment fragment = new FatalOnlineErrorDialogFragment();
        //set parameters as arguments in the fragment
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //extract parameters from the arguments
        String error = getArguments().getString(ARG_ERROR);
        String message = getArguments().getString(ARG_MESSAGE);
        motivation = getArguments().getInt(ARG_MOTIVATION);

        //initialize the Dialog class to be returned, set parameters of the Dialog class
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_online_error_message);

        //retrieve widget references and initialize them (if required)
       Button okButton = (Button) dialog.findViewById(R.id.ok_error_button);
        TextView showedMessage = (TextView) dialog.findViewById(R.id.error_message_text);
        //show the user the message
        if(message.equals("timeout"))
            showedMessage.setText(getString(R.string.game_terminated_timeout));
        else
            showedMessage.setText(getString(R.string.game_terminated_generic,message));
        //set the onClickListener
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
          public void onClick(View v) {
                //tell the activity that should stop the match
               sendResult(Activity.RESULT_OK, MatchMenuActivityActions.STOP_ONLINE, motivation);
               dialog.dismiss();
            }
        });

        return dialog;
    }

    //helper method, sends the result of the user interaction with the dialog in an Intent
    private void sendResult(int resultCode, MatchMenuActivityActions actionCode, int motivation) {
        if(getTargetFragment()==null)
            return;
        //use an intent to return the action to be performed (with the motivation)
        Intent intent=new Intent();
        intent.putExtra(EXTRA_ACTION,actionCode);
        intent.putExtra(EXTRA_MOTIVATION,motivation);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
