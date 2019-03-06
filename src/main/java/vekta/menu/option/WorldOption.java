package vekta.menu.option;

import vekta.context.World;
import vekta.menu.Menu;

import static vekta.Vekta.setContext;

public class WorldOption implements MenuOption {
	private final String name;
	private final World world;

	public WorldOption(String name, World world) {
		this.name = name;
		this.world = world;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void select(Menu menu) {
		setContext(world);
	}
}
