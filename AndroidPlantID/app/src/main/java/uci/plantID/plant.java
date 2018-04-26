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

public class plant implements Comparable<plant>
{

    //to keep things consistent I simply took the object variable and added valid to the front and s to the end, so there are some weird spellings
    public static final List<String> validPlantGroups = Arrays.asList( "Tree", "Grass", "Shrub", "Forb", "Succulent" );
    public static final List<String> validLeafTypes = Arrays.asList("Simple Leaf", "Pinnately Lobed", "Palmately Lobed", "Deeply 3-lobed", "Compound Leaf");
    public static final List<String> validLeafArrangements = Arrays.asList("Alternate", "Opposite", "Bundled", "Whorled", "Roaddte");
    public static final List<String> validGrowthForms = Arrays.asList("Prostrate", "Decumben", "Ascending", "Erect", "Mat-forming", "Clump-forming", "Roaddte");
    public static final List<String> validFlowerColors = Arrays.asList("White", "Yellow", "Red", "Orange", "Blue", "Purple", "Green");
    public static final List<String> validFlowerSymetrys = Arrays.asList("Radial", "Bilateral", "Asymmertical");

    private String scientificName;
    private String commonName;
    private List<String> plantGroup;
    private List<String> leafType;
    private List<String> leafArrangement;
    private List<String> growthForm;
    private List<String> flowerColor;
    private List<String> flowerSymetry;

    //TODO: This method needs to be able to parse a string with a comma in it and split it into multiple strings and add each
    //(This need to be done to make it compatable with JSON parsing in plantBD)
    public plant( String scientificName, String commonName, String plantGroup, String growthForm, String flowerSymetry, String flowerColor,String leafArrangment, String leafType)
    {
        this.plantGroup         = new ArrayList();
        this.leafType           = new ArrayList();
        this.leafArrangement    = new ArrayList();
        this.growthForm         = new ArrayList();
        this.flowerColor        = new ArrayList();
        this.flowerSymetry      = new ArrayList();
        this.setScientificName( scientificName );
        this.setCommonName( commonName );
        this.addPlantGroup( plantGroup );
        this.addGrowthForm( growthForm );
        this.addFlowerSymetry( flowerSymetry );
        this.addFlowerColor( flowerColor );
        this.addLeafArrangement( leafArrangment );
        this.addLeafType( leafType);
    }

    //This default constructor exists almost exclusively to allow us to build the queryPlant, it might not be wise to allow empty plants into the database
    public plant()
    {

    }

    /* TODO: This isnt a very good compareTo method for out purposes, because we would like a plant that is close to another plant (in the arraylist) to be similar*/
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

    public void addLeafType( String leafType )
    {
        if( !validLeafTypes.contains(  leafType ) )
            throw new IllegalArgumentException();

        this.leafType.add( leafType );
    }

    public void removeLeafType( String leafType )
    {
        if( !this.validLeafTypes.contains(  leafType ) )
            throw new IllegalArgumentException();

        this.leafType.remove(leafType);
    }

    public List<String> getleafShape()
    {
        return this.leafType;
    }

}
