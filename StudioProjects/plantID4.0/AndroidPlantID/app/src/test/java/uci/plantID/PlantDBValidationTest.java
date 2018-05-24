package uci.plantID;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class PlantDBValidationTest
{
    String yourLoc = "E:\\AndroidStudioProjects\\";                              //replace as needed
    String testingLoc = "plantID\\AndroidPlantID\\app\\src\\main\\assets\\testingPlants.JSON"; //leave alone (universal)

    @Test
    public void canGetMatches() throws Exception
    {
        InputStreamReader is = new InputStreamReader( new FileInputStream(yourLoc + testingLoc) );
        plantDatabase db = new plantDatabase( is );
    }


}