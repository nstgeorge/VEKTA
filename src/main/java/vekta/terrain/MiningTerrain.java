package vekta.terrain;

import vekta.menu.Menu;
import vekta.object.Ship;

public class MiningTerrain extends Terrain {

	public MiningTerrain() {
		add("Mineable");
		if(chance(.1F)) {
			add("Habitable");
			add("Inhabited");
		}
	}

	@Override
	public String getOverview() {
		return "The surface is flecked with ore and precious metals.";
	}

	@Override
	public void setupLandingMenu(Ship ship, Menu menu) {
		// TODO: scavenge option
	}
}
