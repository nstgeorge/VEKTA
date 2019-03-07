package vekta.context;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.object.SpaceObject;

public interface World extends Context {
	float MAX_AUDITORY_DISTANCE = 3000; // Used for calculating volume of sounds. Higher = hear more
	float MAX_PAN_DISTANCE = 1000; // Distance where sound is panned entirely left/right

	RenderLevel getRenderLevel();

	float getTimeScale();

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

	<T extends SpaceObject> T findRandomObject(Class<T> type); // TODO: add predicate parameter

	SpaceObject findOrbitObject(SpaceObject object);

	void updateTargeters(SpaceObject object);

	void playSound(String sound, PVector location);
}  
