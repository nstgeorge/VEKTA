package vekta.spawner.reward;

import vekta.faction.Faction;
import vekta.economy.Economy;
import vekta.economy.ProductivityModifier;
import vekta.economy.TemporaryModifier;
import vekta.mission.Mission;
import vekta.mission.reward.EconomyReward;
import vekta.spawner.FactionGenerator;
import vekta.spawner.MissionGenerator;
import vekta.spawner.PersonGenerator;
import vekta.terrain.settlement.Settlement;

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
		Faction faction;
		Economy economy;

		// Sometimes affect economy of specific settlement
		if(v.chance(.25F)) {
			Settlement settlement = PersonGenerator.randomHome();
			faction = settlement.getFaction();
			economy = settlement.getEconomy();
		}
		else {
			faction = v.chance(.5F)
					? mission.getPlayer().getFaction()
					: FactionGenerator.randomFaction();
			economy = faction.getEconomy();
		}

		float amount = economy.getValue() * sq(mission.getTier()) * MODIFIER_SCALE;

		ProductivityModifier modifier;

		if(!faction.isEnemy(mission.getPlayer().getFaction())) {
			modifier = new TemporaryModifier("Market Rally", amount, amount / MODIFIER_DURATION);
		}
		else {
			modifier = new TemporaryModifier("Market Panic", -amount, amount / MODIFIER_DURATION);
		}

		mission.add(new EconomyReward(economy, modifier));
	}
}
