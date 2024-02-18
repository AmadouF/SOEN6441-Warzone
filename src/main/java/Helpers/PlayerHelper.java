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
    private List<Player> addOrRemovePlayers(List<Player> p_existingPlayers, String p_operation, String p_argument) {
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
                System.out.println("Invalid Operation on Players");
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
            System.out.print("Player with name : " + p_enteredName + " already Exists. Aborted.");
        } else {
            Player l_newPlayer = new Player(p_enteredName);
            p_existingPlayers.add(l_newPlayer);
            System.out.println("Player with name : " + l_newPlayer.getPlayerName() + " has been added successfully.");
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
                    System.out.println("Player with name : " + p_enteredPlayerName + " has been removed successfully.");
                }
            }
        } else {
            System.out.print("Player with name : " + p_enteredPlayerName + " does not Exist. Aborted.");
        }
    }

    /**
     * Check whether players are loaded or not.
     *
     * @param p_gameState current game state with map and player information
     * @return boolean players exists or not
     */
    public boolean checkPlayersAvailability(GameState p_gameState) {
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
        if (!checkPlayersAvailability(p_gameState))
            return;

        List<Country> l_countries = p_gameState.getD_map().getCountriesList();
        int l_countriesPerPlayer = Math.floorDiv(l_countries.size(), p_gameState.getD_players().size());

        this.assignRandomCountries(l_countriesPerPlayer, l_countries, p_gameState.getD_players());
        this.assignContinents(p_gameState.getD_players(), p_gameState.getD_map().getContinentsList());

        System.out.println("Countries have been assigned to Players.");

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
        List<Country> l_unassignedCountries = new ArrayList<>(p_countries);

        for (Player l_pl : p_players) {
            if (l_unassignedCountries.isEmpty())
                break;

            // Based on number of countries to be assigned to player, it picks random
            // country and assigns to the player
            for (int i = 0; i < p_countriesPerPlayer; i++) {
                Random l_random = new Random();
                int l_randomIndex = l_random.nextInt(l_unassignedCountries.size());
                Country l_randomCountry = l_unassignedCountries.get(l_randomIndex);

                if (l_pl.getOwnedCountries() == null)
                    l_pl.setOwnedCountries(new ArrayList<>());

                l_pl.getOwnedCountries().add(l_randomCountry);

                System.out.println("Player : " + l_pl.getPlayerName() + " is assigned with country : "
                        + l_randomCountry.getD_name());

                l_unassignedCountries.remove(l_randomCountry);
            }
        }
        // If any countries are still left for assignment, it will again assign those
        // among players one by one
        if (!l_unassignedCountries.isEmpty()) {
            assignRandomCountries(1, l_unassignedCountries, p_players);
        }
    }

    /**
     * Checks if player is having any continent as a result of random country
     * assignment.
     *
     * @param p_players list of all available players
     * @param p_continents list of all available continents
     */
    private void assignContinents(List<Player> p_players, List<Continent> p_continents) {
        for (Player l_pl : p_players) {
            List<String> l_countriesOwned = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(l_pl.getOwnedCountries())) {
                l_pl.getOwnedCountries().forEach(l_country -> l_countriesOwned.add(l_country.getD_name()));

                for (Continent l_cont : p_continents) {
                    List<String> l_countriesOfContinent = new ArrayList<>();

                    l_cont.getD_countries().forEach(l_count -> l_countriesOfContinent.add(l_count.getD_name()));

                    if (l_countriesOwned.containsAll(l_countriesOfContinent)) {
                        if (l_pl.getOwnedContinents() == null)
                            l_pl.setOwnedContinents(new ArrayList<>());

                        l_pl.getOwnedContinents().add(l_cont);
                        System.out.println("Player : " + l_pl.getPlayerName() + " is assigned with continent : "
                                + l_cont.getD_name());
                    }
                }
            }
        }
    }

    /**
     * creates the deploy order on the commands entered by the player.
     *
     * @param p_commandEntered command entered by the user
     * @param p_player player to create deploy order
     */
    public void createDeployOrder(String p_commandEntered, Player p_player) {
        List<Order> l_orders = CollectionUtils.isEmpty(p_player.getIssuedOrders()) ? new ArrayList<>()
                : p_player.getIssuedOrders();

        String l_countryName = p_commandEntered.split(" ")[1];
        String l_noOfArmies = p_commandEntered.split(" ")[2];

        if (!isValidArmies(p_player, l_noOfArmies)) {
            System.out.println(
                    "Player does not have enough reinforcements to deploy!! Aborted.");
        } else {
            Order l_order = new Order(p_commandEntered.split(" ")[0], l_countryName,
                    Integer.parseInt(l_noOfArmies));
            l_orders.add(l_order);
            p_player.setIssuedOrders(l_orders);

            int l_reinforcements = p_player.getReinforcements() - Integer.parseInt(l_noOfArmies);
            p_player.setReinforcement(l_reinforcements);

            System.out.println("Order has been created and added to queue.");
        }
    }

    /**
     * Used to test number of armies entered in deploy command to check that player
     * cannot deploy more armies than they have.
     *
     * @param p_player player to create deploy order
     * @param p_noOfArmies number of armies to deploy
     *
     * @return boolean to validate armies to deploy
     */
    public boolean isValidArmies(Player p_player, String p_noOfArmies) {
        return p_player.getReinforcements() >= Integer.parseInt(p_noOfArmies);
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
            int l_continentCtrlValue = 0;

            for (Continent l_continent : p_player.getOwnedContinents()) {
                l_continentCtrlValue += l_continent.getD_value();
            }

            l_armies = l_armies + l_continentCtrlValue;
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
            System.out.println("Player : " + l_pl.getPlayerName() + " has been assigned with " + l_armies + " armies.");

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
            System.out.println("Map not loaded!! Please load the map to add player: " + p_argument);
            return;
        }

        List<Player> l_updatedPlayers = this.addOrRemovePlayers(p_gameState.getD_players(), p_operation, p_argument);

        if (CollectionUtils.isNotEmpty(l_updatedPlayers)) {
            p_gameState.setD_players(l_updatedPlayers);
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
}