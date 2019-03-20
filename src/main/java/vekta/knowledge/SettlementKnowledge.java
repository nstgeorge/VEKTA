package vekta.knowledge;

import vekta.Player;
import vekta.object.SpaceObject;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.v;

public class SettlementKnowledge extends SpaceObjectKnowledge {
	private final Settlement settlement;

	public SettlementKnowledge(KnowledgeLevel level, Settlement settlement) {
		super(level);

		this.settlement = settlement;
	}

	public Settlement getSettlement() {
		return settlement;
	}

	@Override
	public SpaceObject getSpaceObject() {
		return getSettlement().getParent();
	}

	@Override
	public boolean isSimilar(LevelKnowledge other) {
		return other instanceof SettlementKnowledge && getSettlement() == ((SettlementKnowledge)other).getSettlement();
	}

	@Override
	public String getName() {
		if(getLevel() == KnowledgeLevel.AWARE) {
			return "(Unknown Settlement)";
		}
		return getSettlement().getName();
	}

	@Override
	public int getColor(Player player) {
		if(getLevel() == KnowledgeLevel.AWARE) {
			return v.color(100);
		}
		return getSettlement().getColor();
	}

	@Override
	public void draw(Player player, float width, float height) {
		// TODO: generalize stat/quality rendering

		v.fill(getSpaceObject().getColor());
		v.text("Planet: " + getSpaceObject().getName(), 0, 0);

		if(KnowledgeLevel.SCANNED.isAvailableFrom(getLevel())) {
			v.fill(getSettlement().getColor());
			v.text("Population: " + getSettlement().getPopulation(), 0, SPACING * 2);
		}
	}
}
