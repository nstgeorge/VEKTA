package vekta.spawner.objective;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.mission.Mission;
import vekta.mission.objective.DefeatObjective;
import vekta.mission.objective.Objective;
import vekta.object.SpaceObject;
import vekta.object.ship.BossShip;
import vekta.object.ship.Ship;
import vekta.spawner.ItemGenerator;
import vekta.spawner.MissionGenerator;
import vekta.spawner.PersonGenerator;
import vekta.spawner.WorldGenerator;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.register;

public class BossBattleObjectiveSpawner implements MissionGenerator.ObjectiveSpawner {
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
		PVector pos = WorldGenerator.randomSpawnPosition(RenderLevel.PLANET, mission.getPlayer().getShip().getPosition());
		Ship ship = createBossShip(mission.getTier(), pos);

		return new DefeatObjective(ship);
	}

	public static Ship createBossShip(int tier, PVector pos) {
		return createBossShip(PersonGenerator.randomBossName(), tier, pos);
	}

	public static Ship createBossShip(String name, int tier, PVector pos) {
		Ship ship = register(new BossShip(
				name,
				tier,
				PVector.random2D(),
				pos,
				new PVector()));

		ItemGenerator.addLoot(ship.getInventory(), tier);

		SpaceObject orbit = getWorld().findOrbitObject(ship);
		if(orbit != null) {
			WorldGenerator.orbit(orbit, ship, .1F);
		}
		return ship;
	}
}
