package Models;

/**
 * This class the cards owned by a player
 */
public interface Card extends Order {
    /**
     * Validate the card before creating order
     *
     * @param p_gameState
     * @return true of false
     */
    public Boolean checkValidOrder(GameState p_gameState);
}