package it.ma.polimi.briscola.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.model.briscola.statistics.Briscola2PMatchRecord;
import it.ma.polimi.briscola.model.briscola.twoplayers.Briscola2PMatchConfig;
import it.ma.polimi.briscola.view.fragments.Briscola2PMatchFragment;

/**
 * Created by utente on 11/12/17.
 */

public class WinnerMatchDialog {


    private Dialog dialog;


    public Dialog buildDialog(Activity activity,int winnerPlayer, int score, Briscola2PMatchFragment fragment){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_winner_match);

        TextView congrats_or_ops= (TextView) dialog.findViewById(R.id.congrats_or_ops);


        TextView messageTV = (TextView) dialog.findViewById(R.id.who_won_points);

        if(winnerPlayer == Briscola2PMatchConfig.PLAYER0) {
            congrats_or_ops.setText(R.string.congrats_you_won);
            messageTV.setText(activity.getString(R.string.who_won_with,"You",score));
            congrats_or_ops.setBackgroundColor(Color.GREEN);
            congrats_or_ops.setTextColor(Color.WHITE);

        }
        else if(winnerPlayer == Briscola2PMatchConfig.DRAW) {
            congrats_or_ops.setText(R.string.draw);
            messageTV.setText(activity.getString(R.string.none_won));
            congrats_or_ops.setBackgroundColor(Color.BLUE);
            congrats_or_ops.setTextColor(Color.WHITE);


        }
        else if(winnerPlayer == Briscola2PMatchConfig.PLAYER1) {
            congrats_or_ops.setText(R.string.you_lost);
            messageTV.setText(activity.getString(R.string.who_won_with,fragment.getString(R.string.other_player), score));
            congrats_or_ops.setBackgroundColor(Color.RED);
            congrats_or_ops.setTextColor(Color.WHITE);

        }


        Button dialogButton = (Button) dialog.findViewById(R.id.ok_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dialog.dismiss();
                }
            }
        );

        dialog.setOnDismissListener(new DismissWinnerMatchDialogListener(winnerPlayer,score,fragment));

        return dialog;
    }
    public void show(){
        dialog.show();
    }

}

class DismissWinnerMatchDialogListener implements DialogInterface.OnDismissListener{
    private int winnerPlayer, score;
    Briscola2PMatchFragment fragment;

    DismissWinnerMatchDialogListener(int winnerPlayer, int score, Briscola2PMatchFragment fragment){
        this.winnerPlayer = winnerPlayer;
        this.score = score;
        this.fragment = fragment;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // TODO Auto-generated method stub
        fragment.saveMatchData((winnerPlayer== Briscola2PMatchConfig.PLAYER0)?score: Briscola2PMatchRecord.totPoints-score);
    }
}
