package Models;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

/**
 * This class tests the sequential execution of orders.
 */
public class PlayerTest {
    /**
     * Orders list
     */
    List<Order> d_order_list = new ArrayList<Order>();

    /**
     * Player object
     */
    Player d_player = new Player("dev");

    /**
     * Current State of the Game.
     */
    GameState l_gamestate = new GameState();
    /**
     * List of players
     */
    List<Player> d_playersList=new ArrayList<Player>();
    /**
     * Initial method to add players
     */
    @Before
    public void init(){
        d_playersList.add(new Player("Devanshu"));
        d_playersList.add(new Player("John"));
        Map l_map = new Map();
        Country l_c1 = new Country(1, 10, "India");
        l_c1.setD_adjacentCountryIds(Arrays.asList(2));
        Country l_c2 = new Country(2, 10, "China");
        List<Country> l_countryList = new ArrayList<>();
        l_countryList.add(l_c1);
        l_countryList.add(l_c2);
        l_map.setCountries(l_countryList);
        l_gamestate.setD_map(l_map);
    }
//    /**
//     * This method tests the fetching of the next order.
//     */
//    @Test
//    public void testNextExecution(){
//        Order l_order=new Order();
//        l_order.setD_orderAction("deploy");
//        l_order.setD_targetCountryName("France");
//        l_order.setD_numberOfArmiesToDeploy(5);
//
//        Order l_newOrder=new Order();
//        l_newOrder.setD_orderAction("deploy");
//        l_newOrder.setD_targetCountryName("Spain");
//        l_newOrder.setD_numberOfArmiesToDeploy(2);
//
//        List<Order> l_orderlist = new ArrayList<>();
//		l_orderlist.add(l_order);
//		l_orderlist.add(l_newOrder);
//
//		d_playersList.get(0).setIssuedOrders(l_orderlist);
//		Order l_currOrder = d_playersList.get(0).next_order();
//
//		assertEquals(l_currOrder, l_order);
//		assertEquals(d_playersList.get(0).getIssuedOrders().size(), 1);
//    }
     /**
     * This method tests the fetching of the next order.
     */
    @Test
    public void testNextOrder() {

        Order l_deployOrder1 = new Deploy(d_playersList.get(0), "India", 5);
        Order l_deployOrder2 = new Deploy(d_playersList.get(1), "China", 6);

        d_order_list.add(l_deployOrder1);
        d_order_list.add(l_deployOrder2);

        d_playersList.get(0).setIssuedOrders(d_order_list);
        d_playersList.get(1).setIssuedOrders(d_order_list);

        Order l_order = d_playersList.get(0).next_order();
        assertEquals(l_deployOrder1, l_order);
        assertEquals(1, d_playersList.get(0).getIssuedOrders().size());
    }

    /**
     * Validates the deploy armies.
     */
    @Test
    public void testValidateDeployOrderArmies() {
        d_player.setReinforcement(10);
        String l_noOfArmies = "4";

        assertTrue(d_player.isValidArmies(d_player, l_noOfArmies));
    }

    /**
     * Tests the creation of deploy order.
     */
    @Test
    public void testCreateDeployOrder() {
        Player l_pl = new Player("abc");
        l_pl.setReinforcement(20);
        l_pl.createDeployOrder("Deploy India 5");
        assertEquals(l_pl.getReinforcements().toString(), "15");
        assertEquals(l_pl.getIssuedOrders().size(), 1);
    }

    /**
     * Checks whether the country exists or not.
     */
    @Test
    public void testCountryExists() {
        Player l_player = new Player("abc");
        assertTrue(l_player.checkAdjacency(l_gamestate, "India", "China"));
        assertFalse(l_player.checkAdjacency(l_gamestate, "China", "India"));
    }

    /**
     * Tests the creation of Advance order.
     */
    @Test
    public void testCreateAdvanceOrder() {
        Player l_player = new Player("xyz");
        l_player.createAdvanceOrder("advance India China 10", l_gamestate);
        assertEquals(l_player.getIssuedOrders().size(), 1);
    }

    /**
     * Tests the failure of creation of Advance order.
     */
    @Test
    public void testCreateAdvanceOrderFailure() {
        Player l_player = new Player("xyz");
        l_player.createAdvanceOrder("advance India China 0", l_gamestate);
        assertEquals(l_player.getIssuedOrders().size(), 0);
    }
}
