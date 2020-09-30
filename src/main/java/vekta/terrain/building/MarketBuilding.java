package vekta.terrain.building;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.market.Market;
import vekta.menu.Menu;
import vekta.spawner.ItemGenerator;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.SettlementPart;

import static vekta.Vekta.register;
import static vekta.Vekta.v;

public class MarketBuilding implements SettlementPart, Market.Stock {
	private final Class<? extends ItemGenerator.ItemSpawner> spawnerType;
	private final int shopTier;

	private final Market market;

	public MarketBuilding(int shopTier, String shopName, Class<? extends ItemGenerator.ItemSpawner> spawnerType) {
		this(shopTier, shopName, spawnerType, null);
	}

	public MarketBuilding(int shopTier, String shopName, Class<? extends ItemGenerator.ItemSpawner> spawnerType, Market.Stock stock) {
		this.spawnerType = spawnerType;
		this.shopTier = shopTier;
		this.market = register(new Market(shopName, stock != null ? stock : this));

		market.restock();
	}

	@Override
	public String getName() {
		return getGenericName();
	}

	@Override
	public String getGenericName() {
		return market.getName();
	}

	@Override
	public BuildingType getType() {
		return BuildingType.MARKET;
	}

	public Inventory getInventory() {
		return market.getInventory();
	}

	@Override
	public void setup(LandingSite site) {
		site.getTerrain().addFeature("Roads");
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void setupMenu(Menu menu) {
		market.setupMenu(menu, true, true);
	}

	@Override
	public boolean isBuyable(Market market, Item item) {
		ItemGenerator.ItemSpawner spawner = ItemGenerator.getSpawner(spawnerType);
		return spawner == null || spawner.isValid(item);
	}

	@Override
	public float onRestock(Market market) {
		getInventory().add((int)(50 * (v.random(shopTier) + 1)));
		ItemGenerator.addLoot(getInventory(), shopTier, ItemGenerator.getSpawner(spawnerType));

		return 60 * v.random(15, 20);
	}
}
