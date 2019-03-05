package vekta;

public enum ControlKey {
	// TODO: add menu keys
	SHIP_FORWARD('w'),
	SHIP_BACKWARD('s'),
	SHIP_LEFT('a'),
	SHIP_RIGHT('d'),
	SHIP_FIRE('x'),
	SHIP_MENU('v'),
	SHIP_LOADOUT('e'),
	SHIP_MISSIONS('q'),
	OBJECTIVE_CYCLE('o'),
	MISSION_CYCLE('p'),
	SHIP_LAND('\t'),
	SHIP_TARGET_CLEAR('t'),
	SHIP_TARGET_PLANET('1'),
	SHIP_TARGET_SHIP('2'),
	SHIP_TARGET_ASTEROID('3'),
	SHIP_TELESCOPE('r'),
	SHIP_HYPERDRIVE('~');

	private final char defaultKey;

	ControlKey(char defaultKey) {
		this.defaultKey = defaultKey;
	}

	public char getDefaultKey() {
		return defaultKey;
	}
}
