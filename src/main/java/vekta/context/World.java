package vekta.context;

import processing.core.PVector;
import vekta.Player;
import vekta.RenderLevel;
import vekta.Syncable;
import vekta.connection.message.Message;
import vekta.object.SpaceObject;
import vekta.object.Targeter;

import java.io.Serializable;
import java.util.List;

public interface World extends Context {
	RenderLevel getRenderLevel();

	float getTime();

	float getTimeScale();

	float getZoom();

	void setAutoZoom(float zoom);

	void setAutoZoomDirection(boolean outward);

	default void schedule(Callback callback) {
		schedule(0, callback);
	}

	void schedule(float delay, Callback callback);

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
	void sendChanges(Syncable object);

	void sendMessage(Player player, Message message);

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

	<T extends Syncable> List<T> findObjects(Class<T> type);

	<T extends Syncable> T findRandomObject(Class<T> type); // TODO: add predicate overload

	SpaceObject findOrbitObject(SpaceObject object);

	void updateTargeter(Targeter t);

	void playSound(String sound, PVector location);

	interface Callback extends Serializable {
		void callback();
	}
}  
