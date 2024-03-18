package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * This is class implements notification mechanism when state is changed in game.
 */
public class Observable {

    private List<Observer> d_observers;

    /**
     * Constructor of Observable class to create List when there is change in state
     */
    public Observable() {
        d_observers = new ArrayList<>();
    }

    /**
     * This method is used to add state to the list
     * @param p_o
     */
    public void addObserver(Observer p_o) {
        if (p_o == null)
            throw new NullPointerException();
        d_observers.add(p_o);
    }

    /**
     * This method is used to remove state from the list
     * @param p_o
     */
    public void removeObserver(Observer p_o) {
        d_observers.remove(p_o);
    }

    /**
     * This method is used to notify the observer to change the state of game.
     */
    public void notifyObservers() {
        for (Observer l_o: d_observers)
            l_o.update(this);
    }
}
