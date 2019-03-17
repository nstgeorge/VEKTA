package vekta.spawner.reward;

import vekta.Faction;
import vekta.mission.Mission;
import vekta.mission.reward.PeaceReward;
import vekta.mission.reward.WarReward;
import vekta.spawner.MissionGenerator;

public class WarRewardSpawner implements MissionGenerator.RewardSpawner {
	@Override
	public float getWeight() {
		return 2;
	}

	@Override
	public boolean isValid(Mission mission) {
		return mission.getTier() >= 3 && mission.getRewards().stream()
				.noneMatch(r -> r instanceof WarReward || r instanceof PeaceReward);
	}

	@Override
	public void setup(Mission mission) {
		Faction faction = mission.getIssuer().getFaction();
		if(faction.isEnemy(mission.getPlayer().getFaction())) {
			mission.add(new PeaceReward(faction));
		}
		else {
			mission.add(new WarReward(faction));
		}
		MissionGenerator.addRewards(mission);
	}
}
