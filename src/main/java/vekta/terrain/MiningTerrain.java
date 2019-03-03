package vekta.terrain;

import vekta.menu.Menu;
import vekta.object.PlayerShip;

public class MiningTerrain extends Terrain {

	public MiningTerrain() {
		addFeature("Mineable");
		if(chance(.1F)) {
			addFeature("Habitable");
			addFeature("Inhabited");
		}
	}

	@Override
	public String getOverview() {
		return "The surface is flecked with ore and precious metals.";
	}

	@Override
	public void setupLandingMenu(PlayerShip ship, Menu menu) {

	}
}
