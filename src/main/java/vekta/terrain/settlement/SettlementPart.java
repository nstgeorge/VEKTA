package vekta.terrain.settlement;

import vekta.menu.Menu;
import vekta.terrain.LandingSite;

import java.io.Serializable;

public interface SettlementPart extends Serializable {
	String getName();

	String getTypeString();

	void setup(LandingSite site);

	void setupSettlementMenu(Menu menu);
}
