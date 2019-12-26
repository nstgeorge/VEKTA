package vekta.story;

import vekta.Sync;
import vekta.Syncable;
import vekta.story.part.StoryPart;
import vekta.story.subject.StorySubject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Story extends Syncable<Story> {
	private final @Sync List<StoryPart> parts = new ArrayList<>();
	private final @Sync Map<String, StorySubject> subjects = new HashMap<>();

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

	public boolean hasSubject(String key) {
		return getSubjects().containsKey(key);
	}

	public StorySubject getSubject(String key) {
		return getSubjects().get(key);
	}

	@SuppressWarnings("unchecked")
	public <T extends StorySubject> T getSubject(String key, Class<T> subjectClass) {
		StorySubject subject = getSubject(key);
		try {
			return (T)subject;
		}
		catch(ClassCastException e) {
			throw new RuntimeException("Wrong story subject type for key: `" + key + "` (expected " + subjectClass.getName() + "; found " + subject + ")");
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
