package frogger.utilClasses;

import frogger.model.ModelChangeData;

public interface Observable {

    void addObserver(Observer observer);

    void notifyObservers(ModelChangeData changeData);
}
