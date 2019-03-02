package vekta.context;

import processing.core.PVector;
import vekta.object.SpaceObject;
import vekta.object.Targeter;

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

	void updateTargeter(SpaceObject object, Targeter targeter);

	void playSoundAt(String sound, PVector location);
}  
