package vekta.spawner.objective;

import vekta.Resources;
import vekta.mission.Mission;
import vekta.mission.objective.LandAtObjective;
import vekta.mission.objective.Objective;
import vekta.mission.objective.SettlementTaskObjective;
import vekta.spawner.MissionGenerator;
import vekta.spawner.PersonGenerator;
import vekta.terrain.settlement.Settlement;

public class SettlementTaskObjectiveSpawner implements MissionGenerator.ObjectiveSpawner {
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
		Settlement settlement = PersonGenerator.randomHome();
		mission.add(new LandAtObjective(settlement.getParent()));
		return new SettlementTaskObjective(Resources.generateString("task_settlement"), settlement);
	}
}
