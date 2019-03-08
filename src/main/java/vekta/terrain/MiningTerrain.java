package vekta.terrain;

import vekta.menu.Menu;

public class MiningTerrain extends Terrain {

	public MiningTerrain() {
		addFeature("Mineable");
		if(chance(.5F)) {
			addFeature("Habitable");
			addFeature("Inhabited");
		}
	}

	@Override
	public String getOverview() {
		return "The surface is flecked with ore and precious metals.";
	}

	@Override
	public void setupLandingMenu(Menu menu) {
		
	}
}
