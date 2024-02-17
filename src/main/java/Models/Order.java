package Models;

public class Order {
    String d_orderAction;
    String d_targetCountryName;
    String d_sourceCountryName;
    Integer d_numberOfArmiesToDeploy;
    Order order;

    public Order() {

    }

    public Order(String p_orderAction, String p_targetCountryName, Integer p_numberOfArmiesToDeploy) {
        this.d_orderAction = p_orderAction;
        this.d_targetCountryName = p_targetCountryName;
        this.d_numberOfArmiesToDeploy = p_numberOfArmiesToDeploy;
    }

    public String getD_orderAction() {
        return this.d_orderAction;
    }

    public void setD_orderAction(String p_orderAction) {
        this.d_orderAction = p_orderAction;
    }

    public String getD_targetCountryName() {
        return this.d_targetCountryName;
    }

    public void setD_targetCountryName(String p_targetCountryName) {
        this.d_targetCountryName = p_targetCountryName;
    }

    public String getD_sourceCountryName() {
        return this.d_sourceCountryName;
    }

    public void setD_sourceCountryName(String p_sourceCountryName) {
        this.d_sourceCountryName = p_sourceCountryName;
    }

    public Integer getD_numberOfArmiesToDeploy() {
        return this.d_numberOfArmiesToDeploy;
    }

    public void setD_numberOfArmiesToDeploy(Integer p_numberOfArmiesToDeploy) {
        this.d_numberOfArmiesToDeploy = p_numberOfArmiesToDeploy;
    }

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

    public boolean validateOrderCountry(Player p_player, Order p_order) {
        Country l_country = p_player.getOwnedCountries().stream()
                .filter(l_pc -> l_pc.getD_name().equalsIgnoreCase(p_order.getD_targetCountryName())).findFirst()
                .orElse(null);

        return l_country != null;
    }

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
