package vekta.terrain;

import vekta.Player;
import vekta.menu.Menu;
import vekta.terrain.settlement.Settlement;

public class HabitableTerrain extends Terrain {
	private Settlement settlement;
	private String overview;

	public HabitableTerrain() {
		addFeature("Atmosphere");
		addFeature("Habitable");
	}

	public Settlement getSettlement() {
		return settlement;
	}

	public void setSettlement(Settlement settlement) {
		if(settlement == null) {
			throw new RuntimeException("Settlement cannot be null");
		}
		this.settlement = settlement;
		this.overview = settlement.createOverview();
		settlement.setupTerrain(this);
	}

	@Override
	public String getOverview() {
		return overview;
	}

	@Override
	public void setupLandingMenu(Player player, Menu menu) {
		getSettlement().setupLandingMenu(player, menu);
	}
}
