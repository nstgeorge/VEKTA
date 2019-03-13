package vekta.spawner.objective;

import vekta.Resources;
import vekta.mission.Mission;
import vekta.mission.objective.Objective;
import vekta.mission.objective.SurveyFeatureObjective;
import vekta.spawner.MissionGenerator;

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
		String feature = Resources.generateString("planet_feature");
		return new SurveyFeatureObjective(feature);
	}
}
