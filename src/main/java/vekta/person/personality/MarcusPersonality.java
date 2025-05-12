package vekta.person.personality;

import vekta.knowledge.ObservationLevel;
import vekta.knowledge.PersonKnowledge;
import vekta.person.Dialog;
import vekta.person.Person;
import vekta.player.Player;

public class MarcusPersonality extends Personality {
	public boolean isRandomlyAssignable() {
		return false;
	}

	@Override
	public void preparePerson(Person person) {
		person.addInterest("rolling");
	}

	@Override
	public String getDialogType(Person person, Player player) {
		if(player.hasKnowledge(PersonKnowledge.class, k -> k.getPerson() == person && ObservationLevel.VISITED.isBetter(k.getLevel()))) {
			return "marcus_meet";
		}
		return "marcus";
	}

	public void prepareDialog(Dialog dialog) {
		dialog.then("marcus_leave");
	}
}
