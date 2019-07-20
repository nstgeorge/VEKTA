package vekta.story;

import vekta.Sync;
import vekta.Syncable;
import vekta.story.part.StoryPart;
import vekta.story.subject.StorySubject;

import java.util.*;

public class Story extends Syncable<Story> {
	private final @Sync List<StoryPart> parts = new ArrayList<>();
	private final @Sync Map<String, StorySubject> subjects = new HashMap<>();

	public Story() {
	}

	public List<StoryPart> getParts() {
		return parts;
	}

	public void addPart(StoryPart part) {
		if(!parts.contains(part)) {
			parts.add(part);
		}
		syncChanges();
	}

	public Map<String, StorySubject> getSubjects() {
		return subjects;
	}

	@SuppressWarnings("unchecked")
	public <T extends StorySubject> T getSubject(String key) {
		StorySubject subject = getSubjects().get(key);
		try {
			return (T)subject;
		}
		catch(ClassCastException e) {
			throw new RuntimeException("Wrong story subject type for key: `" + key + "` (found " + subject + ")");
		}
	}

	public void setSubject(String key, StorySubject subject) {
		getSubjects().put(key, subject);
	}

	public StoryPart getCurrentPart() {
		return getParts().get(getParts().size() - 1);
	}

	public List<StoryPart> proceed(int maxSteps) {
		StoryPart part = getCurrentPart();
		if(part == null) {
			return Collections.emptyList();
		}
		List<StoryPart> parts = new ArrayList<>();
		for(int i = 0; i < maxSteps; i++) {
			StoryPart next = part.chooseNext(this);
			if(next == null) {
				break;
			}
			addPart(next);
			parts.add(next);
			part = next;
		}
		return parts;
	}
}
