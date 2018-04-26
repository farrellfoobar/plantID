package uci.plantID;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.InputStreamReader;

public class StartPage extends AppCompatActivity
{
    //This plant is the plant we are building from the users answers and is visible in every class
    public static plant queryPlant;

    private static int viewTracker = 0;
    private static int[] views = {
            R.layout.activity_start_page,
            R.layout.plant_group_question,
            R.layout.leaf_shape_question,
            R.layout.leaf_arrangement_question,
            R.layout.plant_growth_form_question
    };
    //I havent gotten to asking if there is a flower and then conditionally asking the next two questions

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        plantDatabase db = null;
        try {
            db = new plantDatabase( new InputStreamReader( this.getAssets().open("plants.JSON") ));
        }catch( Exception e )
        {
            Log.d( "----ERROR----",  e.getMessage() );
        };

        Log.d("", "OUT" + db.getPlant( "Acmispon glaber " ).getCommonName());

    }

    //this method is called when the next button on the activity_main page is pressed, it must take View view as a parameter, and view give access to certain ui elements
    public void respondToNextButton(View view)
    {
        viewTracker++;
        setContentView( views[viewTracker] );
    }

    //the previous button doesn't exists on the layouts but this is the method it should call
    public void respondToPrevButton(View view)
    {
        viewTracker--;
        setContentView( views[viewTracker] );
    }

    public void respondToButton(View view)
    {
        switch ( viewTracker )
        {
            case 0: //start page
                handleStartPageButton(view);
                break;
            case 1: //plant group question
                handlePlantGroupButton(view);
                break;
            case 2: //leaf shape question
                handleLeafShapeButtonButton(view);
                break;
            case 3: //leaf arrangement question
                handleLeafArrangementButton(view);
                break;
            case 4: //plant growth form question
                handleGrowthFormButton(view);
                break;
        }
    }

    public void handleStartPageButton( View view)
    {
        //This is mostly to keep the format the same:
        // The only button on the start page is the next button, so theoretically this method
        //should never be called, unless we add more button at the start
    }

    public void handlePlantGroupButton( View view)
    {
        switch ( view.getId() )
        {
            case R.id.plant_group_checkbox1:
                //modify query plant
                break;
            case R.id.plant_group_checkbox2:
                //modify query plant
                break;
            case R.id.plant_group_checkbox3:
                //modify query plant
                break;
            case R.id.plant_group_checkbox4:
                //modify query plant
                break;
            case R.id.plant_group_checkbox5:
                //modify query plant
                break;
        }
    }

    //TODO: Copy the switch from handlePlantGroupButton with appropriate button names and modify the query plant accordingly
    public void handleLeafShapeButtonButton( View view)
    {

    }

    //TODO: Copy the switch from handlePlantGroupButton with appropriate button names and modify the query plant accordingly
    public void handleLeafArrangementButton( View view)
    {

    }

    //TODO: Copy the switch from handlePlantGroupButton with appropriate button names and modify the query plant accordingly
    public void handleGrowthFormButton( View view)
    {

    }
}