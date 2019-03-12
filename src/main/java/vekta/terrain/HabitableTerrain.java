package vekta.terrain;

import vekta.menu.Menu;
import vekta.menu.option.SettlementOption;
import vekta.terrain.settlement.Settlement;

import java.util.Collections;
import java.util.List;

public class HabitableTerrain extends Terrain {
	private Settlement settlement;
	
	public HabitableTerrain(Settlement settlement) {
		this.settlement = settlement;

		addFeature("Atmosphere");
		addFeature("Habitable");
	}

	public Settlement getSettlement() {
		return settlement;
	}

	@Override
	public List<Settlement> getSettlements() {
		return Collections.singletonList(getSettlement());
	}

	public void changeSettlement(Settlement settlement) {
		if(settlement == null) {
			throw new RuntimeException("Settlement cannot be null");
		}
		this.settlement = settlement;
	}

	@Override
	public String getOverview() {
		return getSettlement().getOverview();
	}

	@Override
	public boolean isInhabited() {
		return getSettlement().isInhabited();
	}

	@Override
	public void setup(LandingSite site) {
		getSettlement().setup(site);
	}

	@Override
	public void setupLandingMenu(Menu menu) {
		menu.add(new SettlementOption(getSettlement()));
	}
}
