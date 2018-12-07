package frogger.utilClasses;

import frogger.model.ModelChangeEventEnum;
import frogger.model.ObjectTypeEnum;

public interface Observable {

	void addObserver(Observer observer);

	void removeObserver(Observer observer);
	
	void notifyObservers(ModelChangeEventEnum changeType, ObjectTypeEnum objectType, int... values);
}
