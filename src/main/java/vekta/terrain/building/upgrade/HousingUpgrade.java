package vekta.terrain.building.upgrade;

import vekta.player.Player;
import vekta.economy.TemporaryModifier;
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
	public boolean isAvailable(Player player, Settlement settlement) {
		return settlement.find(MarketBuilding.class) == null;
	}

	@Override
	public int getCost(Player player, Settlement settlement) {
		return 100 * (1 + settlement.count(HouseBuilding.class));
	}

	@Override
	public void upgrade(Player player, Settlement settlement) {
		PersonGenerator.createPerson(settlement);

		settlement.getEconomy().addModifier(new TemporaryModifier("Increasing Population", .25F, .01F));
	}
}
