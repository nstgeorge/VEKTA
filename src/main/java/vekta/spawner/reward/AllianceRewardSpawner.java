package vekta.spawner.reward;

import vekta.Faction;
import vekta.mission.Mission;
import vekta.mission.reward.AllianceReward;
import vekta.mission.reward.BreakAllianceReward;
import vekta.mission.reward.DiplomacyReward;
import vekta.mission.reward.PeaceReward;
import vekta.spawner.MissionGenerator;

public class AllianceRewardSpawner implements MissionGenerator.RewardSpawner {
	@Override
	public float getWeight() {
		return 2;
	}

	@Override
	public boolean isValid(Mission mission) {
		return mission.getTier() >= 3 && mission.getRewards().stream()
				.noneMatch(r -> r instanceof DiplomacyReward);
	}

	@Override
	public void setup(Mission mission) {
		Faction faction = mission.getIssuer().getFaction();
		if(faction.isAlly(mission.getPlayer().getFaction())) {
			mission.add(new BreakAllianceReward(faction));
			MissionGenerator.addRewards(mission);
		}
		else if(faction.isEnemy(mission.getPlayer().getFaction())) {
			mission.add(new PeaceReward(faction));
		}
		else {
			mission.add(new AllianceReward(faction));
		}
	}
}
