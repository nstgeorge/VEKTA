package vekta.person.personality;

import vekta.Resources;
import vekta.person.Dialog;
import vekta.person.Person;

import java.util.List;
import java.util.Map;

import static vekta.Vekta.v;

public class StorytellingPersonality extends Personality {
	@Override
	public void preparePerson(Person person) {
		person.addInterest("storytelling");
	}

	@Override
	public void prepareDialog(Dialog dialog) {
		if(dialog.getType().equals("chat")) {
			dialog.add("Tell me a story!", dialog.getPerson().createDialog("request_story"));
		}
	}
}
