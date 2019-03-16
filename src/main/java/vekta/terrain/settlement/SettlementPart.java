package vekta.terrain.settlement;

import vekta.menu.Menu;
import vekta.terrain.LandingSite;
import vekta.terrain.building.BuildingType;

import java.io.Serializable;

public interface SettlementPart extends Serializable {
	String getName();

	String getGenericName();

	BuildingType getType();

	default boolean addIfRelevant(SettlementPart part) {
		return false;
	}

	void setup(LandingSite site);

	void setupMenu(Menu menu);
}
