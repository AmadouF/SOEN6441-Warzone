package Models;

import java.util.List;

/**
 * This class is used to store the current state of game.
 * It stores the Game Map, list of players, list of orders, Error
 */
public class GameState {

    /**
     * Map object to store the game map details for the current game
     */
    private Map d_map;

    /**
     * List of Players in the current game
     */
    private List<Player> d_players;

    /**
     * list of orders in queue left to be executed.
     */
    private List<Order> d_ordersToBeExecuted;

    /**
     * Error message
     */
    private String d_error;

    /**
     * Log Entry Buffer to write messages to log file
     */
    LogEntryBuffer d_logEntryBuffer = new LogEntryBuffer();

    /**
     * Getter method to get the game map
     *
     * @return Map
     */
    public Map getD_map() {
        return d_map;
    }

    /**
     * Setter method to set the game map
     *
     * @param p_map Map to be set for current game
     */
    public void setD_map(Map p_map) {
        this.d_map = p_map;
    }

    /**
     * Getter method to get the list of players
     *
     * @return list of players
     */
    public List<Player> getD_players() {
        return d_players;
    }

    /**
     * Setter method to set the list of players.
     *
     * @param p_players list of players
     */
    public void setD_players(List<Player> p_players) {
        this.d_players = p_players;
    }

    /**
     * Getter method to get the list of orders to be executed
     *
     * @return list of orders
     */
    public List<Order> getD_ordersToBeExecuted() {
        return d_ordersToBeExecuted;
    }

    /**
     * Setter method to set the list of orders to be executed
     *
     * @param p_ordersToBeExecuted list of orders
     */
    public void setD_ordersToBeExecuted(List<Order> p_ordersToBeExecuted) {
        this.d_ordersToBeExecuted = p_ordersToBeExecuted;
    }

    /**
     * Getter method for the error message
     *
     * @return Error message string
     */
    public String getError() {
        return d_error;
    }

    /**
     * Setter method to set the error message
     *
     * @param p_error error message
     */
    public void setError(String p_error) {
        this.d_error = p_error;
    }

    /**
     *
     * @param p_logMessage Message to be logged
     * @param p_logType Type of Log : Start, end, Phase, Command, Order
     */
    public void addLogMessage(String p_logMessage, String p_logType) {
        d_logEntryBuffer.logMessage(p_logMessage, p_logType);
    }
}
