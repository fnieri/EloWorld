package src;

import java.util.List;

public class Subject {
    private List<Observer> observers;

    public void notifyObservers() {
        // TODO
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }
}
