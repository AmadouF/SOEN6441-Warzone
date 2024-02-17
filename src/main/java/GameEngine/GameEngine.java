package GameEngine;

import Models.GameState;

/**
 * This is the main controller for the game and will maintain game state, player turns etc. for the game
 */
public class GameEngine {

    /**
     * Stores the information about current Game State
     */
    GameState d_gameState;

    /**
     * Constructor for GameEngine
     * This initiates a new blank GameState
     */
    public GameEngine(){
        d_gameState = new GameState();
    }

    /**
     *  Getter method for current game state
     *
     * @return the current GameState Object
     */
    public GameState getD_gameState() {
        return d_gameState;
    }

    /**
     * Main method to start game
     * @param p_args the program doesn't use default command line arguments
     */
    public static void main(String[] p_args) {
        GameEngine l_gameEngine = new GameEngine();

        l_gameEngine.startGame();
    }

    /**
     *
     */
    private void startGame() {
    }

}
