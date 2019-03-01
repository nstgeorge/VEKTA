package vekta.menu.option;

import vekta.menu.Menu;
import vekta.terrain.Terrain;

public class InfoOption implements MenuOption {
	private final Terrain terrain;
	
	public InfoOption(Terrain terrain) {
		this.terrain = terrain;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	@Override
	public String getName() {
		return "Info";
	}

	@Override
	public void select(Menu menu) {
		// TODO: display info based on the data available to the Terrain instance
	}
}
