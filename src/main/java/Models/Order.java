package Models;

/**
 * This model manages the orders given in the game by players.
 */
public interface Order {
    public void execute(GameState p_gameState);

    public boolean isValid(GameState p_gameState);

    public String orderExecutionLog();

    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_typeOfLog);

    public String getOrderName();

    public void printOrder();
}
