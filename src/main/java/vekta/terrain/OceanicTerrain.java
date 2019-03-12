package vekta.terrain;

import vekta.menu.Menu;

import static vekta.Vekta.v;

public class OceanicTerrain extends Terrain {

	public OceanicTerrain() {
		addFeature("Oceanic");
		if(v.chance(.9F)) {
			addFeature("Atmosphere");
		}
	}

	@Override
	public String getOverview() {
		return "You can't find anywhere to land; a vast ocean covers this planet.";
	}

	@Override
	public void setupLandingMenu(Menu menu) {
	}
}
