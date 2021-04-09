package vekta.terrain;

import vekta.object.planet.TerrestrialPlanet;
import vekta.terrain.condition.PlanetQuantity;

import java.util.Set;

import static processing.core.PApplet.sin;
import static processing.core.PApplet.sq;
import static processing.core.PConstants.DEG_TO_RAD;
import static processing.core.PConstants.TWO_PI;
import static vekta.Vekta.v;

public class AdaptiveTerrain extends Terrain {
	private static final int START_HARMONIC = 1; // Initial radial symmetry (1 corresponding to first harmonic)
	private static final int NUM_HARMONICS = 30; // Number of harmonics for surface detail

	private final float[] harmonics = new float[NUM_HARMONICS]; // Harmonic amplitudes
	private final float[] offsets = new float[NUM_HARMONICS]; // Harmonic phase offsets

	private String customOverview;

	public AdaptiveTerrain(TerrestrialPlanet planet) {
		super(planet);

		for(int i = 0; i < harmonics.length; i++) {
			harmonics[i] = v.gaussian(1);
			offsets[i] = v.random(TWO_PI);
		}
	}

	@Override
	public void onSurveyTags(Set<String> tags) {
		//		if(!getEcosystem().getSpecies().isEmpty()) {
		//			tags.add("Ecosystem");
		//		}
	}

	public String getCustomOverview() {
		return customOverview;
	}

	public void setCustomOverview(String customOverview) {
		this.customOverview = customOverview;
	}

	@Override
	public String getOverview() {

		//		if(hasSettlement()) {
		//			if(isHabitable()) {
		//				return getSettlement().getOverview();
		//			}
		//			return "This planet carries telltale signs of a recently abandoned civilization.";
		//		}

		if(customOverview != null) {
			return customOverview;
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

	@Override
	public float getDisplacement(float angle) {
		float f = 0;
		for(int i = 0; i < harmonics.length; i++) {
			f += harmonics[i] * (1 - sq(sin((angle + offsets[i]) / 2 * (i + START_HARMONIC))));
		}
		return f / harmonics.length;

		// For debugging:
		//		angle = v.normalizeAngle(angle);
		//		return angle>100*DEG_TO_RAD&&angle<110*DEG_TO_RAD?10:0;
	}
}
