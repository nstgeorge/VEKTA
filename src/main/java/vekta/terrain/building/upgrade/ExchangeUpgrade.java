package vekta.terrain.building.upgrade;

import vekta.Player;
import vekta.economy.TemporaryModifier;
import vekta.terrain.building.ExchangeBuilding;
import vekta.terrain.settlement.Settlement;

public class ExchangeUpgrade implements SettlementUpgrade {
	@Override
	public String getName() {
		return "Build Financial Exchange";
	}

	@Override
	public boolean isAvailable(Player player, Settlement settlement) {
		return settlement.find(ExchangeBuilding.class) == null;
	}

	@Override
	public int getCost(Player player, Settlement settlement) {
		return 200;
	}

	@Override
	public void upgrade(Player player, Settlement settlement) {
		settlement.add(new ExchangeBuilding());

		settlement.getEconomy().addModifier(new TemporaryModifier("Financial Influx", 1, .2F));
	}
}
