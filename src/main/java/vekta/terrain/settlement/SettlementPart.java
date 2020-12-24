package vekta.terrain.settlement;

import vekta.menu.Menu;
import vekta.terrain.settlement.building.BuildingType;

import java.io.Serializable;
import java.util.Set;

public interface SettlementPart extends Serializable {
	String getName();

	String getGenericName();

	BuildingType getType();

	default boolean addIfRelevant(SettlementPart part) {
		return false;
	}

	default void onSurveyTags(Set<String> tags) {
	}

	void setupMenu(Menu menu);
}
