package GameEngine;

import Constants.Constants;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Helpers.MapHelper;
import Helpers.PlayerHelper;
import Models.GameState;
import Models.Order;
import Models.Player;
import Utils.Command;
import Views.MapView;
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
    private GameState d_gameState;

   private  MapHelper d_mapHelper;

    private PlayerHelper d_playerHelper;

    /**
     * Constructor for GameEngine
     * This initiates a new blank GameState
     */
    public GameEngine() {
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
        while (true) {
            try {
                System.out.println("-------- Enter Command to be executed [enter 'exit' to quit] --------");
                String l_command = l_bufferedReader.readLine();

                //checking for exit command
                if ("exit".equalsIgnoreCase(l_command.trim())) {
                    System.out.println("---------------- Thanks for Playing ----------------");
                    System.exit(0);
                }

                processCommand(l_command);
            } catch (Exception l_exception) {
                l_exception.printStackTrace();
            }
        }
    }

    public void processCommand(String p_commandInput) throws InvalidMap, IOException, InvalidCommand {
        Command l_playerCommand = new Command(p_commandInput);
        String l_firstCommand = l_playerCommand.getFirstCommand();

        switch (l_firstCommand) {
            case "editmap":
                commonCommandExecutorWithArgumentsOnly(l_playerCommand, l_firstCommand);
                break;

            case "savemap":
                checkIfMapIsLoaded();
                commonCommandExecutorWithArgumentsOnly(l_playerCommand, l_firstCommand);
                break;

            case "validatemap":
                checkIfMapIsLoaded();
                commonCommandExecutorWithNoArguments(l_playerCommand, l_firstCommand);
                break;

            case "showmap":
                commonCommandExecutorWithNoArguments(l_playerCommand, l_firstCommand);
                break;

            case "editcontinent":
                commonCommandExecutorWithArgumentsAndOperations(l_playerCommand, l_firstCommand);
                break;
            
            case "editneighbor":
                commonCommandExecutorWithArgumentsAndOperations(l_playerCommand, l_firstCommand);
                break;
            case "editcountry":
                commonCommandExecutorWithArgumentsAndOperations(l_playerCommand, l_firstCommand);
                break;

            case "gameplayer":
                checkIfMapIsLoaded();
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

    public void loadMap(Command p_command) throws InvalidCommand {
        List < Map < String, String >> l_listOfOperations = p_command.getListOfOperationsAndArguments();
        if (CollectionUtils.isEmpty(l_listOfOperations)) {
            throw new InvalidCommand("No arguments and operations are provided for the laodmap");
        }
        for (Map < String, String > l_map: l_listOfOperations) {
            if (p_command.validateArgumentAndOperation(l_map)) {
                Models.Map l_inputMap = d_mapHelper.load(d_gameState,
                    l_map.get(Constants.ARGUMENT));
                try {
                    if (!l_inputMap.isValidMap()) {
                        // Clear the map in case of an unsuccessful load
                        d_gameState.setD_map(new Models.Map());
                    } else {
                        System.out.println("The maps has been loaded");
                    }
                } catch (InvalidMap l_invalidMapException) {
                    System.out.println(l_invalidMapException.getMessage());
                    d_gameState.setD_map(new Models.Map());
                }
            } else {
                throw new InvalidCommand("The command for loadmap is invalid");
            }
        }

    }


    public void gamePlayer(Command p_command, String baseCommand) throws InvalidCommand {
        List < Map < String, String >> l_listOfOperations = p_command.getListOfOperationsAndArguments();
        if (CollectionUtils.isEmpty(l_listOfOperations)) {
            throw new InvalidCommand("No arguments and operations are provided for " + baseCommand);
        }
        for (Map < String, String > l_map: l_listOfOperations) {
            if (p_command.validateArgumentAndOperation(l_map)) {
                d_playerHelper.updatePlayers(d_gameState, l_map.get(Constants.OPERATION), l_map.get(Constants.ARGUMENT));
            } else {
                throw new InvalidCommand("No arguments or operations are provided for " + baseCommand);
            }
        }

    }

    /**
     *
     */
    private void commonCommandExecutorWithArgumentsAndOperations(Command p_command, String baseCommand) throws IOException, InvalidMap, InvalidCommand {
        checkIfMapIsLoaded();
        List < Map < String, String >> l_listOfOperations = p_command.getListOfOperationsAndArguments();

        if (CollectionUtils.isEmpty(l_listOfOperations)) {
            throw new InvalidCommand("No arguments and operations are provided for " + baseCommand);
        }

        for (Map < String, String > l_map: l_listOfOperations) {
            if (p_command.validateArgumentAndOperation(l_map)) {
                if("editcontinent".equals(baseCommand)) {
                    d_mapHelper.editContinent(d_gameState, l_map.get(Constants.ARGUMENT), l_map.get(Constants.OPERATION));
                } else if ("editcountry".equals(baseCommand)) {
                    d_mapHelper.editCountry(d_gameState, l_map.get(Constants.ARGUMENT), l_map.get(Constants.OPERATION));
                } else if ("editneighbor".equals(baseCommand)) {
                    d_mapHelper.editNeighbour(d_gameState, l_map.get(Constants.ARGUMENT), l_map.get(Constants.OPERATION));
                }
            } else {
                throw new InvalidCommand("Invalid arguments provided for " + baseCommand);
            }
        }
    }

    private void commonCommandExecutorWithArgumentsOnly(Command p_command, String baseCommand) throws IOException, InvalidMap, InvalidCommand {

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

    private void commonCommandExecutorWithNoArguments(Command p_command, String baseCommand) throws IOException, InvalidMap, InvalidCommand {
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

    private void checkIfMapIsLoaded() throws InvalidCommand {
        if (d_gameState.getD_map() == null) {
            throw new InvalidCommand("Cannot execute this command, Map is required to be loaded first");
        }
    }

    public void assignCountries(Command p_command) throws InvalidCommand, IOException {
        checkIfMapIsLoaded();
        List<Map<String, String>> l_listOfOperationsAndArguments = p_command.getListOfOperationsAndArguments();
        if (CollectionUtils.isEmpty(l_listOfOperationsAndArguments)) {
            d_playerHelper.assignCountries(d_gameState);
            startGameLoop();
        } else {
            throw new InvalidCommand("Invalid command. No arguments expected for command 'assigncountries'");
        }
    }

    private void startGameLoop() throws IOException {
        System.out.println("\n\n ------------ Game Starting Now -------------- \n");
        for (int l_i=1; CollectionUtils.isNotEmpty(d_gameState.getD_players()) && d_gameState.getD_players().size()>1 ; l_i++) {
            System.out.println("\n\n ------------ Round " + l_i + " -------------- \n");

            // Assigning army personal to each player
            d_playerHelper.assignArmies(d_gameState);

            // Issuing order for players
            while (d_playerHelper.unassignedArmiesExists(d_gameState.getD_players())) {
                for (Player l_player : d_gameState.getD_players()) {
                    if (l_player.getReinforcements() != null && l_player.getReinforcements() != 0)
                        l_player.issue_order();
                }
            }

            // Executing orders
            while (d_playerHelper.unexecutedOrdersExists(d_gameState.getD_players())) {
                for (Player l_player : d_gameState.getD_players()) {
                    Order l_order = l_player.next_order();
                    if (l_order != null)
                        l_order.execute(d_gameState, l_player);
                }
            }
            MapView l_view = new MapView(d_gameState, d_gameState.getD_players());
            l_view.showMap();

            System.out.println("Press 'y' to continue for next turn or else press 'n' to exit");
            BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String l_continue = l_bufferedReader.readLine();
            if (l_continue.equalsIgnoreCase("n")){
                break;
            }
        }
    }


}