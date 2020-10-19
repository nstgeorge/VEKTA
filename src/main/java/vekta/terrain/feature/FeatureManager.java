package vekta.terrain.feature;

import java.util.ArrayList;

import static vekta.Vekta.v;

public class FeatureManager {

	private static final Feature[] features = {new OceanFeature(null), new IceFeature(null), new CloudFeature(null), new WetlandFeature(null)};

	/**
	 * Returns a new instance of a named feature (if one exists) with no associated planet.
	 * @param name Name of the feature to look for
	 * @return Feature of that type
	 */
	public static Feature searchFor(String name) {
		for(Feature f : features) {
			if(f.getName().equals(name)) {
				return f;
			}
		}
		return null;
	}

	/**
	 * Choose a random feature from the list.
	 * @return
	 */
	public static Feature randomFeature() {
		return v.random(features);
	}
}
