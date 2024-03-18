package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import GameEngine.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Utils.Command;

/**
 * This class represents the phase where players issue orders.
 */
public class issueOrderPhase extends Phase{

    /** The current player. */
    Player d_current_player;

    /**
     * Constructor for issueOrderPhase.
     * @param p_gameEngine The current game engine.
     * @param p_gameState The current game state.
     */
    public issueOrderPhase(GameEngine p_gameEngine, GameState p_gameState) {
        super(p_gameEngine, p_gameState);
    }

    /**
     * Starts the issue order phase.
     */
    public void startPhase(){
        d_phase_name = "Issue Order Phase";
        while (isInstanceOfissueOrderPhase()) {
            issueOrders();
        }
    }

    /**
     * Issues orders to players.
     */
    protected void issueOrders(){
        List<Player> l_players = d_gameState.getD_players();
        while (d_playerHelper.unassignedArmiesExists(d_gameState.getD_players())) {
            for (Player l_player : l_players) {
                if (l_player.getMoreOrders()){
                    try{
                        l_player.issue_order(this);
                    }
                    catch (IOException | InvalidCommand | InvalidMap l_exception) {
                        // TODO Write some log messages here
                        System.out.println(l_exception.getMessage() + "effect");
                    }
                }
            }
        }
        //TODO change phase here
    }

    /**
     * Loads a map.
     * @param p_command The command for loading the map.
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException Thrown if an IO error occurs.
     * @throws InvalidMap Thrown if the map is invalid.
     */
    public void loadMap(Command p_command) throws InvalidCommand, IOException, InvalidMap {
        printCommandInvalidInCurrentState();
        requestNewOrder();
    }

    /**
     * Assigns countries.
     * @param p_command The command for assigning countries.
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException Thrown if an IO error occurs.
     * @throws InvalidMap Thrown if the map is invalid.
     */
    public void assignCountries(Command p_command) throws InvalidCommand, IOException, InvalidMap {
        printCommandInvalidInCurrentState();
        requestNewOrder();
    }

    /**
     * Handles game player commands.
     * @param p_command The command object storing input command data.
     * @param baseCommand The base command of the gameplayer command input string.
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException Thrown if an IO error occurs.
     * @throws InvalidMap Thrown if the map is invalid.
     */
    public void gamePlayer(Command p_command, String baseCommand) throws InvalidCommand, IOException, InvalidMap {
        printCommandInvalidInCurrentState();
        requestNewOrder();
    }

    /**
     * Requests a new order.
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException Thrown if an IO error occurs.
     * @throws InvalidMap Thrown if the map is invalid.
     */
    public void requestNewOrder() throws InvalidCommand, IOException, InvalidMap{
        BufferedReader sc= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\n Please enter a valid command:");
        String l_enteredCommand=sc.readLine();
    
        d_gameState.addLogMessage("[ Player: "+this.d_current_player.getPlayerName()+"] " + l_enteredCommand, "order");
        performCommand(l_enteredCommand);
    }

    /**
     * Performs a command.
     * @param p_command The command to perform.
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException Thrown if an IO error occurs.
     * @throws InvalidMap Thrown if the map is invalid.
     */
    public void performCommand(String p_command) throws InvalidCommand, IOException, InvalidMap{
        processCommand(p_command);
    }

    /**
     * Checks if the current phase is an instance of issueOrderPhase.
     * @return True if the current phase is an instance of issueOrderPhase, false otherwise.
     */
    private boolean isInstanceOfissueOrderPhase(){
        return d_gameEngine.getD_CurrentPhase() instanceof issueOrderPhase;
    }
}
