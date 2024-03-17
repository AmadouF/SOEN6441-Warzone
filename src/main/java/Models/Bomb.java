package Models;

/**
 * This class represents the bomb card given in the game
 */
public class Bomb implements Card {
    /**
     * Player who wants to initiate bomb command
     */
    Player d_playerInitiator;

    /**
     * Name of the target country
     */
    String d_targetCountryName;

    /**
     * Stores info about order execution
     */
    String d_orderExecutionLog;

    /**
     * The constructor receives all required values for creating bomb order
     *
     * @param p_playerInitiator Player who wants to create bomb order
     * @param p_targetCountryName Name of the target country
     */
    public Bomb(Player p_playerInitiator, String p_targetCountryName) {
        this.d_playerInitiator = p_playerInitiator;
        this.d_targetCountryName = p_targetCountryName;
    }

    /**
     * Gets to details of current order
     *
     * @return String
     */
    private String currentOrder() {
        return "Bomb card order - " + "bomb" + " " + this.d_targetCountryName;
    }

    /**
     * Gets the name of the order
     *
     * @return String
     */
    @Override
    public String getOrderName() {
        return "bomb";
    }

    /**
     * Executes the bomb order
     *
     * @param p_gameState current state of the game
     */
    @Override
    public void execute(GameState p_gameState) {
        if (isValid(p_gameState)) {
            Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountryName);
            Integer l_numOfArmiesOnTargetCountry = l_targetCountry.getD_army() == 0 ? 1 :
                    l_targetCountry.getD_army();

            Integer l_newArmies = (int) Math.floor((double) l_numOfArmiesOnTargetCountry / 2);
            l_targetCountry.setD_army(l_newArmies);

            d_playerInitiator.removeCard("bomb");

            this.setD_orderExecutionLog("\nPlayer - " + this.d_playerInitiator.getPlayerName() +
                    " is bombing on country - " + l_targetCountry.getD_name() + " with armies : " + l_numOfArmiesOnTargetCountry +
                    ". New armies - " + l_targetCountry.getD_army(), "default");

            p_gameState.addLogMessage(orderExecutionLog(), "effect");
        }
    }

    /**
     * Checks for the validation before execution
     *
     * @param p_gameState current state of the game
     * @return true or false
     */
    @Override
    public boolean isValid(GameState p_gameState) {
        Country l_ownCountry = d_playerInitiator.getOwnedCountries().stream()
                .filter(l_pl -> l_pl.getD_name().equalsIgnoreCase(this.d_targetCountryName.toString()))
                .findFirst().orElse(null);

        // Player cannot bomb his own country
        if (l_ownCountry != null) {
            this.setD_orderExecutionLog(this.currentOrder() + " is aborted because target" +
                    " country is owned by Initiator player - " + this.d_playerInitiator.getPlayerName() +
                    " and you can not bomb your own country!!", "error");

            p_gameState.addLogMessage(orderExecutionLog(), "error");

            return false;
        }

        // Checking if player has a pact
        if (!d_playerInitiator.negotiationValidation(this.d_targetCountryName)) {
            this.setD_orderExecutionLog(this.currentOrder() + " is aborted because "
            + d_playerInitiator.getPlayerName() + " has negotiated the pact with target country's owner",
                    "error");

            p_gameState.addLogMessage(orderExecutionLog(), "error");
        }

        return true;
    }

    /**
     * Gets the log of execution
     *
     * @return log message
     */
    @Override
    public String orderExecutionLog() {
        return d_orderExecutionLog;
    }

    /**
     * Sets the message of execution log
     *
     * @param p_orderExecutionLog log message
     * @param p_typeOfLog type of the log
     */
    @Override
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_typeOfLog) {
        this.d_orderExecutionLog = p_orderExecutionLog;

        if (p_typeOfLog.equals("error")) {
            System.err.println(p_orderExecutionLog);
        } else {
            System.out.println(p_orderExecutionLog);
        }
    }

    /**
     * Checks if the order is valid or not
     *
     * @param p_gameState current state of the game
     * @return true or false
     */
    @Override
    public Boolean checkValidOrder(GameState p_gameState) {
        Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountryName);

        if (l_targetCountry == null) {
            this.setD_orderExecutionLog("Provided country does not exist!!", "error");
            return false;
        }

        return true;
    }

    /**
     * Prints to details about bomb order
     */
    @Override
    public void printOrder() {
        this.d_orderExecutionLog = "\n******* Bomb card order issued by " + this.d_playerInitiator.getPlayerName() + " *******\n"
                + System.lineSeparator() + "Creating a bomb order on " + this.d_targetCountryName;

        System.out.println(System.lineSeparator() + this.d_orderExecutionLog);
    }
}
