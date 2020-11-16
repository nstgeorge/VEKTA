package vekta;

import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.VK_NUMPAD5;
import static java.awt.event.KeyEvent.VK_NUMPAD9;
import static processing.core.PConstants.CONTROL;
import static processing.core.PConstants.ESC;

public enum KeyBinding {

	MENU_UP('w', "MENU_CONTROLS"),
	MENU_DOWN('s', "MENU_CONTROLS"),
	MENU_LEFT('a', "MENU_CONTROLS"),
	MENU_RIGHT('d', "MENU_CONTROLS"),
	MENU_SELECT(' ', "MENU_CONTROLS"),
	MENU_CLOSE(ESC, "MENU_CONTROLS"),
	QUICK_SAVE(VK_NUMPAD5, "QUICK_ACTIONS"),
	QUICK_LOAD(VK_NUMPAD9, "QUICK_ACTIONS"),
	OBJECTIVE_CYCLE('o', "QUICK_ACTIONS"),
	MISSION_CYCLE('p', "QUICK_ACTIONS"),
	SHIP_FORWARD('w', "SHIP_CONTROLS"),
	SHIP_BACKWARD('s', "SHIP_CONTROLS"),
	SHIP_LEFT('a', "SHIP_CONTROLS"),
	SHIP_RIGHT('d', "SHIP_CONTROLS"),
	ZOOM_IN('z', "QUICK_ACTIONS"),
	ZOOM_OUT('x', "QUICK_ACTIONS"),
	SHIP_ATTACK(' ', "SHIP_CONTROLS"),
	SHIP_DEFEND(CONTROL, "SHIP_CONTROLS"),
	SHIP_MENU('v', "SHIP_COMPUTER"),
	SHIP_LOADOUT('e', "SHIP_COMPUTER"),
	SHIP_MISSIONS('q', "SHIP_COMPUTER"),
	SHIP_KNOWLEDGE('c', "SHIP_COMPUTER"),
	SHIP_INTERNET('y', "SHIP_COMPUTER"),
	SHIP_FOLLOWERS('u', "SHIP_COMPUTER"),
	SHIP_INVENTORY('i', "SHIP_COMPUTER"),
	SHIP_LAND('\t', "SHIP_CONTROLS"),
	SHIP_TARGET('t', "SHIP_COMPUTER"),
	SHIP_TARGET_PLANET('1', "SHIP_COMPUTER"),
	SHIP_TARGET_ASTEROID('2', "SHIP_COMPUTER"),
	SHIP_TARGET_SHIP('3', "SHIP_COMPUTER"),
	SHIP_TARGET_OBJECTIVE('4', "SHIP_COMPUTER"),
	SHIP_SCAN('r', "SHIP_COMPUTER"),
	SHIP_HYPERDRIVE('f', "SHIP_CONTROLS");

	private final int defaultKeyCode;
	private final Section section;

	KeyBinding(char c, String section) {
		this(KeyEvent.getExtendedKeyCodeForChar(c), section);
	}

	KeyBinding(int code, String section) {
		this.defaultKeyCode = code;
		this.section = Section.valueOf(section);

	}

	public int getDefaultKeyCode() {
		return defaultKeyCode;
	}

	public String getSection() { return section.name(); }

	public enum Section {
		SHIP_CONTROLS,
		SHIP_COMPUTER,
		QUICK_ACTIONS,
		MENU_CONTROLS
	}
}
