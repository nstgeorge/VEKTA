package vekta.mission.reward;

import vekta.Faction;
import vekta.mission.Mission;

public class PeaceReward extends DiplomacyReward {
	public PeaceReward(Faction faction) {
		super(faction);
	}

	public PeaceReward(Faction a, Faction b) {
		super(a, b);
	}

	@Override
	public String getTypeName() {
		return "Peace";
	}

	@Override
	public void onReward(Mission mission, Faction a, Faction b) {
		a.setNeutral(b);
	}
}
