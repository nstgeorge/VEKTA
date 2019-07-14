package vekta.object.ship;

import processing.core.PVector;
import vekta.menu.Menu;
import vekta.menu.option.LootMenuOption;

public class LostShip extends Ship {
	private static final float DEF_MASS = 500;
	private static final float DEF_RADIUS = 10;
	
	public LostShip(String name, PVector heading, PVector position, PVector velocity) {
		super(name, heading, position, velocity, 150, 0, 0);
	}

	@Override
	public float getMass() {
		return DEF_MASS;
	}

	@Override
	public float getRadius() {
		return DEF_RADIUS;
	}

	@Override
	public void setupDockingMenu(Menu menu) {
		menu.add(new LootMenuOption("Scavenge", menu.getPlayer().getInventory(), getInventory()));
	}

	@Override
	public void drawNearby(float r) {
		drawShip(r, ShipModelType.FIGHTER);
	}
}  
