package uci.plantID;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

//TODO: Implement saveToJSON and a constructor which reads from JSON
public class plantDatabase
{
    private ArrayList<plant> plants;
    final private File location;

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
        this.location = new File( location );   //this line is just here to prevent "might not have been initialized" compiler error, it might need to be surrounded by a try catch
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
