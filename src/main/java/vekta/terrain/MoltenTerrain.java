package vekta.terrain;

import vekta.menu.Menu;
import vekta.object.Ship;

public class MoltenTerrain extends Terrain {

	public MoltenTerrain() {
		add("Dangerous");
	}

	@Override
	public String getOverview() {
		return "The entire planet is covered in molten lava.";
	}

	@Override
	public void setupLandingMenu(Ship ship, Menu menu) {
		
	}
}
