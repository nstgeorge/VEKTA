package vekta.item;

import vekta.Player;
import vekta.knowledge.StoryKnowledge;
import vekta.story.Story;

import java.io.Serializable;

public class StoryItem extends Item {
	private final String name;
	private final StoryProvider provider;

	private Story story;

	public StoryItem(String name, StoryProvider provider) {
		this.name = name;
		this.provider = provider;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemType getType() {
		return ItemType.MISSION;
	}

	@Override
	public int getMass() {
		return 1;
	}

	@Override
	public int randomPrice() {
		return super.randomPrice() * 2;
	}

	public Story findStory(Player player) {
		if(story == null) {
			story = provider.provide(player);
		}
		return story;
	}

	@Override
	public void onAdd(Player player) {
		boolean hadStory = player.hasKnowledge(StoryKnowledge.class, k -> k.getStory() == findStory(player));
		player.addKnowledge(new StoryKnowledge(findStory(player), getName()));
		if(!hadStory) {
			player.send("New story from " + getName()).withColor(getColor());
		}
		
		//TODO: story UI
	}

	public interface StoryProvider extends Serializable {
		Story provide(Player player);
	}
}
