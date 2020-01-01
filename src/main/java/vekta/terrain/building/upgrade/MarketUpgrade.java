package vekta.terrain.building.upgrade;

import vekta.player.Player;
import vekta.economy.TemporaryModifier;
import vekta.spawner.WorldGenerator;
import vekta.terrain.building.MarketBuilding;
import vekta.terrain.settlement.Settlement;

public class MarketUpgrade implements SettlementUpgrade {
	@Override
	public String getName() {
		return "Add Marketplace";
	}

	@Override
	public boolean isAvailable(Player player, Settlement settlement) {
		return settlement.find(MarketBuilding.class) == null;
	}

	@Override
	public int getCost(Player player, Settlement settlement) {
		return 200;
	}

	@Override
	public void upgrade(Player player, Settlement settlement) {
		settlement.add(WorldGenerator.createMarket(1));

		settlement.getEconomy().addModifier(new TemporaryModifier("Efficient Markets", .5F, .05F));
	}
}
