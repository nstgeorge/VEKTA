package vekta.terrain;

import vekta.spawner.EcosystemGenerator;
import vekta.terrain.settlement.Settlement;

import static processing.core.PApplet.round;
import static vekta.Vekta.v;

public class HabitableTerrain extends Terrain {

	public HabitableTerrain() {
		addFeature("Atmosphere");
		addFeature("Terrestrial");
		addFeature("Habitable");

		if(v.chance(.5F)) {
			addFeature("Natural Ecosystem");
			EcosystemGenerator.populateEcosystem(getEcosystem(), round(v.random(2, 5)));
		}
	}

	public HabitableTerrain(Settlement settlement) {
		this();

		addSettlement(settlement);
	}

	@Override
	public String getOverview() {
		if(getSettlements().isEmpty()) {
			return "You land in a landscape appearing ripe for colonization.";
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
