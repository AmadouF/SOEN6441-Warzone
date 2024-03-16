package Models;

import GameEngine.GameEngine;
import Constants.Constants;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Utils.Command;
import org.apache.commons.collections4.CollectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;


public class StartUpPhase extends Phase{

    public StartUpPhase(GameEngine p_currentGameEngine, GameState p_currentGameState){
        super(p_currentGameEngine, p_currentGameState);
    }
    
     /**
     * This method is used to load map file
     * @param p_command command of lading map
     * @throws InvalidCommand
     */
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

     /**
     * This method is used to add game player to the current game
     * @param p_command command object storing input command data
     * @param baseCommand base command of the gameplayer command input string
     * @throws InvalidCommand
     */
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
     * This method is used to start the game loop with already loaded map file
     * @param p_command command object with assign country argument
     * @throws InvalidCommand
     * @throws IOException
     */
    public void assignCountries(Command p_command) throws InvalidCommand, IOException {
        d_gameEngine.checkIfMapIsLoaded();
        List<Map<String, String>> l_listOfOperationsAndArguments = p_command.getListOfOperationsAndArguments();
        if (CollectionUtils.isEmpty(l_listOfOperationsAndArguments)) {
            d_playerHelper.assignCountries(d_gameState);
            d_gameEngine.startGameLoop();
        } else {
            throw new InvalidCommand("Invalid command. No arguments expected for command 'assigncountries'");
        }
    }

    /**
     * This method starts an infinite loop which processes the input commands from the players
     */
    public void startPhase()  {
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
    
}
