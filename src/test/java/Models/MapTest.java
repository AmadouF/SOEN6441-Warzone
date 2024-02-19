package Models;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import Helpers.MapHelper;
import org.junit.Before;
import org.junit.Test;

import Exceptions.InvalidMap;

import java.util.*;

public class MapTest {
    /**
     * Pointer for the Map object
     */
    Map d_Map;
    /**
     * Pinter for the MapHelper object
     */
    MapHelper d_MapHelper;
    /**
     * Pointer for the GameState object
     */
    GameState d_GameState;
    /**
     * Creates the Map, MapHelper and GameState objects
     */
    @Before
    public void beforeTest(){
        d_Map=new Map();
        d_GameState=new GameState();
        d_MapHelper=new MapHelper();
    }

    /**
     * Tests the validity of the map.
     */
    @Test
    public void testIsValidMap() throws InvalidMap{
        d_Map=d_MapHelper.load(d_GameState,"sampleMap.txt");
        assertTrue(d_Map.isValidMap());
    }
    /**
     * Tests the map with no continent.
     */
    @Test (expected = InvalidMap.class)
    public void testNoContinent() throws InvalidMap{
        assertTrue(d_Map.isValidMap());
    }

    /**
     * Tests the map with the continent having no countries.
     * @throws InvalidMap exception
     */
    @Test (expected=InvalidMap.class)
    public void testNoCountry() throws InvalidMap{
        Continent l_Continent=new Continent("Europe");
        List<Continent> l_Continents=new ArrayList<Continent>();
        l_Continents.add(l_Continent);
        d_Map.setContinents(l_Continents);
        d_Map.isValidMap();
    }
    /**
     * Tests the map with the countries not connected.
     * @throws InvalidMap exception
     */
    @Test (expected=InvalidMap.class)
    public void testCountriesNotConnectedError() throws InvalidMap{
        d_Map.addContinent("Europe", 4);
        d_Map.addCountry("Germany", "Europe");
        d_Map.addCountry("Spain", "Europe");
        d_Map.addCountry("France", "Europe");
        d_Map.addNeighbour("France", "Spain");
        d_Map.addNeighbour("Spain", "France");
        d_Map.addNeighbour("France", "Germany");
        d_Map.areCountriesConnected();
    }

}
