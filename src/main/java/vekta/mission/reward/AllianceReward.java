package vekta.mission.reward;

import vekta.Faction;
import vekta.Player;
import vekta.mission.Mission;

public class AllianceReward extends Reward {
	private final Faction faction;

	public AllianceReward(Faction faction) {
		this.faction = faction;
	}

	public Faction getFaction() {
		return faction;
	}

	@Override
	public String getName() {
		return "Alliance with " + getFaction().getName();
	}

	@Override
	public int getColor() {
		return getFaction().getColor();
	}

	@Override
	public void onReward(Mission mission, Player player) {
		player.getFaction().setAlly(getFaction());
	}
}
