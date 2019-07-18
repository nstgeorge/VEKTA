package vekta.object.ship;

import processing.core.PVector;
import vekta.menu.Menu;
import vekta.menu.option.LootMenuButton;

import static processing.core.PApplet.sq;
import static vekta.Vekta.v;

public class LostShip extends Ship {
	private static final float MASS_PER_RADIUS = 10;

	private final float radius = v.random(5, 30);
	private final ShipModelType model = v.random(ShipModelType.values());

	public LostShip(String name, PVector heading, PVector position, PVector velocity) {
		super(name, heading, position, velocity, 150, 0, 0);
	}

	@Override
	public float getMass() {
		return sq(radius * MASS_PER_RADIUS);
	}

	@Override
	public float getRadius() {
		return radius;
	}

	@Override
	public void setupDockingMenu(Menu menu) {
		menu.add(new LootMenuButton("Scavenge", menu.getPlayer().getInventory(), getInventory()));
	}

	@Override
	public void drawNearby(float r) {
		drawShip(r, model);
	}
}  
