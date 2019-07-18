package vekta.terrain.building;

import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.menu.option.EstateMenuButton;
import vekta.spawner.item.EstateItemSpawner;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;
import vekta.terrain.settlement.SettlementPart;

public class ForumBuilding implements SettlementPart {
	private final Settlement settlement;
	private final Inventory inventory = new Inventory();

	public ForumBuilding(Settlement settlement, int estateCount) {
		this.settlement = settlement;

		for(int i = 0; i < estateCount; i++) {
			getInventory().add(EstateItemSpawner.randomEstateItem(settlement));
		}
	}
	
	public Settlement getSettlement() {
		return settlement;
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public String getName() {
		return getGenericName();
	}

	@Override
	public String getGenericName() {
		return "Forum";
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
		menu.add(new EstateMenuButton(getSettlement(), getInventory()));
	}
}
