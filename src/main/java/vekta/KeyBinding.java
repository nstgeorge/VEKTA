package vekta;

import java.awt.event.KeyEvent;

import com.github.strikerx3.jxinput.enums.XInputButton;

import static java.awt.event.KeyEvent.VK_NUMPAD5;
import static java.awt.event.KeyEvent.VK_NUMPAD9;
import static processing.core.PConstants.CONTROL;
import static processing.core.PConstants.ESC;

public enum KeyBinding {

	MENU_UP('w', XInputButton.DPAD_UP, KeyCategory.MENU_CONTROLS),
	MENU_DOWN('s', XInputButton.DPAD_DOWN, KeyCategory.MENU_CONTROLS),
	MENU_LEFT('a', XInputButton.DPAD_LEFT, KeyCategory.MENU_CONTROLS),
	MENU_RIGHT('d', XInputButton.DPAD_RIGHT, KeyCategory.MENU_CONTROLS),
	MENU_SELECT(' ', XInputButton.A, KeyCategory.MENU_CONTROLS),
	MENU_CLOSE(ESC, XInputButton.START, KeyCategory.MENU_CONTROLS),

	QUICK_SAVE(VK_NUMPAD5, XInputButton.LEFT_THUMBSTICK, KeyCategory.QUICK_ACTIONS),
	QUICK_LOAD(VK_NUMPAD9, XInputButton.RIGHT_THUMBSTICK, KeyCategory.QUICK_ACTIONS),
	OBJECTIVE_CYCLE('o', XInputButton.UNKNOWN, KeyCategory.QUICK_ACTIONS),
	MISSION_CYCLE('p', XInputButton.UNKNOWN, KeyCategory.QUICK_ACTIONS),
	ZOOM_IN('z', XInputButton.LEFT_SHOULDER, KeyCategory.QUICK_ACTIONS),
	ZOOM_OUT('x', XInputButton.RIGHT_SHOULDER, KeyCategory.QUICK_ACTIONS),

	SHIP_FORWARD('w', XInputButton.DPAD_UP, KeyCategory.SHIP_CONTROLS),
	SHIP_BACKWARD('s', XInputButton.DPAD_DOWN, KeyCategory.SHIP_CONTROLS),
	SHIP_LEFT('a', XInputButton.DPAD_LEFT, KeyCategory.SHIP_CONTROLS),
	SHIP_RIGHT('d', XInputButton.DPAD_RIGHT, KeyCategory.SHIP_CONTROLS),
	SHIP_ATTACK(' ', XInputButton.Y, KeyCategory.SHIP_CONTROLS),
	SHIP_DEFEND(CONTROL, XInputButton.X, KeyCategory.SHIP_CONTROLS),
	SHIP_LAND('\t', XInputButton.UNKNOWN, KeyCategory.SHIP_CONTROLS),

	SHIP_MENU('v', XInputButton.UNKNOWN, KeyCategory.SHIP_COMPUTER),
	SHIP_LOADOUT('e', XInputButton.UNKNOWN, KeyCategory.SHIP_COMPUTER),
	SHIP_MISSIONS('q', XInputButton.UNKNOWN, KeyCategory.SHIP_COMPUTER),
	SHIP_KNOWLEDGE('c', XInputButton.UNKNOWN, KeyCategory.SHIP_COMPUTER),
	SHIP_INTERNET('y', XInputButton.UNKNOWN, KeyCategory.SHIP_COMPUTER),
	SHIP_FOLLOWERS('u', XInputButton.UNKNOWN, KeyCategory.SHIP_COMPUTER),
	SHIP_INVENTORY('i', XInputButton.UNKNOWN, KeyCategory.SHIP_COMPUTER),
	SHIP_TARGET('t', XInputButton.UNKNOWN, KeyCategory.SHIP_COMPUTER),
	SHIP_TARGET_PLANET('1', XInputButton.UNKNOWN, KeyCategory.SHIP_COMPUTER),
	SHIP_TARGET_ASTEROID('2', XInputButton.UNKNOWN, KeyCategory.SHIP_COMPUTER),
	SHIP_TARGET_SHIP('3', XInputButton.UNKNOWN, KeyCategory.SHIP_COMPUTER),
	SHIP_TARGET_OBJECTIVE('4', XInputButton.UNKNOWN, KeyCategory.SHIP_COMPUTER),
	SHIP_SCAN('r', XInputButton.UNKNOWN, KeyCategory.SHIP_COMPUTER),
	SHIP_HYPERDRIVE('f', XInputButton.UNKNOWN, KeyCategory.SHIP_CONTROLS);

	private final int defaultKeyCode;
	private final XInputButton button;
	private final KeyCategory category;

	KeyBinding(char c, XInputButton button, KeyCategory category) {
		this(KeyEvent.getExtendedKeyCodeForChar(c), button, category);
	}

	KeyBinding(int code, XInputButton button, KeyCategory category) {
		this.defaultKeyCode = code;
		this.button = button;
		this.category = category;
	}

	public XInputButton getButton() {
		return button;
	}

	public int getDefaultKeyCode() {
		return defaultKeyCode;
	}

	public KeyCategory getCategory() {
		return category;
	}
}
