package Models;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to test functionalities of Advance order
 */
public class AdvanceTest {
    /**
     * State of the game
     */
    GameState d_gameState = new GameState();

    /**
     * Checks whether advance order is invalid.
     */
    @Test
    public void testInvalidAdvanceOrder() {
        Player l_player = new Player("Jay");
        Country l_country1 = new Country(1, 1, "India");
        l_country1.setD_army(10);

        Country l_country2 = new Country(2, 1, "Canada");
        l_country2.setD_army(8);

        List<Country> l_countries = Arrays.asList(l_country1, l_country2);
        l_player.setOwnedCountries(l_countries);

        Advance order1 = new Advance(l_player, "India", "France", 15);
        Assert.assertFalse(order1.isValid(d_gameState));

        Advance order2 = new Advance(l_player, "Canada", "France", 15);
        Assert.assertFalse(order2.isValid(d_gameState));

        Advance order3 = new Advance(l_player, "Russia", "France", 15);
        Assert.assertFalse(order3.isValid(d_gameState));

        Advance order4 = new Advance(l_player, "India", "Canada", 15);
        Assert.assertFalse(order4.isValid(d_gameState));
    }

    /**
     * Checks if after Attacker has won battle,
     * countries and armies are updated correctly
     */
    @Test
    public void testAttackerWon() {
        Player l_sourcePlayer = new Player("Jay");
        Country l_country1 = new Country(1, 1, "India");
        l_country1.setD_army(7);

        List<Country> l_s1 = new ArrayList<Country>();
        l_s1.add(l_country1);
        l_sourcePlayer.setOwnedCountries(l_s1);

        Player l_targetPlayer = new Player("Patel");
        Country l_country2 = new Country(2, 1, "Canada");
        l_country2.setD_army(4);

        List<Country> l_s2 = new ArrayList<>();
        l_s2.add(l_country2);
        l_targetPlayer.setOwnedCountries(l_s2);

        Advance l_advance = new Advance(l_sourcePlayer, "India", "Canada", 5);
        l_advance.handleSurvivingArmies(5, 0, l_country1, l_country2, l_targetPlayer);

        Assert.assertEquals(0, l_targetPlayer.getOwnedCountries().size());
        Assert.assertEquals(2, l_sourcePlayer.getOwnedCountries().size());

        Assert.assertEquals("5", l_sourcePlayer.getOwnedCountries().get(1).getD_army().toString());
    }

    /**
     * Checks if Defender won the battle,
     * countries and armies are updated correctly
     */
    @Test
    public void testDefendersWin() {
        Player l_sourcePlayer = new Player("Jay");
        Country l_country1 = new Country(1, 1, "India");
        l_country1.setD_army(2);

        List<Country> l_s1 = new ArrayList<>();
        l_s1.add(l_country1);
        l_sourcePlayer.setOwnedCountries(l_s1);

        Player l_targetPlayer = new Player("Mike");
        Country l_country2 = new Country(2, 1, "Canada");
        l_country2.setD_army(4);

        List<Country> l_s2 = new ArrayList<>();
        l_s2.add(l_country2);
        l_targetPlayer.setOwnedCountries(l_s2);

        Advance l_advance = new Advance(l_sourcePlayer, "India", "Canada", 5);
        l_advance.handleSurvivingArmies(3, 2, l_country1, l_country2, l_targetPlayer);

        Assert.assertEquals(1, l_targetPlayer.getOwnedCountries().size());
        Assert.assertEquals(1, l_sourcePlayer.getOwnedCountries().size());
        Assert.assertEquals("5", l_sourcePlayer.getOwnedCountries().get(0).getD_army().toString());
        Assert.assertEquals("2", l_targetPlayer.getOwnedCountries().get(0).getD_army().toString());
    }

    /**
     * Checks if armies are deployed to target
     * because target country is also owned by initiator
     */
    @Test
    public void testDeployToTarget() {
        Player l_sourcePlayer = new Player("Jay");
        List<Country> l_s1 = new ArrayList<>();

        Country l_country1 = new Country(1, 1, "India");
        l_country1.setD_army(7);
        l_s1.add(l_country1);

        Country l_country2 = new Country(2, 1, "Canada");
        l_country2.setD_army(4);
        l_s1.add(l_country2);

        l_sourcePlayer.setOwnedCountries(l_s1);

        Advance l_advance = new Advance(l_sourcePlayer, "India", "Canada", 2);
        l_advance.deployArmiesToTarget(l_country2);

        Assert.assertEquals(l_country2.getD_army().toString(), "6");
    }
}
