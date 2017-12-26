package it.ma.polimi.briscola.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.model.briscola.statistics.Briscola2PMatchRecord;
import it.ma.polimi.briscola.persistency.SQLiteRepositoryImpl;

/**
 * Fragment representing a Save Finished Match Record Dialog. Asks the user whether he/she wants to save a record containing statistics about the match (winner/loser, points).
 *
 * @author Francesco Pinto
 */
public class SaveFinishedMatchRecordDialogFragment extends DialogFragment {

    private EditText userName;
    private int player0MatchScore;
    private boolean isOnline;

    private static final String ARG_PLAYER0_SCORE = "scoreWinner",
            ARG_IS_ONLINE = "isOnline";

    /**
     * Builds a new instance of SaveFinishedMatchRecordDialogFragment given the parameters
     *
     * @param player0MatchScore the player 0 match score
     * @param isOnline          whether the match is online
     * @return the save finished match record dialog fragment
     */
    public static SaveFinishedMatchRecordDialogFragment newInstance(int player0MatchScore, boolean isOnline){
        Bundle args = new Bundle();
        //put parameters in the bundle
        args.putInt(ARG_PLAYER0_SCORE,player0MatchScore);
        args.putBoolean(ARG_IS_ONLINE,isOnline);
        SaveFinishedMatchRecordDialogFragment fragment = new SaveFinishedMatchRecordDialogFragment();
        //set parameters as arguments in the fragment
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //extract parameters from the arguments
        player0MatchScore = getArguments().getInt(ARG_PLAYER0_SCORE);
        isOnline = getArguments().getBoolean(ARG_IS_ONLINE);

        //initialize the Dialog class to be returned, set parameters of the Dialog class
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_save_match_data);

        //retrieve widget references and initialize them (if required)
        userName= (EditText) dialog.findViewById(R.id.your_name);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().equals("")){ //ensures input is not empty
                    userName.setError(getString(R.string.mandatory_type_your_name));//remind the user to fill the form
                }else {
                    SQLiteRepositoryImpl repo = new SQLiteRepositoryImpl(getActivity());
                    repo.saveMatchRecord(new Briscola2PMatchRecord(userName.getText().toString(),
                            isOnline?Briscola2PMatchRecord.remotePlayerDefault:Briscola2PMatchRecord.computerPlayerName, //choose player1 name (based on whether the match is online or not)
                            player0MatchScore,
                            Briscola2PMatchRecord.totPoints - player0MatchScore));
                    //tell the activity everything ok
                    sendResult(Activity.RESULT_OK);
                    dialog.dismiss();
                }
            }
        });


        return dialog;
    }

    //helper method, sends the result of the user interaction with the dialog in an Intent
    private void sendResult(int resultCode){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
