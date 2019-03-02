package vekta.terrain;

import vekta.menu.Menu;
import vekta.menu.option.RechargeOption;
import vekta.object.PlayerShip;

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
	public void setupLandingMenu(PlayerShip ship, Menu menu) {
		menu.add(new RechargeOption(ship));
	}
}
