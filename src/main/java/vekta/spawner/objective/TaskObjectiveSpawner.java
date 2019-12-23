package vekta.spawner.objective;

import vekta.Faction;
import vekta.Resources;
import vekta.mission.Mission;
import vekta.mission.objective.LandAtObjective;
import vekta.mission.objective.Objective;
import vekta.mission.objective.LandingTaskObjective;
import vekta.mission.reward.WarReward;
import vekta.spawner.MissionGenerator;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.v;
import static vekta.spawner.MissionGenerator.randomLandingSite;

public class TaskObjectiveSpawner implements MissionGenerator.ObjectiveSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Mission mission) {
		return true;
	}

	@Override
	public Objective getMainObjective(Mission mission) {
		LandingSite site = randomLandingSite();
		mission.add(new LandAtObjective(site.getParent()));
		String type = site.getTerrain().isInhabited() ? "settlement" : "planet";
		for(Settlement settlement : site.getTerrain().getSettlements()) {
			Faction faction = settlement.getFaction();
			boolean playerEnemy = faction.isEnemy(mission.getPlayer().getFaction());
			boolean issuerEnemy = faction.isEnemy(mission.getIssuer().getFaction());
			if(playerEnemy || issuerEnemy || v.chance(.1F)) {
				type = "sabotage";
				if(!issuerEnemy) {
					mission.add(new WarReward(faction, mission.getIssuer().getFaction()));
				}
				else if(!playerEnemy) {
					// Only declare war on player if already at war with issuer faction
					mission.add(new WarReward(faction));
				}
				break;
			}
		}
		String task = Resources.generateString("task_" + type);
		return new LandingTaskObjective(task, site.getParent());
	}
}
