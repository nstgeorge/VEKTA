package vekta.terrain.building.upgrade;

import vekta.Player;
import vekta.terrain.settlement.Settlement;

public interface SettlementUpgrade {
	String getName();

	int getCost(Player player, Settlement settlement);

	boolean isAvailable(Player player, Settlement settlement);

	void upgrade(Player player, Settlement settlement);
}
