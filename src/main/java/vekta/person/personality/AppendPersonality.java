package vekta.person.personality;

import vekta.Resources;

public class AppendPersonality extends Personality {
	@Override
	public String transformDialog(String type, String text) {
		return text + " " + Resources.generateString("personality_append");
	}
}
