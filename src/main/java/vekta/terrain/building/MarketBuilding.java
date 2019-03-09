package vekta.terrain.building;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.option.MarketOption;
import vekta.spawner.ItemGenerator;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.SettlementPart;

import java.util.HashMap;
import java.util.Map;

import static vekta.Vekta.v;

public class MarketBuilding implements SettlementPart {
	//	private static final float ITEM_MARKUP = 1.5F; // Price modifier after buying/selling to a landing terrain

	private final Inventory inventory = new Inventory();
	private final Map<Item, Integer> offers = new HashMap<>();
	private final Map<Item, Integer> shipOffers = new HashMap<>();

	private final String type;
	private final ItemGenerator.ItemSpawner spawner;

	public MarketBuilding(int shopTier, String type, ItemGenerator.ItemSpawner spawner) {
		this.type = type;
		this.spawner = spawner;

		inventory.add((int)(50 * (v.random(shopTier) + 1)));
		ItemGenerator.addLoot(getInventory(), shopTier, spawner);
	}

	@Override
	public String getName() {
		return getTypeString();
	}

	@Override
	public String getTypeString() {
		return type;
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void setup(LandingSite site) {
		site.getTerrain().addFeature("Roads");
	}

	@Override
	public void setupSettlementMenu(Menu menu) {
		Inventory inv = menu.getPlayer().getInventory();

		computeOffers(inv, shipOffers, offers, true);
		computeOffers(getInventory(), offers, shipOffers, false);

		menu.add(new MarketOption(this, true, inv/*, getInventory()*/, offers));
		menu.add(new MarketOption(this, false, inv/*, getInventory()*/, shipOffers));
	}

	public boolean canSell(Item item) {
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
				int price = item.getType().randomPrice();
				thisSide.put(item, price);
			}
		}
	}
}
