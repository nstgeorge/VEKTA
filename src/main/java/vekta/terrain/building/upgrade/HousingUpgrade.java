package vekta.terrain.building.upgrade;

import vekta.Player;
import vekta.spawner.PersonGenerator;
import vekta.terrain.building.HouseBuilding;
import vekta.terrain.building.MarketBuilding;
import vekta.terrain.settlement.Settlement;

public class HousingUpgrade implements SettlementUpgrade {
	@Override
	public String getName() {
		return "Expand Housing";
	}

	@Override
	public int getCost(Player player, Settlement settlement) {
		return 50 * (1 + settlement.count(HouseBuilding.class));
	}

	@Override
	public boolean isAvailable(Player player, Settlement settlement) {
		return settlement.find(MarketBuilding.class) == null;
	}

	@Override
	public void upgrade(Player player, Settlement settlement) {
		PersonGenerator.createPerson(settlement);
	}
}
