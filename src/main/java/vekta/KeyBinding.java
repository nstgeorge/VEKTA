package vekta;

import java.awt.event.KeyEvent;
import com.github.strikerx3.jxinput.enums.XInputButton;

import static java.awt.event.KeyEvent.VK_NUMPAD5;
import static java.awt.event.KeyEvent.VK_NUMPAD9;
import static processing.core.PConstants.CONTROL;
import static processing.core.PConstants.ESC;

public enum KeyBinding {

	MENU_UP('w', XInputButton.DPAD_UP, "MENU_CONTROLS"),
	MENU_DOWN('s', XInputButton.DPAD_DOWN, "MENU_CONTROLS"),
	MENU_LEFT('a', XInputButton.DPAD_LEFT, "MENU_CONTROLS"),
	MENU_RIGHT('d', XInputButton.DPAD_RIGHT, "MENU_CONTROLS"),
	MENU_SELECT(' ', XInputButton.A, "MENU_CONTROLS"),
	MENU_CLOSE(ESC, XInputButton.START, "MENU_CONTROLS"),

	QUICK_SAVE(VK_NUMPAD5, XInputButton.LEFT_THUMBSTICK, "QUICK_ACTIONS"),
	QUICK_LOAD(VK_NUMPAD9, XInputButton.RIGHT_THUMBSTICK, "QUICK_ACTIONS"),
	OBJECTIVE_CYCLE('o', XInputButton.UNKNOWN, "QUICK_ACTIONS"),
	MISSION_CYCLE('p', XInputButton.UNKNOWN, "QUICK_ACTIONS"),
	ZOOM_IN('z', XInputButton.LEFT_SHOULDER, "QUICK_ACTIONS"),
	ZOOM_OUT('x', XInputButton.RIGHT_SHOULDER, "QUICK_ACTIONS"),

	SHIP_FORWARD('w', XInputButton.DPAD_UP, "SHIP_CONTROLS"),
	SHIP_BACKWARD('s', XInputButton.DPAD_DOWN, "SHIP_CONTROLS"),
	SHIP_LEFT('a', XInputButton.DPAD_LEFT, "SHIP_CONTROLS"),
	SHIP_RIGHT('d', XInputButton.DPAD_RIGHT, "SHIP_CONTROLS"),
	SHIP_ATTACK(' ', XInputButton.Y, "SHIP_CONTROLS"),
	SHIP_DEFEND(CONTROL, XInputButton.X, "SHIP_CONTROLS"),
	SHIP_LAND('\t', XInputButton.UNKNOWN, "SHIP_CONTROLS"),

	SHIP_MENU('v', XInputButton.UNKNOWN, "SHIP_COMPUTER"),
	SHIP_LOADOUT('e', XInputButton.UNKNOWN, "SHIP_COMPUTER"),
	SHIP_MISSIONS('q', XInputButton.UNKNOWN, "SHIP_COMPUTER"),
	SHIP_KNOWLEDGE('c', XInputButton.UNKNOWN, "SHIP_COMPUTER"),
	SHIP_INTERNET('y', XInputButton.UNKNOWN, "SHIP_COMPUTER"),
	SHIP_FOLLOWERS('u', XInputButton.UNKNOWN, "SHIP_COMPUTER"),
	SHIP_INVENTORY('i', XInputButton.UNKNOWN, "SHIP_COMPUTER"),
	SHIP_TARGET('t', XInputButton.UNKNOWN, "SHIP_COMPUTER"),
	SHIP_TARGET_PLANET('1', XInputButton.UNKNOWN, "SHIP_COMPUTER"),
	SHIP_TARGET_ASTEROID('2', XInputButton.UNKNOWN, "SHIP_COMPUTER"),
	SHIP_TARGET_SHIP('3', XInputButton.UNKNOWN, "SHIP_COMPUTER"),
	SHIP_TARGET_OBJECTIVE('4', XInputButton.UNKNOWN, "SHIP_COMPUTER"),
	SHIP_SCAN('r', XInputButton.UNKNOWN, "SHIP_COMPUTER"),
	SHIP_HYPERDRIVE('f', XInputButton.UNKNOWN, "SHIP_CONTROLS");

	private final int defaultKeyCode;
	private final XInputButton button;
	private final Section section;

	KeyBinding(char c, XInputButton button, String section) {
		this(KeyEvent.getExtendedKeyCodeForChar(c), button, section);
	}

	KeyBinding(int code, XInputButton button, String section) {
		this.defaultKeyCode = code;
		this.button = button;
		this.section = Section.valueOf(section);

	}

	public XInputButton getButton() { return button; }

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
