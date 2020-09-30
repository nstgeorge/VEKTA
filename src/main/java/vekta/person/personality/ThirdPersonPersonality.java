package vekta.person.personality;

import vekta.person.Dialog;
import vekta.person.Person;

public class ThirdPersonPersonality extends Personality {
	private static final String[] WORDS = {"I", "me", "myself"};

	@Override
	public String transformDialog(Dialog dialog, String type, String text) {
		text = " " + text;
		for(String word : WORDS) {
			text = text.replaceAll(" " + word + " ", " " + word + ", " + dialog.getPerson().getName() + ", ");
		}
		return text.trim();
	}
}
