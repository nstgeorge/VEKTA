package vekta.menu.option;

import vekta.item.Item;
import vekta.menu.Menu;
import vekta.object.SpaceObject;

import java.util.function.Supplier;

import static vekta.Vekta.register;

public class DeployButton implements ButtonOption {
	private final String name;
	private final Item item;
	private final Supplier<SpaceObject> supplier;

	public DeployButton(String name, Item item, Supplier<SpaceObject> supplier) {
		this.name = name;
		this.item = item;
		this.supplier = supplier;
	}

	@Override
	public String getName() {
		return name;
	}

	public Item getItem() {
		return item;
	}

	public Supplier<SpaceObject> getSupplier() {
		return supplier;
	}

	@Override
	public void onSelect(Menu menu) {
		register(getSupplier().get()).setPersistent(true);
		menu.getPlayer().getInventory().remove(getItem());
		menu.close();
	}
}
