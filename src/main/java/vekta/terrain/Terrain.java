package vekta.terrain;

import vekta.Sync;
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
	private final @Sync List<String> features = new ArrayList<>();
	private final @Sync List<Settlement> settlements = new ArrayList<>();

	private @Sync LandingSite site;

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
			syncChanges();
		}
	}

	public void remove(String feature) {
		if(getFeatures().contains(feature)) {
			getFeatures().remove(feature);
			syncChanges();
		}
	}

	public boolean isInhabited() {
		return !getSettlements().isEmpty();
	}

	public List<Settlement> getSettlements() {
		return settlements;
	}

	public void addSettlement(Settlement settlement) {
		if(!getSettlements().contains(settlement)) {
			getSettlements().add(settlement);
			if(site != null) {
				settlement.setup(site);
				syncChanges();
			}
		}
	}

	public void removeSettlement(Settlement settlement) {
		if(getSettlements().contains(settlement)) {
			if(site != null) {
				settlement.cleanup();
			}
			getSettlements().remove(settlement);
			syncChanges();
		}
	}

	public abstract String getOverview();

	public void setup(LandingSite site) {
		if(this.site != null) {
			throw new RuntimeException("Terrain was already set up");
		}
		this.site = site;

		for(Settlement settlement : getSettlements()) {
			settlement.setup(site);
		}

		syncChanges();
	}

	public void setupLandingMenu(LandingSite site, Menu menu) {
	}
}
