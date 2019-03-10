package vekta.item;

import vekta.Player;
import vekta.menu.Menu;

import java.io.Serializable;

import static vekta.Vekta.v;

public class Item implements Serializable, Comparable<Item> {
	private final String name;
	private final ItemType type;

	public Item(String name, ItemType type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public ItemType getType() {
		return type;
	}

	public int randomPrice() {
		return (int)(v.random(1, 10) * getType().getPriceModifier());
	}

	public void onAdd(Player player) {
	}

	public void onRemove(Player player) {
	}

	public void onMenu(Menu menu) {
	}

	@Override
	public int compareTo(Item other) {
		return this.getName().compareTo(other.getName());
	}
}
