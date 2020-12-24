package vekta.terrain.settlement.building;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.knowledge.KnowledgeSource;
import vekta.market.Market;
import vekta.menu.Menu;
import vekta.menu.handle.MenuHandle;
import vekta.menu.option.CustomButton;
import vekta.menu.option.SellKnowledgeMenuButton;
import vekta.spawner.ItemGenerator;
import vekta.spawner.item.KnowledgeItemSpawner;
import vekta.terrain.settlement.Settlement;
import vekta.terrain.settlement.SettlementPart;

import static vekta.Vekta.setContext;
import static vekta.Vekta.v;

public class AcademyBuilding implements SettlementPart, KnowledgeSource, Market.Stock {
	private final Settlement settlement;

	private final Market market = new Market("Data", this);

	public AcademyBuilding(Settlement settlement) {
		this.settlement = settlement;

		market.restock();
	}

	public Settlement getSettlement() {
		return settlement;
	}

	public Inventory getInventory() {
		return market.getInventory();
	}

	@Override
	public String getName() {
		return getSettlement().getName() + " " + getGenericName();
	}

	@Override
	public String getGenericName() {
		return "Academy";
	}

	@Override
	public BuildingType getType() {
		return BuildingType.KNOWLEDGE;
	}

	@Override
	public void setupMenu(Menu menu) {
		menu.add(new CustomButton(getGenericName(), m -> {
			Menu sub = new Menu(m, new MenuHandle());
			market.setupMenu(sub, true, false);
			sub.add(new SellKnowledgeMenuButton(getSettlement(), getInventory()));
			sub.addDefault();
			setContext(sub);
		}));
	}

	@Override
	public boolean isBuyable(Market market, Item item) {
		return item.getType() == ItemType.KNOWLEDGE;
	}

	@Override
	public float onRestock(Market market) {
		getInventory().add((int)v.random(10, 100));
		int itemCt = (int)v.random(5, 10);
		for(int i = 0; i < itemCt; i++) {
			getInventory().add(ItemGenerator.getSpawner(KnowledgeItemSpawner.class).create());
		}

		return 60 * v.random(30, 40);
	}
}
