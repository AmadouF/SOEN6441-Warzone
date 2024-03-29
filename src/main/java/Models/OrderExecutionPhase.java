package Models;
import GameEngine.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Utils.Command;
import Views.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OrderExecutionPhase extends Phase {

  /**
   * Constructs an OrderExecutionPhase object with the specified game engine and game state.
   *
   * @param p_gameEngine The game engine responsible for managing the game.
   * @param p_gameState  The current state of the game.
   */
  public OrderExecutionPhase(GameEngine p_gameEngine, GameState p_gameState) {
    super(p_gameEngine, p_gameState);
  }

  /**
   * Starts the order execution phase.
   *
   * @throws IOException Thrown if an IO error occurs.
   */
  public void startPhase() throws IOException {
    d_phase_name = "Order Execution Phase";
    while (isInstanceOfOrderExecutionPhase()) {
      executeOrders();
      MapView l_view = new MapView(d_gameState, d_gameState.getD_players());
      l_view.showMap();
      if (isGameOver(d_gameState)) {
        break;
      }
      while (d_gameState.getD_players().size() != 0) {
        System.out.println("Press 'y' to continue for next turn or press 'n' to exit");
        try {
          BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
          String l_continue = l_bufferedReader.readLine();
          switch (l_continue) {
            case "n":
            case "N":
              break;

            case "y":
            case "Y":
              d_playerHelper.assignArmies(d_gameState);
              issueOrderPhase l_issueOrderPhase = new issueOrderPhase(d_gameEngine, d_gameState);
              d_gameEngine.setCurrentPhase(l_issueOrderPhase);
              break;
            default:
              System.out.println("Invalid input");
          }
        } catch (IOException l_exception) {
          System.out.println("Invalid input");
        }
      }
    }
  }

  /**
   * Checks if the game is over.
   *
   * @param p_gameState The current state of the game.
   * @return True if the game is over, false otherwise.
   */
  private boolean isGameOver(GameState p_gameState) {
    for (Player l_player : p_gameState.getD_players()) {
      if (l_player.getOwnedCountries().size() == p_gameState.getD_map().getCountriesList().size()) {
        System.out.println("CONGRATS! Player Name : " + l_player.getPlayerName() +
            " has conquered all of the countries countries and won. The Game is over .....");
        return true;
      }
    }
    return false;
  }

  /**
   * Executes orders for all players.
   */
  protected void executeOrders() {
    System.out.println("Starting Order Execution Phase");
    while (d_playerHelper.unexecutedOrdersExists(d_gameState.getD_players())) {
      for (Player l_player : d_gameState.getD_players()) {
        Order l_order = l_player.next_order();
        if (l_order != null) {
          l_order.printOrder();
          d_gameState.addLogMessage(l_order.orderExecutionLog(), "effect");
          l_order.execute(d_gameState);
        }
      }
    }
    // TODO reset players data
  }

  /**
   * Loads a map.
   *
   * @param p_command The command for loading the map.
   * @throws InvalidCommand Thrown if the command is invalid.
   * @throws IOException     Thrown if an IO error occurs.
   * @throws InvalidMap      Thrown if the map is invalid.
   */
  public void loadMap(Command p_command) throws InvalidCommand, IOException, InvalidMap {
    printCommandInvalidInCurrentState();
  }

  public void deployCommand(String p_command) throws InvalidCommand, IOException, InvalidMap {
    printCommandInvalidInCurrentState();
  }

  public void advanceCommand(String p_command) throws InvalidCommand, IOException, InvalidMap {
    printCommandInvalidInCurrentState();
  }

  public void CardCommand(String p_command) throws InvalidCommand, IOException, InvalidMap {
    printCommandInvalidInCurrentState();
  }

  /**
   * Assigns countries to players.
   *
   * @param p_command The command for assigning countries.
   * @throws InvalidCommand Thrown if the command is invalid.
   * @throws IOException     Thrown if an IO error occurs.
   * @throws InvalidMap      Thrown if the map is invalid.
   */
  public void assignCountries(Command p_command) throws InvalidCommand, IOException, InvalidMap {
    printCommandInvalidInCurrentState();
  }

  /**
   * Handles game player commands.
   *
   * @param p_command    The command object storing input command data.
   * @param baseCommand  The base command of the gameplayer command input string.
   * @throws InvalidCommand Thrown if the command is invalid.
   * @throws IOException     Thrown if an IO error occurs.
   * @throws InvalidMap      Thrown if the map is invalid.
   */
  public void gamePlayer(Command p_command, String baseCommand) throws InvalidCommand, IOException, InvalidMap {
    printCommandInvalidInCurrentState();
  }

  /**
   * Checks if the current phase is an instance of OrderExecutionPhase.
   *
   * @return True if the current phase is an instance of OrderExecutionPhase, false otherwise.
   */
  private boolean isInstanceOfOrderExecutionPhase() {
    return d_gameEngine.getD_CurrentPhase() instanceof OrderExecutionPhase;
  }

}
