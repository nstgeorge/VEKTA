package vekta.knowledge;

import vekta.KeyBinding;
import vekta.Player;
import vekta.display.Layout;
import vekta.display.TextDisplay;
import vekta.item.ItemType;
import vekta.story.Story;
import vekta.story.part.StoryPart;

import java.util.HashSet;
import java.util.Set;

public class StoryKnowledge implements Knowledge {
	private final Story story;
	private final String name;

	private final Set<String> askedForDetails = new HashSet<>();

	public StoryKnowledge(Story story, String name) {
		this.story = story;
		this.name = name;
	}

	public Story getStory() {
		return story;
	}

	public boolean hasAskedForDetails(String key) {
		return askedForDetails.contains(key);
	}

	public void setAskedForDetails(String key) {
		askedForDetails.add(key);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getColor(Player player) {
		return ItemType.KNOWLEDGE.getColor();
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
		layout.customize()
				.spacing(14);

		for(StoryPart part : getStory().getParts()) {
			layout.add(new TextDisplay(part.getText()));
		}
		if(!getStory().getCurrentPart().isConclusion()) {
			layout.add(new TextDisplay("To be continued..."))
					.customize()
					.color(100);
		}
	}

	@Override
	public void onKeyPress(Player player, KeyBinding key) {
		// TEMP
		//		player.getShip().setNavigationTarget(getStory().getSubject("main person", PersonSubject.class).getPerson().findHomeObject());
	}
}
