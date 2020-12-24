package vekta.terrain.settlement.building.upgrade;

import vekta.player.Player;
import vekta.economy.TemporaryModifier;
import vekta.terrain.settlement.building.ForumBuilding;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.v;

public class ForumUpgrade implements SettlementUpgrade {
	@Override
	public String getName() {
		return "Build Economic Forum";
	}

	@Override
	public boolean isAvailable(Player player, Settlement settlement) {
		return settlement.find(ForumBuilding.class) == null;
	}

	@Override
	public int getCost(Player player, Settlement settlement) {
		return 250;
	}

	@Override
	public void upgrade(Player player, Settlement settlement) {
		settlement.add(new ForumBuilding(settlement, (int)v.random(3, 6)));
		
		settlement.getEconomy().addModifier(new TemporaryModifier("Economic Infrastructure", 1, .1F));
	}
}
