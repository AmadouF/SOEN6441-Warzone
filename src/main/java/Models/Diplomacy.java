package Models;

import Helpers.PlayerHelper;

/**
 * Represent the diplomacy command
 */
public class Diplomacy implements Card {
    /**
     * Player who initiated to negotiation order
     */
    Player d_issuingPlayer;

    /**
     * Player name with whom the initiator wants to negotiate
     */
    String d_targetPlayerName;

    /**
     * Log message of execution
     */
    String d_orderExecutionLog;

    /**
     * Constructor that created the diplomacy order
     *
     * @param p_issuingPlayer Player who initiated the order
     * @param p_targetPlayerName Player with whom the initiator wants to negotiate
     */
    public Diplomacy(Player p_issuingPlayer, String p_targetPlayerName) {
        this.d_issuingPlayer = p_issuingPlayer;
        this.d_targetPlayerName = p_targetPlayerName;
    }

    /**
     * Gets the name of current order
     *
     * @return String
     */
    @Override
    public String getOrderName() {
        return "diplomacy";
    }

    /**
     * Gets the details of diplomacy order
     *
     * @return String
     */
    private String currentOrder() {
        return "Diplomacy order - negotiate " + this.d_targetPlayerName;
    }

    /**
     * Executes the diplomacy order
     *
     * @param p_gameState current state of the game
     */
    @Override
    public void execute(GameState p_gameState) {
        PlayerHelper l_playerHelper = new PlayerHelper();

        Player l_targetPlayer = l_playerHelper.findPlayerByName(d_targetPlayerName, p_gameState);

        l_targetPlayer.addPlayerNegotiation(d_issuingPlayer);
        d_issuingPlayer.addPlayerNegotiation(l_targetPlayer);

        this.setD_orderExecutionLog("Negotiation with " + l_targetPlayer.getPlayerName()
        + " approached by " + d_issuingPlayer.getPlayerName() + "is successful!!", "default");

        p_gameState.addLogMessage(orderExecutionLog(), "effect");
    }

    /**
     * Check if the command is valid
     *
     * @param p_gameState current state of the game
     * @return true
     */
    @Override
    public boolean isValid(GameState p_gameState) {
        return true;
    }

    /**
     * Gets the log of execution
     *
     * @return execution log
     */
    @Override
    public String orderExecutionLog() {
        return this.d_orderExecutionLog;
    }

    /**
     * Sets the log message for execution
     *
     * @param p_orderExecutionLog log message
     * @param p_logType type of the log
     */
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
        this.d_orderExecutionLog = p_orderExecutionLog;

        if (p_logType.equals("error")) {
            System.err.println(p_orderExecutionLog);
        } else {
            System.out.println(p_orderExecutionLog);
        }
    }

    /**
     * Checks if order is valid or not
     *
     * @param p_gameState current state of the game
     * @return true or false
     */
    @Override
    public Boolean checkValidOrder(GameState p_gameState) {
        PlayerHelper l_playerHelper = new PlayerHelper();

        Player l_targetPlayer = l_playerHelper.findPlayerByName(d_targetPlayerName, p_gameState);

        if (!p_gameState.getD_players().contains(l_targetPlayer)) {
            this.setD_orderExecutionLog("Provided player does not exist!!", "error");

            p_gameState.addLogMessage(orderExecutionLog(), "effect");

            return false;
        }

        return true;
    }

    /**
     * Prints the details of the log
     */
    @Override
    public void printOrder() {
        this.d_orderExecutionLog = "******* Diplomacy order issued by player " + d_issuingPlayer.getPlayerName()
                + " *******" + System.lineSeparator() + "Request to negotiate from" + this.d_targetPlayerName;

        System.out.println(System.lineSeparator() + this.d_orderExecutionLog);
    }
}
