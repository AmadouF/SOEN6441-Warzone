package Views;

import java.util.*;

import Constants.Constants;
import Exceptions.InvalidMap;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Player;
import Models.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.davidmoten.text.utils.WordWrap;

/**
 *  MapView Class handles printing the game state to the console.
 */
public class MapView {
    /**
     * list of players in the game
     */
    private List<Player> d_players;

    /**
     * current state of the game.
     */
    private GameState d_gameState;

    /**
     * object of the map class
     */
    private Map d_map;

    /**
     * List of countries in the map.
     */
    private List<Country> d_countries;

    /**
     * List of continents in the map.
     */
    private List<Continent> d_continents;

    /**
     * Constructor to initialise MapView.
     *
     * @param p_gameState Current GameState.
     */
    public MapView(GameState p_gameState) {
        this.d_gameState = p_gameState;
        this.d_map = p_gameState.getD_map();
        this.d_countries = d_map.getCountriesList();
        this.d_continents = d_map.getContinentsList();
    }

    /**
     * Constructor to initialise MapView with Players.
     *
     * @param p_gameState Current GameState
     * @param p_players List of Player Objects
     */
    public MapView(GameState p_gameState, List<Player> p_players) {
        this.d_gameState = p_gameState;
        this.d_map = p_gameState.getD_map();
        this.d_players = p_players;
        this.d_countries = d_map.getCountriesList();
        this.d_continents = d_map.getContinentsList();
    }

    /**
     * Prints separators for heading
     */
    private void printSeparator() {
        System.out.format("+%s+%n", "-".repeat(Constants.CONSOLE_WIDTH - 2));
    }

    /**
     * Prints the Center String for Heading.
     *
     * @param p_width Defined width in formatting.
     * @param p_str String to be rendered.
     */
    private void printCenteredString(int p_width, String p_str) {
        String l_centeredString = String.format("%-" + p_width + "s", String.format("%" + (p_str.length() + (p_width - p_str.length()) / 2) + "s", p_str));

        System.out.format(l_centeredString + "\n");
    }

    /**
     * Prints the continent Name with formatted centered string and separator.
     *
     * @param p_continentName Continent Name to be rendered.
     */
    private void printContinentName(String p_continentName) {
        String l_continentName = p_continentName + " ( Bonus : " + d_gameState.getD_map().getContinent(p_continentName).getD_value() + " )";

        printSeparator();
        printCenteredString(Constants.CONSOLE_WIDTH, l_continentName);
        printSeparator();
    }

    /**
     * Returns the Country Name as Formatted.
     *
     * @param p_index Index of Countries.
     * @param p_countryName Country Name to be rendered.
     *
     * @return Returns the Formatted String
     */
    private String getFormattedCountryName(int p_index, String p_countryName) {
        // Prefixing one digit number with 0 to get double digits
        String l_indexedString = String.format("%02d. %s", p_index, p_countryName);

        if(d_players != null) {
            String l_armies = "( Armies :" + getCountryArmies(p_countryName) + " )";
            l_indexedString = String.format("%02d. %s %s", p_index, p_countryName, l_armies);
        }

        return String.format("%-30s", l_indexedString);
    }

    /**
     * Prints Adjacent Countries in Formatted Settings.
     *
     * @param p_adjCountries List of adjacent countries to be rendered.
     */
    private void printFormattedAdjacentCountryName(List<Country> p_adjCountries){
        StringBuilder l_commaSeparatedCountries = new StringBuilder();

        // Creating comma separated countries
        for(int i = 0; i < p_adjCountries.size(); i++) {
            l_commaSeparatedCountries.append(p_adjCountries.get(i).getD_name());

            if(i < p_adjCountries.size() - 1)
                l_commaSeparatedCountries.append(", ");
        }
        String l_adjacentCountry = "Connections : " + WordWrap.from(l_commaSeparatedCountries.toString()).maxWidth(Constants.CONSOLE_WIDTH).wrap();
        System.out.println(l_adjacentCountry);
        System.out.println();
    }

    /**
     * Prints the Player details in formatted settings.
     *
     * @param p_index Index of the Player
     * @param p_player Player Object
     */
    private void printPlayerDetails(Integer p_index, Player p_player) {
        String l_playerDetails = String.format("%02d. %-8s", p_index, p_player.getPlayerName());
        System.out.println(l_playerDetails);
    }

    /**
     * Prints the Players in Formatted Settings.
     */
    private void printPlayers() {
        int l_count = 0;

        printSeparator();
        printCenteredString(Constants.CONSOLE_WIDTH, "Players in the Game");
        printSeparator();

        for (Player player: d_players) {
            l_count++;
            printPlayerDetails(l_count, player);
        }
    }

    /**
     * Gets the number of armies for a country.
     *
     * @param p_countryName name of the country
     *
     * @return number of armies
     */
    private Integer getCountryArmies(String p_countryName) {
        Integer l_armies = d_gameState.getD_map().getCountryByName(p_countryName).getD_army();

        if (l_armies == null) {
            return 0;
        }

        return l_armies;
    }

    /**
	 * This method displays the list of continents and countries present in the map files
     * alongside current state of the game.
     */
    public void showMap() {
        if (d_players != null) {
            printPlayers();
        }

        if(CollectionUtils.isNotEmpty(d_continents)) {
            d_continents.forEach(l_continent -> {
                printContinentName(l_continent.getD_name());

                List<Country> l_continentCountries = l_continent.getD_countries();
                final int[] l_countryIndex = { 1 };

                if(CollectionUtils.isNotEmpty(l_continentCountries)) {
                    l_continentCountries.forEach((l_country) -> {
                        String l_formattedCountryName = getFormattedCountryName(l_countryIndex[0]++, l_country.getD_name());
                        System.out.println(l_formattedCountryName);

                        try {
                            List<Country> l_adjCountries = d_map.getAdjacentCountry(l_country);

                            printFormattedAdjacentCountryName(l_adjCountries);
                        } catch (InvalidMap l_invalidMap) {
                            System.out.println(l_invalidMap.getMessage());
                        }
                    });
                } else {
                    System.out.println("There is no country in the continent!");
                }
            });
        } else {
            System.out.println("There is no continents!");
        }
    }
}