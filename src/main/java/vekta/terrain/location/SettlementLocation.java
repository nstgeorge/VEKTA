package vekta.terrain.location;

import vekta.menu.Menu;
import vekta.menu.option.SettlementButton;
import vekta.object.planet.TerrestrialPlanet;
import vekta.terrain.settlement.Settlement;

public class SettlementLocation extends Location {

	private Settlement settlement;

	public SettlementLocation(TerrestrialPlanet planet) {
		super(planet);
	}

	public void notifySettlement(Settlement settlement) {
		if(this.settlement != null) {
			throw new RuntimeException(getClass().getSimpleName() + " already has a settlement: " + this.settlement);
		}
		this.settlement = settlement;

		getPlanet().notifySettlement(settlement);
	}

	public Settlement getSettlement() {
		return settlement;
	}

	@Override
	public String getName() {
		return getSettlement().getName();
	}

	@Override
	public String getOverview() {
		return getSettlement().getOverview();
	}

	@Override
	public int getColor() {
		return getSettlement().getColor(); //// State duplication
	}

	@Override
	protected void onSetupMenu(Menu menu) {
		menu.add(new SettlementButton(getSettlement()));
	}
}
