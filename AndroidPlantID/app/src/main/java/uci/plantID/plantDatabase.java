package uci.plantID;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static uci.plantID.plant.NUM_ATTRIB;

public class plantDatabase extends Activity
{
    private ArrayList<plant> plants;
    final private String location = "plantDB.JSON";

    //NOTE: If the writing of this database is made easier by implementing some kind of writeToJSON() and readFromJSON() method for the plant object feel free to do that

    public plantDatabase( InputStreamReader in ) throws IllegalArgumentException, java.io.FileNotFoundException, java.io.IOException, org.json.simple.parser.ParseException, org.json.JSONException
    {
        plants = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONArray plantArray = (JSONArray) parser.parse( in );
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

        ArrayList<rankedPlant> ranks = new ArrayList<rankedPlant>(numToBeRanked);

        for(int i = 0; i < numToBeRanked; i++)
            ranks.add(new rankedPlant(null, 0));

        ranks.set(0, new rankedPlant( plants.get(startLocation), query.getMatch( plants.get(startLocation), 0 ) ) );

        double currentMatch;
        double worstTopMatch = 0;//ranks.get( numToBeRanked-1 ).getRank();
        //for( int i = startLocation+1; i % startLocation != 0; i++)  //iterate from startlocation to startlocation*2
        for(int i = 0; i < plants.size(); i++)
        {
            currentMatch = query.getMatch( plants.get(i), worstTopMatch );
            System.out.println(i);
            if( currentMatch  > worstTopMatch )
            {
                System.out.println(plants.get(i));
                ranks.set( ranks.size()-1, new rankedPlant( plants.get(i), currentMatch ) );
                Collections.sort(ranks);
                worstTopMatch = ranks.get( numToBeRanked-1 ).getRank();
            }
            /*
            else if( currentMatch == worstTopMatch) //happens too often maybe issue with thresh
            {
                ranks.add( new rankedPlant( plants.get(i), currentMatch ) );
            } */
        }

        return ranks;
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

}
