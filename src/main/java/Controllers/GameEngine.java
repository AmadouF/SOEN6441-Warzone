package Controllers;

import Models.GameState;
import Utils.Command;

public class GameEngine{
 
    private GameState d_currentGameState = new GameState();

    public GameState getD_gameState() {
		return d_currentGameState;
	}

    public void processCommand(String p_commandInput){
        Command l_playerCommand = new Command(p_commandInput);
        String l_firstCommand = l_playerCommand.getFirstCommand();
        // TODO check if game is loaded
        if (l_firstCommand == "editmap"){
            processEditMapCommand(l_playerCommand);
        }
    }

    public void processEditMapCommand(Command p_playerCommand){
            
    }
}