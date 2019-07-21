package vekta.spawner.item;

import vekta.Resources;
import vekta.item.Item;
import vekta.item.StoryItem;
import vekta.spawner.ItemGenerator;
import vekta.spawner.StoryGenerator;
import vekta.story.Story;

import java.util.List;
import java.util.Map;

import static vekta.Vekta.v;

public class StoryItemSpawner implements ItemGenerator.ItemSpawner {
	private static final Map<String, List<String>> STORY_ITEM_MAP = Resources.getStringMap("item_story_map", false);

	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public boolean isValid(Item item) {
		return item instanceof StoryItem;
	}

	@Override
	public Item create() {
		return randomStoryItem();
	}

	public static Item randomStoryItem() {
		String type = v.random(STORY_ITEM_MAP.keySet());
		return new StoryItem(Resources.generateString("item_story_decorator").replaceAll("\\*", type), p -> {
			Story story = new Story();
			story.addPart(StoryGenerator.createPart(story, v.random(STORY_ITEM_MAP.get(type))));
			story.proceed(10);
			return story;
		});
	}
}
