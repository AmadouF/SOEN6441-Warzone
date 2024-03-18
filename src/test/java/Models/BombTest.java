package Models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to test functionality of Bomb class
 */
public class BombTest {
    /**
     * Player 1
     */
    Player d_player1;

    /**
     * Player 2
     */
    Player d_player2;

    /**
     * Bomb Order 1
     */
    Bomb d_bombOrder1;

    /**
     * Bomb Order 2
     */
    Bomb d_bombOrder2;

    /**
     * Bomb Order 3
     */
    Bomb d_bombOrder3;

    /**
     * Bomb Order 4
     */
    Bomb d_bombOrder4;

    Order deployOrder;

    /**
     * name of the target country.
     */
    String d_targetCountry;

    /**
     * list of orders.
     */
    List<Order> d_order_list;

    /**
     * Game State object.
     */
    GameState d_gameState;

    /**
     * Setup before each test case.
     */
    @Before
    public void setup() {
        d_gameState = new GameState();
        d_order_list = new ArrayList<>();

        d_player1 = new Player("Jay");
        d_player2 = new Player("Patel");

        List<Country> l_countryList = new ArrayList<Country>();
        l_countryList.add(new Country(1, 1, "India"));
        l_countryList.add(new Country(1, 1, "Russia"));
        d_player1.setOwnedCountries(l_countryList);
        d_player2.setOwnedCountries(l_countryList);

        List<Country> l_mapCountries = new ArrayList<Country>();
        Country l_country1 = new Country(1, 1, "India");
        Country l_country2 = new Country(2, 1, "Russia");
        Country l_country3 = new Country(3, 1, "Nepal");
        Country l_country4 = new Country(4, 1, "Bhutan");
        Country l_country5 = new Country(5, 1, "Ukraine");

        l_country3.setD_army(4);
        l_country4.setD_army(15);
        l_country5.setD_army(1);

        l_mapCountries.add(l_country1);
        l_mapCountries.add(l_country2);
        l_mapCountries.add(l_country3);
        l_mapCountries.add(l_country4);
        l_mapCountries.add(l_country5);

        Map l_map = new Map();
        l_map.setCountries(l_mapCountries);
        d_gameState.setD_map(l_map);

        d_bombOrder1 = new Bomb(d_player1, "Nepal");
        d_bombOrder2 = new Bomb(d_player1, "India");
        d_bombOrder3 = new Bomb(d_player1, "Bhutan");
        d_bombOrder4 = new Bomb(d_player1, "Ukraine");

        d_order_list.add(d_bombOrder1);
        d_order_list.add(d_bombOrder2);

        d_player2.setIssuedOrders(d_order_list);
    }

    /**
     * Test Bomb Card Execution
     */
    @Test
    public void testBombExecution() {
        // Test calculation of half armies.
        d_bombOrder1.execute(d_gameState);
        Country l_targetCountry = d_gameState.getD_map().getCountryByName("Nepal");
        Assert.assertEquals("2", l_targetCountry.getD_army().toString());

        // Test round down of armies calculation.
        d_bombOrder3.execute(d_gameState);
        Country l_targetCountry2 = d_gameState.getD_map().getCountryByName("Bhutan");
        Assert.assertEquals("7", l_targetCountry2.getD_army().toString());

        // targeting a territory with 1 army will leave 0.
        d_bombOrder4.execute(d_gameState);
        Country l_targetCountry3 = d_gameState.getD_map().getCountryByName("Ukraine");
        Assert.assertEquals("0", l_targetCountry3.getD_army().toString());
    }

    /**
     * Test validation of bomb order
     */
    @Test
    public void testValidBombOrder() {
        // Player cannot bomb own territory
        boolean l_actualBoolean = d_bombOrder1.isValid(d_gameState);
        Assert.assertTrue(l_actualBoolean);

        // fail if target country is owned by player
        boolean l_actualBoolean1 = d_bombOrder2.isValid(d_gameState);
        Assert.assertFalse(l_actualBoolean1);
    }
}
