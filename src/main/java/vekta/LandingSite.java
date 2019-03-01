package vekta;

import processing.core.PVector;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.InfoOption;
import vekta.menu.Menu;
import vekta.menu.handle.LandingMenuHandle;
import vekta.menu.handle.MenuHandle;
import vekta.menu.TradeMenuOption;
import vekta.object.SpaceObject;
import vekta.object.Spaceship;

import java.util.HashMap;
import java.util.Map;

import static vekta.Vekta.*;

/**
 * A landing site for one spacecraft-like object.
 * State management for landing sites should be handled or proxied through this class.
 */
public class LandingSite {
	private static final float ITEM_MARKUP = 1.5F; // Markup after buying/selling to a landing site

	private final Inventory inventory = new Inventory();
	private final Map<Item, Integer> offers = new HashMap<>();
	private final Map<Item, Integer> shipOffers = new HashMap<>();

	private final SpaceObject parent;

	private Spaceship landed;

	public LandingSite(SpaceObject parent) {
		this.parent = parent;
	}

	public Spaceship getLanded() {
		return landed;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Map<Item, Integer> getOffers() {
		return offers;
	}

	public Map<Item, Integer> getShipOffers() {
		return shipOffers;
	}

	public SpaceObject getParent() {
		return parent;
	}

	public boolean land(Spaceship ship) {
		if(landed != null) {
			return false;
		}

		landed = ship;
		Vekta.removeObject(ship);
		computeOffers(ship.getInventory(), shipOffers, offers, 1 / ITEM_MARKUP);
		computeOffers(getInventory(), offers, shipOffers, ITEM_MARKUP);
		MenuHandle handle = new LandingMenuHandle(this, getWorld());
		Menu menu = new Menu(handle);
		menu.add(new TradeMenuOption(true, ship.getInventory(), getInventory(), offers));
		menu.add(new TradeMenuOption(false, ship.getInventory(), getInventory(), shipOffers));
		menu.add(new InfoOption());
		menu.add(handle.getDefault());
		Vekta.setContext(menu);
		return true;
	}

	public boolean takeoff() {
		if(landed == null) {
			return false;
		}

		PVector offset = landed.getPosition().copy().sub(getParent().getPosition());
		landed.setVelocity(offset.setMag(getLaunchSpeed()).add(getParent().getVelocity()));
		landed.update(); // Boost the ship away from the planet
		Vekta.addObject(landed);
		landed = null;
		return true;
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

	/**
	 * Compute the launch speed (subject to rebalancing)
	 */
	private float getLaunchSpeed() {
		return (float)Math.sqrt((2 * G * (parent.getMass() / SCALE)) / (parent.getRadius() * SCALE));
	}
}
