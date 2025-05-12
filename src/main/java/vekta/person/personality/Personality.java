package vekta.person.personality;

import vekta.knowledge.PersonKnowledge;
import vekta.person.Dialog;
import vekta.person.Person;
import vekta.player.Player;

import java.io.Serializable;

public abstract class Personality implements Serializable {
	public boolean isRandomlyAssignable() {
		return true;
	}

	public void preparePerson(Person person) {
	}

	public String getDialogType(Person person, Player player) {
		if(player.hasKnowledge(PersonKnowledge.class, k -> k.getPerson() == person)) {
			return "greeting";
		}
		if(!person.getOpinion(player.getFaction()).isPositive()) {
			return "acting_suspicious";
		}
		return "unknown";
	}

	public String transformDialog(Dialog dialog, String type, String text) {
		return text;
	}

	public void prepareDialog(Dialog dialog) {
	}
}
