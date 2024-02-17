package Services;

import Models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerService {
    public boolean isPlayerNameUnique(List<Player> p_existingPlayers, String p_playerName) {
        boolean l_isUnique = true;

        if (!CommonUtil.isCollectionEmpty(p_existingPlayers)) {
            for (Player l_player: p_existingPlayers) {
                if (l_player.getPlayerName().equalsIgnoreCase(p_playerName)) {
                    l_isUnique = false;
                    break;
                }
            }
        }

        return l_isUnique;
    }

    private List<Player> addOrRemovePlayers(List<Player> p_existingPlayers, String p_operation, String p_argument) {
        List<Player> l_updatedPlayers = new ArrayList<>();

        if(!CommonUtil.isCollectionEmpty(p_existingPlayers))
            l_updatedPlayers.addAll(p_existingPlayers);

        String l_enteredPlayerName = p_argument.split(" ")[0];
        boolean l_playerNameAlreadyExists = !isPlayerNameUnique(p_existingPlayers, l_enteredPlayerName);

        switch (p_operation.toLowerCase()) {
            case "add":
                addPlayer(l_updatedPlayers, l_enteredPlayerName, l_playerNameAlreadyExists);
                break;
            case "remove":
                removePlayer(p_existingPlayers, l_updatedPlayers, l_enteredPlayerName, );
                break;
            default:
                System.out.println("Invalid Operation on Players");
        }

        return l_updatedPlayers;
    }

    private void addPlayer(List<Player> p_existingPlayers, String p_enteredName, boolean p_playerNameExists) {
        if(p_playerNameExists) {
            System.out.print("Player with name : " + p_enteredName + " already Exists. Aborted.");
        } else {
            Player l_newPlayer = new Player(p_enteredName);
            p_existingPlayers.add(l_newPlayer);
            System.out.println("Player with name : " + l_newPlayer.getPlayerName() + " has been added successfully.");
        }
    }

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

    public boolean checkPlayersAvailability(GameState p_gameState) {
        if (p_gameState.getD_players() == null || p_gameState.getD_players().isEmpty()) {
            System.out.println("There are no players!! Please add them.");
            return false;
        }

        return true;
    }

    public void assignCountries(GameState p_gameState) {
        if (!checkPlayersAvailability(p_gameState))
            return;

        List<Country> l_countries = p_gameState.getD_map().getCountriesList();
        int l_countriesPerPlayer = Math.floorDiv(l_countries.size(), p_gameState.getD_players().size());

        this.assignRandomCountries(l_countriesPerPlayer, l_countries, p_gameState.getD_players());
        this.assignContinents(p_gameState.getD_players(), p_gameState.getD_map().getContinentsList());

        System.out.println("Countries have been assigned to Players.");

    }

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

    private void assignContinents(List<Player> p_players, List<Continent> p_continents) {
        for (Player l_pl : p_players) {
            List<String> l_countriesOwned = new ArrayList<>();

            if (!CommonUtil.isCollectionEmpty(l_pl.getOwnedCountries())) {
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

    public void createDeployOrder(String p_commandEntered, Player p_player) {
        List<Order> l_orders = CommonUtil.isCollectionEmpty(p_player.getIssuedOrders()) ? new ArrayList<>()
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

    public boolean isValidArmies(Player p_player, String p_noOfArmies) {
        return p_player.getReinforcements() > Integer.parseInt(p_noOfArmies);
    }

    public int calculateArmiesForPlayer(Player p_player) {
        int l_armies = 0;

        if (!CommonUtil.isCollectionEmpty(p_player.getOwnedCountries())) {
            l_armies = Math.max(3, Math.round(p_player.getOwnedCountries().size()) / 3));
        }
        if (!CommonUtil.isCollectionEmpty(p_player.getOwnedContinents())) {
            int l_continentCtrlValue = 0;

            for (Continent l_continent : p_player.getOwnedContinents()) {
                l_continentCtrlValue += l_continent.getD_value();
            }

            l_armies = l_armies + l_continentCtrlValue;
        }
        return l_armies;
    }

    public void assignArmies(GameState p_gameState) {
        for (Player l_pl : p_gameState.getD_players()) {
            int l_armies = this.calculateArmiesForPlayer(l_pl);
            System.out.println("Player : " + l_pl.getPlayerName() + " has been assigned with " + l_armies + " armies.");

            l_pl.setReinforcement(l_armies);
        }
    }

    public boolean unexecutedOrdersExists(List<Player> p_playersList) {
        int l_totalUnexecutedOrders = 0;

        for (Player l_player : p_playersList) {
            l_totalUnexecutedOrders += l_player.getIssuedOrders().size();
        }

        return l_totalUnexecutedOrders != 0;
    }

    public boolean unassignedArmiesExists(List<Player> p_playersList) {
        int l_unassignedArmies = 0;

        for (Player l_player : p_playersList) {
            l_unassignedArmies += l_player.getReinforcements();
        }

        return l_unassignedArmies != 0;
    }

    public void updatePlayers(GameState p_gameState, String p_operation, String p_argument) {
        if (!isMapLoaded(p_gameState)) {
            System.out.println("Map not loaded!! Please load the map to add player: " + p_argument);
            return;
        }

        List<Player> l_updatedPlayers = this.addOrRemovePlayers(p_gameState.getD_players(), p_operation, p_argument);

        if (!CommonUtil.isNull(l_updatedPlayers)) {
            p_gameState.setD_players(l_updatedPlayers);
        }
    }

    public boolean isMapLoaded(GameState p_gameState) {
        return CommonUtil.isNull(p_gameState.getD_map()) ? false : true;
    }
}
