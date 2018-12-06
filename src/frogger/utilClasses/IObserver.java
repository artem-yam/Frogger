package frogger.utilClasses;

public interface IObserver<T> {

	void subscribe(IObservable<T> observable);

	void unsubscribe(IObservable<T> observable);

	void observableChanged(Object changeData);

}
