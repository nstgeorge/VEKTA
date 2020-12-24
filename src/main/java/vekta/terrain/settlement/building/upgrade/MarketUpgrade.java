package vekta.terrain.settlement.building.upgrade;

import vekta.economy.TemporaryModifier;
import vekta.player.Player;
import vekta.spawner.SettlementGenerator;
import vekta.terrain.settlement.Settlement;
import vekta.terrain.settlement.building.MarketBuilding;

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
		settlement.add(SettlementGenerator.createMarket(1));

		settlement.getEconomy().addModifier(new TemporaryModifier("Efficient Markets", .5F, .05F));
	}
}
