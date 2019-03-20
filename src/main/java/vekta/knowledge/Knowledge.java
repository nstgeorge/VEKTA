package vekta.knowledge;

import vekta.Player;

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
	
	/**
	 * Draw observation information where the top left of the panel is (0, 0)
	 */
	void draw(Player player, float width, float height);

	void onSelect(Player player);
}
