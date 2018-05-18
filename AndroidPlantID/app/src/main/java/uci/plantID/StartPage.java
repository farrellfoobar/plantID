package uci.plantID;

import android.content.Intent;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.ArrayList;


public class StartPage extends AppCompatActivity
{
    //This plant is the plant we are building from the users answers and is visible in every class
    public static plant queryPlant;
    public static plantDatabase db;
    private ViewController control;


    //TODO: DONE
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init();

        setContentView(R.layout.activity_start_page);
    }

    private void init(){
        try {
            db = new plantDatabase(this.getAssets());
        }catch( Exception e )
        {
            Log.d( "----ERROR----",  e.getMessage() );
        }
        queryPlant = new plant();
        control = new ViewController(this);
    }
    public void respond(View view){
        /*int id = 1000;
        //control.respond(this, view);
        View test = getLayoutInflater().inflate(R.layout.plant_standard_linear_scroll, null);
        LinearLayout a = test.findViewById(R.id.container);
        CardView b = new CardView(this);
        ConstraintLayout c = new ConstraintLayout(this);
        final ImageButton d = new ImageButton(this);
        TextView e = new TextView(this);
        e.setText("TRAIT");
        TextView descrip = new TextView(this);
        descrip.setText("DESCRIPTION DESCRIPTION DESCRIPTION DESCRIPTION DESCRIPTION DESCRIPTION DESCRIPTION");
        descrip.setLines(4);
        a.setId(View.generateViewId());
        b.setId(View.generateViewId());
        c.setId(View.generateViewId());
        d.setId(View.generateViewId());
        e.setId(View.generateViewId());
        descrip.setId(++id);



        b.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        c.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        b.setCardBackgroundColor(getResources().getColor(R.color.lightgreen));
        d.setLayoutParams(new LinearLayout.LayoutParams(250, 250));

        final ViewStub stub = new ViewStub(this);
        stub.setLayoutResource(R.layout.test_stub);


        a.addView(b);

        b.addView(c);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.setLayoutParams(new ConstraintLayout.LayoutParams(400,400));
            }

        });

        if (findViewById(R.id.question_next_button) != null)
            findViewById(R.id.question_next_button).setEnabled(true);
        c.addView(e);
        c.addView(d);
        c.addView(descrip);

        ConstraintSet cs = new ConstraintSet();
        cs.clone(c);
        cs.connect(e.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 10);
        cs.connect(e.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 20);

        cs.connect(d.getId(), ConstraintSet.TOP, e.getId(), ConstraintSet.BOTTOM, 10);
        cs.connect(d.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 10);

        cs.connect(descrip.getId(), ConstraintSet.LEFT, d.getId(), ConstraintSet.RIGHT, 10);
        cs.connect(descrip.getId(), ConstraintSet.TOP, e.getId(), ConstraintSet.BOTTOM, 10);

        cs.applyTo(c);




        View test2 = getLayoutInflater().inflate(R.layout.plant_standard_linear_scroll, null);


        if (counter == 0)
            setContentView(test);
        else
            setContentView(test2);
        ++counter;*/
    }

    public void nextButton(View view){
        control.nextButton(this, view, queryPlant, db);

    }

    public void backButton(View view){
        control.backButton(this, view, queryPlant, db);
    }

    public void homeButton(View view){
        queryPlant = new plant();
        control.homeButton(this, view);
    }
    public void respondToButton(View view){

    }

}