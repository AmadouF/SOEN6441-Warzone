package Views;

import Models.LogEntryBuffer;
import Models.Observable;
import Models.Observer;
import Constants.Constants;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * This class is used to update the log file whenever there is change in state derived from LogEntryBuffer class
 */

public class LogWriterView implements Observer {

    /**
     * Constructor of to add LogWriterView Observer object to the list of observers
     * @param p_logEntryBuffer
     */
    public LogWriterView(LogEntryBuffer p_logEntryBuffer){
        p_logEntryBuffer.addObserver(this);
    }

    /**
     * Updated LogEntryBuffer Observable Object.
     */
    LogEntryBuffer d_logEntryBuffer;

    /**
     * Writes/Appends updated LogEntryBuffer Object into the log file.
     * @param p_observable Object of LogEntryBuffer class
     */
    @Override
    public void update(Observable p_observable) {
        d_logEntryBuffer = (LogEntryBuffer) p_observable;
        File l_logfile = new File(Constants.GAMELOGS_FILE_NAME);
        String l_logMessage = d_logEntryBuffer.getD_logMessage();

        try{
            if(l_logMessage.startsWith(Constants.STARTING_THE_GAME_LOG_MESSAGE)) {
                Files.newBufferedWriter(Paths.get(Constants.GAMELOGS_FILE_NAME), StandardOpenOption.TRUNCATE_EXISTING).write(" ");
            }
            Files.write(Paths.get(Constants.GAMELOGS_FILE_NAME), l_logMessage.getBytes(StandardCharsets.US_ASCII), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }catch(Exception l_e){
            l_e.printStackTrace();
        }
    }
}
