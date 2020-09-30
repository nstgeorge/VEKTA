package vekta.person.personality;

import vekta.person.Dialog;
import vekta.person.Person;

import static vekta.Vekta.v;

public class ShoutingPersonality extends Personality {
	@Override
	public void preparePerson(Person person) {
		person.addInterest("shouting");
	}

	@Override
	public String transformDialog(Dialog dialog, String type, String text) {
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
