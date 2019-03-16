package vekta.terrain.building;

import vekta.item.EconomyItem;
import vekta.menu.Menu;
import vekta.menu.option.EstateMenuOption;
import vekta.spawner.item.EstateItemSpawner;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;
import vekta.terrain.settlement.SettlementPart;

import java.util.ArrayList;
import java.util.List;

public class EconomyBuilding implements SettlementPart {
	private final List<EconomyItem> items = new ArrayList<>();

	public EconomyBuilding(Settlement settlement, int estateCount) {
//		add(BondItemSpawner.createBondItem(settlement.getFaction()));

		for(int i = 0; i < estateCount; i++) {
			add(EstateItemSpawner.randomEstateItem(settlement));
		}
	}

	public void add(EconomyItem item) {
		if(!items.contains(item)) {
			items.add(item);
		}
	}

	public void remove(EconomyItem item) {
		items.remove(item);
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

	public List<EconomyItem> getItems() {
		return items;
	}

	@Override
	public void setup(LandingSite site) {
	}

	@Override
	public void setupMenu(Menu menu) {
		menu.add(new EstateMenuOption(getItems()));
	}
}
