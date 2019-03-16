package vekta.terrain.building.upgrade;

import vekta.Player;
import vekta.terrain.building.RefineryBuilding;
import vekta.terrain.settlement.Settlement;

public class RefineryUpgrade implements SettlementUpgrade {
	@Override
	public String getName() {
		return "Add Refinery";
	}

	@Override
	public int getCost(Player player, Settlement settlement) {
		return 150;
	}

	@Override
	public boolean isAvailable(Player player, Settlement settlement) {
		return settlement.find(RefineryBuilding.class) == null;
	}

	@Override
	public void upgrade(Player player, Settlement settlement) {
		settlement.add(new RefineryBuilding());
	}
}
