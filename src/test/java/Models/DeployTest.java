package Models;

import Exceptions.InvalidCommand;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class tests the functionalities of Deploy class
 */
public class DeployTest {
    /**
     * First Player
     */
    Player d_player1;

    /**
     * Second Player
     */
    Player d_player2;

    /**
     * First Deploy Order.
     */
    Deploy d_deployOrder1;

    /**
     * Second Deploy Order.
     */
    Deploy d_deployOrder2;

    /**
     * State of the game.
     */
    GameState d_gameState = new GameState();

    /**
     * The setup is called before each test case of this class is executed.
     */
    @Before
    public void setup() {
        d_player1 = new Player("Jay");
        d_player2 = new Player("Patel");

        List<Country> l_countries = new ArrayList<Country>();

        l_countries.add(new Country(1, 1, "India"));
        l_countries.add(new Country(2, 2, "Canada"));
        d_player1.setOwnedCountries(l_countries);
        d_player2.setOwnedCountries(l_countries);

        List<Country> l_mapCountries = new ArrayList<Country>();
        Country l_country1 = new Country(1, 1, "India");
        Country l_country2 = new Country(2, 2, "Canada");
        l_country2.setD_army(5);

        l_mapCountries.add(l_country1);
        l_mapCountries.add(l_country2);

        Map l_map = new Map();
        l_map.setCountries(l_mapCountries);
        d_gameState.setD_map(l_map);

        d_deployOrder1 = new Deploy(d_player1, "India", 5);
        d_deployOrder2 = new Deploy(d_player2, "Canada", 15);
    }

    /**
     * Checks if the country belongs to the player or not
     */
    @Test
    public void testValidateDeployOrderCountry() {
        boolean l_actualBoolean = d_deployOrder1.isValid(d_gameState);
        Assert.assertTrue(l_actualBoolean);

        boolean l_actualBoolean2 = d_deployOrder2.isValid(d_gameState);
        Assert.assertTrue(l_actualBoolean2);
    }

    /**
     * Used to test execution of deploy order and check if right number of armies are
     * deployed at country level or not.
     */
    @Test
    public void testDeployOrderExecution() {
        d_deployOrder1.execute(d_gameState);

        Country l_countryIndia = d_gameState.getD_map().getCountryByName("India");
        Assert.assertNotEquals("10", l_countryIndia.getD_army().toString());

        d_deployOrder2.execute(d_gameState);
        Country l_countryCanada = d_gameState.getD_map().getCountryByName("Canada");
        Assert.assertEquals("20", l_countryCanada.getD_army().toString());
    }

    /**
     * Tests deploy order logic to see if required order is created and armies are
     * re-calculated.
     *
     * @throws InvalidCommand if given command is invalid
     */
    @Test
    public void testDeployOrder() throws InvalidCommand {
        Player l_player = new Player("John");
        l_player.setReinforcement(10);

        Country l_country = new Country(3, 1, "Nepal");
        l_player.setOwnedCountries(List.of(l_country));

        l_player.createDeployOrder("deploy Nepal 4");

        Assert.assertEquals("6", l_player.getReinforcements().toString());
        Assert.assertEquals(l_player.getIssuedOrders().size(), 1);

        Deploy l_order = (Deploy) l_player.getIssuedOrders().get(0);

        Assert.assertEquals("Nepal", l_order.d_targetCountryName);
        Assert.assertEquals("4", String.valueOf(l_order.d_numberOfArmiesToDeploy));
    }
}
