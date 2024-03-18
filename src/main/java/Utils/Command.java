package Utils;

import Constants.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Command class processes the command strings entered by the user.
 */
public class Command {

    /**
     * Represents the input command from the player.
     */
    private String d_playerCommand;

    /**
     * Represents the root command from the player.
     */
    private String d_rootCommand;

    /**
     * Constructs a Command object with the given player command.
     *
     * @param p_playerCommand Entered player command
     */
    public Command(String p_playerCommand) {
        this.d_playerCommand = p_playerCommand.trim().replaceAll("\\s+", " ");
        this.d_rootCommand = this.d_playerCommand.split(" ")[0];
    }

    /**
     * Retrieves the player command.
     *
     * @return the player command
     */
    public String getD_playerCommand() {
        return d_playerCommand;
    }

    /**
     * Retrieves the root command.
     *
     * @return the root command
     */
    public String getD_rootCommand() {
        return d_rootCommand;
    }

    /**
     * Retrieves the first command from the player command.
     *
     * @return the first command
     */
    public String getFirstCommand() {
        return this.d_rootCommand;
    }

    /**
     * Splits the player command into operations and arguments.
     *
     * @return a list of hashmaps containing arguments and operations
     */
    public List<Map<String, String>> getListOfOperationsAndArguments() {
        List<Map<String, String>> l_listOfOperations = new ArrayList<Map<String, String>>();
        String l_operationsStr = d_playerCommand.replace(d_rootCommand, "").trim();
        if (l_operationsStr.isEmpty()) {
            return l_listOfOperations;
        }
        boolean doesCommandHaveArguments = l_operationsStr.contains("-") || l_operationsStr.contains(" ");
        if (!doesCommandHaveArguments) {
            l_operationsStr = "-filename " + l_operationsStr;
        }
        String[] l_operationsList = l_operationsStr.split("-");
        for (String l_operations : l_operationsList) {

            if (!l_operations.isEmpty()) {
                Map<String, String> l_operationAndArgumentMap = new HashMap<String, String>();
                String[] l_splitOperations = l_operations.split(" ");
                String l_argumentsString = "";
                l_operationAndArgumentMap.put(Constants.OPERATION, l_splitOperations[0]);

                String[] l_argumentsList = Arrays.copyOfRange(l_splitOperations, 1, l_splitOperations.length);
                if (l_argumentsList.length >= 1) {
                    l_argumentsString = String.join(" ", l_argumentsList);
                }
                l_operationAndArgumentMap.put(Constants.ARGUMENT, l_argumentsString);
                l_listOfOperations.add(l_operationAndArgumentMap);

            }
        }

        return l_listOfOperations;
    }

    /**
     * Checks if the argument and operation are not empty.
     *
     * @param p_Map the map containing the argument and operation
     * @return true if both argument and operation are not empty, false otherwise
     */
    public boolean validateArgumentAndOperation(Map<String, String> p_Map) {
        return StringUtils.isNotBlank(p_Map.get(Constants.ARGUMENT)) && StringUtils.isNotBlank(p_Map.get(Constants.OPERATION));
    }

    /**
     * Checks if the argument is not empty.
     *
     * @param p_Map the map containing the argument
     * @return true if the argument is not empty, false otherwise
     */
    public boolean validateArgumentsOnly(Map<String, String> p_Map) {
        return StringUtils.isNotBlank(p_Map.get(Constants.ARGUMENT));
    }
}
