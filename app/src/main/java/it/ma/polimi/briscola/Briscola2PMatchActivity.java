package it.ma.polimi.briscola;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import it.ma.polimi.briscola.view.Briscola2PMatchView;

public class Briscola2PMatchActivity extends AppCompatActivity {

    private Briscola2PMatchView matchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("OkOncreate","OkOnCreate");
        super.onCreate(savedInstanceState);
       /* ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();*/

       //todo  per il flip della carta: https://developer.android.com/training/animation/cardflip.html
   /*     ImageView mDeck = new ImageView(this);
        mDeck.setImageResource(R.mipmap.defaultCardBackground);

        mDeck.setId(id);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(0, WRAP_CONTENT);
        layoutParams.rightToRight = PARENT_ID;
        layoutParams.leftToLeft = guideline_60.getId(); //Vertical GuideLine of 60%
        layoutParams.rightMargin = 8;
        textView.setLayoutParams(layoutParams);

        ConstraintSet set = new ConstraintSet();
        set.connect(textView.getId(), ConstraintSet.TOP,
                previousTextView.getId(), ConstraintSet.BOTTOM, 60);
        ConstraintLayout mConstraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        mConstraintLayout.addView(mDeck, position);

        per connettere oggetti creati in modo simile
                puoi fare
        set.clone(markerLayout);
        //set.addToVerticalChain(textView.getId(),previousTextViewId,PARENT_ID);
        set.connect(textView.getId(), ConstraintSet.TOP, markerLayout.getId(), ConstraintSet.TOP, 60);
        set.applyTo(markerLayout);
        vedi
        https://stackoverflow.com/questions/42589868/android-constraintlayout-how-to-add-a-dynamic-view-one-below-another
        // init surface view
       /* matchView = new Briscola2PMatchView(this);*/
       setContentView(R.layout.activity_briscola2_pmatch);

    }

    @Override
    public void onResume() {
        super.onResume();
        // set content view
      //  setContentView(matchView);
    }

    @Override
    public void onPause() {
        super.onPause();
      /*  matchView.getThread().setGo(false);
        try {
            matchView.getThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
