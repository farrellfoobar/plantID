package uci.plantID;

import android.util.JsonWriter;
import android.util.Log;

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
    private ArrayList<String> plantGroup;
    private ArrayList<String> leafType;
    private ArrayList<String> leafArrangement;
    private ArrayList<String> growthForm;
    private ArrayList<String> flowerColor;
    private ArrayList<String> flowerSymetry;

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
        this.addPlantGroup( plantGroup.split(", ") );
        this.addLeafArrangement( leafArrangement.split(", ") );
        this.addLeafType( leafType.split(", ") );
        this.addGrowthForm( growthForm.split(", ") );
        this.addFlowerSymetry( flowerSymetry.split(", ") );
        this.addFlowerColor( flowerColor.split(", ") );
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
        this.plantGroup         = new ArrayList<>();
        this.leafType           = new ArrayList<>();
        this.leafArrangement    = new ArrayList<>();
        this.growthForm         = new ArrayList<>();
        this.flowerColor        = new ArrayList<>();
        this.flowerSymetry      = new ArrayList<>();
    }

    /* TODO: This isnt a very good compareTo method for out purposes, because we would like a plant that is close to another plant (in the arraylist) to be similar*/
    @Override
    public int compareTo( plant p )
    {
        if( this.plantGroup.get(0) != p.plantGroup.get(0)  )
            return this.plantGroup.get(0).compareTo( p.plantGroup.get(0) );
        else if( this.leafType.get(0) != p.leafType.get(0)  )
            return this.leafType.get(0).compareTo( p.leafType.get(0) );
        else if( this.leafArrangement.get(0) != p.leafArrangement.get(0)  )
            return this.leafArrangement.get(0).compareTo( p.leafArrangement.get(0) );
        else if( this.growthForm.get(0) != p.growthForm.get(0)  )
            return this.growthForm.get(0).compareTo( p.growthForm.get(0) );
        else if( this.flowerColor.get(0) != p.flowerColor.get(0)  )
            return this.flowerColor.get(0).compareTo( p.flowerColor.get(0) );
        else if( this.flowerSymetry.get(0) != p.flowerSymetry.get(0)  )
            return this.flowerSymetry.get(0).compareTo( p.flowerSymetry.get(0) );
        else
            return 0;
    }

    @Override
    public String toString()
    {
        return this.getScientificName() + "\n" +
                this.getCommonName() + "\n" +
                this.getPlantGroup() + "\n" +
                this.getleafType() + "\n" +
                this.getLeafArrangements() + "\n" +
                this.getGrowthForm() + "\n" +
                this.getFlowerColor() + "\n" +
                this.getFlowerSymetry() + "\n";
    }

    public double getMatch( plant p, double thresh  )
    {
        double match = 1.0;
        int i = 0;
        while( match*100 > thresh && i < NUM_ATTRIB - 2)    //-2 for common name and sci name
        {
            double intersectionSize = 0;
            double uninionSize = this.getList(i).size() + p.getList(i).size();
            for( String s : this.getList(i) )
            {
                if( p.getList(i).contains(s) )
                    intersectionSize++;
            }

            match -=  1.0/(NUM_ATTRIB-2) * (1-(intersectionSize / (uninionSize-intersectionSize)) );
            i++;
        }

        return match*100;
    }

    @Override
    public boolean equals( Object p )
    {
        if( p instanceof plant )
            return (this.compareTo( (plant) p) == 0);
        else
            return false;
    }

    //This is just a utility method
    public ArrayList<String> getList( int i)
    {
        if( i > NUM_ATTRIB-2 || i < 0)
            throw new IllegalArgumentException("The " + i + "th attributes doesnt exist");

        ArrayList<String> out = null;

        switch ( i )
        {
            case 0:
                out =this.getPlantGroup();
                break;
            case 1:
                out =this.getleafType();
                break;
            case 2:
                out =this.getLeafArrangements();
                break;
            case 3:
                out =this.getGrowthForm();
                break;
            case 4:
                out =this.getFlowerColor();
                break;
            case 5:
                out =this.getFlowerSymetry();
                break;
        }

        return out;
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

    public ArrayList<String> getPlantGroup()
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

    public ArrayList<String> getGrowthForm()
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

    public ArrayList<String> getFlowerSymetry()
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

    public ArrayList<String> getFlowerColor()
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

    public ArrayList<String> getLeafArrangements()
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

    public ArrayList<String> getleafType()
    {
        return this.leafType;
    }

}
