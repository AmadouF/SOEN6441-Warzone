package Models;

/**
 * This class represents deploy order given by the player
 */
public class Deploy implements Order {
    /**
     * Name of the target country
     */
    String d_targetCountryName;

    /**
     * Number of armies to deploy
     */
    Integer d_numberOfArmiesToDeploy;

    /**
     * The player who initiated this order
     */
    Player d_playerInitiator;

    /**
     * Log message containing details about this order
     */
    String d_orderExecutionLog;

    /**
     * This constructor receives all the values required to create an order
     *
     * @param p_playerInitiator player that created the order
     * @param p_targetCountryName country where new armies will be deployed
     * @param p_numberOfArmiesToDeploy number of armies to be deployed
     */
    public Deploy(Player p_playerInitiator, String p_targetCountryName, Integer p_numberOfArmiesToDeploy) {
        this.d_targetCountryName = p_targetCountryName;
        this.d_playerInitiator = p_playerInitiator;
        this.d_numberOfArmiesToDeploy = p_numberOfArmiesToDeploy;
    }

    /**
     * Returns the name of the current order
     *
     * @return String
     */
    @Override
    public String getOrderName() {
        return "deploy";
    }

    /**
     * Executes the deployment order
     *
     * @param p_gameState current state of the game
     */
    @Override
    public void execute(GameState p_gameState) {
        if (isValid(p_gameState)) {
            for (Country l_country: p_gameState.getD_map().getCountriesList()) {
                if (l_country.getD_name().equalsIgnoreCase(this.d_targetCountryName.toString())) {
                    Integer l_finalArmies = l_country.getD_army() == null ?
                            this.d_numberOfArmiesToDeploy : l_country.getD_army() + this.d_numberOfArmiesToDeploy;

                    l_country.setD_army(l_finalArmies);

                    this.setD_orderExecutionLog(this.d_numberOfArmiesToDeploy + " armies has been deployed" +
                            " successfully on target country - " + this.d_targetCountryName, "default");
                }
            }
        } else {
            this.setD_orderExecutionLog("Deploy order - deploy " + this.d_targetCountryName +
                    " " + this.d_numberOfArmiesToDeploy + " is aborted because the country is now owned by the player - "
                    + this.d_playerInitiator.getPlayerName(), "error");

            d_playerInitiator.setReinforcement(d_playerInitiator.getReinforcements() + this.d_numberOfArmiesToDeploy);
        }

        p_gameState.addLogMessage(orderExecutionLog(), "effect");
    }

    /**
     * Validates the country for deployment
     *
     * @param p_gameState current state of the game
     * @return true or false
     */
    @Override
    public boolean isValid(GameState p_gameState) {
        Country l_country = d_playerInitiator.getOwnedCountries().stream()
                .filter(l_pl -> l_pl.getD_name().equalsIgnoreCase(this.d_targetCountryName.toString()))
                .findFirst().orElse(null);

        return l_country != null;
    }

    /**
     * To get the log of execution
     *
     * @return log of execution
     */
    @Override
    public String orderExecutionLog() {
        return d_orderExecutionLog;
    }

    /**
     * Sets the log message and prints it
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
     * Prints the order details
     */
    @Override
    public void printOrder() {
        this.d_orderExecutionLog = "\n******* Deploy order issued by " + this.d_playerInitiator.getPlayerName() + " *******\n"
                + System.lineSeparator() + "Deploy " + this.d_numberOfArmiesToDeploy + " armies to " + this.d_targetCountryName;

        System.out.println(System.lineSeparator() + this.d_orderExecutionLog);
    }
}
