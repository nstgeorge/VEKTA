package vekta.spawner.objective;

import vekta.faction.Faction;
import vekta.mission.Mission;
import vekta.mission.objective.DestroyObjective;
import vekta.mission.objective.Objective;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.MissionGenerator;
import vekta.spawner.WorldGenerator;
import vekta.spawner.world.AsteroidSpawner;
import vekta.terrain.settlement.Settlement;
import vekta.world.RenderLevel;

import java.util.List;
import java.util.stream.Collectors;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class DestroyPlanetObjectiveSpawner implements MissionGenerator.ObjectiveSpawner {
	@Override
	public float getWeight() {
		return 1;
	}

	@Override
	public boolean isValid(Mission mission) {
		return mission.getTier() >= 4;
	}

	@Override
	public Objective getMainObjective(Mission mission) {
		List<SpaceObject> candidates = getWorld().findObjects(TerrestrialPlanet.class).stream()
				.filter(p -> {
					for(Settlement settlement : p.getTerrain().findVisitableSettlements()) {
						Faction faction = settlement.getFaction();
						if(faction.isAlly(mission.getPlayer().getFaction()) || faction.isAlly(mission.getIssuer().getFaction())) {
							return false;
						}
					}
					return true;
				})
				.collect(Collectors.toList());

		SpaceObject obj = !candidates.isEmpty()
				? v.random(candidates)
				: AsteroidSpawner.createAsteroid(WorldGenerator.randomSpawnPosition(RenderLevel.PLANET, mission.getPlayer().getShip().getPosition()));

		return new DestroyObjective(obj);
	}
}
