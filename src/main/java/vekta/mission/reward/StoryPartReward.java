package vekta.mission.reward;

import vekta.knowledge.StoryKnowledge;
import vekta.mission.Mission;
import vekta.player.Player;
import vekta.story.StoryPart;

public class StoryPartReward extends Reward {
	private final StoryKnowledge knowledge;
	private final StoryPart part;

	public StoryPartReward(StoryKnowledge knowledge, StoryPart part) {
		this.knowledge = knowledge;
		this.part = part;
	}

	public StoryKnowledge getKnowledge() {
		return knowledge;
	}

	public StoryPart getPart() {
		return part;
	}

	@Override
	public String getName() {
		return "Story progress (" + getKnowledge().getName() + ")";
	}

	@Override
	public void onReward(Mission mission, Player player) {
		getKnowledge().getStory().addPart(getPart());
		player.send("Story updated: " + getKnowledge().getName()).withColor(getKnowledge().getColor(player));
	}
}
