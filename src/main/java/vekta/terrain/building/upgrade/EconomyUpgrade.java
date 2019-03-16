package vekta.terrain.building.upgrade;

import vekta.Player;
import vekta.economy.TemporaryModifier;
import vekta.terrain.building.EconomyBuilding;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.v;

public class EconomyUpgrade implements SettlementUpgrade {
	@Override
	public String getName() {
		return "Build Economic Forum";
	}

	@Override
	public boolean isAvailable(Player player, Settlement settlement) {
		return settlement.find(EconomyBuilding.class) == null;
	}

	@Override
	public int getCost(Player player, Settlement settlement) {
		return 250;
	}

	@Override
	public void upgrade(Player player, Settlement settlement) {
		settlement.add(new EconomyBuilding(settlement, (int)v.random(3, 6)));
		
		settlement.getEconomy().addModifier(new TemporaryModifier("Economic Infrastructure", 1, .1F));
	}
}
