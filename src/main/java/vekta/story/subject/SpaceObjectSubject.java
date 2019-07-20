package vekta.story.subject;

import vekta.object.SpaceObject;

public class SpaceObjectSubject implements StorySubject {
	private final SpaceObject spaceObject;

	public SpaceObjectSubject(SpaceObject spaceObject) {
		this.spaceObject = spaceObject;
	}

	public SpaceObject getSpaceObject() {
		return spaceObject;
	}

	@Override
	public String chooseFullName() {
		return getSpaceObject().getName();
	}
}
