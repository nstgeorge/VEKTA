package vekta.person.personality;

import static vekta.Vekta.v;

public class ShoutingPersonality extends Personality {
	@Override
	public String transformDialog(String type, String text) {
		// Occasionally say something without shouting
		if(v.chance(.1F)) {
			return text;
		}

		if(!text.startsWith("!")) {
			text = "!" + text;
		}
		return text.toUpperCase()
				.replace("?", "!?")
				.replace(".", "!");
	}
}
