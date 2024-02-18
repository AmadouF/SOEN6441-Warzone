package Models;

/**
 * This model manages the orders given in the game by players.
 */
public class Order {
    /**
     * order action.
     */
    String d_orderAction;

    /**
     * Name of the target country.
     */
    String d_targetCountryName;

    /**
     * Name of the source country.
     */
    String d_sourceCountryName;

    /**
     * Number of armies to deploy.
     */
    Integer d_numberOfArmiesToDeploy;

    /**
     * Object of the order class.
     */
    Order order;

    /**
     * Unparameterized constructor.
     */
    public Order() {

    }

    /**
     * Parameterized constructor.
     *
     * @param p_orderAction order action
     * @param p_targetCountryName name of the target country
     * @param p_numberOfArmiesToDeploy number of armies to deploy
     */
    public Order(String p_orderAction, String p_targetCountryName, Integer p_numberOfArmiesToDeploy) {
        this.d_orderAction = p_orderAction;
        this.d_targetCountryName = p_targetCountryName;
        this.d_numberOfArmiesToDeploy = p_numberOfArmiesToDeploy;
    }

    /**
     * getter method to get order actions.
     *
     * @return order action
     */
    public String getD_orderAction() {
        return this.d_orderAction;
    }

    /**
     * setter method to get order actions.
     *
     * @param p_orderAction order action
     */
    public void setD_orderAction(String p_orderAction) {
        this.d_orderAction = p_orderAction;
    }

    /**
     * getter method to get name of the target country.
     *
     * @return name of the target country
     */
    public String getD_targetCountryName() {
        return this.d_targetCountryName;
    }

    /**
     * setter method to set name of the target country.
     *
     * @param p_targetCountryName name of the target country
     */
    public void setD_targetCountryName(String p_targetCountryName) {
        this.d_targetCountryName = p_targetCountryName;
    }

    /**
     * getter method to get the name of the source country.
     *
     * @return name of the source country
     */
    public String getD_sourceCountryName() {
        return this.d_sourceCountryName;
    }

    /**
     * setter method to set the name of the source coutry.
     *
     * @param p_sourceCountryName name of the source country
     */
    public void setD_sourceCountryName(String p_sourceCountryName) {
        this.d_sourceCountryName = p_sourceCountryName;
    }

    /**
     * getter method to get number of armies to deploy.
     *
     * @return number of armies to deploy
     */
    public Integer getD_numberOfArmiesToDeploy() {
        return this.d_numberOfArmiesToDeploy;
    }

    /**
     * setter method to set number of armies to deploy.
     *
     * @param p_numberOfArmiesToDeploy number of armies to deploy
     */
    public void setD_numberOfArmiesToDeploy(Integer p_numberOfArmiesToDeploy) {
        this.d_numberOfArmiesToDeploy = p_numberOfArmiesToDeploy;
    }

    /**
     * Enacts the order object and makes necessary changes in game state.
     *
     * @param p_gameState current state of the game
     * @param p_player player whose order is being executed
     */
    public void execute(GameState p_gameState, Player p_player) {
        switch (this.d_orderAction) {
            case "deploy":
                if(this.validateOrderCountry(p_player, this)) {
                    this.executeDeploy(this, p_gameState, p_player);
                    System.out.println("\nOrder has been executed successfully. " + this.getD_numberOfArmiesToDeploy()
                        + " number of armies has been deployed to country : "
                        + this.getD_targetCountryName());
                } else {
                    System.out.println("\nOrder is invalid, because the target country given does not belong to player : "
                            + p_player.getPlayerName());
                }
                break;
            default:
                System.out.println("Invalid order!!, Order execution aborted.");
        }
    }

    /**
     * Validates whether country given for deploy belongs to player or not
     *
     * @param p_player player whose order is being executed
     * @param p_order order which is being executed
     * @return true/false
     */
    public boolean validateOrderCountry(Player p_player, Order p_order) {
        Country l_country = p_player.getOwnedCountries().stream()
                .filter(l_pc -> l_pc.getD_name().equalsIgnoreCase(p_order.getD_targetCountryName())).findFirst()
                .orElse(null);

        return l_country != null;
    }

    /**
     * Executes deploy order and updates game state with latest map.
     *
     * @param p_order order which is being executed
     * @param p_gameState current state of the game
     * @param p_player player whose order is being executed
     */
    public void executeDeploy(Order p_order, GameState p_gameState, Player p_player) {
        for (Country l_country: p_gameState.getD_map().getCountriesList()) {
            if (l_country.getD_name().equalsIgnoreCase(p_order.getD_targetCountryName())) {
                Integer l_armiesToChange = l_country.getD_army() == null ? p_order.getD_numberOfArmiesToDeploy()
                        : l_country.getD_army() + p_order.getD_numberOfArmiesToDeploy();
                l_country.setD_army(l_armiesToChange);
            }
        }
    }
}
