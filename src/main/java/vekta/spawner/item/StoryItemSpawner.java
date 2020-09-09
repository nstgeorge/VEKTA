package vekta.spawner.item;

import vekta.Resources;
import vekta.item.Item;
import vekta.item.StoryItem;
import vekta.spawner.ItemGenerator;
import vekta.spawner.StoryGenerator;
import vekta.story.part.Story;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static vekta.Vekta.register;

public class StoryItemSpawner implements ItemGenerator.ItemSpawner {
	private static final List<String[]> ASSOCIATIONS = Arrays.stream(Resources.getStrings("story_item_association"))
			.map(s -> Arrays.stream(s.split(":"))
					.map(String::trim)
					.toArray(String[]::new))
			.collect(Collectors.toList());

	@Override
	public float getWeight() {
		return .05F;
	}

	@Override
	public boolean isValid(Item item) {
		return item instanceof StoryItem;
	}

	@Override
	public Item create() {
		return randomStoryItem();
	}

	private static String getStoryType(String title) {
		for(String[] parts : ASSOCIATIONS) {
			if(title.contains(parts[0])) {
				return parts[1];
			}
		}
		return Resources.generateString("story_start_filter");
	}

	public static StoryItem randomStoryItem() {
		String title = Resources.generateString("item_story");
		String type = getStoryType(title);
		return new StoryItem(title, p -> {
			Story story = register(new Story());
			story.addPart(StoryGenerator.createPart(story, type));
			return story;
		});
	}
}
