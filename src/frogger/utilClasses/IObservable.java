package frogger.utilClasses;

import frogger.model.ModelChangeEventEnum;
import frogger.model.ObjectTypeEnum;

public interface IObservable<T> {

	void notifySubscriber(IObserver<T> observer, ModelChangeEventEnum changeType, ObjectTypeEnum objectType,
			int... values);

	void notifySubscribers(ModelChangeEventEnum changeType, ObjectTypeEnum objectType, int... values);
}
