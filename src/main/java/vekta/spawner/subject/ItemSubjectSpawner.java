package vekta.spawner.subject;

import vekta.spawner.ItemGenerator;
import vekta.spawner.StoryGenerator;
import vekta.story.Story;
import vekta.story.subject.ItemSubject;
import vekta.story.subject.StorySubject;

public class ItemSubjectSpawner implements StoryGenerator.SubjectSpawner {
	@Override
	public String getKey() {
		return "item";
	}

	@Override
	public StorySubject randomSubject(Story story, String name) {
		return new ItemSubject(ItemGenerator.randomItem());
	}
}
