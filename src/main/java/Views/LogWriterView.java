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

public class LogWriterView implements Observer {

    public LogWriterView(LogEntryBuffer p_logEntryBuffer){
        p_logEntryBuffer.addObserver(this);
    }
    LogEntryBuffer d_logEntryBuffer;
    @Override
    public void update(Observable p_observable) {
        d_logEntryBuffer = (LogEntryBuffer) p_observable;
        File l_logfile = new File(Constants.GAMELOGS_FILE_NAME);
        String l_logMessage = d_logEntryBuffer.getD_logMessage();

        try{
            if(l_logMessage.equals(Constants.STARTING_THE_GAME_LOG_MESSAGE)) {
                Files.newBufferedWriter(Paths.get(Constants.GAMELOGS_FILE_NAME), StandardOpenOption.TRUNCATE_EXISTING).write(" ");
            }
            Files.write(Paths.get(Constants.GAMELOGS_FILE_NAME), l_logMessage.getBytes(StandardCharsets.US_ASCII), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }catch(Exception l_e){
            l_e.printStackTrace();
        }
    }
}
