package vekta.spawner.objective;

import vekta.Player;
import vekta.mission.Mission;
import vekta.mission.objective.DialogObjective;
import vekta.mission.objective.Objective;
import vekta.person.Dialog;
import vekta.person.Person;

import static vekta.Vekta.v;
import static vekta.spawner.MissionGenerator.ObjectiveSpawner;
import static vekta.spawner.MissionGenerator.randomMissionPerson;

public class AssassinateObjectiveSpawner implements ObjectiveSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Mission mission) {
		return mission.getTier() >= 3;
	}

	@Override
	public Objective getMainObjective(Mission mission) {
		Person person = randomMissionPerson(mission.getIssuer());
		return new DialogObjective("Assassinate", person.createDialog("nervous_assassin"));
	}

	public static Dialog randomAdviceDialog(Player player, Person person) {
		Dialog dialog = person.createDialog("advice");
		int adviceCt = (int)v.random(2) + 1;
		while(adviceCt-- > 0) {
			// Add continuation dialog for subsequent advice
			if(adviceCt > 0) {
				dialog.then("continue").then("advice");
			}
		}
		return dialog;
	}
}
