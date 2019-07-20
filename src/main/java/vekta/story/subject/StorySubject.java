package vekta.story.subject;

import java.io.Serializable;

public interface StorySubject extends Serializable {
	String chooseFullName();

	default String chooseShortName() {
		return chooseFullName();
	}
}
