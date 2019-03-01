package vekta.context;

import processing.core.PVector;

public interface World extends Context {
	float MAX_AUDITORY_DISTANCE = 3000; // Used for calculating volume of sounds. Higher = hear more
	float MAX_PAN_DISTANCE 		= 1000; // Distance where sound is panned entirely left/right

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
	
	void playSoundAt(String sound, PVector location);
}  
