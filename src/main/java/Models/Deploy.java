package Models;

public class Deploy implements Order {
    String d_targetCountryName;
    Integer d_numberOfArmiesToDeploy;
    Player d_playerInitiator;
    String d_orderExecutionLog;

    public Deploy(Player p_playerInitiator, String p_targetCountryName, Integer p_numberOfArmiesToDeploy) {
        this.d_targetCountryName = p_targetCountryName;
        this.d_playerInitiator = p_playerInitiator;
        this.d_numberOfArmiesToDeploy = p_numberOfArmiesToDeploy;
    }

    @Override
    public String getOrderName() {
        return "deploy";
    }

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

    @Override
    public boolean isValid(GameState p_gameState) {
        Country l_country = d_playerInitiator.getOwnedCountries().stream()
                .filter(l_pl -> l_pl.getD_name().equalsIgnoreCase(this.d_targetCountryName.toString()))
                .findFirst().orElse(null);

        return l_country != null;
    }

    @Override
    public String orderExecutionLog() {
        return d_orderExecutionLog;
    }

    @Override
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_typeOfLog) {
        this.d_orderExecutionLog = p_orderExecutionLog;

        if (p_typeOfLog.equals("error")) {
            System.err.println(p_orderExecutionLog);
        } else {
            System.out.println(p_orderExecutionLog);
        }
    }

    @Override
    public void printOrder() {
        this.d_orderExecutionLog = "\n******* Deploy order issued by " + this.d_playerInitiator.getPlayerName() + " *******\n"
                + System.lineSeparator() + "Deploy " + this.d_numberOfArmiesToDeploy + " armies to " + this.d_targetCountryName;

        System.out.println(System.lineSeparator() + this.d_orderExecutionLog);
    }
}
