package uci.plantID;

import android.util.JsonWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.*;

//TODO: Work on plantDatabase and if necessary add and implement writeToJSON and readFromJSON methods
public class plant implements Comparable<plant>
{

    //to keep things consistent I simply took the object variable and added valid to the front and s to the end, so there are some weird spellings
    public static final List<String> validPlantGroups = Arrays.asList( "Tree", "Grass", "Shrub", "Forb", "Succulent" );
    public static final List<String> validLeafShapes = Arrays.asList("Simple Leaf", "Pinnately Lobed", "Palmately Lobed", "Deeply 3-lobed", "Compound Leaf");
    public static final List<String> validLeafArrangements = Arrays.asList("Alternate", "Opposite", "Bundled", "Whorled", "Roaddte");
    public static final List<String> validGrowthForms = Arrays.asList("Prostrate", "Decumben", "Ascending", "Erect", "Mat-forming", "Clump-forming", "Roaddte");
    public static final List<String> validFlowerColors = Arrays.asList("White", "Yellow", "Red", "Orange", "Blue", "Purple", "Green");
    public static final List<String> validFlowerSymetrys = Arrays.asList("Radial", "Bilateral", "Asymmertical");

    private String scientificName;
    private String commonName;
    private List<String> plantGroup;
    private List<String> leafShape;
    private List<String> leafArrangement;
    private List<String> growthForm;
    private List<String> flowerColor;
    private List<String> flowerSymetry;

    public plant( String scientificName, String commonName, String plantGroup, String growthForm, String flowerSymetry, String flowerColor,String leafArrangment, String leafShape )
    {
        this.setScientificName( scientificName );
        this.setCommonName( commonName );
        this.addPlantGroup( plantGroup );
        this.addGrowthForm( growthForm );
        this.addFlowerSymetry( flowerSymetry );
        this.addFlowerColor( flowerColor );
        this.addLeafArrangement( leafArrangment );
        this.addLeafShape( leafShape );
    }

    //This default constructor exists almost exclusively to allow us to build the queryPlant, it might not be wise to allow empty plants into the database
    public plant()
    {

    }

    //we dont explicitly need to throw ClassCastException here but I was debating adding a try catch and rethrow with more information
    //in the future we might want to handle the error differently
    public plant( String JSONlocation ) throws org.json.JSONException, ClassCastException
    {
        JSONObject in = new JSONObject( JSONlocation );

        this.plantGroup         = (List<String>) in.get("plantGroup");
        this.leafArrangement    = (List<String>) in.get("leafArrangement");
        this.leafShape          = (List<String>) in.get("leafShape");
        this.growthForm         = (List<String>) in.get("growthForm");
        this.flowerColor        = (List<String>) in.get("flowerColor");
        this.flowerSymetry      = (List<String>) in.get("flowerSymetry");
    }

    // The scientific names should be unique
    @Override
    public int compareTo( plant p )
    {
        return this.scientificName.compareTo( p.scientificName );
    }

    @Override
    public boolean equals( Object p )
    {
        if( p instanceof plant )
            return (this.compareTo( (plant) p) == 0);
        else
            return false;
    }

    //adders, removers and getters:
    public void setScientificName(String scientificName)
    {
        this.scientificName = scientificName;
    }

    public String getScientificName(String scientificName)
    {
        return this.scientificName;
    }

    public void setCommonName(String commonName)
    {
        this.commonName = commonName;
    }

    public String getCommonName(String commonName)
    {
        return this.commonName;
    }

    public void addPlantGroup( String plantGroup )
    {
        if( !validPlantGroups.contains(  plantGroup ) )
            throw new IllegalArgumentException();

        this.plantGroup.add(plantGroup);
    }

    public void removePlantGroup( String plantGroup )
    {
        if( !this.plantGroup.contains(  plantGroup ) )
            throw new IllegalArgumentException();

        this.plantGroup.remove(plantGroup);
    }

    public List<String> getPlantGroup()
    {
        return this.plantGroup;
    }

    public void addGrowthForm( String growthForm )
    {
        if( !validGrowthForms.contains(  growthForm ) )
            throw new IllegalArgumentException();

        this.growthForm.add(growthForm);
    }

    public void removeGrowthForm( String growthForm )
    {
        if( !this.growthForm.contains(  growthForm ) )
            throw new IllegalArgumentException();

        this.growthForm.remove(growthForm);
    }

    public List<String> getGrowthForm()
    {
        return this.growthForm;
    }

    public void addFlowerSymetry( String flowerSymetry )
    {
        if( !validFlowerSymetrys.contains(  flowerSymetry ) )
            throw new IllegalArgumentException();

        this.flowerSymetry.add(flowerSymetry);
    }

    public void removeFlowerSymetry( String flowerSymetry )
    {
        if( !this.flowerSymetry.contains(  flowerSymetry ) )
            throw new IllegalArgumentException();

        this.flowerSymetry.remove(flowerSymetry);
    }

    public List<String> getFlowerSymetry()
    {
        return this.flowerSymetry;
    }

    public void addFlowerColor( String flowerColor )
    {
        if( !validFlowerColors.contains(  flowerColor ) )
            throw new IllegalArgumentException();

        this.flowerColor.add(flowerColor);
    }

    public void removeFlowerColor( String flowerColor )
    {
        if( !this.flowerColor.contains(  flowerColor ) )
            throw new IllegalArgumentException();

        this.flowerColor.remove(flowerColor);
    }

    public List<String> getFlowerColor()
    {
        return this.flowerColor;
    }

    public void addLeafArrangement( String leafArrangment )
    {
        if( !validLeafArrangements.contains(  leafArrangment ) )
            throw new IllegalArgumentException();

        this.leafArrangement.add(leafArrangment);
    }

    public void removeLeafArrangement( String leafArrangment )
    {
        if( !this.leafArrangement.contains(  leafArrangment ) )
            throw new IllegalArgumentException();

        this.leafArrangement.add(leafArrangment);
    }

    public List<String> getLeafArrangements()
    {
        return this.leafArrangement;
    }

    public void addLeafShape( String leafShape )
    {
        if( !validLeafShapes.contains(  leafShape ) )
            throw new IllegalArgumentException();

        this.leafShape.add(leafShape);
    }

    public void removeLeafShape( String leafShape )
    {
        if( !this.leafShape.contains(  leafShape ) )
            throw new IllegalArgumentException();

        this.leafShape.remove(leafShape);
    }

    public List<String> getleafShape()
    {
        return this.leafShape;
    }

}