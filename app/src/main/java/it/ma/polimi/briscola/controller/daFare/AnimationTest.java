package it.ma.polimi.briscola.controller.daFare;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import it.ma.polimi.briscola.R;
import it.ma.polimi.briscola.controller.daFare.CardAnimationListener;
import it.ma.polimi.briscola.controller.daFare.CardAnimationType;

public class AnimationTest extends AppCompatActivity {

    private ImageView card;
    private ConstraintLayout layout;
    private ImageView player0Card0Slot;
    private boolean changed = false;
    private Map<Integer,CardAnimationType> cardAnimationDestination = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_test);

        player0Card0Slot = (ImageView) findViewById(R.id.player0Card0Slot);

        String mDrawableName = "fc";
        int resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
        card = new ImageView(this);
        card.setImageResource(resID);
        card.setId(View.generateViewId());



        layout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        // Get existing constraints into a ConstraintSet


        layout.addView(card);
        // Now constrain the ImageView so it is centered on the screen.
        // There is also a "center" method that can be used here.
        //Todo: questo è per centrare l'immagine nel mezzo dello schermo
        /*constraints.constrainWidth(card.getId(), ConstraintSet.WRAP_CONTENT);
        constraints.constrainHeight(card.getId(), ConstraintSet.WRAP_CONTENT);
        constraints.center(card.getId(), ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
                0, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0, 0.5f);
        constraints.center(card.getId(), ConstraintSet.PARENT_ID, ConstraintSet.TOP,
                0, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0, 0.5f);
        constraints.applyTo(layout);*/


        /*posiziona sullo card0Player0 //todo: il seguente centra sullo slot0 di Player0, io ne farei una funzione
        android:layout_marginLeft="8dp"
        app:layout_constraintLeft_toLeftOf="@+id/player0Card0Slot"
        android:layout_marginRight="0dp"
        app:layout_constraintRight_toRightOf="@+id/player0Card0Slot"
        */

        centerViewInSlot(R.id.player0Card0Slot, card.getId()).applyTo(layout);
        cardAnimationDestination.put(card.getId(), CardAnimationType.ToSurfaceSlot0); //todo, questo slot va settato dinamicamente durante il match

        card.setOnClickListener(new CardAnimationListener(layout,this));
        /*card.setOnClickListener(new View.OnClickListener(){

                private ConstraintSet constraints;
                @Override
                public void onClick(View view) {
                    TransitionManager.beginDelayedTransition(layout);
                    if(changed)
                        constraints = centerViewInSlot(R.id.player0Card0Slot, card.getId());
                    else
                        constraints = centerViewInSlot(R.id.surfaceSlot0, card.getId());

                    constraints.applyTo(layout);
                    changed = !changed;
                }
            });
*/




    }

    private ConstraintSet centerViewInSlot(int idSlot, int idView){
        ConstraintSet constraints = new ConstraintSet();
        constraints.clone(layout);
        constraints.constrainWidth(idView, ConstraintSet.WRAP_CONTENT);
        constraints.constrainHeight(idView, ConstraintSet.WRAP_CONTENT);
        constraints.connect(idView, ConstraintSet.LEFT,idSlot,ConstraintSet.LEFT);
        constraints.connect(idView, ConstraintSet.RIGHT,idSlot,ConstraintSet.RIGHT);
        constraints.connect(idView, ConstraintSet.BOTTOM,idSlot,ConstraintSet.BOTTOM);
        return constraints;
    }

    public CardAnimationType getCardAnimationType(int idView){
        return cardAnimationDestination.get(idView);
    }

    public void deleteCardAnimationType(int idView){
        cardAnimationDestination.remove(idView);
    }
}
