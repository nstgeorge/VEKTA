package vekta.mission.reward;

import vekta.Faction;
import vekta.mission.Mission;

public class WarReward extends DiplomacyReward {
	public WarReward(Faction faction) {
		super(faction);
	}

	public WarReward(Faction a, Faction b) {
		super(a, b);
	}

	@Override
	public String getTypeName() {
		return "War";
	}

	@Override
	public void onReward(Mission mission, Faction a, Faction b) {
		a.setEnemy(b);
	}
}
