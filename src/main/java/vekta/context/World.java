package vekta.context;

import processing.core.PVector;
import vekta.Resources;
import vekta.object.PlayerShip;

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

	PlayerShip getPlayerShip();

	default void playSoundAt(String sound, PVector location) {
		float distance = getPlayerShip().getPosition().dist(location);
		float distanceX = getPlayerShip().getPosition().x - location.x;

		// Pan

		float pan = (MAX_PAN_DISTANCE - distanceX) / MAX_PAN_DISTANCE;
		if(pan < -1) pan = -1;
		if(pan >  1) pan =  1;

		// Volume
		float volume = (MAX_AUDITORY_DISTANCE - distance) / MAX_AUDITORY_DISTANCE;
		if(volume < 0) volume = 0;
		if(volume > 1) volume = 1;

		System.out.println("Pan: " + pan);
		System.out.println("Volume: " + volume);

		Resources.setSoundVolume(sound, volume);
		Resources.setSoundPan(sound, pan);
		Resources.playSound(sound);
	}
}  
