package Models;

import Helpers.PlayerHelper;

public class Diplomacy implements Card {
    Player d_issuingPlayer;
    String d_targetPlayerName;
    String d_orderExecutionLog;

    public Diplomacy(Player p_issuingPlayer, String p_targetPlayerName) {
        this.d_issuingPlayer = p_issuingPlayer;
        this.d_targetPlayerName = p_targetPlayerName;
    }

    @Override
    public String getOrderName() {
        return "diplomacy";
    }

    private String currentOrder() {
        return "Diplomacy order - negotiate " + this.d_targetPlayerName;
    }

    @Override
    public void execute(GameState p_gameState) {
        PlayerHelper l_playerHelper = new PlayerHelper();

        Player l_targetPlayer = l_playerHelper.findPlayerByName(d_targetPlayerName, p_gameState);

        l_targetPlayer.addPlayerNegotiation(d_issuingPlayer);
        d_issuingPlayer.addPlayerNegotiation(l_targetPlayer);

        this.setD_orderExecutionLog("Negotiation with " + l_targetPlayer.getPlayerName()
        + " approached by " + d_issuingPlayer.getPlayerName() + "is successful!!", "default");

        // TODO: Update the log
    }

    @Override
    public boolean isValid(GameState p_gameState) {
        return true;
    }

    @Override
    public String orderExecutionLog() {
        return this.d_orderExecutionLog;
    }

    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
        this.d_orderExecutionLog = p_orderExecutionLog;

        if (p_logType.equals("error")) {
            // TODO: Log error
        } else {
            // TODO: Log message
        }
    }

    @Override
    public Boolean checkValidOrder(GameState p_gameState) {
        PlayerHelper l_playerHelper = new PlayerHelper();

        Player l_targetPlayer = l_playerHelper.findPlayerByName(d_targetPlayerName, p_gameState);

        if (!p_gameState.getD_players().contains(l_targetPlayer)) {
            this.setD_orderExecutionLog("Provided player does not exist!!", "error");
            // TODO: Update the log

            return false;
        }

        return true;
    }

    @Override
    public void printOrder() {
        this.d_orderExecutionLog = "******* Diplomacy order issued by player " + d_issuingPlayer.getPlayerName()
                + " *******" + System.lineSeparator() + "Request to negotiate from" + this.d_targetPlayerName;

        System.out.println(System.lineSeparator() + this.d_orderExecutionLog);
    }
}
