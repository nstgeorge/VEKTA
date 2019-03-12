package vekta.spawner.objective;

import vekta.Resources;
import vekta.mission.Mission;
import vekta.mission.objective.LandAtObjective;
import vekta.mission.objective.Objective;
import vekta.mission.objective.TaskObjective;
import vekta.spawner.MissionGenerator;
import vekta.terrain.LandingSite;

import static vekta.spawner.MissionGenerator.randomLandingSite;

public class TaskObjectiveSpawner implements MissionGenerator.ObjectiveSpawner {
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
		LandingSite site = randomLandingSite();
		mission.add(new LandAtObjective(site.getParent()));
		String task = Resources.generateString(site.getTerrain().isInhabited() ? "settlement_task" : "planet_task");
		return new TaskObjective(task, site.getParent());
	}
}
