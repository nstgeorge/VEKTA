package vekta.story.subject;

public class TextSubject implements StorySubject {
	private final String name;

	public TextSubject(String name) {
		this.name = name;
	}

	@Override
	public String getFullName() {
		return name;
	}

	@Override
	public String chooseShortName() {
		return name;
	}
}
