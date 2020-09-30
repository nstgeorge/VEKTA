package vekta.knowledge;

import vekta.player.Player;
import vekta.display.Layout;
import vekta.display.TextDisplay;

import static vekta.Vekta.v;

public class TopicKnowledge implements Knowledge {
	private final String topic;
	private final String description;
	private final int value;

	public TopicKnowledge(String topic, String description, int value) {
		this.topic = topic;
		this.description = description;
		this.value = value;
	}

	public String getTopic() {
		return topic;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return getTopic();
	}

	@Override
	public int getArchiveValue() {
		return value;
	}

	@Override
	public int getColor(Player player) {
		return v.color(200);
	}

	@Override
	public KnowledgeDelta getDelta(Knowledge other) {
		return other instanceof TopicKnowledge && getName().equals(other.getName()) ? KnowledgeDelta.BETTER : KnowledgeDelta.DIFFERENT;
	}
	
	@Override
	public void onLayout(Player player, Layout layout) {
		layout.add(new TextDisplay(getDescription()));
	}
}
