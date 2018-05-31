package uci.plantID;

import android.app.Activity;
import android.content.res.AssetManager;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import static uci.plantID.plant.NUM_ATTRIB;

public class plantDatabase extends Activity
{
    private ArrayList<plant> plants;
    final private String location = "plants.JSON";
    private AssetManager assetManager;

    //NOTE: If the writing of this database is made easier by implementing some kind of writeToJSON() and readFromJSON() method for the plant object feel free to do that

    public plantDatabase( AssetManager assetManager ) throws IllegalArgumentException, java.io.FileNotFoundException, java.io.IOException, org.json.simple.parser.ParseException, org.json.JSONException
    {
        this.assetManager = assetManager;
        plants = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONArray plantArray = (JSONArray) parser.parse( new InputStreamReader( this.assetManager.open(location) ) );
        JSONArray attributeArrayJSON;
        String [] attributeArray = new String[ NUM_ATTRIB ];

        for( int i = 0; i < plantArray.size(); i++)
        {
            //Cast the JSONobject to a JSON Array (the array that represents a specific plant)
            attributeArrayJSON = ((JSONArray) plantArray.get(i) );

            if( attributeArrayJSON.size() != NUM_ATTRIB )
                throw new IllegalArgumentException();

            for( int j = 0; j < NUM_ATTRIB; j++)
            {
                attributeArray[j] = (String) attributeArrayJSON.get(j);
            }

            plants.add( new plant( attributeArray ) );
            //NOTE: Plant images are only attached to the plant object when the plant is a result of a search. This is necessary to preserve memory
        }
    }

    //this constructor is exclusively for testing
    public plantDatabase( InputStreamReader isr ) throws java.io.IOException, org.json.simple.parser.ParseException, Exception
    {
        plants = new ArrayList<>();
        JSONParser parser = new JSONParser();

        JSONArray plantArray = (JSONArray) parser.parse(isr);

        JSONArray attributeArrayJSON;
        String [] attributeArray = new String[ NUM_ATTRIB ];

        for( int i = 0; i < plantArray.size(); i++)
        {
            //Cast the JSONobject to a JSON Array (the array that represents a specific plant)
            attributeArrayJSON = ((JSONArray) plantArray.get(i) );

            if( attributeArrayJSON.size() != NUM_ATTRIB )
                throw new IllegalArgumentException("There appears to be more attributes in the JSON than are supposed to be");


            for( int j = 0; j < NUM_ATTRIB; j++)
            {
                attributeArray[j] = (String) attributeArrayJSON.get(j);
            }

            try {
                plants.add(new plant(attributeArray));
            }catch (Exception e)
            {
                throw new Exception( "The following parse error occured on plant #" + i + "\n" + e.getMessage() );
            }
        }
    }

    /*
    TODO: look for the best numToBeRanked plants in the database, being sure to stop looking at a particular as soon as it is worst than the wost one on the short list  */
    //TODO: THIS IS THE METHOD THAT ISNT WORKING
    public ArrayList<rankedPlant> getGreatestMatch( plant query, int numToBeRanked)
    {
        int startLocation = Collections.binarySearch( plants, query );

        // -insertionPoint -1 is returned if the exact plant is not there (see java collections documentation)
        if( startLocation < 0)
            startLocation = -startLocation +1;

        ArrayList<rankedPlant> ranks = new ArrayList<>(numToBeRanked);

        double currentMatch;
        double worstTopMatch = 0;
        //for( int i = 0; i  < plants.size(); i++)
        int i = startLocation;
        do
        {
            currentMatch = query.getMatch( plants.get(i), worstTopMatch );
            if( currentMatch  > worstTopMatch )
            {
                if( ranks.size() < numToBeRanked )
                    ranks.add( new rankedPlant( plants.get(i), currentMatch ) );
                else
                    ranks.set(0, new rankedPlant( plants.get(i), currentMatch ) );  //replace the worst

                Collections.sort(ranks);                    //sorts in ascending order

                if( ranks.size() < numToBeRanked )
                    worstTopMatch = 0;                          //we need to populate more results
                else
                    worstTopMatch = ranks.get( 0 ).getRank();   //get(0) is the worst match

            }

            i++;

            if( i >= plants.size() )
                i = i % plants.size();

        }while( i != startLocation);

        for( rankedPlant p : ranks)
            p.getPlant().setImage(assetManager);

        Collections.reverse( ranks ); //ascending -> descending

        return ranks;
    }

    public int size()
    {
        return plants.size();
    }

    //This method is for debugging
    public plant getPlant( String scientificName )
    {
        plant query = new plant();
        query.setScientificName( scientificName );
        int index = Collections.binarySearch( plants, query );

        if( index > -1 )
            return plants.get(index);
        else
            return query;   //query is not in db
    }

    public plant getPlant( int i)
    {
        return this.plants.get(i);
    }
}
