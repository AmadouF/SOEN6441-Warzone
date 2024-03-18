package Models;

import Exceptions.InvalidMap;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * This class tests the airlift model.
 */
public class AirliftTest {
    /**
     * Player
     */
    Player d_player;
    /**
     * Airlift order
     */
    Airlift d_airlift;
    /**
     * Current game state
     */
    GameState d_gameState;

    /**
     * Setup before every test case
     * @throws InvalidMap Invalid map exception
     */
    @Before
    public void setup() throws InvalidMap {
        d_gameState = new GameState();
        d_player = new Player("test");
        d_player.setPlayerName("devanshu");

        List<Country> l_countryList = new ArrayList<Country>();
        Country l_country = new Country(0, 1, "India");
        l_country.setD_army(9);
        l_countryList.add(l_country);

        Country l_countryNeighbour = new Country(1, 1, "China");
        l_countryNeighbour.addAdjacentCountry(0);
        l_country.addAdjacentCountry(1);
        l_countryNeighbour.setD_army(10);
        l_countryList.add(l_countryNeighbour);

        Country l_countryNotNeighbour = new Country(2, 1, "Malaysia");
        l_countryNotNeighbour.setD_army(15);
        l_countryList.add(l_countryNotNeighbour);

        Map l_map = new Map();
        l_map.setCountries(l_countryList);

        d_gameState.setD_map(l_map);
        d_player.setOwnedCountries(l_countryList);
        d_airlift = new Airlift("India", "Malaysia", d_player, 2);
    }

    /**
     * Tests the execution of airlift order.
     */
    @Test
    public void testAirliftExecution() {
        d_airlift.execute(d_gameState);
        Country l_countryMalaysia = d_gameState.getD_map().getCountryByName("Malaysia");
        assertEquals("17", l_countryMalaysia.getD_army().toString());
    }

    /**
     * Tests the invalid order
     */
    @Test
    public void testInvalidAirLift() {
        d_airlift = new Airlift("France", "India", d_player, 1);

        assertFalse(d_airlift.checkValidOrder(d_gameState));
    }

}
