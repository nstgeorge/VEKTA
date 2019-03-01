package vekta.context;

public interface World extends Context {
	/**
	 * Initializes all required variables
	 */
	void init();

	/**
	 * Called when player dies
	 */
	void setDead();
	
	/**
	 * Called when the gamemode restarts
	 */
	void restart();

	void addObject(Object object);

	void removeObject(Object object);
}  
