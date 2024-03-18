package Models;

/**
 * This model manages the orders given in the game by players.
 */
public interface Order {
    /**
     * Method that will be called by player to execute the order
     *
     * @param p_gameState current state of the game
     */
    public void execute(GameState p_gameState);

    /**
     * Validates the order before execution
     *
     * @param p_gameState current state of the game
     * @return true or false
     */
    public boolean isValid(GameState p_gameState);

    /**
     * Gets the log of execution
     *
     * @return String
     */
    public String orderExecutionLog();

    /**
     * Sets the execution log message
     *
     * @param p_orderExecutionLog log message
     * @param p_typeOfLog type of the log
     */
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_typeOfLog);

    /**
     * Gets the name of the order
     *
     * @return String
     */
    public String getOrderName();

    /**
     * Prints the details about the current order
     */
    public void printOrder();
}
