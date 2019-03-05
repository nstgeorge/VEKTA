package vekta.terrain;

import vekta.menu.Menu;
import vekta.object.ship.ModularShip;

public class AsteroidTerrain extends Terrain {
	public AsteroidTerrain() {
		addFeature("Asteroid");
		if(chance(.25F)) {
			addFeature("Mineable");
		}
		if(chance(.25F)) {
			addFeature("Inhabited");
		}
	}

	@Override
	public String getOverview() {
		return "You carefully touch down on the asteroid.";
	}

	@Override
	public void setupLandingMenu(ModularShip ship, Menu menu) {

	}
}
