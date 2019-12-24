package vekta.terrain.building;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.option.MarketButton;
import vekta.spawner.ItemGenerator;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.SettlementPart;

import java.util.HashMap;
import java.util.Map;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class MarketBuilding implements SettlementPart {
	private final Inventory inventory = new Inventory();
	private final Map<Item, Integer> offers = new HashMap<>();
	private final Map<Item, Integer> shipOffers = new HashMap<>();

	private final String type;
	private final Class<? extends ItemGenerator.ItemSpawner> spawnerType;
	private final int shopTier;

	private float prevRestockTime;

	public MarketBuilding(int shopTier, String type, Class<? extends ItemGenerator.ItemSpawner> spawnerType) {
		this.type = type;
		this.spawnerType = spawnerType;
		this.shopTier = shopTier;

		restock();
	}

	@Override
	public String getName() {
		return getGenericName();
	}

	@Override
	public String getGenericName() {
		return type;
	}

	@Override
	public BuildingType getType() {
		return BuildingType.MARKET;
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void setup(LandingSite site) {
		site.getTerrain().addFeature("Roads");
	}

	@Override
	public void cleanup() {
	}

	public void restock() {
		prevRestockTime = getWorld().getTime();

		inventory.clear();
		inventory.add((int)(50 * (v.random(shopTier) + 1)));
		ItemGenerator.addLoot(getInventory(), shopTier, ItemGenerator.getSpawner(spawnerType));
	}

	@Override
	public void setupMenu(Menu menu) {
		Inventory inv = menu.getPlayer().getInventory();

		computeOffers(inv, shipOffers, offers, true);
		computeOffers(getInventory(), offers, shipOffers, false);

		menu.add(new MarketButton(this, true, inv/*, getInventory()*/, offers));
		menu.add(new MarketButton(this, false, inv/*, getInventory()*/, shipOffers));
	}

	public boolean canSell(Item item) {
		ItemGenerator.ItemSpawner spawner = ItemGenerator.getSpawner(spawnerType);
		return spawner == null || spawner.isValid(item);
	}

	private void computeOffers(Inventory inv, Map<Item, Integer> thisSide, Map<Item, Integer> otherSide, boolean buying) {
		// TODO: adjust based on economic system
		for(Item item : inv) {
			if(otherSide.containsKey(item)) {
				float markup = item.getType().getMarkupFactor();
				if(buying) {
					markup = 1 / markup;
				}
				int price = (int)(otherSide.get(item) * markup);
				thisSide.put(item, price);
			}
			else if(!thisSide.containsKey(item)) {
				int price = item.randomPrice();
				thisSide.put(item, price);
			}
		}
	}
}
