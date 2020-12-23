package vekta.terrain;

import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.handle.MenuHandle;
import vekta.menu.option.SettlementButton;
import vekta.menu.option.SurveyButton;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.sync.Sync;
import vekta.ecosystem.Ecosystem;
import vekta.menu.Menu;
import vekta.terrain.location.Location;
import vekta.terrain.settlement.Settlement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static vekta.Vekta.v;

/**
 * An abstract representation of planetary terrain.
 */
public abstract class Terrain extends Location {
	//	private final @Sync List<Feature> features = new ArrayList<Feature>();
	private final @Sync List<Settlement> settlements = new ArrayList<>();
	private final Ecosystem ecosystem;

	public Terrain(TerrestrialPlanet planet) {
		super(planet);

		ecosystem = new Ecosystem(planet.getAtmosphereDensity() * v.random(1e7F, 1e9F));
	}

	@Override
	public String getName() {
		return getPlanet().getName();
	}

	public abstract boolean isHabitable();

	public boolean isInhabited() {
		return !findVisitableSettlements().isEmpty();
	}

	public List<Settlement> findVisitableSettlements() {
		if(!settlements.isEmpty() && !isHabitable()) {
			return new ArrayList<>();
		}
		return settlements;
	}

	//	public List<Feature> getFeatures() {
	//		return features;
	//	}
	//
	//	public void addFeature(Feature feature) {
	//		if(!features.contains(feature)) {
	//			features.add(feature);
	//		}
	//	}

	public void addSettlement(Settlement settlement) {
		if(!settlements.contains(settlement)) {
			settlements.add(settlement);
		}
	}

	public Ecosystem getEcosystem() {
		return ecosystem;
	}

	public abstract String getOverview();

	@Override
	public void onVisitMenu(Menu menu) {
		for(Settlement settlement : findVisitableSettlements()) {
			menu.add(new SettlementButton(settlement));
		}
		menu.add(new SurveyButton(this));
	}

	@Override
	protected MenuHandle createMenuHandle() {
		// TODO: convert to `TerrainMenuHandle`
		return new LandingMenuHandle(getPlanet().getLandingSite());
	}

	public void onOrbit(SpaceObject orbitObject) {
	}
}
