package vekta.knowledge;

import vekta.KeyBinding;
import vekta.display.Layout;
import vekta.player.Player;

import java.io.Serializable;

public interface Knowledge extends Serializable {
	KnowledgeDelta getDelta(Knowledge other);

	String getName();

	int getArchiveValue();

	int getColor(Player player);

	default boolean isValid(Player player) {
		return true;
	}

	default String getSecondaryText(Player player) {
		return null;
	}

	default String getCursorText(Player player) {
		return null;
	}

	default void onKeyPress(Player player, KeyBinding key) {
	}

	default void onLayout(Player player, Layout layout) {
	}
}