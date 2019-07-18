package vekta.menu.option;

import processing.core.PVector;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.object.CargoCrate;
import vekta.object.ship.Ship;

import static vekta.Vekta.register;
import static vekta.Vekta.v;

public class JettisonButton implements ButtonOption {
	private final Item item;
	private final Inventory inv;

	public JettisonButton(Item item, Inventory inv) {
		this.item = item;
		this.inv = inv;
	}

	@Override
	public String getName() {
		return item.getName();
	}

	@Override
	public int getColor() {
		return item.getColor();
	}

	@Override
	public String getSelectVerb() {
		return "jettison";
	}

	@Override
	public void onSelect(Menu menu) {
		inv.remove(item);
		Ship ship = menu.getPlayer().getShip();
		register(new CargoCrate(item, ship.getPosition().add(PVector.random2D().mult(ship.getRadius() * v.random(5, 10))), ship.getVelocity()));
		menu.remove(this);
	}
}
