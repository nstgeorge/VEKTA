package vekta.menu.option;

import vekta.context.World;
import vekta.menu.Menu;
import vekta.object.Ship;

import static vekta.Vekta.setContext;

public class UndockOption implements MenuOption {
	private final Ship ship;
	private final World world;

	public UndockOption(Ship ship, World world) {
		this.ship = ship;
		this.world = world;
	}

	@Override
	public String getName() {
		return "Undock";
	}

	@Override
	public void select(Menu menu) {
		setContext(world);
		ship.undock();
	}
}
