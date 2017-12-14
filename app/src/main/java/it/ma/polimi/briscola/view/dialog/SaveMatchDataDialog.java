package it.ma.polimi.briscola.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.model.briscola.statistics.Briscola2PMatchRecord;
import it.ma.polimi.briscola.persistency.SQLiteRepositoryImpl;

/**
 * Created by utente on 11/12/17.
 */

public class SaveMatchDataDialog {

    private final Activity activity;
    private EditText userName;
    private int player0MatchScore;
    private boolean isOnline;


    public SaveMatchDataDialog(Activity activity, int player0MatchScore, boolean isOnline){
        this.activity = activity;
        this.player0MatchScore = player0MatchScore;
        this.isOnline = isOnline;
    }

    public void showDialog(){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialog_save_match_data);

            userName= (EditText) dialog.findViewById(R.id.your_name);


            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(userName.getText().toString().equals("")){
                        //userName.setHint(R.string.mandatory_type_your_name);
                        //userName.setHintTextColor(Color.RED);
                        userName.setError(activity.getString(R.string.mandatory_type_your_name));
                    }else {
                        SQLiteRepositoryImpl repo = new SQLiteRepositoryImpl(activity); //todo, sposta questo o nel controller o nel model (non dovresti fare operazioni logiche così complicate in una view!)
                        repo.saveMatchRecord(new Briscola2PMatchRecord(userName.getText().toString(),
                                isOnline?Briscola2PMatchRecord.remotePlayerDefault:Briscola2PMatchRecord.computerPlayerName,
                                player0MatchScore,
                                Briscola2PMatchRecord.totPoints - player0MatchScore));
                        Log.d("TAG", "Just saved the " + repo.findAllMatchRecords().size() + "th record");
                        dialog.dismiss();
                    }
                }
            });



        /*dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                Utils.showShortToast("Back button pressed?");
            }
        });*/
            dialog.show();

    }

}
