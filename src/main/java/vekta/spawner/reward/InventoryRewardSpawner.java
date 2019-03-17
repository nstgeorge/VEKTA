package vekta.spawner.reward;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.mission.Mission;
import vekta.mission.reward.ItemReward;
import vekta.mission.reward.MoneyReward;
import vekta.spawner.ItemGenerator;
import vekta.spawner.MissionGenerator;

import static vekta.Vekta.v;

public class InventoryRewardSpawner implements MissionGenerator.RewardSpawner {
	@Override
	public float getWeight() {
		return 3;
	}

	@Override
	public boolean isValid(Mission mission) {
		return mission.getTier() <= 3;
	}

	@Override
	public void setup(Mission mission) {
		Inventory inv = new Inventory();
		inv.add((int)(10 * mission.getTier() * v.random(1, mission.getTier()) + 1));
		if(v.chance(.3F)) {
			inv.add(ItemGenerator.randomItem());
		}
		if(inv.getMoney() > 0) {
			mission.add(new MoneyReward(inv.getMoney()));
		}
		for(Item item : inv) {
			mission.add(new ItemReward(item));
		}
	}
}
