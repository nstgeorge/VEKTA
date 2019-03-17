package vekta.spawner.reward;

import vekta.Faction;
import vekta.economy.ProductivityModifier;
import vekta.economy.TemporaryModifier;
import vekta.mission.Mission;
import vekta.mission.reward.EconomyReward;
import vekta.spawner.FactionGenerator;
import vekta.spawner.MissionGenerator;

import static processing.core.PApplet.sq;
import static vekta.Vekta.v;

public class EconomyRewardSpawner implements MissionGenerator.RewardSpawner {
	private static final float MODIFIER_SCALE = .005F;
	private static final float MODIFIER_DURATION = 10;

	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Mission mission) {
		return mission.getTier() >= 3;
	}

	@Override
	public void setup(Mission mission) {
		Faction faction = v.chance(.5F)
				? mission.getPlayer().getFaction()
				: FactionGenerator.randomFaction();

		float amount = faction.getEconomy().getValue() * sq(mission.getTier()) * MODIFIER_SCALE;

		ProductivityModifier modifier;

		if(!faction.isEnemy(mission.getPlayer().getFaction())) {
			modifier = new TemporaryModifier("Market Rally", amount, amount / MODIFIER_DURATION);
		}
		else {
			modifier = new TemporaryModifier("Market Panic", -amount, amount / MODIFIER_DURATION);
		}

		mission.add(new EconomyReward(faction, modifier));
	}
}
