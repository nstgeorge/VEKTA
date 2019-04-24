package vekta.person.personality;

import vekta.Resources;
import vekta.person.Dialog;

public class AppendPersonality extends Personality {
	@Override
	public String transformDialog(Dialog dialog, String type, String text) {
		return text + " " + Resources.generateString("personality_append");
	}
}
