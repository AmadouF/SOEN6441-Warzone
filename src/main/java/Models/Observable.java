package Models;

import java.util.ArrayList;
import java.util.List;

public class Observable {

    private List<Observer> d_observers;

    public Observable() {
        d_observers = new ArrayList<>();
    }

    public void addObserver(Observer p_o) {
        if (p_o == null)
            throw new NullPointerException();
        d_observers.add(p_o);
    }

    public void removeObserver(Observer p_o) {
        d_observers.remove(p_o);
    }

    public void notifyObservers() {
        for (Observer l_o: d_observers)
            l_o.update(this);
    }
}
