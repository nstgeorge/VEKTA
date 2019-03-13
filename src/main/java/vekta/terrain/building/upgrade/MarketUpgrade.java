package vekta.terrain.building.upgrade;

import vekta.Player;
import vekta.spawner.WorldGenerator;
import vekta.terrain.building.MarketBuilding;
import vekta.terrain.settlement.Settlement;

public class MarketUpgrade implements SettlementUpgrade {
	@Override
	public String getName() {
		return "Add Marketplace";
	}

	@Override
	public int getCost(Player player, Settlement settlement) {
		return 50;
	}

	@Override
	public boolean isAvailable(Player player, Settlement settlement) {
		return settlement.find(MarketBuilding.class) == null;
	}

	@Override
	public void upgrade(Player player, Settlement settlement) {
		settlement.add(WorldGenerator.createMarket(1));
	}
}
