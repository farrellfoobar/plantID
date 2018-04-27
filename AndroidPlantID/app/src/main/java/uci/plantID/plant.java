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
    public static final List<String> validPlantGroups = Arrays.asList( "tree", "grass", "shrub", "forb", "succulent" );
    //Cylindropuntia prolifera is the only plant with N.A. as its valid leaf form (its a cactus), Im not sure how to handle that, but this works
    public static final List<String> validLeafTypes = Arrays.asList("simple", "lobed", "pinnate", "compound/ deeply divided", "blade", "N.A.");
    public static final List<String> validLeafArrangements = Arrays.asList("alternate", "opposite", "bundled", "whorled", "basal", "rosette");
    public static final List<String> validGrowthForms = Arrays.asList("prostrate", "decumbent", "ascending", "erect", "mat", "clump-forming", "rosette", "basal", "vine");
    public static final List<String> validFlowerColors = Arrays.asList("white", "yellow", "red", "orange", "blue", "purple", "green", "pink", "N.A.");
    public static final List<String> validFlowerSymetrys = Arrays.asList("radial", "bilateral", "asymmertical");

    public static final int NUM_ATTRIB = 8;   //6 attributes + scientific name and common name

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
    public plant( String scientificName, String commonName, String plantGroup, String leafType, String leafArrangement, String growthForm,String flowerColor, String flowerSymetry)
    {
        this.plantGroup         = new ArrayList<>();
        this.leafType           = new ArrayList<>();
        this.leafArrangement    = new ArrayList<>();
        this.growthForm         = new ArrayList<>();
        this.flowerColor        = new ArrayList<>();
        this.flowerSymetry      = new ArrayList<>();
        this.setScientificName( scientificName );
        this.setCommonName( commonName );
        this.addPlantGroup( plantGroup );
        this.addLeafArrangement( leafArrangement );
        this.addLeafType( leafType);
        this.addGrowthForm( growthForm );
        this.addFlowerSymetry( flowerSymetry );
        this.addFlowerColor( flowerColor );
    }

    public plant( String[] attribs ) throws IllegalArgumentException
    {
        if( attribs.length != NUM_ATTRIB )
            throw new IllegalArgumentException( "Expected " + NUM_ATTRIB + " length array to build plant, got " + attribs.length +" length array");

        this.plantGroup         = new ArrayList<>();
        this.leafType           = new ArrayList<>();
        this.leafArrangement    = new ArrayList<>();
        this.growthForm         = new ArrayList<>();
        this.flowerColor        = new ArrayList<>();
        this.flowerSymetry      = new ArrayList<>();
        this.setScientificName(     attribs[0] );
        this.setCommonName(         attribs[1] );
        this.addPlantGroup(         attribs[2].split(", ") );
        this.addLeafType(           attribs[3].split(", ") );
        this.addLeafArrangement(    attribs[4].split(", ") );
        this.addGrowthForm(         attribs[5].split(", ") );
        this.addFlowerColor(        attribs[6].split(", ") );
        this.addFlowerSymetry(      attribs[7].split(", ") );
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
    public void setScientificName( String scientificName)
    {
        this.scientificName = scientificName;
    }

    public String getScientificName()
    {
        return this.scientificName;
    }

    public void setCommonName(String commonName)
    {
        this.commonName = commonName;
    }

    public String getCommonName()
    {
        return this.commonName;
    }

    public void addPlantGroup( String plantGroup )
    {
        if( !validPlantGroups.contains(  plantGroup ) )
            throw new IllegalArgumentException("Expected valid plant group, got " + plantGroup);

        this.plantGroup.add(plantGroup);
    }

    public void addPlantGroup( String[] plantGroup )
    {
        for( String s : plantGroup )
            this.addPlantGroup( s );
    }

    public void removePlantGroup( String plantGroup )
    {
        if( !this.plantGroup.contains(  plantGroup ) )
            throw new IllegalArgumentException("Expected valid input, got " + plantGroup);

        this.plantGroup.remove(plantGroup);
    }

    public List<String> getPlantGroup()
    {
        return this.plantGroup;
    }

    public void addGrowthForm( String growthForm )
    {
        if( !validGrowthForms.contains(  growthForm ) )
            throw new IllegalArgumentException("Expected valid growth form got " + growthForm);

        this.growthForm.add(growthForm);
    }

    public void addGrowthForm( String[] growthForm )
    {
        for( String s : growthForm )
            this.addGrowthForm( s );
    }

    public void removeGrowthForm( String growthForm )
    {
        if( !this.growthForm.contains(  growthForm ) )
            throw new IllegalArgumentException("Expected valid input, got " + growthForm);

        this.growthForm.remove(growthForm);
    }

    public List<String> getGrowthForm()
    {
        return this.growthForm;
    }

    public void addFlowerSymetry( String flowerSymetry )
    {
        if( !validFlowerSymetrys.contains(  flowerSymetry ) )
            throw new IllegalArgumentException("Expected valid flower symetry, got " + flowerSymetry);

        this.flowerSymetry.add(flowerSymetry);
    }

    public void addFlowerSymetry( String[] flowerSymetry )
    {
        for( String s : flowerSymetry )
            this.addFlowerSymetry( s );
    }

    public void removeFlowerSymetry( String flowerSymetry )
    {
        if( !this.flowerSymetry.contains(  flowerSymetry ) )
            throw new IllegalArgumentException("Expected valid input, got " + flowerSymetry);

        this.flowerSymetry.remove(flowerSymetry);
    }

    public List<String> getFlowerSymetry()
    {
        return this.flowerSymetry;
    }

    public void addFlowerColor( String flowerColor )
    {
        if( !validFlowerColors.contains(  flowerColor ) )
            throw new IllegalArgumentException("Expected valid flower color, got " + flowerColor);

        this.flowerColor.add(flowerColor);
    }

    public void addFlowerColor( String[] flowerColor )
    {
        for( String s : flowerColor )
            this.addFlowerColor( s );
    }

    public void removeFlowerColor( String flowerColor )
    {
        if( !this.flowerColor.contains(  flowerColor ) )
            throw new IllegalArgumentException("Expected valid input, got " + flowerColor);

        this.flowerColor.remove(flowerColor);
    }

    public List<String> getFlowerColor()
    {
        return this.flowerColor;
    }

    public void addLeafArrangement( String leafArrangment )
    {
        if( !validLeafArrangements.contains(  leafArrangment ) )
            throw new IllegalArgumentException("Expected valid leaf arrangement, got " + leafArrangment);

        this.leafArrangement.add(leafArrangment);
    }

    public void addLeafArrangement( String[] leafArrangement )
    {
        for( String s : leafArrangement )
            this.addLeafArrangement( s );
    }

    public void removeLeafArrangement( String leafArrangment )
    {
        if( !this.leafArrangement.contains(  leafArrangment ) )
            throw new IllegalArgumentException("Expected valid input, got " + leafArrangment);

        this.leafArrangement.add(leafArrangment);
    }

    public List<String> getLeafArrangements()
    {
        return this.leafArrangement;
    }

    public void addLeafType( String leafType )
    {
        if( !validLeafTypes.contains(  leafType ) )
            throw new IllegalArgumentException("Expected valid leaf type, got " + leafType);

        this.leafType.add( leafType );
    }

    public void addLeafType( String[] leafType )
    {
        for( String s : leafType )
            this.addLeafType( s );
    }

    public void removeLeafType( String leafType )
    {
        if( !this.validLeafTypes.contains(  leafType ) )
            throw new IllegalArgumentException("Expected valid input, got " + leafType);

        this.leafType.remove(leafType);
    }

    public List<String> getleafType()
    {
        return this.leafType;
    }

}
