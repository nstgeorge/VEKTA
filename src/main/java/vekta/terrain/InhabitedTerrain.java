package vekta.terrain;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.option.TradeMenuOption;
import vekta.object.Ship;

import java.util.HashMap;
import java.util.Map;

public class InhabitedTerrain extends Terrain {
	private static final float ITEM_MARKUP = 1.5F; // Price modifier after buying/selling to a landing terrain

	private final Inventory inventory = new Inventory();
	private final Map<Item, Integer> offers = new HashMap<>();
	private final Map<Item, Integer> shipOffers = new HashMap<>();

	public InhabitedTerrain() {
		add("Atmosphere");
		add("Habitable");
		add("Inhabited");
		if(chance(.5)) {
			add("Advanced Civilization");
		}
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override 
	public String getOverview() {
		return "A bustling city greets you on the surface.";
	}

	@Override
	public void setupLandingMenu(Ship ship, Menu menu) {
		computeOffers(ship.getInventory(), shipOffers, offers, 1 / ITEM_MARKUP);
		computeOffers(getInventory(), offers, shipOffers, ITEM_MARKUP);
		
		menu.add(new TradeMenuOption(true, ship.getInventory(), getInventory(), offers));
		menu.add(new TradeMenuOption(false, ship.getInventory(), getInventory(), shipOffers));
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
