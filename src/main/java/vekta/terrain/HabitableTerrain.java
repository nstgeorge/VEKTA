package vekta.terrain;

import vekta.terrain.settlement.Settlement;

public class HabitableTerrain extends Terrain {

	public HabitableTerrain(Settlement settlement) {
		this();

		addSettlement(settlement);
	}

	public HabitableTerrain() {
		addFeature("Atmosphere");
		addFeature("Terrestrial");
		addFeature("Habitable");
	}

	@Override
	public String getOverview() {
		if(getSettlements().isEmpty()) {
			return "You land among a flourishing ecosystem of exotic life forms.";
		}
		return getSettlements().get(0).getOverview();
	}

	@Override
	public boolean isInhabited() {
		for(Settlement settlement : getSettlements()) {
			if(settlement.isInhabited()) {
				return true;
			}
		}
		return false;
	}
}
