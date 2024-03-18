package Models;

import Exceptions.InvalidMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * This class tests the functionalities of Diplomacy class
 */
public class DiplomacyTest {
    /**
     * First Player.
     */
    Player d_player1;

    /**
     * Second Player
     */
    Player d_player2;

    /**
     * Bomb Order.
     */
    Bomb d_bombOrder;

    /**
     * diplomacy order.
     */
    Diplomacy d_diplomacyOrder;

    /**
     * country to shift armies from.
     */
    Airlift d_invalidAirLift1;

    /**
     * Game State.
     */
    GameState d_gameState;

    /**
     * Setup before the tests.
     *
     * @throws InvalidMap Exception
     */
    @Before
    public void setup() throws InvalidMap {
        d_gameState = new GameState();
        d_player1 = new Player("Jay");
        d_player2 = new Player("Patel");


        List<Country> l_countryList = new ArrayList<>();
        Country l_country = new Country(1, 1, "India");
        l_country.setD_army(9);
        l_countryList.add(l_country);

        Country l_countryNeighbour =new Country(2, 1, "Nepal");
        l_countryNeighbour.addAdjacentCountry(1);
        l_country.addAdjacentCountry(2);
        l_countryNeighbour.setD_army(10);
        l_countryList.add(l_countryNeighbour);

        List<Country> l_countryList2 = new ArrayList<Country>();
        Country l_countryNotNeighbour = new Country(3, 1, "India");
        l_countryNotNeighbour.setD_army(15);
        l_countryList2.add(l_countryNotNeighbour);

        Map l_map = new Map();
        l_map.setCountries(new ArrayList<Country>(){{ addAll(l_countryList); addAll(l_countryList2); }});

        d_gameState.setD_map(l_map);
        d_player1.setOwnedCountries(l_countryList);
        d_player2.setOwnedCountries(l_countryList2);

        List<Player> l_playerList = new ArrayList<Player>();
        l_playerList.add(d_player1);
        l_playerList.add(d_player2);
        d_gameState.setD_players(l_playerList);

        d_diplomacyOrder = new Diplomacy(d_player1, d_player2.getPlayerName());
        d_bombOrder = new Bomb(d_player2, "Nepal");
    }

    /**
     * Tests if diplomacy works or not
     */
    @Test
    public void testDiplomacyExecution(){
        d_diplomacyOrder.execute(d_gameState);

        Assert.assertEquals(d_player1.d_negotiatedPlayers.get(0), d_player2);
    }

    /**
     * Tests the bomb order after negotiation if it works.
     */
    @Test
    public void NegotiationTesting(){
        d_diplomacyOrder.execute(d_gameState);

        d_bombOrder.execute(d_gameState);

        // Checking if bombing worked or not
        Country targetCountry = d_gameState.getD_map().getCountryByName(d_bombOrder.d_targetCountryName);
        Assert.assertNotEquals("5", targetCountry.getD_army());
    }
}
