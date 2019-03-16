package vekta.item;

import vekta.Player;
import vekta.menu.Menu;

import java.io.Serializable;

import static processing.core.PApplet.*;
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
	
	public int getColor() {
		return getType().getColor();
	}

	public int randomPrice() {
		return max(0, round(v.random(2, 5) * getType().getPriceModifier()));
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
