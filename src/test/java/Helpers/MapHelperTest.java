package Helpers;
import Models.Map;
import Models.Continent;
import Models.GameState;
import Exceptions.*;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;
/**
 * This class tests the operations on map like addition and deletion of countries, continents and neighbors
 */
public class MapHelperTest {

    Map d_Map;
    GameState d_GameState;
    MapHelper d_MapHelper;
    /**
     * Creates new map, state and helper objects
     */
    @Before
    public void init(){
        d_Map=new Map();
        d_GameState=new GameState();
        d_MapHelper=new MapHelper();
        d_MapHelper.load(d_GameState, "sampleMap.txt");
    }
    /**
     * This method tests the add continent operation
     * @throws IOException exception
     * @throws InvalidMap invalid map exception
     */
    @Test
    public void testAddContinent() throws IOException, InvalidMap{
        Map l_newMap=new Map();
        d_GameState.setD_map(l_newMap);
        d_MapHelper.editContinent(d_GameState, "Europe 5", "add");

        assertEquals(d_GameState.getD_map().getContinentsList().size(), 1);
		assertEquals(d_GameState.getD_map().getContinentsList().get(0).getD_name(), "Europe");
		assertEquals(d_GameState.getD_map().getContinentsList().get(0).getD_value().toString(), "5");
    }
    /**
     * This method tests the remove continent operation
     * @throws IOException exception
     * @throws InvalidMap invalid map exception
     */
    @Test
    public void testRemoveContinent() throws IOException, InvalidMap{
        List<Continent> l_continents = new ArrayList<>();
		Continent l_c1 = new Continent(1,"Europe",5);

		Continent l_c2 = new Continent(2,"Asia",3);

		l_continents.add(l_c1);
		l_continents.add(l_c2);

		Map l_newMap = new Map();
		l_newMap.setContinents(l_continents);
		d_GameState.setD_map(l_newMap);
		d_MapHelper.editContinent(d_GameState, "Asia", "remove");

		assertEquals(d_GameState.getD_map().getContinentsList().size(), 1);
		assertEquals(d_GameState.getD_map().getContinentsList().get(0).getD_name(), "Europe");
		assertEquals(d_GameState.getD_map().getContinentsList().get(0).getD_value().toString(), "5");
    }
    /**
     * This method tests the add country operation
     * @throws IOException exception
     * @throws InvalidMap invalid map exception
     */
    @Test
    public void testAddCountry() throws IOException, InvalidMap{
        Map l_newMap=new Map();
        d_GameState.setD_map(l_newMap);
        d_MapHelper.editContinent(d_GameState, "Asia 5", "add");
		d_MapHelper.editCountry(d_GameState, "India Asia", "add");

		assertEquals(d_GameState.getD_map().getCountryByName("India").getD_name(), "India");
    }
    /**
     * This method tests the remove country operation
     * @throws IOException exception
     * @throws InvalidMap invalid map exception
     */
    @Test (expected = InvalidMap.class)
    public void testRemoveCountry() throws IOException, InvalidMap{
        Map l_newMap=new Map();
        d_GameState.setD_map(l_newMap);
        d_MapHelper.editContinent(d_GameState, "Asia 5", "add");
		d_MapHelper.editCountry(d_GameState, "India Asia", "add");
		d_MapHelper.editCountry(d_GameState, "China", "remove");
    }
    /**
     * This method tests the add neighbor operation
     * @throws IOException exception
     * @throws InvalidMap invalid map exception
     */
    @Test
    public void testAddNeighbors() throws IOException, InvalidMap{
        Map l_newMap=new Map();
        d_GameState.setD_map(l_newMap);
		d_MapHelper.editContinent(d_GameState, "Asia 5", "add");
		d_MapHelper.editCountry(d_GameState, "India Asia", "add");
		d_MapHelper.editCountry(d_GameState, "China Asia", "add");
		d_MapHelper.editNeighbour(d_GameState, "India China", "add");

		assertEquals(d_GameState.getD_map().getCountryByName("India").getD_adjacentCountryIds().get(0), 
                    d_GameState.getD_map().getCountryByName("China").getD_id());
    }
    /**
     * This method tests the remove neighbor operation
     * @throws IOException exception
     * @throws InvalidMap invalid map exception
     */
    @Test (expected = InvalidMap.class)
    public void testRemoveNeighbors() throws IOException, InvalidMap{
        Map l_newMap=new Map();
        d_GameState.setD_map(l_newMap);
		d_MapHelper.editContinent(d_GameState, "Asia 5", "add");
		d_MapHelper.editCountry(d_GameState, "India Asia", "add");
		d_MapHelper.editCountry(d_GameState, "China Asia", "add");
		d_MapHelper.editNeighbour(d_GameState, "India China", "add");
        d_MapHelper.editNeighbour(d_GameState, "China India", "remove");
    }
    
}
