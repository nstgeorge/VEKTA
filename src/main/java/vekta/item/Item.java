package vekta.item;

import vekta.menu.Menu;

public class Item implements Comparable<Item> {
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
	
	public void setupActionMenu(Menu menu){
	}

	@Override
	public int compareTo(Item other) {
		return this.getName().compareTo(other.getName());
	}
}
