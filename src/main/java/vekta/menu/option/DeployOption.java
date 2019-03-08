package vekta.menu.option;

import vekta.Player;
import vekta.menu.Menu;
import vekta.object.SpaceObject;

import java.util.function.Supplier;

import static vekta.Vekta.addObject;

public class DeployOption implements MenuOption {
	private final String name;
	private final Player player;
	private final Supplier<SpaceObject> supplier;

	public DeployOption(String name, Player player, Supplier<SpaceObject> supplier) {
		this.name = name;
		this.player = player;
		this.supplier = supplier;
	}

	@Override
	public String getName() {
		return name;
	}

	public Player getPlayer() {
		return player;
	}

	public Supplier<SpaceObject> getSupplier() {
		return supplier;
	}

	@Override
	public void select(Menu menu) {
		addObject(getSupplier().get());
		menu.close();
	}
}
