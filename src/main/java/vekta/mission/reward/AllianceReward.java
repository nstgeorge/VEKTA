package vekta.mission.reward;

import vekta.faction.Faction;
import vekta.mission.Mission;

public class AllianceReward extends DiplomacyReward {
	public AllianceReward(Faction faction) {
		super(faction);
	}

	public AllianceReward(Faction a, Faction b) {
		super(a, b);
	}

	@Override
	public String getTypeName() {
		return "Alliance";
	}

	@Override
	public void onReward(Mission mission, Faction a, Faction b) {
		a.setAlly(b);
	}
}
