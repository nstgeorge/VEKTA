package vekta.terrain.building;

import vekta.ItemGenerator;
import vekta.Player;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.option.TradeMenuOption;
import vekta.terrain.Terrain;
import vekta.terrain.settlement.SettlementPart;

import java.util.HashMap;
import java.util.Map;

public class MarketBuilding implements SettlementPart {
	private static final float ITEM_MARKUP = 1.5F; // Price modifier after buying/selling to a landing terrain

	private final Inventory inventory = new Inventory();
	private final Map<Item, Integer> offers = new HashMap<>();
	private final Map<Item, Integer> shipOffers = new HashMap<>();

	public MarketBuilding(int shopLevel) {
		ItemGenerator.addLoot(getInventory(), shopLevel);
	}

	@Override
	public String getName() {
		return "Marketplace";
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void setupTerrain(Terrain terrain) {
		terrain.addFeature("Roads");
	}

	@Override
	public void setupLandingMenu(Player player, Menu menu) {
		Inventory inv = player.getShip().getInventory();

		computeOffers(inv, shipOffers, offers, 1 / ITEM_MARKUP);
		computeOffers(getInventory(), offers, shipOffers, ITEM_MARKUP);

		menu.add(new TradeMenuOption(true, inv, getInventory(), offers));
		menu.add(new TradeMenuOption(false, inv, getInventory(), shipOffers));
	}

	private void computeOffers(Inventory inv, Map<Item, Integer> thisSide, Map<Item, Integer> otherSide, float markup) {
		// TODO: adjust based on economic system
		for(Item item : inv) {
			if(otherSide.containsKey(item)) {
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
