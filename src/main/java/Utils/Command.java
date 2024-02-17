package Utils;

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
     * @param d_playerCommand
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
}