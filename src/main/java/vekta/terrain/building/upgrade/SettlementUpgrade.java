package vekta.terrain.building.upgrade;

import vekta.Player;
import vekta.terrain.settlement.Settlement;

import java.io.Serializable;

public interface SettlementUpgrade extends Serializable {
	String getName();

	int getCost(Player player, Settlement settlement);

	boolean isAvailable(Player player, Settlement settlement);

	void upgrade(Player player, Settlement settlement);
}
