package vekta.mission.reward;

import vekta.mission.Mission;
import vekta.player.Player;
import vekta.terrain.settlement.Settlement;

public class SettlementReward extends Reward {
	private final Settlement settlement;

	public SettlementReward(Settlement settlement) {
		this.settlement = settlement;
	}

	public Settlement getSettlement() {
		return settlement;
	}

	@Override
	public String getName() {
		return getSettlement().getName() + " (" + getSettlement().getParent().getName() + ")";
	}

	@Override
	public void onReward(Mission mission, Player player) {
		getSettlement().setFaction(player.getFaction());
	}
}
