package vekta.terrain.settlement;

import vekta.Player;
import vekta.menu.Menu;
import vekta.terrain.Terrain;

public interface SettlementPart {
	String getName();

	void setupTerrain(Terrain terrain);

	void setupLandingMenu(Player player, Menu menu);
}
