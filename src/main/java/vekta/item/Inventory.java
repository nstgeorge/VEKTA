package vekta.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Inventory implements Iterable<Item> {
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

