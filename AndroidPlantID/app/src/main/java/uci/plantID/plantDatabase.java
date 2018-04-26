package uci.plantID;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

//TODO: Implement saveToJSON and a constructor which reads from JSON
public class plantDatabase extends Activity
{
    private ArrayList<plant> plants;
    final private String location = "plantDB.JSON";
    final int NUM_ATTRIB = 8;   //6 attributes + scientific name and common name

    //NOTE: If the writing of this database is made easier by implementing some kind of writeToJSON() and readFromJSON() method for the plant object feel free to do that

    /*
    The constructor creates the database, given a location on disk to load from and store to
    if the file at location already exists, instantiate the plants arraylist using the JSON file
    if the file exists but isnt a json file, or is not formatted correctly, throw an exception (fileNotFound, or some json exception like JsonException)
    if the file does not exist, create a new ArrayList
    After the method is complete the arraylist must be sorted. The arraylist will be sorted when it is saved to disk, so unless the parsing of json messes with
    the sorting, the arraylist should not need to be resorted.
    TODO: have this method parse the JSON and fill in the plants Arraylist Accordingly. */
    public plantDatabase( String location)
    {
        File f = new File( location );
        try
        {
            JSONParser parser = new JSONParser();
            JSONArray a = (JSONArray) parser.parse(new FileReader(location));
            String [] plantBuilder = new String[NUM_ATTRIB];

            for( int i = 0; i < a.length(); i++)
            {
                for( int j = 0; j < NUM_ATTRIB; j++)
                {
                    //this is a nasty line: we are casting JSONobject to JSON array, getting the Jth string in that array and setting plantBuilder[j] to it;
                    plantBuilder[j] = (String) ( (JSONArray) a.get(i) ).get(j);
                }
            }
        }
        catch( Exception e)
        {

        }
    }

    /*
    TODO: look for the best numToBeRanked plants in the database, being sure to stop looking at a particular as soon as it is worst than the best numToBeRanked  */
    public plant[] getGreatestMatch( plant query, int numToBeRanked)
    {
        int startLocation = Collections.binarySearch( plants, query );
        return new plant[] {};  //placeholder
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
