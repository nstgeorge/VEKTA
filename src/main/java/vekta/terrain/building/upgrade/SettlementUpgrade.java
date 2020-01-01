package vekta.terrain.building.upgrade;

import vekta.player.Player;
import vekta.terrain.settlement.Settlement;

import java.io.Serializable;

public interface SettlementUpgrade extends Serializable {
	String getName();

	boolean isAvailable(Player player, Settlement settlement);

	int getCost(Player player, Settlement settlement);

	void upgrade(Player player, Settlement settlement);
}
