package vekta.spawner;

import vekta.Resources;
import vekta.item.ItemType;
import vekta.menu.option.StoryProgressButton;
import vekta.person.Dialog;
import vekta.person.Person;
import vekta.story.Story;
import vekta.story.part.StoryPart;
import vekta.story.subject.StorySubject;
import vekta.story.subject.TextSubject;

import java.io.Serializable;
import java.util.*;

import static vekta.Vekta.register;
import static vekta.Vekta.v;

public final class StoryGenerator {
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

	public static Story createStory() {
		return createStory(10);
	}

	public static Story createStory(int maxSteps) {
		Story story = register(new Story());
		story.addPart(createPart(story, v.random(START_FILTER)));
		story.proceed(maxSteps);
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
		String[] data = v.random(chooseStrings(story, Resources.getStrings("part_" + key))).split("\\*");
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
			String text = v.random(chooseStrings(story, Resources.getStrings("subject_" + type))).replaceAll("\\*", name);
			return new TextSubject(parseSubjects(story, text));
		}
	}

	public static List<String> chooseStrings(Story story, String[] strings) {
		List<String> relevant = new ArrayList<>();
		List<String> other = new ArrayList<>();
		for(String string : strings) {
			if(string.startsWith("?[")) {
				int openIndex = string.indexOf('[');
				int closeIndex = string.indexOf(']');
				String key = string.substring(openIndex + 1, closeIndex);
				String result = string.substring(closeIndex + 1).trim();
				if(story.hasSubject(key)) {
					relevant.add(Resources.parseString(result));
				}
			}
			else {
				other.add(string);
			}
		}
		if(relevant.isEmpty()) {
			return Collections.singletonList(Resources.parseString(v.random(other)));
		}
		return relevant;
	}

	public static String parseSubjects(Story story, String string) {
		int openIndex, closeIndex;
		while((openIndex = string.indexOf('[')) != -1 && (closeIndex = string.indexOf(']')) > openIndex) {
			String key = string.substring(openIndex + 1, closeIndex);
			String text;
			StorySubject subject = story.getSubject(key);
			if(subject == null) {
				String[] data = key.split(" ");
				if(data.length != 2) {
					throw new RuntimeException("Invalid subject key: [" + key + "]");
				}
				String name = data[0];
				String type = data[1];
				subject = randomSubject(story, name, type);
				story.setSubject(key, subject);
				text = subject.getFullName();
			}
			else {
				text = subject.chooseShortName();
			}
			string = string.substring(0, openIndex) + text + string.substring(closeIndex + 1);
		}
		return string;
	}

	public static Dialog createDialog(Person person, Story story) {
		return createDialog(person, story, -1);
	}

	public static Dialog createDialog(Person person, Story story, int maxSteps) {
		Dialog dialog = new Dialog("story", person, story.getCurrentPart().getText(), ItemType.KNOWLEDGE.getColor());
		if(maxSteps != 0 && !story.getCurrentPart().isConclusion()) {
			dialog.add(new StoryProgressButton(story, person, maxSteps - 1));
		}
		return dialog;
	}

	public interface SubjectSpawner extends Serializable {
		String getKey();

		StorySubject randomSubject(Story story, String name);
	}
}
