package vekta.terrain.building;

import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.LootMenuButton;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.SettlementPart;

public class JunkyardBuilding implements SettlementPart {
	private final Inventory inv = new Inventory();

	public JunkyardBuilding() {
	}

	@Override
	public String getName() {
		return getGenericName();
	}

	@Override
	public String getGenericName() {
		return "Junkyard";
	}

	@Override
	public BuildingType getType() {
		return BuildingType.INDUSTRIAL;
	}

	@Override
	public void setup(LandingSite site) {
		site.getTerrain().addFeature("Industrial");
	}

	@Override
	public void setupMenu(Menu menu) {
		menu.setAuto(new LootMenuButton("Scavenge", menu.getPlayer().getInventory(), inv));
	}
}
