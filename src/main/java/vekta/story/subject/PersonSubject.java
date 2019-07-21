package vekta.story.subject;

import vekta.person.Person;

public class PersonSubject implements StorySubject {
	private final Person person;

	public PersonSubject(Person person) {
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	@Override
	public String getFullName() {
		return getPerson().getFullName();
	}

	@Override
	public String chooseShortName() {
		return getPerson().getName();
	}
}
