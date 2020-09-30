package vekta.knowledge;

import vekta.display.Layout;
import vekta.display.TextDisplay;
import vekta.faction.Faction;
import vekta.menu.option.PlayerKnowledgeButton;
import vekta.player.Player;
import vekta.util.InfoGroup;

import static vekta.Vekta.v;

public class FactionKnowledge implements Knowledge {
	private final Faction faction;

	public FactionKnowledge(Faction faction) {
		this.faction = faction;
	}

	public Faction getFaction() {
		return faction;
	}

	@Override
	public String getName() {
		return getFaction().getName();
	}

	@Override
	public int getArchiveValue() {
		return 2;
	}

	@Override
	public int getColor(Player player) {
		return getFaction().getColor();
	}

	@Override
	public KnowledgeDelta getDelta(Knowledge other) {
		return other instanceof FactionKnowledge && getName().equals(other.getName()) ? KnowledgeDelta.BETTER : KnowledgeDelta.DIFFERENT;
	}

	@Override
	public void onLayout(Player player, Layout layout) {
		//		layout.add(new TextDisplay(getFaction().getDescription()));

		InfoGroup info = new InfoGroup();
		for(Faction faction : getFaction().getAllies()) {
			info.addStat("Ally", faction.getName());
		}
		for(Faction faction : getFaction().getEnemies()) {
			info.addStat("Enemy", faction.getName());
		}
		for(PersonKnowledge knowledge : player.findKnowledge(PersonKnowledge.class, k -> k.getPerson().getFaction() == getFaction())) {
			info.addTrait(knowledge.getPerson().getFullName());
		}
		info.onLayout(layout);

	}
}
