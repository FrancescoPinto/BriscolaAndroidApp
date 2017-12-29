package it.ma.polimi.briscola.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.audio.GameEvent;
import it.ma.polimi.briscola.audio.SoundService;
import it.ma.polimi.briscola.model.briscola.statistics.Briscola2PMatchRecord;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.view.activities.MatchActivity;

/**
 * Fragment representing a WinnerMatchDialogFragment. Shows the outcome of a match to the user (who won, with how many points).
 *
 * @author Francesco Pinto
 */
public class WinnerMatchDialogFragment extends DialogFragment {

    private static final String ARG_WINNER = "winner", ARG_SCORE_WINNER = "scoreWinner",
                                ARG_IS_ONLINE = "isOnline";
    /**
     * EXTRA_PLAYER0_SCORE ID
     */
    public static final String EXTRA_PLAYER0_SCORE = "it.ma.polimi.briscola.winnermatchdialog.playerscore",
    /**
     * EXTRA_IS_ONLINE ID.
     */
    EXTRA_IS_ONLINE = "it.ma.polimi.briscola.winnermatchdialog.isonline";

    private Dialog dialog;


    /**
     * Builds a new WinnerMatchDialogFragment instance given the parameters
     *
     * @param winner      the winner index (can be either PLAYER0 or PLAYER1)
     * @param scoreWinner the score of the winner
     * @param isOnline    whether the match is online
     * @return the winner match dialog fragment
     */
    public static WinnerMatchDialogFragment newInstance(int winner, int scoreWinner, boolean isOnline){
        //put parameters in the bundle
        Bundle args = new Bundle();
        args.putInt(ARG_SCORE_WINNER, scoreWinner);
        args.putInt(ARG_WINNER,winner);
        args.putBoolean(ARG_IS_ONLINE,isOnline);
        WinnerMatchDialogFragment fragment = new WinnerMatchDialogFragment();
        //set parameters as arguments in the fragment
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //extract parameters from the arguments
        final int winnerPlayer = getArguments().getInt(ARG_WINNER);
        final int score = getArguments().getInt(ARG_SCORE_WINNER);
        final boolean isOnline = getArguments().getBoolean(ARG_IS_ONLINE);

        //initialize the Dialog class to be returned, set parameters of the Dialog class
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_winner_match);

        //retrieve widget references and initialize them (if required)
        TextView congrats_or_ops= (TextView) dialog.findViewById(R.id.congrats_or_ops);


        TextView messageTV = (TextView) dialog.findViewById(R.id.who_won_points);
        SoundService soundManager = ((MatchActivity) getActivity()).getSoundManager();

        //based on the winner index, show different messages and play different end-match sounds
        if(winnerPlayer == Briscola2PMatchConfig.PLAYER0) {
            congrats_or_ops.setText(R.string.congrats_you_won);
            messageTV.setText(getString(R.string.who_won_with,getString(R.string.you),score));
            congrats_or_ops.setBackgroundColor(Color.GREEN);
            congrats_or_ops.setTextColor(Color.WHITE);
            soundManager.playSoundForGameEvent(GameEvent.WinMatch);

        }
        else if(winnerPlayer == Briscola2PMatchConfig.DRAW) {
            congrats_or_ops.setText(R.string.draw);
            messageTV.setText(getString(R.string.none_won));
            congrats_or_ops.setBackgroundColor(Color.BLUE);
            congrats_or_ops.setTextColor(Color.WHITE);
            soundManager.playSoundForGameEvent(GameEvent.LoseMatch); //a draw is not a win ... if you don't win you lose, at least in some sense

        }
        else if(winnerPlayer == Briscola2PMatchConfig.PLAYER1) {
            congrats_or_ops.setText(R.string.you_lost);
            messageTV.setText(getString(R.string.who_won_with,getString(R.string.other_player), score));
            congrats_or_ops.setBackgroundColor(Color.RED);
            congrats_or_ops.setTextColor(Color.WHITE);
            soundManager.playSoundForGameEvent(GameEvent.LoseMatch);

        }


        Button dialogButton = (Button) dialog.findViewById(R.id.ok_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //simpy close the dialog and send match data to the activity
                    sendResult(Activity.RESULT_OK,(winnerPlayer== Briscola2PMatchConfig.PLAYER0)?score: Briscola2PMatchRecord.totPoints-score, isOnline);
                    dialog.dismiss();}
            }
        );

        return dialog;
    }

    //helper method, sends the result of the user interaction with the dialog in an Intent
    private void sendResult(int resultCode,int player0Score, boolean isOnline){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_PLAYER0_SCORE,player0Score);
        intent.putExtra(EXTRA_IS_ONLINE, isOnline);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

}

