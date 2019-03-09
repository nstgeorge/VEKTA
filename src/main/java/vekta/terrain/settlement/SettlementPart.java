package vekta.terrain.settlement;

import vekta.menu.Menu;
import vekta.terrain.LandingSite;

public interface SettlementPart {
	String getName();

	String getTypeString();
	
	void setup(LandingSite site);

	void setupSettlementMenu(Menu menu);
}
