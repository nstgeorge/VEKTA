package vekta.terrain;

import vekta.menu.Menu;
import vekta.terrain.settlement.Settlement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static vekta.Vekta.v;

/**
 * An abstract representation of planetary terrain.
 */
public abstract class Terrain {
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

	protected boolean chance(float amount) {
		return v.random(1) < amount;
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
