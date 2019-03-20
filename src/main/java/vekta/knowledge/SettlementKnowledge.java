package vekta.knowledge;

import vekta.Player;
import vekta.display.Layout;
import vekta.display.TextDisplay;
import vekta.display.VerticalLayout;
import vekta.object.SpaceObject;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.v;

public class SettlementKnowledge extends SpaceObjectKnowledge {
	private final Settlement settlement;

	public SettlementKnowledge(ObservationLevel level, Settlement settlement) {
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
	public boolean isSimilar(ObservationKnowledge other) {
		return other instanceof SettlementKnowledge && getSettlement() == ((SettlementKnowledge)other).getSettlement();
	}

	@Override
	public String getName() {
		if(getLevel() == ObservationLevel.AWARE) {
			return "(Unknown Settlement)";
		}
		return getSettlement().getName();
	}

	@Override
	public int getColor(Player player) {
		if(getLevel() == ObservationLevel.AWARE) {
			return v.color(100);
		}
		return getSettlement().getColor();
	}

	@Override
	public void onLayout(Player player, Layout layout) {
		layout.add(new TextDisplay("Planet: " + getSpaceObject().getName()))
				.customize().color(getSpaceObject().getColor());

		if(ObservationLevel.SCANNED.isAvailableFrom(getLevel())) {
			Layout scanned = layout.add(new VerticalLayout());

			scanned.add(new TextDisplay("Population: " + getSettlement().getPopulation()));
		}
	}
}
