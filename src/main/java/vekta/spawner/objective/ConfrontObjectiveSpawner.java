package vekta.spawner.objective;

import vekta.Player;
import vekta.mission.Mission;
import vekta.mission.MissionIssuer;
import vekta.mission.objective.DialogObjective;
import vekta.mission.objective.Objective;
import vekta.person.Dialog;
import vekta.person.Person;
import vekta.spawner.MissionGenerator;

import static vekta.Vekta.v;
import static vekta.spawner.MissionGenerator.randomMissionPerson;

public class ConfrontObjectiveSpawner implements MissionGenerator.ObjectiveSpawner {
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
		return new DialogObjective("Confront", randomConfrontDialog(mission.getPlayer(), person, mission.getIssuer()));
	}

	public static Dialog randomConfrontDialog(Player player, Person person, MissionIssuer sender) {
		Dialog dialog = person.createDialog("confronted");

		dialog.then("confession");

		if(v.chance(.75F)) {
			Dialog greeting = person.createDialog("greeting");
			greeting.add(sender.getName() + " sent me to chat with you.", dialog);
			return greeting;
		}
		return dialog;
	}
}
