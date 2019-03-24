package vekta.item;

import vekta.InfoGroup;
import vekta.Player;
import vekta.menu.Menu;

import java.io.Serializable;

import static processing.core.PApplet.max;
import static processing.core.PApplet.round;
import static vekta.Vekta.v;

public abstract class Item implements Serializable, Comparable<Item> {

	public abstract String getName();

	public abstract ItemType getType();

	public abstract int getMass();

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

	public void onInfo(InfoGroup info) {
	}

	@Override
	public int compareTo(Item other) {
		return getName().compareTo(other.getName());
	}
}
