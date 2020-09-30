package vekta.item;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public final class Inventory implements Serializable, Iterable<Item> {
	private final InventoryListener listener;

	private final List<Item> items = new CopyOnWriteArrayList<>();
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
		return items;
	}

	public boolean has(Item item) {
		return items.contains(item);
	}

	public void add(Item item) {
		int index = 0;
		while(index < items.size() && item.compareTo(items.get(index)) > 0) {
			index++;
		}
		items.add(index, item);
		//		Collections.sort(items);
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

	public void clearMoney() {
		remove(getMoney());
	}

	public void clearItems() {
		for(Item item : this) {
			remove(item);
		}
	}

	public void clear() {
		clearMoney();
		clearItems();
	}

	public void moveTo(Inventory other) {
		other.add(getMoney());
		for(Item item : this) {
			other.add(item);
		}
		clear();
	}

	public Stream<Item> stream() {
		return getItems().stream();
	}

	@Override
	public Iterator<Item> iterator() {
		return getItems().iterator();
	}
}

