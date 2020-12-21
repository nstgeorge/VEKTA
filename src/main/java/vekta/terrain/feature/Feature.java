package vekta.terrain.feature;

import vekta.object.planet.Planet;
import vekta.sync.Syncable;

import java.util.ArrayList;

public abstract class Feature extends Syncable<Feature> {

	private Planet planet;

	public Feature(Planet planet) {
		this.planet = planet;
	}

	/**
	 * Return the human-readable name of the feature.
	 * @return
	 */
	public abstract String getName();

	/**
	 * If this feature has additional descriptors that provide more information, they are provided here.
	 * @return
	 */
	public abstract ArrayList<String> getDescriptors();

	/**
	 * Can this feature exist given the conditions of the provided planet?
	 * @param planet Planet to check feature against
	 * @return true if this feature can exist here, false otherwise
	 */
	public boolean canExistOn(Planet planet) {
		return true;
	}

	/**
	 * Get the planet associated with this feature.
	 * @return Planet
	 */
	public Planet getPlanet() {
		return planet;
	}

	/**
	 * Change the planet associated with this feature.
	 * @param planet
	 */
	public void setPlanet(Planet planet) {
		this.planet = planet;
	}

	/**
	 * Update the descriptors of this feature and check if the feature can still exist on its planet. If not, returns a new Feature
	 * @return Next feature, if the planet has exceeded the possible conditions for this feature type
	 */
	public abstract Feature updateOrReplace();
}
