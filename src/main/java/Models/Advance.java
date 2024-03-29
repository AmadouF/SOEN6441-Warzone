package Models;

import Helpers.PlayerHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the advance order given by the player
 */
public class Advance implements Order {
    /**
     * Name of the source country
     */
    String d_sourceCountryName;

    /**
     * Name of the target country
     */
    String d_targetCountryName;

    /**
     * Number of armies to advance from source to target
     */
    Integer d_numberOfArmiesToAdvance;

    /**
     * Player who initiated the advance order
     */
    Player d_playerInitiator;

    /**
     * Stores log of execution
     */
    String d_orderExecutionLog;

    /**
     * This constructor receives all values required to create advance order
     *
     * @param p_playerInitiator Player who initiated the command
     * @param p_sourceCountryName Name of the source country
     * @param p_targetCountryName Name of the target country
     * @param p_numberOfArmiesToAdvance Number of armies to advance
     */
    public Advance(Player p_playerInitiator, String p_sourceCountryName, String p_targetCountryName,
                   Integer p_numberOfArmiesToAdvance) {
        this.d_playerInitiator = p_playerInitiator;
        this.d_sourceCountryName = p_sourceCountryName;
        this.d_targetCountryName = p_targetCountryName;
        this.d_numberOfArmiesToAdvance = p_numberOfArmiesToAdvance;
    }

    /**
     * Gets the name of the order
     *
     * @return String
     */
    @Override
    public String getOrderName() {
        return "advance";
    }

    /**
     * Gets the details of the order
     *
     * @return String
     */
    private String currentOrder() {
        return "Advance order - advance " + this.d_numberOfArmiesToAdvance + " armies from "
                + this.d_sourceCountryName + " to " + this.d_targetCountryName;
    }

    /**
     * Executes the advance order
     *
     * @param p_gameState current state of the game
     */
    @Override
    public void execute(GameState p_gameState) {
        if (isValid(p_gameState)) {
            Player l_playerOfTargetCountry = getPlayerOfTargetCountry(p_gameState);

            Country l_sourceCountry = p_gameState.getD_map().getCountryByName(d_sourceCountryName);
            Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountryName);
            Integer l_sourceArmiesToChange = l_sourceCountry.getD_army() - this.d_numberOfArmiesToAdvance;
            l_sourceCountry.setD_army(l_sourceArmiesToChange);

            if (l_playerOfTargetCountry.getPlayerName().equalsIgnoreCase(this.d_playerInitiator.getPlayerName())) {
                deployArmiesToTarget(l_targetCountry);
            } else if (l_targetCountry.getD_army() == 0) {
                conquerTargetCountry(p_gameState, l_playerOfTargetCountry, l_targetCountry);
                this.d_playerInitiator.assignCard();
            } else {
                getOrderResult(p_gameState, l_playerOfTargetCountry, l_targetCountry, l_sourceCountry);
            }
        } else {
            p_gameState.addLogMessage(orderExecutionLog(), "error");
        }
    }

    /**
     * Validates to command before the execution
     *
     * @param p_gameState current state of the game
     * @return true or false
     */
    @Override
    public boolean isValid(GameState p_gameState) {
        Country l_country = d_playerInitiator.getOwnedCountries().stream()
                .filter(l_pl -> l_pl.getD_name().equalsIgnoreCase(this.d_sourceCountryName.toString()))
                .findFirst().orElse(null);

        if (l_country == null) {
            this.setD_orderExecutionLog(this.currentOrder() + " is aborted because given source country - "
                    + this.d_sourceCountryName + " does not belong to the player - " +
                    this.d_playerInitiator.getPlayerName(), "error");

            p_gameState.addLogMessage(orderExecutionLog(), "effect");
            return false;
        }

        if (this.d_numberOfArmiesToAdvance > l_country.getD_army()) {
            this.setD_orderExecutionLog(this.currentOrder() + " is aborted because given number of armies exceeds " +
                    "the armies of source country - " + this.d_sourceCountryName, "error");

            p_gameState.addLogMessage(orderExecutionLog(), "effect");
            return false;
        }

        if (this.d_numberOfArmiesToAdvance == l_country.getD_army()) {
            this.setD_orderExecutionLog(this.currentOrder() + " is aborted because source country - " +
                    this.d_sourceCountryName + " has " + l_country.getD_army() + " armies and all of those cannot be given in advance order " +
                    ", at least one army has be in the country", "error");

            p_gameState.addLogMessage(orderExecutionLog(), "effect");
            return false;
        }

        if (!d_playerInitiator.negotiationValidation(this.d_targetCountryName)) {
            this.setD_orderExecutionLog(this.currentOrder() + " is aborted because " +
                    this.d_playerInitiator.getPlayerName() +
                    " has negotiation pact with the source country's owner", "error");

            p_gameState.addLogMessage(orderExecutionLog(), "effect");
            return false;
        }

        return true;
    }

    /**
     * Gets the owner of the target country
     *
     * @param p_gameState current state of the game
     * @return Player - Owner of the country
     */
    private Player getPlayerOfTargetCountry(GameState p_gameState) {
        Player l_playerOfTargetCountry = null;

        for (Player l_player : p_gameState.getD_players()) {
            String l_cont = l_player.getCountryNames().stream()
                    .filter(l_country -> l_country.equalsIgnoreCase(this.d_targetCountryName))
                    .findFirst().orElse(null);

            if (StringUtils.isNotEmpty(l_cont)) {
                l_playerOfTargetCountry = l_player;
            }
        }

        return l_playerOfTargetCountry;
    }

    /**
     * Deploys the armies to target country (Because target country is owned by the initiator himself)
     *
     * @param p_targetCountry target country
     */
    public void deployArmiesToTarget(Country p_targetCountry) {
        Integer l_updatedTargetContArmies = p_targetCountry.getD_army() + this.d_numberOfArmiesToAdvance;
        p_targetCountry.setD_army(l_updatedTargetContArmies);
    }

    /**
     * Removes the target country from its owner and assign it to attacker player
     * hence, conquering the country
     *
     * @param p_gameState current state of the game
     * @param p_playerOfTargetCountry Owner of target country
     * @param p_targetCountry target country
     */
    private void conquerTargetCountry(GameState p_gameState, Player p_playerOfTargetCountry, Country p_targetCountry) {
        p_targetCountry.setD_army(d_numberOfArmiesToAdvance);

        p_playerOfTargetCountry.getOwnedCountries().remove(p_targetCountry);
        this.d_playerInitiator.getOwnedCountries().add(p_targetCountry);

        this.setD_orderExecutionLog(
                "Player : " + this.d_playerInitiator.getPlayerName() + " is assigned with Country : "
                        + p_targetCountry.getD_name() + " and armies : " + p_targetCountry.getD_army(),
                "default");

        p_gameState.addLogMessage(orderExecutionLog(), "effect");

        this.updateContinents(this.d_playerInitiator, p_playerOfTargetCountry, p_gameState);
    }

    /**
     * Updates to continents after removing and adding new country
     * this will be called when the player conquers the counntry
     *
     * @param p_playerOfSourceCountry Player of the source country
     * @param p_playerOfTargetCountry Player of the target country
     * @param p_gameState current state of the game
     */
    private void updateContinents(Player p_playerOfSourceCountry, Player p_playerOfTargetCountry,
                                  GameState p_gameState) {
        System.out.println("Updating continents of players...");

        List<Player> l_playersList = new ArrayList<>();

        p_playerOfSourceCountry.setOwnedContinents(new ArrayList<>());
        p_playerOfTargetCountry.setOwnedContinents(new ArrayList<>());
        l_playersList.add(p_playerOfSourceCountry);
        l_playersList.add(p_playerOfTargetCountry);

        PlayerHelper l_playerHelper = new PlayerHelper();

        l_playerHelper.assignContinents(l_playersList, p_gameState.getD_map().getContinentsList());
    }

    /**
     * Calculates the result of the order
     *
     * @param p_gameState current state of the game
     * @param p_playerOfTargetCountry Owner of the target country
     * @param p_targetCountry target country
     * @param p_sourceCountry source country
     */
    private void getOrderResult(GameState p_gameState, Player p_playerOfTargetCountry, Country p_targetCountry,
                                Country p_sourceCountry) {
        Integer l_armiesInBattle = this.d_numberOfArmiesToAdvance < p_targetCountry.getD_army()
                ? this.d_numberOfArmiesToAdvance
                : p_targetCountry.getD_army();

        List<Integer> l_attackerArmies = generateRandomArmyUnits(l_armiesInBattle, "attacker");
        List<Integer> l_defenderArmies = generateRandomArmyUnits(l_armiesInBattle, "defender");

        this.getBattleResult(p_sourceCountry, p_targetCountry, l_attackerArmies, l_defenderArmies,
                p_playerOfTargetCountry);

        p_gameState.addLogMessage(orderExecutionLog(), "effect");

        this.updateContinents(this.d_playerInitiator, p_playerOfTargetCountry, p_gameState);
    }

    /**
     * Generates army values according to warzone rules
     * Attacking army - 60%
     * Defending army - 70%
     *
     * @param p_size size of the total army
     * @param p_role role of the army (Attacker or Defender)
     * @return List of values of all army units
     */
    private List<Integer> generateRandomArmyUnits(int p_size, String p_role) {
        List<Integer> l_armyList = new ArrayList<>();

        Double l_probability = "attacker".equalsIgnoreCase(p_role) ? 0.6 : 0.7;

        for (int l_i = 0; l_i < p_size; l_i++) {
            int l_randomNumber = getRandomInteger(10, 1);

            Integer l_armyUnit = (int) Math.round(l_randomNumber * l_probability);
            l_armyList.add(l_armyUnit);
        }

        return l_armyList;
    }

    /**
     * Generated a random integer between two values
     *
     * @param p_max max value
     * @param p_min min value
     * @return integer
     */
    private static int getRandomInteger(int p_max, int p_min) {
        return ((int) (Math.random() * (p_max - p_min))) + p_min;
    }

    /**
     * Decides the result of the battle
     *
     * @param p_sourceCountry source country
     * @param p_targetCountry target country
     * @param p_attackerArmies attacking army
     * @param p_defenderArmies defending army
     * @param p_playerOfTargetCountry Owner of the target country
     */
    private void getBattleResult(Country p_sourceCountry, Country p_targetCountry, List<Integer> p_attackerArmies,
                                 List<Integer> p_defenderArmies, Player p_playerOfTargetCountry) {
        Integer l_attackerArmiesLeft = this.d_numberOfArmiesToAdvance > p_targetCountry.getD_army()
                ? this.d_numberOfArmiesToAdvance - p_targetCountry.getD_army()
                : 0;
        Integer l_defenderArmiesLeft = this.d_numberOfArmiesToAdvance < p_targetCountry.getD_army()
                ? p_targetCountry.getD_army() - this.d_numberOfArmiesToAdvance
                : 0;

        for (int l_i = 0; l_i < p_attackerArmies.size(); l_i++) {
            if (p_attackerArmies.get(l_i) > p_defenderArmies.get(l_i)) {
                l_attackerArmiesLeft++;
            } else {
                l_defenderArmiesLeft++;
            }
        }

        this.handleSurvivingArmies(l_attackerArmiesLeft, l_defenderArmiesLeft, p_sourceCountry, p_targetCountry,
                p_playerOfTargetCountry);
    }

    /**
     * Handles the army units survived after the end of battle
     *
     * @param p_attackerArmiesLeft units left in attacking army
     * @param p_defenderArmiesLeft units left in defending army
     * @param p_sourceCountry source country
     * @param p_targetCountry target country
     * @param p_playerOfTargetCountry owner of the target country
     */
    public void handleSurvivingArmies(Integer p_attackerArmiesLeft, Integer p_defenderArmiesLeft,
                                      Country p_sourceCountry, Country p_targetCountry, Player p_playerOfTargetCountry) {
        if (p_defenderArmiesLeft == 0) {
            p_playerOfTargetCountry.getOwnedCountries().remove(p_targetCountry);
            this.d_playerInitiator.getOwnedCountries().add(p_targetCountry);

            p_targetCountry.setD_army(p_attackerArmiesLeft);

            this.setD_orderExecutionLog(
                    "Player - " + this.d_playerInitiator.getPlayerName() + " won the Country : "
                            + p_targetCountry.getD_name() + " and armies : " + p_targetCountry.getD_army(),
                    "default");

            this.d_playerInitiator.assignCard();
        } else {
            p_targetCountry.setD_army(p_defenderArmiesLeft);

            Integer l_sourceArmiesToChange = p_sourceCountry.getD_army() + p_attackerArmiesLeft;
            p_sourceCountry.setD_army(l_sourceArmiesToChange);

            String l_country1 = "Country - " + p_targetCountry.getD_name() + " is left with "
                    + p_targetCountry.getD_army() + " armies and is still owned by player : "
                    + p_playerOfTargetCountry.getPlayerName();
            String l_country2 = "Country : " + p_sourceCountry.getD_name() + " is left with "
                    + p_sourceCountry.getD_army() + " armies and is still owned by player : "
                    + this.d_playerInitiator.getPlayerName();

            this.setD_orderExecutionLog(l_country1 + System.lineSeparator() + l_country2, "default");
        }
    }

    /**
     * Gets the log of execution
     *
     * @return String
     */
    @Override
    public String orderExecutionLog() {
        return this.d_orderExecutionLog;
    }

    /**
     * Sets the log message of execution
     *
     * @param p_orderExecutionLog log message
     * @param p_typeOfLog type of the log
     */
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_typeOfLog) {
        this.d_orderExecutionLog = p_orderExecutionLog;

        if (p_typeOfLog.equals("error")) {
            System.err.println(p_orderExecutionLog);
        } else {
            System.out.println(p_orderExecutionLog);
        }
    }

    /**
     * Prints the details about the advance order
     */
    @Override
    public void printOrder() {
        this.d_orderExecutionLog = "\n******* Advance order issued by " + this.d_playerInitiator.getPlayerName()
                + " *******\n" + System.lineSeparator() + "Move " + this.d_numberOfArmiesToAdvance + " armies from"
                + this.d_sourceCountryName + " to " + this.d_targetCountryName;

        System.out.println(System.lineSeparator() + this.d_orderExecutionLog);
    }
}
