package vekta.story.subject;

import java.io.Serializable;

public interface StorySubject extends Serializable {
	String getFullName();

	default String chooseShortName() {
		return getFullName();
	}
}
