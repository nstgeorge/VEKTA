package vekta.spawner.subject;

import vekta.spawner.MissionGenerator;
import vekta.spawner.StoryGenerator;
import vekta.story.Story;
import vekta.story.subject.SpaceObjectSubject;
import vekta.story.subject.StorySubject;

public class PlanetSubjectSpawner implements StoryGenerator.SubjectSpawner {
	@Override
	public String getKey() {
		return "planet";
	}

	@Override
	public StorySubject randomSubject(Story story, String name) {
		return new SpaceObjectSubject(MissionGenerator.randomLandingSite().getParent());
	}
}
