package Models;


import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import GameEngine.GameEngine;
import Helpers.MapHelper;
import Helpers.PlayerHelper;
import Utils.Command;

import java.io.IOException;


public abstract class Phase {
        /**
     * Stores the information about current Game State
     */
    protected GameState d_gameState;

    /**
     * Stores the information about the
     */
    protected  MapHelper d_mapHelper;

    /**
     * Helper to change the state of a player object
     */
    protected PlayerHelper d_playerHelper;

    protected GameEngine d_gameEngine;


    public Phase(GameEngine p_currentGameEngine, GameState p_currentGameState){
        d_gameEngine = p_currentGameEngine;
        d_gameState = p_currentGameState;
        d_gameState = new GameState();
        d_mapHelper = new MapHelper();
        d_playerHelper = new PlayerHelper();
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
     *
     * This method takes the command from the user like editmap, savemap, showmap etc.
     * @param p_commandInput command from the user
     * @throws InvalidMap
     * @throws IOException
     * @throws InvalidCommand
     */
    public void processCommand(String p_commandInput) throws InvalidMap, IOException, InvalidCommand {
        Command l_playerCommand = new Command(p_commandInput);
        String l_firstCommand = l_playerCommand.getFirstCommand();

        switch (l_firstCommand) {
            case "editmap":
            d_gameEngine.commonCommandExecutorWithArgumentsOnly(l_playerCommand, l_firstCommand);
                break;

            case "savemap":
                d_gameEngine.checkIfMapIsLoaded();
                d_gameEngine.commonCommandExecutorWithArgumentsOnly(l_playerCommand, l_firstCommand);
                break;

            case "validatemap":
                d_gameEngine.checkIfMapIsLoaded();
                d_gameEngine.commonCommandExecutorWithNoArguments(l_playerCommand, l_firstCommand);
                break;

            case "showmap":
                d_gameEngine.commonCommandExecutorWithNoArguments(l_playerCommand, l_firstCommand);
                break;

            case "editcontinent", "editneighbor", "editcountry":
                d_gameEngine.commonCommandExecutorWithArgumentsAndOperations(l_playerCommand, l_firstCommand);
                break;

            case "gameplayer":
                d_gameEngine.checkIfMapIsLoaded();
                gamePlayer(l_playerCommand, l_firstCommand);
                break;

            case "loadmap":
                loadMap(l_playerCommand);
                break;

            case "assigncountries":
                assignCountries(l_playerCommand);
                break;
            default:
                throw new InvalidMap(" !!!  Base command Invalid  !!!");

        }
    }

    /**
     * This method is used to load map file
     * @param p_command command of lading map
     * @throws InvalidCommand
     */
    protected abstract void loadMap(Command p_command) throws InvalidCommand, IOException, InvalidMap;

    /**
     * This method is used to add game player to the current game
     * @param p_command command object storing input command data
     * @param baseCommand base command of the gameplayer command input string
     * @throws InvalidCommand
     */
    protected abstract void gamePlayer(Command p_command, String baseCommand) throws InvalidCommand, IOException, InvalidMap;

    /**
     * This method is used to start the game loop with already loaded map file
     * @param p_command command object with assign country argument
     * @throws InvalidCommand
     * @throws IOException
     */
    protected abstract void assignCountries(Command p_command) throws InvalidCommand, IOException, InvalidMap;

    /**
     * This is the methods initializing the current phase
     */
    public abstract void startPhase();

    /**
     * Method to the print that the current command can't be executed in the current state
     */
    public void printCommandInvalidInCurrentState(){
        //TODO add log
        System.out.println("The entered command cannot be executed in the current state of the game. Please try again.");
    }
}
