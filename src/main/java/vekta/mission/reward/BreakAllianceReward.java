package vekta.mission.reward;

import vekta.Faction;
import vekta.mission.Mission;

public class BreakAllianceReward extends DiplomacyReward {
	public BreakAllianceReward(Faction faction) {
		super(faction);
	}

	public BreakAllianceReward(Faction a, Faction b) {
		super(a, b);
	}

	@Override
	public String getTypeName() {
		return "Break Alliance";
	}

	@Override
	public void onReward(Mission mission, Faction a, Faction b) {
		a.setNeutral(b);
	}
}
