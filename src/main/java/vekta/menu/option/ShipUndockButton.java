package vekta.menu.option;

import vekta.world.World;
import vekta.menu.Menu;
import vekta.object.ship.Ship;

import static vekta.Vekta.setContext;

public class ShipUndockButton extends ButtonOption {
	private final Ship ship;
	private final World world;

	public ShipUndockButton(Ship ship, World world) {
		this.ship = ship;
		this.world = world;
	}

	@Override
	public String getName() {
		return "Undock";
	}

	@Override
	public void onSelect(Menu menu) {
		setContext(world);
		ship.undock();
	}
}
