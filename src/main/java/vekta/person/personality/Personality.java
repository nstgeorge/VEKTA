package vekta.person.personality;

import vekta.person.Dialog;

import java.io.Serializable;

public abstract class Personality implements Serializable {
	public String transformDialog(String type, String text) {
		return text;
	}
	
	public void setupDialog(Dialog dialog) {
	}
}
