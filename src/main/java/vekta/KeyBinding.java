package vekta;

import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.*;
import static processing.core.PConstants.*;

public enum KeyBinding {
	MENU_UP('w'),
	MENU_DOWN('s'),
	MENU_LEFT('a'),
	MENU_RIGHT('d'),
	MENU_SELECT(' '),
	MENU_CLOSE(ESC),
	QUICK_SAVE(VK_NUMPAD5),
	QUICK_LOAD(VK_NUMPAD9),
	SHIP_FORWARD('w'),
	SHIP_BACKWARD('s'),
	SHIP_LEFT('a'),
	SHIP_RIGHT('d'),
	SHIP_ATTACK(' '),
	SHIP_DEFEND(CONTROL),
	SHIP_MENU('v'),
	SHIP_LOADOUT('e'),
	SHIP_MISSIONS('q'),
	SHIP_NAVIGATION('c'),
	SHIP_INTERNET('y'),
	OBJECTIVE_CYCLE('o'),
	MISSION_CYCLE('p'),
	SHIP_LAND('\t'),
	SHIP_TARGET_CLEAR('t'),
	SHIP_TARGET_PLANET('1'),
	SHIP_TARGET_ASTEROID('2'),
	SHIP_TARGET_SHIP('3'),
	SHIP_TARGET_OBJECTIVE('4'),
	SHIP_SCAN('r'),
	SHIP_HYPERDRIVE('f');

	private final int defaultKeyCode;

	KeyBinding(char c) {
		this.defaultKeyCode = KeyEvent.getExtendedKeyCodeForChar(c);
	}

	KeyBinding(int code) {
		this.defaultKeyCode = code;
	}

	public int getDefaultKeyCode() {
		return defaultKeyCode;
	}
}
