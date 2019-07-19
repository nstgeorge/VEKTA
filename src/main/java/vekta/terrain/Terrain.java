package vekta.terrain;

import vekta.Syncable;
import vekta.menu.Menu;
import vekta.terrain.settlement.Settlement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An abstract representation of planetary terrain.
 */
public abstract class Terrain extends Syncable<Terrain> {
	private final List<String> features = new ArrayList<>();

	public Terrain() {
	}

	public Collection<String> getFeatures() {
		return features;
	}

	public boolean hasFeature(String prop) {
		return getFeatures().contains(prop);
	}

	public void addFeature(String feature) {
		if(!getFeatures().contains(feature)) {
			getFeatures().add(feature);
			Collections.sort(features);
		}
	}

	public void remove(String feature) {
		getFeatures().remove(feature);
	}

	public boolean isInhabited() {
		return false;
	}

	public List<Settlement> getSettlements() {
		return Collections.emptyList();
	}

	public abstract String getOverview();

	public void setup(LandingSite site) {

	}

	public abstract void setupLandingMenu(LandingSite site, Menu menu);

	//	public abstract setupSurveyMenu(Player player, Menu menu);
}
