package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import GameEngine.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Utils.Command;
public class issueOrderPhase extends Phase{

    Player d_current_player;

    public issueOrderPhase(GameEngine p_gameEngine, GameState p_gameState) {
        super(p_gameEngine, p_gameState);
    }


    public void startPhase(){
        d_phase_name = "Issue Order Phase";
        while (isInstanceOfissueOrderPhase()) {
            issueOrders();
        }
    }
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
    //TODO change phase here
    }
}

public void loadMap(Command p_command) throws InvalidCommand, IOException, InvalidMap {
    printCommandInvalidInCurrentState();
    requestNewOrder();
}

public void assignCountries(Command p_command) throws InvalidCommand, IOException, InvalidMap {
    printCommandInvalidInCurrentState();
    requestNewOrder();
}

public void gamePlayer(Command p_command, String baseCommand) throws InvalidCommand, IOException, InvalidMap {
    printCommandInvalidInCurrentState();
    requestNewOrder();
}

public void requestNewOrder() throws InvalidCommand, IOException, InvalidMap{
    BufferedReader sc= new BufferedReader(new InputStreamReader(System.in));
    System.out.println("\n Please enter a valid command:");
    String l_enteredCommand=sc.readLine();

    performCommand(l_enteredCommand);
}

public void performCommand(String p_command) throws InvalidCommand, IOException, InvalidMap{
    processCommand(p_command);

}

    private boolean isInstanceOfissueOrderPhase(){
        return d_gameEngine.getD_CurrentPhase() instanceof issueOrderPhase;
    }

}
