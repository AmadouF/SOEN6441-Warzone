package Models;

import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import GameEngine.GameEngine;
import Helpers.MapHelper;
import Helpers.PlayerHelper;
import Utils.Command;

import java.io.IOException;

/**
 * This class represents a phase in the game.
 */
public abstract class Phase {
    
    /** Stores the information about current Game State */
    protected GameState d_gameState;

    /** Stores the map helper information */
    protected MapHelper d_mapHelper;

    /** Helper to change the state of a player object */
    protected PlayerHelper d_playerHelper;

    /** The game engine */
    protected GameEngine d_gameEngine;

    /** The name of the phase */
    public String d_phase_name;

    /**
     * Constructor for Phase.
     * @param p_currentGameEngine The current game engine.
     * @param p_currentGameState The current game state.
     */
    public Phase(GameEngine p_currentGameEngine, GameState p_currentGameState){
        d_gameEngine = p_currentGameEngine;
        d_gameState = p_currentGameState;
        d_mapHelper = new MapHelper();
        d_playerHelper = new PlayerHelper();
    }

    /**
     * Getter method for current game state.
     * @return The current GameState object.
     */
    public GameState getD_gameState() {
        return d_gameState;
    }

    /**
     * Getter method for phase name.
     * @return The phase name.
     */
    public String getPhaseName(){
        return d_phase_name;
    }

    /**
     * Processes a command from the user.
     * @param p_commandInput The command from the user.
     * @throws InvalidMap Thrown if the map is invalid.
     * @throws IOException Thrown if an IO error occurs.
     * @throws InvalidCommand Thrown if the command is invalid.
     */
    public void processCommand(String p_commandInput) throws InvalidMap, IOException, InvalidCommand {
        Command l_playerCommand = new Command(p_commandInput);
        String l_firstCommand = l_playerCommand.getFirstCommand();
        d_gameState.addLogMessage(l_playerCommand.getD_playerCommand(), "command");

        switch (l_firstCommand) {
            case "editmap":
                d_gameEngine.commonCommandExecutorWithArgumentsOnly(l_playerCommand, l_firstCommand);
                break;

            case "savemap":
                if(d_gameEngine.checkIfMapIsNotLoaded(l_firstCommand)){
                    break;
                }
                d_gameEngine.commonCommandExecutorWithArgumentsOnly(l_playerCommand, l_firstCommand);
                break;

            case "validatemap":
                if(d_gameEngine.checkIfMapIsNotLoaded(l_firstCommand)){
                    break;
                }
                d_gameEngine.commonCommandExecutorWithNoArguments(l_playerCommand, l_firstCommand);
                break;

            case "showmap":
                d_gameEngine.commonCommandExecutorWithNoArguments(l_playerCommand, l_firstCommand);
                break;

            case "editcontinent", "editneighbor", "editcountry":
                d_gameEngine.commonCommandExecutorWithArgumentsAndOperations(l_playerCommand, l_firstCommand);
                break;

            case "gameplayer":
                if(d_gameEngine.checkIfMapIsNotLoaded(l_firstCommand)){
                    break;
                }
                gamePlayer(l_playerCommand, l_firstCommand);
                break;

            case "loadmap":
                loadMap(l_playerCommand);
                break;

            case "assigncountries":
                if(d_gameEngine.checkIfMapIsNotLoaded(l_firstCommand)){
                    break;
                }
                assignCountries(l_playerCommand);
                break;

            case "deploy":
                deployCommand(p_commandInput);
                break;

            case "advance":
                advanceCommand(p_commandInput);
                break;

            case "airlift":
            case "blockade":
            case "negotiate":
            case "bomb":
                {
                    CardCommand(p_commandInput);
                    break;
                }
                
            default:
                throw new InvalidMap(" !!!  Base command Invalid  !!!");
        }
    }

    /**
     * This method is used to load a map file.
     * @param p_command The command for loading the map.
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException Thrown if an IO error occurs.
     * @throws InvalidMap Thrown if the map is invalid.
     */
    protected abstract void loadMap(Command p_command) throws InvalidCommand, IOException, InvalidMap;

    /**
     * This method is used to add a game player to the current game.
     * @param p_command The command object storing input command data.
     * @param baseCommand The base command of the gameplayer command input string.
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException Thrown if an IO error occurs.
     * @throws InvalidMap Thrown if the map is invalid.
     */
    protected abstract void gamePlayer(Command p_command, String baseCommand) throws InvalidCommand, IOException, InvalidMap;

    /**
     * This method is used to start the game loop with an already loaded map file.
     * @param p_command The command object with assign country argument.
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException Thrown if an IO error occurs.
     * @throws InvalidMap Thrown if the map is invalid.
     */
    protected abstract void assignCountries(Command p_command) throws InvalidCommand, IOException, InvalidMap;

    
    /**
     * This method is used to tp perfomr deploy command.
     * @param p_command The command object with assign country argument.
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException Thrown if an IO error occurs.
     * @throws InvalidMap Thrown if the map is invalid.
     */
    protected abstract void deployCommand(String p_command) throws InvalidCommand, IOException, InvalidMap;

    /**
     * This method is used to tp perfomr deploy command.
     * @param p_command The command object with assign country argument.
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException Thrown if an IO error occurs.
     * @throws InvalidMap Thrown if the map is invalid.
     */
    protected abstract void advanceCommand(String p_command) throws InvalidCommand, IOException, InvalidMap;

    protected abstract void CardCommand(String p_command) throws InvalidCommand, IOException, InvalidMap;

    /**
     * This method initializes the current phase.
     * @throws IOException Thrown if an IO error occurs.
     */
    public abstract void startPhase() throws IOException;

    /**
     * Prints that the current command cannot be executed in the current state.
     */
    public void printCommandInvalidInCurrentState(){
        //TODO add log
        System.out.println("The entered command cannot be executed in the current state of the game. Please try again.");
    }
}
