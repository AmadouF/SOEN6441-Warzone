package Models;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class tests the blockade order.
 */
public class BlockadeTest {
    /**
     * Player 1
     */
    Player d_player1;
    /**
     * Player 2
     */
    Player d_player2;
    /**
     * Neutral Player
     */
    Player d_playerNeutral;
    /**
     * Blockage order 1
     */
    Blockade d_blockade1;
    /**
     * Blockage order 2
     */
    Blockade d_blockade2;
    /**
     * Blockage order 3
     */
    Blockade d_blockade3;
    /**
     * List of orders
     */
    List<Order> d_list;
    /**
     * Current game state
     */
    GameState d_gameState;

    /**
     * Executes before every test case.
     */
    @Before
    public void setup() {
        d_gameState = new GameState();
        d_list = new ArrayList<Order>();
        d_player1 = new Player("devanshu");
        d_player2 = new Player("avi");
        d_playerNeutral = new Player("neutral");

        List<Country> l_countryList = new ArrayList<Country>();
        l_countryList.add(new Country(1,1,"China"));
        l_countryList.add(new Country(2,2,"India"));
        d_player1.setOwnedCountries(l_countryList);
        d_player2.setOwnedCountries(l_countryList);
        d_playerNeutral.setOwnedCountries(l_countryList);

        List<Country> l_mapCountries = new ArrayList<Country>();
        Country l_country1 = new Country(1,  1,"Canada");
        Country l_country2 = new Country(2, 2, "India");
        l_country1.setD_army(10);
        l_country2.setD_army(5);

        l_mapCountries.add(l_country1);
        l_mapCountries.add(l_country2);

        Map l_map = new Map();
        l_map.setCountries(l_mapCountries);
        d_gameState.setD_map(l_map);

        List<Player> l_playerList = new ArrayList<Player>();
        l_playerList.add(d_playerNeutral);
        d_gameState.setD_players(l_playerList);

        d_blockade1 = new Blockade(d_player1, "India");
        d_blockade2 = new Blockade(d_player1, "USA");

        d_list.add(d_blockade1);
        d_list.add(d_blockade2);

        d_player2.setIssuedOrders(d_list);
        d_blockade3 = new Blockade(d_player2, "India");

    }

    /**
     * Test Blockade order execution.
     */
    @Test
    public void testBlockadeExecution() {
        d_blockade1.execute(d_gameState);
        Country l_countryIndia = d_gameState.getD_map().getCountryByName("India");
        assertEquals("15", l_countryIndia.getD_army().toString());
    }

    /**
     * Test Validation of Blockade Order.
     */
    @Test
    public void testValidBlockadeOrder() {

        boolean l_actualBoolean = d_blockade1.isValid(d_gameState);
        assertTrue(l_actualBoolean);
        boolean l_actualBoolean2 = d_blockade2.isValid(d_gameState);
        assertFalse(l_actualBoolean2);

    }
}
