package uci.plantID;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


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
        control.respondToButton(this, view);
    }

}