package vekta.item;

import java.io.Serializable;

public interface InventoryListener extends Serializable {
	default void onMoneyAdd(int amount) {
	}

	default void onMoneyRemove(int amount) {
	}

	default void onItemAdd(Item item) {
	}

	default void onItemRemove(Item item) {
	}
}
