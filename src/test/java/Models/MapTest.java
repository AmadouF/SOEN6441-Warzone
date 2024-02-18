package Models;
import static org.junit.Assert.assertEquals;
import Helpers.MapHelper;
import org.junit.Before;
import org.junit.Test;

import Exceptions.InvalidMap;

import java.util.*;
/**
 * Test class to test the map model.
 */
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
     * Tests the validatity of the map.
     * @throws InvalidMap exception
     */
    @Test (expected = InvalidMap.class)
    public void testIsValidMap() throws InvalidMap{
        d_Map=d_MapHelper.load(d_GameState,"canada.map");
        assertEquals(d_Map.isValidMap(),true);
    }
    /**
     * Tests the map with no continent.
     * @throws InvalidMap exception
     */
    @Test (expected = InvalidMap.class)
    public void testNoContinent() throws InvalidMap{
        assertEquals(d_Map.isValidMap(), true);
    }
    /**
     * Tests the map with the continent having no countries.
     * @throws InvalidMap exception
     */
    @Test (expected=InvalidMap.class)
    public void testNoCountry() throws InvalidMap{
        Continent l_Continent=new Continent("");
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
    public void testAreCountriesConnected() throws InvalidMap{
        d_Map.addContinent("Europe", 4);
        d_Map.addCountry("Germany", "Europe");
        d_Map.addCountry("Spain", "Europe");
        d_Map.addCountry("France", "Europe");
        d_Map.addNeighbour("France", "Spain");
        d_Map.addNeighbour("Spain", "France");
        d_Map.addNeighbour("France", "Germany");
        d_Map.areCountriesConnected();
    }
    /**
     * Tests the map with the empty continent
     * @throws InvalidMap exception
     */
    @Test (expected=InvalidMap.class)
    public void testIsContinentConnected() throws InvalidMap{
        d_Map.addContinent("Europe", 4);
        Continent l_Continent=d_Map.getContinentByName("Europe");
        d_Map.isContinentConnected(l_Continent);
    }
}
