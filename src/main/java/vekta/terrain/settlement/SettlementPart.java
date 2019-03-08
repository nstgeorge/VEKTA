package vekta.terrain.settlement;

import vekta.menu.Menu;
import vekta.terrain.Terrain;

public interface SettlementPart {
	String getName();

	String getTypeString();
	
	void setupTerrain(Terrain terrain);

	void setupSettlementMenu(Menu menu);
}
