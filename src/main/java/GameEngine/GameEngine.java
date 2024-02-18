package GameEngine;

import Constants.Constants;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Helpers.MapHelper;
import Helpers.PlayerHelper;
import Models.GameState;
import Utils.Command;
import org.apache.commons.collections4.CollectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * This is the main controller for the game and will maintain game state, player turns etc. for the game
 */
public class GameEngine {

    /**
     * Stores the information about current Game State
     */
    GameState d_gameState;

    MapHelper d_mapHelper;

    PlayerHelper d_playerHelper;

    /**
     * Constructor for GameEngine
     * This initiates a new blank GameState
     */
    public GameEngine(){
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

        BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                System.out.println("-------- Enter Command to be executed [enter 'exit' to quit] --------");
                String l_command = l_bufferedReader.readLine();

                processCommand(l_command);
                if ("exit".equalsIgnoreCase(l_command.trim())){
                    System.out.println("---------------- Thanks for Playing ----------------");
                }
            } catch (IOException | InvalidMap | InvalidCommand l_exception) {
                l_exception.printStackTrace();
            }
        }
    }

    public void processCommand(String p_commandInput) throws InvalidMap, IOException, InvalidCommand {
        Command l_playerCommand = new Command(p_commandInput);
        String l_firstCommand = l_playerCommand.getFirstCommand();
        // TODO check if game is loaded

        switch(l_firstCommand){
            case "editmap":
                editCommand(l_playerCommand, "editmap");
                break;

            case "editcontinent":
                editCommand(l_playerCommand, "editcontinent");
                break;

            case "editcountry":
                editCommand(l_playerCommand, "editcountry");
                break;

            case "editneighbor":
                editCommand(l_playerCommand, "editneighbor");
                break;

            default:
                throw new InvalidMap(" !!!  Base command Invalid  !!!");

        }
    }

    /**
     *
     */
    private void editCommand(Command p_command, String baseCommand) throws IOException, InvalidMap, InvalidCommand {
        checkIfMapIsLoaded();
        List<Map<String, String>> l_listOfOperations = p_command.getListOfOperationsAndArguments();

        if (CollectionUtils.isEmpty(l_listOfOperations)) {
            throw new InvalidCommand("No arguments and operations are provided for " + baseCommand);
        }

        for (Map<String, String> l_map : l_listOfOperations) {
            if (p_command.validateArgumentAndOperation(l_map)) {
                if("editmap".equals(baseCommand)) {
                    d_mapHelper.editMap(d_gameState, l_map.get(Constants.ARGUMENT));
                } else if("editcontinent".equals(baseCommand)) {
                    d_mapHelper.editContinent(d_gameState, l_map.get(Constants.ARGUMENT), l_map.get(Constants.OPERATION));
                } else if("editcountry".equals(baseCommand)){
                    d_mapHelper.editContinent(d_gameState, l_map.get(Constants.ARGUMENT), l_map.get(Constants.OPERATION));
                } else if("editneighbor".equals(baseCommand)){
                    d_mapHelper.editNeighbour(d_gameState, l_map.get(Constants.ARGUMENT), l_map.get(Constants.OPERATION));
                }
            } else {
                throw new InvalidCommand("Invalid arguments provided for " + baseCommand);
            }
        }
    }

    private void checkIfMapIsLoaded() throws InvalidCommand {
        if (d_gameState.getD_map() == null) {
            throw new InvalidCommand("Cannot execute this command, Map is required to be loaded first");
        }
    }

}
