package vekta.terrain;

import vekta.menu.Menu;
import vekta.terrain.settlement.Settlement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An abstract representation of planetary terrain.
 */
public abstract class Terrain implements Serializable {
	private final List<String> features = new ArrayList<>();

	public Terrain() {

	}

	public List<String> getFeatures() {
		return features;
	}

	public boolean hasFeature(String prop) {
		return getFeatures().contains(prop);
	}

	public void addFeature(String feature) {
		if(!getFeatures().contains(feature)) {
			getFeatures().add(feature);
			Collections.sort(getFeatures());
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

	public abstract void setupLandingMenu(Menu menu);

	//	public abstract setupSurveyMenu(Player player, Menu menu);
}
