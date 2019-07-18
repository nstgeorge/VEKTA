package vekta.menu.option;

import vekta.Player;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.object.SpaceObject;

import java.util.function.Supplier;

import static vekta.Vekta.register;

public class DeployButton implements ButtonOption {
	private final String name;
	private final Player player;
	private final Item item;
	private final Supplier<SpaceObject> supplier;

	public DeployButton(String name, Player player, Item item, Supplier<SpaceObject> supplier) {
		this.name = name;
		this.player = player;
		this.item = item;
		this.supplier = supplier;
	}

	@Override
	public String getName() {
		return name;
	}

	public Player getPlayer() {
		return player;
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
		getPlayer().getInventory().remove(getItem());
		menu.close();
	}
}
