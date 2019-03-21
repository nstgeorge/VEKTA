package vekta.knowledge;

import vekta.KeyBinding;
import vekta.Player;
import vekta.display.Layout;

import java.io.Serializable;

public interface Knowledge extends Serializable {
	KnowledgeDelta getDelta(Knowledge other);

	String getName();

	int getArchiveValue();
	
	int getColor(Player player);

	String getSecondaryText(Player player);

	String getCursorText(Player player);

	boolean isValid(Player player);

	void onKeyPress(Player player, KeyBinding key);

	void onLayout(Player player, Layout layout);
}