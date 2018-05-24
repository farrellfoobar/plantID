package uci.plantID;

import android.util.Log;

import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PlantUnitTest
{
    @Test
    public void canCreatePlant()
    {
        plant p = new plant( "Acmispon glaber", "Deerweed", "shrub", "pinnate", "opposite", "erect", "yellow", "bilateral" );
        assert( p.getCommonName().equals("Deerweed") );
        assert( p.getPlantGroup().get(0).equals("shrub") );
    }

    @Test
    public void canMatchPlants()
    {
        plant p1 = new plant( "Acmispon glaber", "Deerweed", "shrub", "pinnate", "opposite", "erect", "yellow", "bilateral" );
        plant p2 = new plant( "Acmispon glaber", "Deerweed", "forb", "pinnate", "opposite", "erect", "yellow", "bilateral" );
        plant p3 = new plant( "Centaurea melitensis ", "Malta or Maltese Star-thistle or Tocalote", "forb", "lobed", "basal", "rosette", "yellow", "radial" );

        System.out.println("----DATA----" + "P1 to p2: " + Double.toString( p1.getMatch(p2, 0) ) );
        System.out.println("----DATA----" + "P2 to p1: " + Double.toString( p2.getMatch(p1, 0) ) );
        System.out.println("----DATA----" + "P1 to p3: " + Double.toString( p1.getMatch(p3, 0) ) );

        System.out.println( "----DATA----" + "P1 to p3 with thresh: " + Double.toString( p1.getMatch(p3, 83.33) )  );
    }

    @Test
    public void dataBaseMachChecking()
    {
        plant p1 = new plant( "", "", "shrub", "pinnate", "opposite", "erect", "yellow", "bilateral" );
        plant p2 = new plant( "", "", "forb", "pinnate", "opposite", "prostrate", "yellow", "bilateral" );

        System.out.println(" db: " + p1.getMatch(p2, 0) );
    }
}