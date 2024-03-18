package GameEngine;

import Constants.Constants;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Helpers.MapHelper;
import Helpers.PlayerHelper;
import Models.GameState;
import Models.Phase;
import Models.StartUpPhase;
import Utils.Command;
import Views.MapView;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * This is the main controller for the game and will maintain game state, player turns etc. for the game
 */
public class GameEngine {

    /**
     * Stores the information about current Game State
     */
    private GameState d_gameState;

    /**
     * Stores the information about the
     */
   private  MapHelper d_mapHelper;

    /**
     * Helper to change the state of a player object
     */
    private PlayerHelper d_playerHelper;

    private Phase d_currentPhase;

    /**
     * Constructor for GameEngine
     * This initiates a new blank GameState
     */
    public GameEngine() {
        d_gameState = new GameState();
        d_mapHelper = new MapHelper();
        d_playerHelper = new PlayerHelper();
        d_currentPhase = new StartUpPhase(this, d_gameState);
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
     *  Getter method for current game phase
     *
     * @return the current phase Object
     */
	public Phase getD_CurrentPhase(){
		return d_currentPhase;
	}

    public void setCurrentPhase(Phase p_currentPhase){
        System.out.println();
        d_currentPhase = p_currentPhase;
    }


    /**
     * Main method to start game
     * @param p_args the program doesn't use default command line arguments
     */
    public static void main(String[] p_args) throws IOException{
        GameEngine l_gameEngine = new GameEngine();

        l_gameEngine.getD_CurrentPhase().getD_gameState().addLogMessage(Constants.STARTING_THE_GAME_LOG_MESSAGE, "start");
        l_gameEngine.commonGameEngineLogger("------- Startup Phase -------", "phase");
        l_gameEngine.getD_CurrentPhase().startPhase();
    }


    /**
     * This method is used to execute command which have both arguments and operations
     * @param p_command command object storing argument and operations
     * @param p_firstCommand base command of the current command input string
     * @throws IOException
     * @throws InvalidMap
     * @throws InvalidCommand
     */
    public void commonCommandExecutorWithArgumentsAndOperations(Command p_command, String p_firstCommand) throws IOException, InvalidMap, InvalidCommand {
        if(checkIfMapIsNotLoaded(p_firstCommand)){
            return;
        }
        List < Map < String, String >> l_listOfOperations = p_command.getListOfOperationsAndArguments();

        if (CollectionUtils.isEmpty(l_listOfOperations)) {
            throw new InvalidCommand("No arguments and operations are provided for " + p_firstCommand);
        }

        for (Map < String, String > l_map: l_listOfOperations) {
            if (p_command.validateArgumentAndOperation(l_map)) {
                if("editcontinent".equals(p_firstCommand)) {
                    d_mapHelper.editContinent(d_gameState, l_map.get(Constants.ARGUMENT), l_map.get(Constants.OPERATION));
                } else if ("editcountry".equals(p_firstCommand)) {
                    d_mapHelper.editCountry(d_gameState, l_map.get(Constants.ARGUMENT), l_map.get(Constants.OPERATION));
                } else if ("editneighbor".equals(p_firstCommand)) {
                    d_mapHelper.editNeighbour(d_gameState, l_map.get(Constants.ARGUMENT), l_map.get(Constants.OPERATION));
                }
            } else {
                throw new InvalidCommand("Invalid arguments provided for " + p_firstCommand);
            }
        }
    }

    /**
     * This method is used to execute command which have arguments only and no operations
     * @param p_command command object storing arguments
     * @param baseCommand base command of the current command input string
     * @throws IOException
     * @throws InvalidMap
     * @throws InvalidCommand
     */
    public void commonCommandExecutorWithArgumentsOnly(Command p_command, String baseCommand) throws IOException, InvalidMap, InvalidCommand {

        List<Map<String, String>> l_listOfOperationsAndArguments = p_command.getListOfOperationsAndArguments();

        if (CollectionUtils.isEmpty(l_listOfOperationsAndArguments)) {
            throw new InvalidCommand("No arguments and operations are provided for " + baseCommand);
        }

        for (Map<String, String> l_map : l_listOfOperationsAndArguments) {
            if (p_command.validateArgumentsOnly(l_map)) {
                if("editmap".equals(baseCommand)) {
                    d_mapHelper.editMap(d_gameState, l_map.get(Constants.ARGUMENT));
                } else if("savemap".equals(baseCommand)) {
                    if (d_mapHelper.saveMap(d_gameState, l_map.get(Constants.ARGUMENT))) {
                        System.out.println("savemap has successfully updated the map in file");
                    }
                    else {
                        System.out.println(d_gameState.getError());
                    }
                }
            } else {
                throw new InvalidCommand("Invalid arguments provided for " + baseCommand);
            }
        }
    }

    /**
     * This method is used to execute command which have no arguments
     * @param p_command command object with no arguments
     * @param baseCommand base command of the current command input string
     * @throws IOException
     * @throws InvalidMap
     * @throws InvalidCommand
     */
    public void commonCommandExecutorWithNoArguments(Command p_command, String baseCommand) throws IOException, InvalidMap, InvalidCommand {
        if (CollectionUtils.isEmpty(p_command.getListOfOperationsAndArguments())) {
                if("validatemap".equals(baseCommand)) {
                    Models.Map l_currentMap = d_gameState.getD_map();
                    if (l_currentMap.isValidMap()) {
                        System.out.println("!!!!! Map Validation Successful !!!!!");
                    } else {
                        System.out.println("!!!!! Map Validation Failed !!!!!");
                    }
                } else if("showmap".equals(baseCommand)) {
                    MapView l_mapView = new MapView(d_gameState);
                    l_mapView.showMap();
                }
            } else {
                throw new InvalidCommand("Invalid Command. No arguments are allowed for : " + baseCommand);
            }
    }

    /**
     * This is method is check if map file is loading
     * @throws InvalidCommand
     */
    public boolean checkIfMapIsNotLoaded(String p_firstCommand) {
        if (d_gameState.getD_map() == null) {
            commonGameEngineLogger("!!! Cannot execute command <" + p_firstCommand+">, Map is required to be loaded first !!!", "effect");
            return true;
        }
        return false;
    }

    /**
     * Common method to print log to console and also add to log file
     *
     * @param p_log Message to be logged
     * @param p_logType Type of log message
     */
    public void commonGameEngineLogger(String p_log, String p_logType) {
        d_currentPhase.getD_gameState().addLogMessage(p_log, p_logType);
        String l_printMessage = p_logType.equalsIgnoreCase("phase")
                ? "\n\n ----------- " + p_log + "-------------- \n\n"
                : p_log;
        System.out.println(l_printMessage);
    }


}