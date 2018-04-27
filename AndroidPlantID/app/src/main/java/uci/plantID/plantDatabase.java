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
import java.util.Collections;

import static uci.plantID.plant.NUM_ATTRIB;

//TODO: Implement saveToJSON and a constructor which reads from JSON
public class plantDatabase extends Activity
{
    private ArrayList<plant> plants;
    final private String location = "plantDB.JSON";

    //NOTE: If the writing of this database is made easier by implementing some kind of writeToJSON() and readFromJSON() method for the plant object feel free to do that

    /*
    The constructor creates the database, given a location on disk to load from and store to
    if the file at location already exists, instantiate the plants arraylist using the JSON file
    if the file exists but isnt a json file, or is not formatted correctly, throw an exception (fileNotFound, or some json exception like JsonException)
    if the file does not exist, create a new ArrayList
    After the method is complete the arraylist must be sorted. The arraylist will be sorted when it is saved to disk, so unless the parsing of json messes with
    the sorting, the arraylist should not need to be resorted.
    TODO: have this method parse the JSON and fill in the plants Arraylist Accordingly. */
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
    TODO: look for the best numToBeRanked plants in the database, being sure to stop looking at a particular as soon as it is worst than the best numToBeRanked  */
    public plant[] getGreatestMatch( plant query, int numToBeRanked)
    {
        int startLocation = Collections.binarySearch( plants, query );
        return new plant[] {};  //placeholder
    }

    //TODO: This method is for testing
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

    /*
    Saving to JSON must maintain the order of the elements in the arraylist.
    TODO: save the database, as a json file, overwriting the existing file located at location  */
    public void saveToJSON()
    {

    }


    // This method makes use of the Java collections class which implements a binary search based upon the compareTo method in the plant class
    public void addPlant( plant p )
    {
        Collections.addAll( plants, p );
        this.saveToJSON();
    }

    // This method makes use of the Java collections class which implements a binary search based upon the compareTo method in the plant class
    public void removePlant( plant p )
    {
        int i = Collections.binarySearch( plants, p );

        if( !plants.get(i).equals(p)  )
            throw new IllegalArgumentException("This plant is not in the database");    //we might want to handle this more gracefully than throwing an error at some point

        plants.remove(i);

        this.saveToJSON();
    }
}
