package Views;

import java.util.*;

import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Player;
import Models.Map;
import org.davidmoten.text.utils.WordWrap;

public class MapView {
    private List<Player> d_players;
    private GameState d_gameState;
    private Map d_map;
    private List<Country> d_countries;
    private List<Continent> d_continents;

    public final int CONSOLE_WIDTH = 80;
    public final String CONTROL_VALUE = "Control Value";

    // Expects GameState Controller and related methods.
    public MapView(GameState p_gameState) {
        this.d_gameState = p_gameState;
        this.d_map = p_gameState.getD_map();
        this.d_countries = d_map.getCountriesList();
        this.d_continents = d_map.getContinentsList();
    }

    // Expects GameState Controller and related methods.
    public MapView(GameState p_gameState, List<Player> p_players) {
        this.d_gameState = p_gameState;
        this.d_map = p_gameState.getD_map();
        this.d_players = p_players;
        this.d_countries = d_map.getCountriesList();
        this.d_continents = d_map.getContinentsList();
    }

    private void printSeparator() {
        System.out.format("+%s+%n", "-".repeat(CONSOLE_WIDTH - 2));
    }

    private void printCenteredString(int p_width, String p_str) {
        String l_centeredString = String.format("%-" + p_width + "s", String.format("%" + (p_str.length() + (p_width - p_str.length()) / 2) + "s", p_str));

        System.out.format(l_centeredString + "\n");
    }

    private void printContinentName(String p_continentName) {
        String l_continentName = p_continentName + " ( " + CONTROL_VALUE + " : " + d_gameState.getD_map().getContinent(p_continentName).getD_continentValue() + " )";

        printSeparator();
        printCenteredString(CONSOLE_WIDTH, l_continentName);
        printSeparator();
    }

    private String getFormattedCountryName(int p_index, String p_countryName) {
        String l_indexedString = String.format("%02d. %s", p_index, p_countryName);

        if(d_players != null) {
            String l_armies = "( Armies :" + getCountryArmies(p_countryName) + " )";
            l_indexedString = String.format("%02d. %s %s", p_index, p_countryName, l_armies);
        }

        return String.format("%-30s", l_indexedString);
    }

    private void printFormattedAdjacentCountryName(List<Country> p_adjCountries){
        StringBuilder l_commaSeparatedCountries = new StringBuilder();

        for(int i = 0; i < p_adjCountries.size(); i++) {
            l_commaSeparatedCountries.append(p_adjCountries.get(i).getD_name());
            if(i < p_adjCountries.size() - 1)
                l_commaSeparatedCountries.append(", ");
        }
        String l_adjacentCountry = "Connections : "+ WordWrap.from(l_commaSeparatedCountries.toString()).maxWidth(CONSOLE_WIDTH).wrap();
        System.out.println(l_adjacentCountry);
        System.out.println();
    }

    private void printPlayerDetails(Integer p_index, Player p_player) {
        String l_playerDetails = String.format("%02d. %-8s", p_index, p_player.getPlayerName());
        System.out.println(l_playerDetails);
    }

    private void printPlayers() {
        int l_count = 0;

        printSeparator();
        printCenteredString(CONSOLE_WIDTH, "Players in the Game");
        printSeparator();

        for (Player player: d_players) {
            l_count++;
            printPlayerDetails(l_count, player);
        }
    }

    // Expects gameState methods.
    private Integer getCountryArmies(String p_countryName) {
        Integer l_armies = d_gameState.getD_map().getCountryByName(p_countryName).getD_armies();

        if (l_armies == null) {
            return 0;
        }

        return l_armies;
    }

    // Expects CommonUtil methods
    // Expects Map Class, Continent Class and related methods
    // Expects Custom Exception - InvalidMap
    public void showMap() {
        if (d_players != null) {
            printPlayers();
        }

        if(!CommonUtil.isNull(d_continents)) {
            d_continents.forEach(l_continent -> {
                printContinentName(l_continent.getD_name());

                List<Country> l_continentCountries = l_continent.getD_countries();
                final int[] l_countryIndex = { 1 };

                if(!CommonUtil.isCollectionEmpty(l_continentCountries)) {
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