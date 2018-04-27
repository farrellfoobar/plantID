package uci.plantID;

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
}