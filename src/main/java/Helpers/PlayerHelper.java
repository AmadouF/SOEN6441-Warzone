package Helpers;

import Models.*;
import org.apache.commons.collections4.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This service class handles the players.
 */
public class PlayerHelper {
    /**
     * log messages of player related operations
     */
    String d_playerLog;

    /**
     * Log message of country and continent assignment
     */
    String d_assignmentLog = "Country / Continent Assignment: ";

    /**
     * Checks if player name exists in given existing player list.
     *
     * @param p_existingPlayers existing players list present in game
     * @param p_playerName player name which needs to be looked upon
     * @return boolean true if player name is unique, false if it's not
     */
    public boolean isPlayerNameUnique(List<Player> p_existingPlayers, String p_playerName) {
        boolean l_isUnique = true;

        if (CollectionUtils.isNotEmpty(p_existingPlayers)) {
            for (Player l_player: p_existingPlayers) {
                if (l_player.getPlayerName().equalsIgnoreCase(p_playerName)) {
                    l_isUnique = false;
                    break;
                }
            }
        }

        return l_isUnique;
    }

    /**
     * This method is used to add and remove players.
     *
     * @param p_existingPlayers current player list.
     * @param p_operation operation to add or remove player.
     * @param p_argument name of player to add or remove.
     * @return return updated list of player.
     */
    public List<Player> addOrRemovePlayers(List<Player> p_existingPlayers, String p_operation, String p_argument) {
        List<Player> l_updatedPlayers = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(p_existingPlayers))
            l_updatedPlayers.addAll(p_existingPlayers);

        String l_enteredPlayerName = p_argument.split(" ")[0];
        boolean l_playerNameAlreadyExists = !isPlayerNameUnique(p_existingPlayers, l_enteredPlayerName);

        switch (p_operation.toLowerCase()) {
            case "add":
                addPlayer(l_updatedPlayers, l_enteredPlayerName, l_playerNameAlreadyExists);
                break;
            case "remove":
                removePlayer(p_existingPlayers, l_updatedPlayers, l_enteredPlayerName, l_playerNameAlreadyExists);
                break;
            default:
                setD_playerLog("Invalid operation on players!!");
        }

        return l_updatedPlayers;
    }

    /**
     * Adds player to Game if it's not there already.
     *
     * @param p_existingPlayers updated player list with newly added player
     * @param p_enteredName new player name to be added
     * @param p_playerNameExists true if player to be added already exists
     */
    private void addPlayer(List<Player> p_existingPlayers, String p_enteredName, boolean p_playerNameExists) {
        if(p_playerNameExists) {
            setD_playerLog("Player with name : " + p_enteredName + " already Exists. Aborted.");
        } else {
            Player l_newPlayer = new Player(p_enteredName);
            p_existingPlayers.add(l_newPlayer);
            setD_playerLog("Player with name : " + l_newPlayer.getPlayerName() + " has been added successfully.");
        }
    }

    /**
     * Remove player from the game if it exists.
     *
     * @param p_existingPlayers Existing player list present in game
     * @param p_updatedPlayers Updated player list with removal to be done
     * @param p_enteredPlayerName Player name which is to be removed
     * @param p_playerNameAlreadyExist true if player already exists
     */
    private void removePlayer(List<Player> p_existingPlayers, List<Player> p_updatedPlayers,
                                  String p_enteredPlayerName, boolean p_playerNameAlreadyExist) {
        if (p_playerNameAlreadyExist) {
            for (Player l_player : p_existingPlayers) {
                if (l_player.getPlayerName().equalsIgnoreCase(p_enteredPlayerName)) {
                    p_updatedPlayers.remove(l_player);
                    setD_playerLog("Player with name : " + p_enteredPlayerName + " has been removed successfully.");
                }
            }
        } else {
            setD_playerLog("Player with name : " + p_enteredPlayerName + " does not Exist. Aborted.");
        }
    }

    /**
     * Check whether players are loaded or not.
     *
     * @param p_gameState current game state with map and player information
     * @return boolean players exists or not
     */
    public boolean arePlayersAvailabe(GameState p_gameState) {
        if (p_gameState.getD_players() == null || p_gameState.getD_players().isEmpty()) {
            System.out.println("There are no players!! Please add them.");
            return false;
        }

        return true;
    }

    /**
     * This method is used to assign countries randomly among players.
     *
     * @param p_gameState current game state with map and player information
     */
    public void assignCountries(GameState p_gameState) {
        if (!arePlayersAvailabe(p_gameState)) {
            p_gameState.addLogMessage("Please add players before continuing!!", "effect");
            return;
        }

        List<Country> l_countries = p_gameState.getD_map().getCountriesList();

        int l_countriesPerPlayer = (int) Math.floor((double) l_countries.size() / p_gameState.getD_players().size());
        this.assignRandomCountries(l_countriesPerPlayer, l_countries, p_gameState.getD_players());

        this.assignContinents(p_gameState.getD_players(), p_gameState.getD_map().getContinentsList());

        p_gameState.addLogMessage(d_assignmentLog, "effect");
        System.out.println("Assigned countries successfully!!");

    }

    /**
     * Performs random country assignment to all players.
     *
     * @param p_countriesPerPlayer countries which are to be assigned to each player
     * @param p_countries list of all countries present in map
     * @param p_players list of all available players
     */
    private void assignRandomCountries(int p_countriesPerPlayer, List<Country> p_countries,
                                                List<Player> p_players) {
        List<Country> l_remainingCountries = new ArrayList<>(p_countries);

        for (Player l_pl : p_players) {
            if (l_remainingCountries.isEmpty())
                break;

            // Picking random country and assigning to the players
            for (int i = 0; i < p_countriesPerPlayer; i++) {
                Random l_random = new Random();

                // Choosing random countries
                int l_randomIndex = l_random.nextInt(l_remainingCountries.size());
                Country l_randomCountry = l_remainingCountries.get(l_randomIndex);

                if (l_pl.getOwnedCountries() == null)
                    l_pl.setOwnedCountries(new ArrayList<>());

                l_pl.getOwnedCountries().add(l_randomCountry);

                System.out.println("Player : " + l_pl.getPlayerName() + " is assigned with country : "
                        + l_randomCountry.getD_name());

                d_assignmentLog += "Player : " + l_pl.getPlayerName() + " is assigned with country : "
                        + l_randomCountry.getD_name();

                l_remainingCountries.remove(l_randomCountry);
            }
        }

        // If countries are still remaining assign it one by one to players
        if (!l_remainingCountries.isEmpty()) {
            assignRandomCountries(1, l_remainingCountries, p_players);
        }
    }

    /**
     * Checks if player is having any continent as a result of random country
     * assignment.
     *
     * @param p_players list of all available players
     * @param p_continents list of all available continents
     */
    public void assignContinents(List<Player> p_players, List<Continent> p_continents) {
        for (Player l_pl : p_players) {
            List<String> l_countriesOwned = new ArrayList<String>();

            if (CollectionUtils.isNotEmpty(l_pl.getOwnedCountries())) {
                // Getting all countries names
                l_pl.getOwnedCountries().forEach(l_country -> l_countriesOwned.add(l_country.getD_name()));

                for (Continent l_cont : p_continents) {
                    List<String> l_countriesOfContinent = new ArrayList<>();

                    // Getting all countries of the continent
                    l_cont.getD_countries().forEach(l_count -> l_countriesOfContinent.add(l_count.getD_name()));

                    // If player owns all the countries of the continent, assign continent
                    if (l_countriesOwned.containsAll(l_countriesOfContinent)) {
                        if (l_pl.getOwnedContinents() == null)
                            l_pl.setOwnedContinents(new ArrayList<>());

                        l_pl.getOwnedContinents().add(l_cont);
                        System.out.println("Player : " + l_pl.getPlayerName() + " is assigned with continent : "
                                + l_cont.getD_name());

                        d_assignmentLog += "Player : " + l_pl.getPlayerName() + " is assigned with continent : "
                                + l_cont.getD_name();
                    }
                }
            }
        }
    }

    /**
     * Calculates armies of player based on countries and continents owned.
     *
     * @param p_player player for which armies have to be calculated
     *
     * @return Integer armies to be assigned to player
     */
    public int calculateArmiesForPlayer(Player p_player) {
        int l_armies = 0;

        if (CollectionUtils.isNotEmpty(p_player.getOwnedCountries())) {
            l_armies = Math.max(3, Math.round(p_player.getOwnedCountries().size()) / 3);
        }
        if (CollectionUtils.isNotEmpty(p_player.getOwnedContinents())) {
            int l_totalContinentBonus = 0;

            for (Continent l_continent : p_player.getOwnedContinents()) {
                l_totalContinentBonus += l_continent.getD_value();
            }

            l_armies = l_armies + l_totalContinentBonus;
        }
        return l_armies;
    }

    /**
     * Assigns armies to each player of the game.
     *
     * @param p_gameState current game state with map and player information
     */
    public void assignArmies(GameState p_gameState) {
        for (Player l_pl : p_gameState.getD_players()) {
            int l_armies = this.calculateArmiesForPlayer(l_pl);
            this.setD_playerLog("Player : " + l_pl.getPlayerName() + " has been assigned with " + l_armies + " armies.");

            p_gameState.addLogMessage(this.d_playerLog, "effect");

            l_pl.setReinforcement(l_armies);
        }
    }

    /**
     * Check if unexecuted orders exists in the game.
     *
     * @param p_playersList players involved in game
     *
     * @return boolean true if unexecuted orders exists with any of the players or
     *         else false
     */
    public boolean unexecutedOrdersExists(List<Player> p_playersList) {
        int l_totalUnexecutedOrders = 0;

        for (Player l_player : p_playersList) {
            l_totalUnexecutedOrders += l_player.getIssuedOrders().size();
        }

        return l_totalUnexecutedOrders != 0;
    }

    /**
     * Check if any of the players have unassigned armies.
     *
     * @param p_playersList players involved in game
     *
     * @return boolean true if unassigned armies exists with any of the players or
     *         else false
     */
    public boolean unassignedArmiesExists(List<Player> p_playersList) {
        int l_unassignedArmies = 0;

        for (Player l_player : p_playersList) {
            l_unassignedArmies += l_player.getReinforcements();
        }

        return l_unassignedArmies != 0;
    }

    /**
     * This method is called by controller to add players, update gameState.
     *
     * @param p_gameState update game state with players information.
     * @param p_operation operation to add or remove player.
     * @param p_argument name of player to add or remove.
     */
    public void updatePlayers(GameState p_gameState, String p_operation, String p_argument) {
        if (!isMapLoaded(p_gameState)) {
            this.setD_playerLog("Map not loaded!! Please load the map to add player: " + p_argument);
            p_gameState.addLogMessage(this.d_playerLog, "effect");
            return;
        }

        List<Player> l_updatedPlayers = this.addOrRemovePlayers(p_gameState.getD_players(), p_operation, p_argument);

        if (CollectionUtils.isNotEmpty(l_updatedPlayers)) {
            p_gameState.setD_players(l_updatedPlayers);

            p_gameState.addLogMessage(d_playerLog, "effect");
        }
    }

    /**
     * Check whether map is loaded or not.
     *
     * @param p_gameState current game state with map and player information
     *
     * @return boolean map is loaded or not
     */
    public boolean isMapLoaded(GameState p_gameState) {
        return p_gameState.getD_map() != null;
    }

    /**
     * Checks if the players have more orders or not
     *
     * @param p_playersList List of players
     * @return true or false
     */
    public boolean checkForMoreOrders(List<Player> p_playersList) {
        for (Player l_player: p_playersList) {
            if (l_player.getMoreOrders()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Resets the players info before issuing new orders
     *
     * @param p_players List of the players
     */
    public void resetPlayers(List<Player> p_players) {
        for (Player l_player: p_players) {
            // If only the player is not Neutral player then change orders flag
            if (!l_player.getPlayerName().equalsIgnoreCase("Neutral"))
                l_player.setMoreOrders(true);

            l_player.setOneCard(false);
            l_player.clearNegotiatedPlayers();
        }
    }

    /**
     * Sets the log message of player operations
     *
     * @param p_playerLog log message
     */
    public void setD_playerLog(String p_playerLog) {
        this.d_playerLog = p_playerLog;

        System.out.println(p_playerLog);
    }

    /**
     * Finds the player in the game by name
     *
     * @param p_playerName Name of the player
     * @param p_gameState current state of the game
     * @return Player
     */
    public Player findPlayerByName(String p_playerName, GameState p_gameState) {
        return p_gameState.getD_players().stream().filter(l_pl -> l_pl.getPlayerName().equals(p_playerName))
                .findFirst().orElse(null);
    }
}
