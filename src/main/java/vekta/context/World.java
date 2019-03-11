package vekta.context;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.Syncable;
import vekta.object.SpaceObject;
import vekta.object.Targeter;

public interface World extends Context {
	float MAX_AUDITORY_DISTANCE = 3000; // Used for calculating volume of sounds. Higher = hear more
	float MAX_PAN_DISTANCE = 1000; // Distance where sound is panned entirely left/right

	RenderLevel getRenderLevel();

	float getTimeScale();

	/**
	 * Add (or replace existing) Syncable object
	 */
	<T extends Syncable> T register(T object);
	
	/**
	 * Remove Syncable object
	 */
	void remove(Syncable object);

	/**
	 * Called when an object should be synchronized
	 */
	void syncChanges(Syncable object);

	/**
	 * Called when player dies
	 */
	void setDead();

	/**
	 * Called when the gamemode reloads from an autosave
	 */
	void reload();

	/**
	 * Called when the gamemode restarts
	 */
	void restart();

	<T> T findRandomObject(Class<T> type); // TODO: add predicate overload

	SpaceObject findOrbitObject(SpaceObject object);

	void updateTargeter(Targeter t);

	void playSound(String sound, PVector location);
}  
