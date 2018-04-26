package uci.plantID;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PlantDBUnitTest
{
    @Test
    public void canCreateDB() throws Exception
    {
        plantDatabase d = new plantDatabase();
        plant p = new plant( "Acmispon glaber", "Deerweed", "shrub", "pinnate", "opposite", "erect", "yellow", "bilateral" );

        assert d.getPlant("Acmispon glaber" ).equals(p);
    }
}