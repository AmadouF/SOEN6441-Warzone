package Models;

/**
 * Observer Interface with update method for writing and reading logs of the game
 */
public interface Observer {

    /**
     * This method defines operation to used to notify the Observable object and display new updated state
     * @param o Object of Observable class
     */
    void update(Observable o);
}
