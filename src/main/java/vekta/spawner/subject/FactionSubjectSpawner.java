package vekta.spawner.subject;

import vekta.spawner.FactionGenerator;
import vekta.spawner.StoryGenerator;
import vekta.story.Story;
import vekta.story.subject.FactionSubject;
import vekta.story.subject.StorySubject;

public class FactionSubjectSpawner implements StoryGenerator.SubjectSpawner {
	@Override
	public String getKey() {
		return "faction";
	}

	@Override
	public StorySubject randomSubject(Story story, String name) {
		return new FactionSubject(FactionGenerator.randomFaction());
	}
}
