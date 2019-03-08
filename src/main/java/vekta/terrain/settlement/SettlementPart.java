package vekta.terrain.settlement;

import vekta.menu.Menu;
import vekta.terrain.Terrain;

public interface SettlementPart {
	String getName();

	void setupTerrain(Terrain terrain);

	void setupLandingMenu(Menu menu);
}
