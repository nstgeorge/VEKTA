package vekta.item;

public interface InventoryListener {
	default void onMoneyAdd(int amount) {
	}

	default void onMoneyRemove(int amount) {
	}

	default void onItemAdd(Item item) {
	}

	default void onItemRemove(Item item) {
	}
}
