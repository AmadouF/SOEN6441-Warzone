package Models;

public class Bomb implements Card {
    Player d_playerInitiator;
    String d_targetCountryName;
    String d_orderExecutionLog;

    public Bomb(Player p_playerInitiator, String p_targetCountryName) {
        this.d_playerInitiator = p_playerInitiator;
        this.d_targetCountryName = p_targetCountryName;
    }

    private String currentOrder() {
        return "Bomb card order - " + "bomb" + " " + this.d_targetCountryName;
    }

    @Override
    public String getOrderName() {
        return "bomb";
    }

    @Override
    public void execute(GameState p_gameState) {
        if (isValid(p_gameState)) {
            Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountryName);
            Integer l_numOfArmiesOnTargetCountry = l_targetCountry.getD_army() == 0 ? 1 :
                    l_targetCountry.getD_army();

            Integer l_newArmies = (int) Math.floor((double) l_numOfArmiesOnTargetCountry / 2);
            l_targetCountry.setD_army(l_newArmies);

            // TODO: remove card from player

            this.setD_orderExecutionLog("\nPlayer - " + this.d_playerInitiator.getPlayerName() +
                    " is bombing on country - " + l_targetCountry.getD_name() + " with armies : " + l_numOfArmiesOnTargetCountry +
                    ". New armies - " + l_targetCountry.getD_army(), "default");

            // TODO: Update the log
        }
    }

    @Override
    public boolean isValid(GameState p_gameState) {
        Country l_ownCountry = d_playerInitiator.getOwnedCountries().stream()
                .filter(l_pl -> l_pl.getD_name().equalsIgnoreCase(this.d_targetCountryName.toString()))
                .findFirst().orElse(null);

        // Player cannot bomb his own country
        if (l_ownCountry != null) {
            // TODO: Log error for bombing own country

            return false;
        }

        // TODO: Check for if the player has negotiated for a pact

        return true;
    }

    @Override
    public String orderExecutionLog() {
        return d_orderExecutionLog;
    }

    @Override
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_typeOfLog) {
        this.d_orderExecutionLog = p_orderExecutionLog;

        if (p_typeOfLog.equals("error")) {
            // TODO: Log the error
        } else {
            // TODO: Log the message
        }
    }

    @Override
    public Boolean checkValidOrder(GameState p_gameState) {
        Country l_targetCountry = p_gameState.getD_map().getCountryByName(d_targetCountryName);

        if (l_targetCountry == null) {
            this.setD_orderExecutionLog("Provided country does not exist!!", "error");
            return false;
        }

        return true;
    }

    @Override
    public void printOrder() {
        this.d_orderExecutionLog = "\n******* Bomb card order issued by " + this.d_playerInitiator.getPlayerName() + " *******\n"
                + System.lineSeparator() + "Creating a bomb order on " + this.d_targetCountryName;

        // TODO: Log the message
    }
}
