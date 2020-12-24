package vekta.terrain;

import vekta.object.planet.TerrestrialPlanet;
import vekta.terrain.condition.PlanetQuantity;

import java.util.Set;

public class AdaptiveTerrain extends Terrain {

	public AdaptiveTerrain(TerrestrialPlanet planet) {
		super(planet);
	}

	@Override
	public void onSurveyTags(Set<String> tags) {
		//		if(getEcosystem().getCapacity() > 0) {
		//			tags.add("Ecosystem");
		//		}
	}

	@Override
	public String getOverview() {

		if(hasSettlement()) {
			if(isHabitable()) {
				return getSettlement().getOverview();
			}
			return "This planet carries telltale signs of a recently abandoned civilization.";
		}

		if(isHabitable()) {
			return "You land in a landscape which appears ripe for colonization.";
		}

		return "You land on the planet's surface."; // TODO: flavor text based on features/locations
	}

	@Override
	public boolean isHabitable() {
		return PlanetQuantity.HABITABLE.getScore(getPlanet()) >= 1;
	}
}
