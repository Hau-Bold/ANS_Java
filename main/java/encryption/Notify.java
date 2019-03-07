package encryption;

public interface Notify {

	public abstract void addObserver(Observer observer);

	public abstract void notifyObserver();

}
