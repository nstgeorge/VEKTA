package vekta.knowledge;

import vekta.display.Layout;
import vekta.display.TextDisplay;
import vekta.display.VerticalLayout;
import vekta.display.VisualDisplay;
import vekta.object.SpaceObject;
import vekta.player.Player;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.quantityString;
import static vekta.Vekta.v;

public class SettlementKnowledge extends SpaceObjectKnowledge {
	private static final float VISUAL_SIZE = 400;

	private final Settlement settlement;

	public SettlementKnowledge(ObservationLevel level, Settlement settlement) {
		super(level);

		this.settlement = settlement;
	}

	@Override
	public int getArchiveValue() {
		return (int)(getLevel().ordinal() / 2 * getSettlement().getValueScale());
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
			return "(Unknown " + getSettlement().getGenericName() + ")";
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
		if(ObservationLevel.VISITED.isAvailableFrom(getLevel())) {
			// Draw settlement
			layout.add(new VisualDisplay(getSettlement().getVisual(), VISUAL_SIZE))
					.customize().color(v.lerpColor(0, getSettlement().getColor(), .5F));
		}

		layout.add(new TextDisplay("Location: " + getSettlement().getLocation().getFullName()))
				.customize().color(getSpaceObject().getColor());

		if(ObservationLevel.SCANNED.isAvailableFrom(getLevel())) {
			Layout scanned = layout.add(new VerticalLayout());
			scanned.customize().spacing(layout.getStyle().spacing() / 2);

			scanned.add(new TextDisplay("Type: " + getSettlement().getGenericName()));

			scanned.add(new TextDisplay(getSettlement().isInhabited()
					? "Population: " + quantityString(getSettlement().getPopulation())
					: "Uninhabited"));
		}
	}
}
