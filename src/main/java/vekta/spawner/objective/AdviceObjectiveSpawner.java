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

public class AdviceObjectiveSpawner implements ObjectiveSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Mission mission) {
		return true;
	}

	@Override
	public Objective getMainObjective(Mission mission) {
		Person person = randomMissionPerson(mission.getIssuer());
		return new DialogObjective("Advise", randomAdviceDialog(mission.getPlayer(), person));
	}

	public static Dialog randomAdviceDialog(Player player, Person person) {
		Dialog dialog = null;
		int dialogCt = (int)v.random(2) + 1;
		for(int i = 0; i < dialogCt; i++) {
			if(dialog != null) {
				Dialog next = person.createDialog("continue");
				next.addContinuation(dialog);
				Dialog appreciate = person.createDialog("appreciate");
				appreciate.addContinuation(next);
				dialog = appreciate;
			}
			Dialog advice = person.createDialog("advice");
			advice.addContinuation(dialog);
			dialog = advice;
		}
		return dialog;
	}
}
