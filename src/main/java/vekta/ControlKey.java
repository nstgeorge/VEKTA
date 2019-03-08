package vekta;

import static processing.core.PConstants.ESC;

public enum ControlKey {
	MENU_UP('w'),
	MENU_DOWN('s'),
	MENU_LEFT('a'),
	MENU_RIGHT('d'),
	MENU_SELECT(' '),
	MENU_CLOSE(ESC),
	SHIP_FORWARD('w'),
	SHIP_BACKWARD('s'),
	SHIP_LEFT('a'),
	SHIP_RIGHT('d'),
	SHIP_FIRE(' '),
	SHIP_MENU('v'),
	SHIP_LOADOUT('e'),
	SHIP_MISSIONS('q'),
	OBJECTIVE_CYCLE('o'),
	MISSION_CYCLE('p'),
	SHIP_LAND('\t'),
	SHIP_TARGET_CLEAR('t'),
	SHIP_TARGET_PLANET('1'),
	SHIP_TARGET_ASTEROID('2'),
	SHIP_TARGET_SHIP('3'),
	SHIP_TARGET_OBJECTIVE('4'),
	SHIP_TELESCOPE('r'),
	SHIP_HYPERDRIVE('f');

	private final char defaultKey;

	ControlKey(char defaultKey) {
		this.defaultKey = defaultKey;
	}

	public char getDefaultKey() {
		return defaultKey;
	}
}
