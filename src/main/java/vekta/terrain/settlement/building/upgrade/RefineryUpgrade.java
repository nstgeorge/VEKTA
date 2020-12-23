package vekta.terrain.settlement.building.upgrade;

import vekta.player.Player;
import vekta.economy.TemporaryModifier;
import vekta.terrain.settlement.building.RefineryBuilding;
import vekta.terrain.settlement.Settlement;

public class RefineryUpgrade implements SettlementUpgrade {
	@Override
	public String getName() {
		return "Add Refinery";
	}

	@Override
	public boolean isAvailable(Player player, Settlement settlement) {
		return settlement.find(RefineryBuilding.class) == null;
	}

	@Override
	public int getCost(Player player, Settlement settlement) {
		return 150;
	}

	@Override
	public void upgrade(Player player, Settlement settlement) {
		settlement.add(new RefineryBuilding());
		
		settlement.getEconomy().addModifier(new TemporaryModifier("Industrial Exports", .2F, .01F));
	}
}
