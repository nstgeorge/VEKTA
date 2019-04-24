package vekta.person.personality;

import vekta.person.Dialog;

public class ThirdPersonPersonality extends Personality {
	@Override
	public String transformDialog(Dialog dialog, String type, String text) {
		return (" " + text).replaceAll(" I ", "I, " + dialog.getPerson().getName() + ", ").trim();
	}
}
