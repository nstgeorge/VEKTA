package vekta.terrain.building;

import vekta.menu.Menu;
import vekta.menu.option.BondMenuButton;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.SettlementPart;

public class ExchangeBuilding implements SettlementPart {
	public ExchangeBuilding() {
	}

	@Override
	public String getName() {
		return getGenericName();
	}

	@Override
	public String getGenericName() {
		return "Exchange";
	}

	@Override
	public BuildingType getType() {
		return BuildingType.ECONOMY;
	}

	@Override
	public void setup(LandingSite site) {
	}

	@Override
	public void setupMenu(Menu menu) {
		menu.add(new BondMenuButton());
	}
}
