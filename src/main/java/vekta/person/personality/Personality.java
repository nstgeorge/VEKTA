package vekta.person.personality;

import vekta.person.Dialog;

import java.io.Serializable;

public abstract class Personality implements Serializable {
	public String transformDialog(Dialog dialog, String type, String text) {
		return text;
	}

	public void prepareDialog(Dialog dialog) {
	}
}
