package vekta.knowledge;

import vekta.Player;
import vekta.display.Layout;

import java.io.Serializable;

public interface Knowledge extends Serializable {
	KnowledgeDelta getDelta(Knowledge other);

	String getName();

	int getColor(Player player);

	String getSecondaryText(Player player);

	default String getSelectText(Player player) {
		return "select";
	}

	boolean isValid(Player player);

	void onSelect(Player player);

	void onLayout(Player player, Layout layout);
}
