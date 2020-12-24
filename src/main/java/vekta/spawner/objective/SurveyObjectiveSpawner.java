package vekta.spawner.objective;

import vekta.mission.Mission;
import vekta.mission.objective.Objective;
import vekta.mission.objective.SurveyObjective;
import vekta.spawner.MissionGenerator;
import vekta.terrain.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
		List<String> tags = new ArrayList<>();
		for(int i = 0; i < mission.getTier() + v.random(2); i++) {
			String tag = randomSurveyTag();
			if(!tags.contains(tag)) {
				tags.add(tag);
			}
		}

		for(int i = 1; i < tags.size(); i++) {
			mission.add(new SurveyObjective(tags.get(i)));
		}
		return new SurveyObjective(tags.get(0));
	}

	public static String randomSurveyTag() {
		Set<String> tags = null;
		int features = 0;
		while(features == 0) {
			Terrain terrain = MissionGenerator.randomLandingSite().getTerrain();
			tags = terrain.findSurveyTags();
			features = tags.size();
		}

		return v.random(tags);
	}
}
