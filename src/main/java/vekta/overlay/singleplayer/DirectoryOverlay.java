package vekta.overlay.singleplayer;

import vekta.KeyBinding;
import vekta.Player;
import vekta.overlay.Overlay;

import java.util.LinkedHashMap;

import static processing.core.PConstants.*;
import static vekta.Vekta.UI_COLOR;
import static vekta.Vekta.v;

public class DirectoryOverlay implements Overlay {
	private static final int BAR_HEIGHT = 30;
	private static final int PADDING = 30;

	private final Player player;

	public DirectoryOverlay(Player player) {
		this.player = player;
	}

	// TODO: migrate to world shortcut key system
	private static final LinkedHashMap<KeyBinding, String> PANES = new LinkedHashMap<KeyBinding, String>() {{
		put(KeyBinding.SHIP_MENU, "Dashboard");
		put(KeyBinding.SHIP_KNOWLEDGE, "Navigation");
		put(KeyBinding.SHIP_LOADOUT, "Loadout");
		put(KeyBinding.SHIP_MISSIONS, "Missions");
		put(KeyBinding.SHIP_HYPERDRIVE, "Hyperdrive");
		put(KeyBinding.SHIP_INTERNET, "Internet");
	}};

	@Override
	public void render() {
		v.stroke(UI_COLOR);
		v.fill(0);
		v.rectMode(CORNERS);
		v.rect(-1, -1, v.width + 1, BAR_HEIGHT);

		v.textAlign(LEFT, CENTER);
		v.fill(UI_COLOR);
		int x = PADDING;
		for(KeyBinding key : PANES.keySet()) {
			String text = "[" + (char)key.getDefaultKeyCode() + "]: " + PANES.get(key);
			v.text(text, x, BAR_HEIGHT / 2F);
			x += v.textWidth(text) + PADDING;
		}
	}
}
