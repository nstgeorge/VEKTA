package vekta.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Inventory implements Serializable, Iterable<Item> {
	private final InventoryListener listener;

	private final List<Item> items = new ArrayList<>();
	private int money;

	public Inventory() {
		this(null);
	}

	public Inventory(InventoryListener listener) {
		this.listener = listener;
	}

	public int itemCount() {
		return getItems().size();
	}

	public int getMoney() {
		return money;
	}

	public boolean has(int amount) {
		return amount <= money;
	}

	public void add(int amount) {
		money += amount;
		if(listener != null) {
			listener.onMoneyAdd(amount);
		}
	}

	public boolean remove(int amount) {
		if(!has(amount)) {
			return false;
		}
		money -= amount;
		if(listener != null) {
			listener.onMoneyRemove(amount);
		}
		return true;
	}

	public List<Item> getItems() {
		return new ArrayList<>(items);
	}

	public boolean has(Item item) {
		return items.contains(item);
	}

	public void add(Item item) {
		items.add(item);
		Collections.sort(items);
		if(listener != null) {
			listener.onItemAdd(item);
		}
	}

	public boolean remove(Item item) {
		if(items.remove(item)) {
			if(listener != null) {
				listener.onItemRemove(item);
			}
			return true;
		}
		return false;
	}

	public void moveTo(Inventory other) {
		other.add(money);
		for(Item item : this) {
			other.add(item);
		}
		items.clear();
		money = 0;
	}

	@Override
	public Iterator<Item> iterator() {
		return getItems().iterator();
	}
}

