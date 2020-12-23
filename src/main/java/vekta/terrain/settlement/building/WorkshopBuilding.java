package vekta.terrain.settlement.building;

import vekta.menu.Menu;
import vekta.menu.option.CraftMenuButton;
import vekta.terrain.settlement.SettlementPart;

import java.util.Set;

public class WorkshopBuilding implements SettlementPart {
	public WorkshopBuilding() {
	}

	@Override
	public String getName() {
		return getGenericName();
	}

	@Override
	public String getGenericName() {
		return "Workshop";
	}

	@Override
	public BuildingType getType() {
		return BuildingType.INDUSTRIAL;
	}

	@Override
	public void onSurveyTags(Set<String> tags) {
		tags.add("Industrial");
	}

	@Override
	public void setupMenu(Menu menu) {
		menu.add(new CraftMenuButton(getName()));
	}
}
