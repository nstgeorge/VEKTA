package vekta.terrain;

import vekta.menu.Menu;
import vekta.object.PlayerShip;

public class OceanicTerrain extends Terrain {

	public OceanicTerrain() {
		if(chance(.9F)) {
			add("Atmosphere");
		}
	}

	@Override
	public String getOverview() {
		return "You can't find anywhere to land; a vast ocean covers this planet.";
	}

	
	@Override
	public void setupLandingMenu(PlayerShip ship, Menu menu) {
		// TODO: scavenge option
	}
}
