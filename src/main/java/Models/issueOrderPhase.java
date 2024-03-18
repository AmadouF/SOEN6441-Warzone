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
 * Represents a phase where players issue orders.
 * In this phase, players are allowed to issue orders until all orders are issued.
 */
public class issueOrderPhase extends Phase{

    /**
     * The current player in this phase.
     */
    Player d_current_player;

    /**
     * Constructs an issueOrderPhase object with the specified game engine and game state.
     *
     * @param p_gameEngine The game engine responsible for managing the game.
     * @param p_gameState  The current state of the game.
     */
    public issueOrderPhase(GameEngine p_gameEngine, GameState p_gameState) {
        super(p_gameEngine, p_gameState);
    }


    /**
     * Starts the issue order phase.
     * This method executes until all players have issued their orders.
     */
    public void startPhase() throws IOException {
        d_phase_name = "Issue Order Phase";
        while (isInstanceOfissueOrderPhase()) {
            issueOrders();
        }
    }

    /**
     * Issues orders to players.
     * This method iterates through players and allows them to issue orders.
     */
    protected void issueOrders() throws IOException {
        List<Player> l_players = d_gameState.getD_players();
        while (d_playerHelper.unassignedArmiesExists(d_gameState.getD_players())) {
            for (Player l_player : l_players) {
                if (l_player.getMoreOrders()){
                    try{
                        this.d_current_player = l_player;
                        l_player.issue_order(this);
                    }
                    catch (IOException | InvalidCommand | InvalidMap l_exception) {
                        // TODO Write some log messages here
                        System.out.println(l_exception.getMessage() + "effect");
                }
            }

        }

        this.d_gameEngine.commonGameEngineLogger("Order Execution Phase", "phase");
        this.d_gameEngine.setCurrentPhase(new OrderExecutionPhase(this.d_gameEngine, d_gameState));

    }
}

    /**
     * Loads a map.
     * This method is not applicable in the issue order phase and prints a message indicating an invalid command.
     *
     * @param p_command The command for loading the map.
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException     Thrown if an IO error occurs.
     * @throws InvalidMap      Thrown if the map is invalid.
     */
    public void loadMap(Command p_command) throws InvalidCommand, IOException, InvalidMap {
        printCommandInvalidInCurrentState();
        requestNewOrder();
    }

    /**
     * Assigns countries to players.
     * This method is not applicable in the issue order phase and prints a message indicating an invalid command.
     *
     * @param p_command The command for assigning countries.
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException     Thrown if an IO error occurs.
     * @throws InvalidMap      Thrown if the map is invalid.
     */
    public void assignCountries(Command p_command) throws InvalidCommand, IOException, InvalidMap {
        printCommandInvalidInCurrentState();
        requestNewOrder();
    }

    /**
     * Handles game player commands.
     * This method is not applicable in the issue order phase and prints a message indicating an invalid command.
     *
     * @param p_command    The command object storing input command data.
     * @param baseCommand  The base command of the gameplayer command input string.
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException     Thrown if an IO error occurs.
     * @throws InvalidMap      Thrown if the map is invalid.
     */
    public void gamePlayer(Command p_command, String baseCommand) throws InvalidCommand, IOException, InvalidMap {
        printCommandInvalidInCurrentState();
        requestNewOrder();
    }

    /**
     * Requests a new order from the player.
     * This method prompts the player to enter a valid command and performs the command.
     *
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException     Thrown if an IO error occurs.
     * @throws InvalidMap      Thrown if the map is invalid.
     */
    public void requestNewOrder() throws InvalidCommand, IOException, InvalidMap{
        BufferedReader sc= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\n " + "[ Player: "+ this.d_current_player.getPlayerName()+ "] " + "Please enter a command to issue order:");
        String l_enteredCommand=sc.readLine();

        d_gameState.addLogMessage("[ Player: "+this.d_current_player.getPlayerName()+"] " + l_enteredCommand, "order");
        performCommand(l_enteredCommand);
    }

    protected void deployCommand(String p_command) throws IOException {
        d_current_player.createDeployOrder(p_command);
        d_gameState.addLogMessage(this.d_current_player.getLog(), "effect");
        d_playerHelper.checkForMoreOrders(d_gameState.getD_players());
    }

    protected void CardCommand(String p_enteredCommand) throws IOException {
    	if(d_current_player.getOwnedCards().contains(p_enteredCommand.split(" ")[0])) {
    		d_current_player.handleCardCommands(p_enteredCommand, d_gameState);
            d_gameState.addLogMessage(this.d_current_player.getLog(), "effect");
    	}  
        d_playerHelper.checkForMoreOrders(d_gameState.getD_players());
    }

    protected void advanceCommand(String p_command) throws IOException {
        d_current_player.createAdvanceOrder(p_command, d_gameState);
        d_gameState.addLogMessage(this.d_current_player.getLog(), "effect");
        d_playerHelper.checkForMoreOrders(d_gameState.getD_players());
    }


    /**
     * Performs a command.
     * This method processes the command entered by the player and logs the effect.
     *
     * @param p_command The command to be performed.
     * @throws InvalidCommand Thrown if the command is invalid.
     * @throws IOException     Thrown if an IO error occurs.
     * @throws InvalidMap      Thrown if the map is invalid.
     */
    public void performCommand(String p_command) throws InvalidCommand, IOException, InvalidMap{
        processCommand(p_command);
        d_gameState.addLogMessage(this.d_current_player.getLog(), "effect");

    }

    /**
     * Checks if the current phase is an instance of issueOrderPhase.
     *
     * @return True if the current phase is an instance of issueOrderPhase, false otherwise.
     */
    private boolean isInstanceOfissueOrderPhase(){
        return d_gameEngine.getD_CurrentPhase() instanceof issueOrderPhase;
    }

}
