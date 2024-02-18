package Models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to test functionality of Order Class
 */
public class OrderTest {
    /**
     * Order class reference
     */
    Order d_order;

    /**
     * Player class reference
     */
    Player d_player;

    /**
     * Setup is called before each test case of this class
     */
    @Before
    public void setup() {
        d_order = new Order();
        d_player = new Player("Test");
    }

    /**
     * Used to test country name entered by player in deploy command
     * Checks if entered country name belongs to player or not. If it does not belong to
     * player, order will be aborted.
     */
    @Test
    public void testValidateOrderCountry() {
        d_order.setD_targetCountryName("India");
        List<Country> l_countries = new ArrayList<Country>();

        l_countries.add(new Country(1, 1, "India"));
        l_countries.add(new Country(2, 1, "Canada"));
        d_player.setOwnedCountries(l_countries);

        boolean l_isValid = d_order.validateOrderCountry(d_player, d_order);

        Assert.assertTrue(l_isValid);
    }

    /**
     * Used to test execution of deploy order and check if appropriate armies are
     * deployed at country object or not
     */
    @Test
    public void testExecuteDeployOrder() {
        Order l_order1 = new Order("deploy", "India", 1);
        Order l_order2 = new Order("deploy", "Canada", 2);

        Player l_player = new Player("Test");

        List<Country> l_playerCountries = new ArrayList<Country>();
        l_playerCountries.add(new Country(1, 1, "India"));
        l_playerCountries.add(new Country(2, 1, "Canada"));
        l_player.setOwnedCountries(l_playerCountries);

        List<Country> l_mapCountries = new ArrayList<Country>();
        Country l_country1 = new Country(1, 1, "India");
        l_country1.setD_army(5);
        Country l_country2 = new Country(2, 1, "Canada");
        Country l_country3 = new Country(3, 1, "Israel");

        l_mapCountries.add(l_country1);
        l_mapCountries.add(l_country2);
        l_mapCountries.add(l_country3);

        Map l_map = new Map();
        l_map.setCountries(l_mapCountries);

        GameState l_gameState = new GameState();
        l_gameState.setD_map(l_map);

        l_order1.execute(l_gameState, l_player);
        Country l_countryIndia = l_gameState.getD_map().getCountryByName("India");
        Assert.assertEquals("10", l_countryIndia.getD_army().toString());

        l_order2.execute(l_gameState, l_player);
        Country l_countryCanada = l_gameState.getD_map().getCountryByName("Canada");
        Assert.assertEquals("2", l_countryCanada.getD_army().toString());
    }
}
