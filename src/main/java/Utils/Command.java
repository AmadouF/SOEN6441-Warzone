package Utils;

import Constants.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class Command is a util processing the command strings entered by the user.
 */
public class Command {
    /**
     * d_playerCommand represents the input command from the player
     */
    private String d_playerCommand;
    /**
     * d_rootCommand represents the root command from the player
     */
    private String d_rootCommand;

    /**
     * Constructor will set the values of the d_playerCommand and d_rootCommand date members.
     * 
     * @param d_playerCommand Entered player command
     */
    public Command(String p_playerCommand) {
        this.d_playerCommand = p_playerCommand.trim().replaceAll("\\s+", " ");
        this.d_rootCommand = this.d_playerCommand.split(" ")[0];
    }

    /**
     * Getter method for the first command
     * 
     * @return the private data member d_rootCommand to respect encapsulation
     */
    public String getFirstCommand() {
        return this.d_rootCommand;
    }

    /**
     * Go over the list of operations and split them between operations and arguments
     * 
     * @return a list of hashmap of arguments and operations
     */
    public List<Map<String, String>> getListOfOperationsAndArguments(){
        List<Map<String , String>> l_listOfOperations  = new ArrayList<Map<String,String>>();
        String l_operationsStr = d_playerCommand.replace(d_rootCommand, "").trim();
        if (l_operationsStr.isEmpty()){
            return l_listOfOperations;
        }
        boolean doesCommandHaveArguments = l_operationsStr.contains("-") || l_operationsStr.contains(" ");
        if (! doesCommandHaveArguments){
            l_operationsStr = "-filename " + l_operationsStr;
        }
        String[] l_operationsList = l_operationsStr.split("-");
        for (String l_operations : l_operationsList){
            
            if (! l_operations.isEmpty()){
                Map<String, String> l_operationAndArgumentMap = new HashMap<String, String>();
                String[] l_splitOperations = l_operations.split(" ");
                String l_argugmentsString = "";
                l_operationAndArgumentMap.put(Constants.OPERATION, l_splitOperations[0]);
                
                String[] l_argumentsList = Arrays.copyOfRange(l_splitOperations, 1, l_splitOperations.length);
                if (l_argumentsList.length >= 1){
                    l_argugmentsString = String.join(" ",l_argumentsList);
                }
                l_operationAndArgumentMap.put( Constants.ARGUMENT, l_argugmentsString);
                l_listOfOperations.add(l_operationAndArgumentMap);

            }
        }

        return l_listOfOperations;


}

    /**
     * Check the list of arguments and operations are not empty
     * 
     * @return a boolean stating if the list of operations and arguments is empty
     */
    public boolean validateArgumentAndOperation(Map<String, String> p_Map) {
        return StringUtils.isNotBlank(p_Map.get(Constants.ARGUMENT)) && StringUtils.isNotBlank(p_Map.get(Constants.OPERATION));
    }


    /**
     * Check the list of arguments is not empty
     * 
     * @return a boolean stating if the list of arguments is empty
     */
    public boolean validateArgumentsOnly(Map<String, String> p_Map) {
        return StringUtils.isNotBlank(p_Map.get(Constants.ARGUMENT));
    }
}