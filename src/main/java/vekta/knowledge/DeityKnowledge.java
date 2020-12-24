package vekta.knowledge;

import vekta.deity.Deity;
import vekta.display.Layout;
import vekta.display.TextDisplay;
import vekta.display.VerticalLayout;
import vekta.item.ItemType;
import vekta.player.Player;

import static vekta.Vekta.v;

public class DeityKnowledge implements Knowledge {
	private final Deity deity;

	private float favor;

	public DeityKnowledge(Deity deity) {
		this.deity = deity;
	}

	public Deity getDeity() {
		return deity;
	}

	public float getFavor() {
		return favor;
	}

	public void addFavor(float favor) {
		this.favor += favor;
	}

	@Override
	public String getName() {
		return getDeity().getName();
	}

	@Override
	public int getColor(Player player) {
		return v.color(200);
	}

	@Override
	public int getArchiveValue() {
		return 20;
	}

	@Override
	public KnowledgeDelta getDelta(Knowledge other) {
		return other instanceof DeityKnowledge && getName().equals(other.getName()) ? KnowledgeDelta.BETTER : KnowledgeDelta.DIFFERENT;
	}

	@Override
	public void onLayout(Player player, Layout layout) {

		layout.add(new TextDisplay(String.format("Favor: %.1f", getFavor())))
				.customize().color(ItemType.KNOWLEDGE.getColor());

		VerticalLayout traits = layout.add(new VerticalLayout());
		traits.customize().color(100);

		for(String trait : getDeity().getTraits()) {
			traits.add(new TextDisplay(trait));
		}
	}
}
