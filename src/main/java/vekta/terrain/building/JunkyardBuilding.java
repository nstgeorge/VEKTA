package vekta.terrain.building;

import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.LootMenuButton;
import vekta.spawner.ItemGenerator;
import vekta.spawner.item.JunkItemSpawner;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.SettlementPart;

public class JunkyardBuilding implements SettlementPart {
	private final Inventory inv = new Inventory();

	public JunkyardBuilding() {
		ItemGenerator.addLoot(inv, 2, new JunkItemSpawner());
	}

	public Inventory getInventory() {
		return inv;
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
	public void cleanup() {
	}

	@Override
	public void setupMenu(Menu menu) {
		if(getInventory().itemCount() > 0) {
			menu.add(new LootMenuButton("Junkyard", menu.getPlayer().getInventory(), getInventory()));
		}
	}
}
