package vekta.menu.option;

import vekta.context.World;
import vekta.menu.Menu;
import vekta.object.ship.Ship;

import static vekta.Vekta.setContext;

public class ShipUndockOption implements MenuOption {
	private final Ship ship;
	private final World world;

	public ShipUndockOption(Ship ship, World world) {
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
