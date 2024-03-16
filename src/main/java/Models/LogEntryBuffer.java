package Models;

import Views.LogWriterView;

public class LogEntryBuffer extends  Observable{

    /**
     * message to logged
     */
    String d_logMessage;

    /**
     * Constructor to add LogWriter Observer object to the list of observers
     */
    public LogEntryBuffer(){
        LogWriterView l_logWriter = new LogWriterView();
        this.addObserver(l_logWriter);
    }

    /**
     * Getter for the Log Message.
     *
     * @return Log Message
     */
    public String getD_logMessage(){
        return d_logMessage;
    }

    /**
     * Sets the Log Message and Notifies the Observer Objects.
     *
     * @param p_logMessage Message to be logged
     * @param p_logType Type of Log : Start, end, Phase, Command, Order
     */
    public void logMessage(String p_logMessage, String p_logType){

        switch(p_logType.toLowerCase()){
            case "start", "end":
                d_logMessage = p_logMessage + System.lineSeparator();
                break;
            case "phase":
                d_logMessage = System.lineSeparator()+ "----------"+ p_logMessage + "----------"+System.lineSeparator();
                break;
            case "command":
                d_logMessage = System.lineSeparator() + "Command Executed: " + p_logMessage + System.lineSeparator();
                break;
            case "order":
                d_logMessage = System.lineSeparator() + "Order Issued: " + p_logMessage + System.lineSeparator();
                break;
            case "effect":
                d_logMessage = "Log: "+ p_logMessage + System.lineSeparator();
                break;
        }
        notifyObservers();
    }
}
