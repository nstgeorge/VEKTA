package vekta.menu.option;

import processing.core.PVector;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.object.CargoCrate;
import vekta.object.ship.Ship;

import static vekta.Vekta.register;
import static vekta.Vekta.v;

public class JettisonButton extends ItemButton {
	private final Inventory inv;

	public JettisonButton(Item item, Inventory inv) {
		super(item);
		this.inv = inv;
	}

	@Override
	public String getSelectVerb() {
		return "jettison";
	}

	@Override
	public void onSelect(Menu menu) {
		inv.remove(getItem());
		Ship ship = menu.getPlayer().getShip();
		register(new CargoCrate(getItem(), ship.getPosition().add(PVector.random2D().mult(ship.getRadius() * v.random(5, 10))), ship.getVelocity()));
		menu.remove(this);
	}
}
