package vekta.knowledge;

import vekta.Player;
import vekta.display.Layout;
import vekta.display.TextDisplay;
import vekta.story.Story;
import vekta.story.part.StoryPart;

public class StoryKnowledge implements Knowledge {
	private final Story story;
	private final String name;

	public StoryKnowledge(Story story, String name) {
		this.story = story;
		this.name = name;
	}

	public Story getStory() {
		return story;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getColor(Player player) {
		return 200;
	}

	@Override
	public int getArchiveValue() {
		return 1;
	}

	@Override
	public KnowledgeDelta getDelta(Knowledge other) {
		return other instanceof StoryKnowledge && ((StoryKnowledge)other).getStory() == getStory()
				? KnowledgeDelta.SAME
				: KnowledgeDelta.DIFFERENT;
	}

	@Override
	public void onLayout(Player player, Layout layout) {
		for(StoryPart part : getStory().getParts()) {
			layout.add(new TextDisplay(part.getText()));
		}
	}
}
