package vekta.spawner.objective;

import vekta.mission.Mission;
import vekta.mission.objective.Objective;
import vekta.mission.objective.SurveyFeatureObjective;
import vekta.spawner.MissionGenerator;
import vekta.terrain.Terrain;

import static vekta.Vekta.v;

public class SurveyFeatureObjectiveSpawner implements MissionGenerator.ObjectiveSpawner {
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
		Terrain terrain = MissionGenerator.randomLandingSite().getTerrain();
		return new SurveyFeatureObjective(v.random(terrain.getFeatures()));
	}
}
