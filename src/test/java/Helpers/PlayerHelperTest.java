package Helpers;

import Exceptions.InvalidCommand;
import Models.*;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to test functionalities of PlayerHelper class
 */
public class PlayerHelperTest {
    /**
     * Player class object.
     */
    Player d_player;

    /**
     * Player Helper object.
     */
    PlayerHelper d_playerHelper;

    /**
     * Map class object.
     */
    Map d_map;

    /**
     * GameState class object.
     */
    GameState d_gameState;

    /**
     * MapService class object.
     */
    MapHelper d_mapHelper;

    /**
     * Existing Player List.
     */
    List<Player> d_existingPlayers = new ArrayList<Player>();


    private final ByteArrayOutputStream d_outContent = new ByteArrayOutputStream();

    /**
     * The setup is called before each test case of this class.
     */
    @Before
    public void setup() {
        d_player = new Player("Test");
        d_playerHelper = new PlayerHelper();
        d_gameState = new GameState();

        d_existingPlayers.add(new Player("John"));
        d_existingPlayers.add(new Player("Cena"));
    }

    /**
     * The testAddPlayer is used to test the add functionality of addOrRemovePlayers.
     */
    @Test
    public void testAddPlayer () {
        Assert.assertFalse(CollectionUtils.isEmpty(d_existingPlayers));

        List<Player> l_updatedPlayers = d_playerHelper.addOrRemovePlayers(d_existingPlayers, "add", "Jay");
        Assert.assertEquals("Jay", l_updatedPlayers.get(2).getPlayerName());

        System.setOut(new PrintStream(d_outContent));
        d_playerHelper.addOrRemovePlayers(d_existingPlayers, "add", "John");

        Assert.assertEquals("Player with name : John already Exists. Aborted.", d_outContent.toString().trim());
    }

    /**
     * The testRemovePlayer is used to test the remove functionality of addOrRemovePlayers
     */
    @Test
    public void testRemovePlayer() {
        List<Player> l_updatedPlayers = d_playerHelper.addOrRemovePlayers(d_existingPlayers, "remove", "John");
        Assert.assertEquals(1, l_updatedPlayers.size());

        System.setOut(new PrintStream(d_outContent));
        d_playerHelper.addOrRemovePlayers(d_existingPlayers, "remove", "Jay");
        Assert.assertEquals("Player with name : Jay does not Exist. Aborted.", d_outContent.toString().trim());
    }

    /**
     * Used to check if players are present in the game or not
     */
    @Test
    public void testCheckPlayersAvailability() {
        boolean l_playersExists = d_playerHelper.arePlayersAvailabe(d_gameState);
        Assert.assertFalse(l_playersExists);
    }

    /**
     * Used to test assignCountries to the player
     */
    @Test
    public void testPlayerCountryAssignment() {
        d_mapHelper = new MapHelper();
        d_map = new Map();
        d_map = d_mapHelper.load(d_gameState, "canada.map");
        d_gameState.setD_map(d_map);
        d_gameState.setD_players(d_existingPlayers);
        d_playerHelper.assignCountries(d_gameState);

        int l_assignedCountriesSize = 0;
        for (Player l_pl : d_gameState.getD_players()) {
            Assert.assertNotNull(l_pl.getOwnedCountries());
            l_assignedCountriesSize += l_pl.getOwnedCountries().size();
        }

        Assert.assertEquals(l_assignedCountriesSize, d_gameState.getD_map().getCountriesList().size());
    }

    /**
     * Used to check calculateArmies for each player
     */
    @Test
    public void testCalculateArmiesForPlayer() {
        Player l_playerInfo = new Player("Test");
        List<Country> l_countries = new ArrayList<Country>();
        l_countries.add(new Country(1, 1, "Waadt"));
        l_countries.add(new Country(2, 1, "Neuenburg"));
        l_countries.add(new Country(3, 1, "Fribourg"));
        l_countries.add(new Country(4, 1, "Geneve"));

        l_playerInfo.setOwnedCountries(l_countries);

        List<Continent> l_continents = new ArrayList<Continent>();
        l_continents.add(new Continent(1, "Asia", 5));

        l_playerInfo.setOwnedContinents(l_continents);

        l_playerInfo.setReinforcement(10);

        Integer l_actualResult = d_playerHelper.calculateArmiesForPlayer(l_playerInfo);
        Integer l_expectedResult = 8;

        Assert.assertEquals(l_expectedResult, l_actualResult);
    }
}
