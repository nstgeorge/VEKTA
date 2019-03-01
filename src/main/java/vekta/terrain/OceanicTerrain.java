package vekta.terrain;

import vekta.menu.Menu;
import vekta.object.Ship;

public class OceanicTerrain extends Terrain {

	public OceanicTerrain() {
		if(chance(.9)) {
			add("Atmosphere");
		}
		add("Oceans");
	}

	@Override
	public String getOverview() {
		return "You can't find anywhere to land; a vast ocean covers this planet.";
	}

	
	@Override
	public void setupLandingMenu(Ship ship, Menu menu) {
		// TODO: scavenge option
	}
}
