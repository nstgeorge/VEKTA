package vekta.spawner.subject;

import vekta.spawner.PersonGenerator;
import vekta.spawner.StoryGenerator;
import vekta.story.part.Story;
import vekta.story.subject.PersonSubject;
import vekta.story.subject.StorySubject;

public class PersonSubjectSpawner implements StoryGenerator.SubjectSpawner {
	@Override
	public String getKey() {
		return "person";
	}

	@Override
	public StorySubject randomSubject(Story story, String name) {
		return new PersonSubject(PersonGenerator.createPerson());
	}
}
