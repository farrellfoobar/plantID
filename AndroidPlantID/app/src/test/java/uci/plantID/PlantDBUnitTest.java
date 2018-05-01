package uci.plantID;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PlantDBUnitTest
{
    String yourLoc = "C:\\Users\\farre\\Documents\\";                              //replace as needed
    String loc = "plantID\\AndroidPlantID\\app\\src\\main\\assets\\plants.JSON"; //leave alone (universal)
    String testingLoc = "plantID\\AndroidPlantID\\app\\src\\main\\assets\\testingPlants.JSON"; //leave alone (universal)

    @Test
    public void canCreateDB() throws Exception
    {
        /*
        InputStreamReader is = new InputStreamReader( new FileInputStream(yourLoc + loc) );
        plantDatabase db = new plantDatabase( is );
        plant p = new plant( "Acmispon glaber", "Deerweed", "shrub", "pinnate", "opposite", "erect", "yellow", "bilateral" );

        assert db.getPlant("Acmispon glaber").equals(p);
        assert db.getPlant("Acmispon glaber").getleafType().equals(p.getleafType());
        */
    }

    @Test
    public void canGetMatches() throws Exception
    {
        InputStreamReader is = new InputStreamReader( new FileInputStream(yourLoc + testingLoc) );
        plantDatabase db = new plantDatabase( is );

        plant p = new plant( "", "", "shrub", "pinnate", "opposite", "erect", "yellow", "bilateral" );

        ArrayList<rankedPlant> matches;

        System.out.println(" --- CALLING GET MATCH --- " );
        long m = System.currentTimeMillis();
        long n = System.nanoTime();
        matches = db.getGreatestMatch(p, 5);
        m = System.currentTimeMillis()-m;
        n = System.nanoTime()-n;
        double t = (m + n*Math.pow(10, -6) );
        System.out.println(" --- " + db.size() + " PLANTS PROCESSED IN: " + t + " MILISECONDS --- ");

        System.out.println( "----- TEST RESULTS -----" );
        for( rankedPlant s : matches)
            System.out.println( s.getPlant().getScientificName() + " is a % " + s.getRank() + " match");
    }


}