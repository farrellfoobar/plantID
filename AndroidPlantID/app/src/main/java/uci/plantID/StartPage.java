package uci.plantID;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Scanner;


public class StartPage extends AppCompatActivity
{
    //This plant is the plant we are building from the users answers and is visible in every class
    public static plant queryPlant;
    public static plantDatabase db;
    private ViewController control;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);


        init();
    }

    //TODO: THIS METHOD IS FOR TESTING, you can feel free to delete it but I left so you can see how it works
    public void imageTesting()
    {
        Log.d("!!!!! TESTING !!!!!", "HERERERERERERERERE");
        try {
            db = new plantDatabase( this.getAssets() );
        }catch( Exception e )
        {
            Log.d( "----ERROR----",  e.toString() );
            e.printStackTrace();
        }

        ImageView drawable = (ImageView) findViewById(R.id.imageView);
        Log.d("!!!!! TESTING !!!!!", Integer.toString( db.size() ) );
        plant p = db.getPlant(0);
        Log.d("!!!!! TESTING !!!!!", p.getCommonName());
        drawable.setImageDrawable( p.getImage().get(0) );

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

    private void testPlantImage(){
        ImageView image = (ImageView) findViewById(R.id.imageView);
        plant p = db.getPlant(0);
        Log.d("----LOG----", " plant is: " + p.getCommonName());
        image.setImageDrawable(p.getImage().get(0));
    }

    public void nextButton(View view){
        control.nextButton(this, view, queryPlant, db);

    }

    public void creditsButton(View view)
    {
        setContentView(R.layout.credits_page);
        TextView tv = (TextView)findViewById(R.id.creditsContent);

        Scanner scanner = null;
        String credits = "";

        try
        {
            scanner = new Scanner( getAssets().open("credits.txt") );
            scanner.useDelimiter("\n");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        while( scanner.hasNext() )
            credits += scanner.next() + "\n";

        tv.setText( credits );
        tv.setTextSize( (float) 0.25 * tv.getTextSize() );
    }

    public void creditsBackButton(View view)
    {
        setContentView(R.layout.activity_start_page);
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