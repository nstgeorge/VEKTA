package vekta.terrain;

import vekta.object.planet.TerrestrialPlanet;
import vekta.terrain.condition.PlanetQuantity;
import vekta.terrain.settlement.Settlement;

import java.util.List;
import java.util.Set;

public class AdaptiveTerrain extends Terrain {

	public AdaptiveTerrain(TerrestrialPlanet planet) {
		super(planet);
	}

	public AdaptiveTerrain(TerrestrialPlanet planet, Settlement settlement) {
		this(planet);

		addSettlement(settlement);
	}

	@Override
	public void onSurveyTags(Set<String> tags) {
//		if(getEcosystem().getCapacity() > 0) {
//			tags.add("Ecosystem");
//		}
	}

	@Override
	public String getOverview() {
		if(findVisitableSettlements().isEmpty()) {
			return "You land in a landscape which appears ripe for colonization.";
		}

		// TODO: determine from features/locations
		List<Settlement> settlements = findVisitableSettlements();
		if(!settlements.isEmpty()){
			return settlements.get(0).getOverview();
		}

		return null;///////////
	}

	@Override
	public boolean isHabitable() {
		return PlanetQuantity.HABITABLE.getScore(getPlanet()) >= 1;
	}

	@Override
	public boolean isInhabited() {
		for(Settlement settlement : findVisitableSettlements()) {
			if(settlement.isInhabited()) {
				return true;
			}
		}
		return false;
	}
}
