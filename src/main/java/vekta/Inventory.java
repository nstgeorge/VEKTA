package vekta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class Inventory implements Iterable<Item> {
	private final List<Item> items = new ArrayList<>();
	private int money;

	public Inventory() {

	}

	public int getMoney() {
		return money;
	}

	public boolean has(int amount) {
		return amount <= money;
	}

	public void add(int amount) {
		money += amount;
	}

	public boolean remove(int amount) {
		if(!has(amount)) {
			return false;
		}
		money -= amount;
		return true;
	}

	public List<Item> getItems() {
		return items;
	}

	public boolean has(Item item) {
		return items.contains(item);
	}

	public void add(Item item) {
		items.add(item);
		Collections.sort(items);
	}

	public boolean remove(Item item) {
		return items.remove(item);
	}

	@Override
	public Iterator<Item> iterator() {
		return getItems().iterator();
	}
}

class Item implements Comparable<Item> {
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

	@Override
	public int compareTo(Item other) {
		return this.getName().compareTo(other.getName());
	}
}

enum ItemType {
	COMMON(0xFFCCCCCC, 1),
	RARE(0xFFFFAA88, 2),
	LEGENDARY(0xFFAA88FF, 10);

	private final int color;
	private final float priceModifier;

	ItemType(int color, float priceModifier) {
		this.color = color;
		this.priceModifier = priceModifier;
	}

	public int getColor() {
		return color;
	}

	public float getPriceModifier() {
		return priceModifier;
	}
	
	public int randomPrice() {
		return (int)(Vekta.getInstance().random(1, 10) * priceModifier);
	}
}
