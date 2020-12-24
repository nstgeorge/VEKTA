package vekta.logic;

import vekta.terrain.condition.PlanetQuantity;
import vekta.terrain.location.Location;

public class LocationLogic<R> extends Logic<LocationLogic<R>, Location, R> {

	public PlanetQuantity type;
	public float min = Float.NEGATIVE_INFINITY;
	public float max = Float.POSITIVE_INFINITY;

	public LocationLogic() {
	}

	public LocationLogic(R value) {
		super(value);
	}

	@Override
	public boolean hasValue(Location location) {
		if(type != null) {
			float score = type.getScore(location.getPlanet());
			return score >= min && score <= max;
		}
		return super.hasValue(location);
	}
}
