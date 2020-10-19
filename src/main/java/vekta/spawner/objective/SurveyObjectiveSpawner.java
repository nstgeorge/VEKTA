package vekta.spawner.objective;

import vekta.mission.Mission;
import vekta.mission.objective.Objective;
import vekta.mission.objective.SurveyFeatureObjective;
import vekta.spawner.MissionGenerator;
import vekta.terrain.Terrain;

import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.v;

public class SurveyObjectiveSpawner implements MissionGenerator.ObjectiveSpawner {
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
		List<String> features = new ArrayList<>();
		for(int i = 0; i < mission.getTier() + v.random(2); i++) {
			String feature = randomSurveyFeature();
			if(!features.contains(feature)) {
				features.add(feature);
			}
		}

		for(int i = 1; i < features.size(); i++) {
			mission.add(new SurveyFeatureObjective(features.get(i)));
		}
		return new SurveyFeatureObjective(features.get(0));
	}

	public static String randomSurveyFeature() {
		Terrain terrain = MissionGenerator.randomLandingSite().getTerrain();
		return v.random(terrain.getFeatures()).getName();
	}
}
