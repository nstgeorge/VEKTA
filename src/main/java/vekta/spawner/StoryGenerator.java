package vekta.spawner;

import vekta.Resources;
import vekta.story.Story;
import vekta.story.part.StoryPart;
import vekta.story.subject.StorySubject;
import vekta.story.subject.TextSubject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static vekta.Vekta.register;
import static vekta.Vekta.v;

public final class StoryGenerator {
	private static final char SUBJECT_BEFORE = '[';
	private static final char SUBJECT_AFTER = ']';
	private static final Map<String, SubjectSpawner> SUBJECT_SPAWNERS = new HashMap<>();
	private static final List<String> START_FILTER = Arrays.asList(Resources.getStrings("story_start_filter"));
	private static final List<String> END_FILTER = Arrays.asList(Resources.getStrings("story_end_filter"));

	static {
		for(String key : START_FILTER) {
			checkPartStrings(key);
		}

		for(SubjectSpawner spawner : Resources.findSubclassInstances(SubjectSpawner.class)) {
			SUBJECT_SPAWNERS.put(spawner.getKey(), spawner);
		}
	}

	public static Story createStory(int length) {
		Story story = register(new Story());
		if(length > 0) {
			story.addPart(createPart(story, v.random(START_FILTER)));
			story.proceed(length - 1);
		}
		return story;
	}

	private static void checkPartStrings(String key) {
		for(String line : Resources.getStrings("part_" + key)) {
			String[] data = line.split("\\*");
			if(data.length == 1 && !END_FILTER.contains(key)) {
				throw new RuntimeException("Story part ended too early: " + key + " (" + data[0] + ")");
			}
			for(int i = 1; i < data.length; i++) {
				String path = data[i].trim();
				if(!Resources.hasStrings("part_" + path)) {
					throw new RuntimeException("Story path not found: " + path + " (found in part_" + key + ")");
				}
				checkPartStrings(path);
			}
		}
	}

	public static StoryPart createPart(Story story, String key) {
		String[] data = Resources.generateString("part_" + key).split("\\*");
		String text = parseSubjects(story, data[0]).trim();
		StoryPart part = new StoryPart(text);
		for(int i = 1; i < data.length; i++) {
			String path = data[i].trim();
			part.addPath(path);
		}
		return part;
	}

	public static StorySubject randomSubject(Story story, String name, String type) {
		SubjectSpawner spawner = SUBJECT_SPAWNERS.get(type);
		if(spawner != null) {
			return spawner.randomSubject(story, name);
		}
		else {
			return new TextSubject(parseSubjects(story, Resources.generateString("subject_" + type).replaceAll("\\*", name)));
		}
	}

	public static String parseSubjects(Story story, String string) {
		int openIndex, closeIndex;
		while((openIndex = string.indexOf(SUBJECT_BEFORE)) != -1 && (closeIndex = string.indexOf(SUBJECT_AFTER)) > openIndex) {
			String key = string.substring(openIndex + 1, closeIndex);
			String text;
			StorySubject subject = story.getSubject(key);
			if(subject == null) {
				String[] data = key.split(" ");
				if(data.length != 2) {
					throw new RuntimeException("Invalid subject key: " + SUBJECT_BEFORE + key + SUBJECT_AFTER);
				}
				String name = data[0];
				String type = data[1];
				subject = randomSubject(story, name, type);
				story.setSubject(key, subject);
				text = subject.chooseFullName();
			}
			else {
				text = subject.chooseShortName();
			}
			string = string.substring(0, openIndex) + text + string.substring(closeIndex + 1);
		}
		return string;
	}

	public interface SubjectSpawner extends Serializable {
		String getKey();

		StorySubject randomSubject(Story story, String name);
	}
}
