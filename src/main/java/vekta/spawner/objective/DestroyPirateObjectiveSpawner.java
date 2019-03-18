package vekta.spawner.objective;

import vekta.mission.Mission;
import vekta.mission.objective.DestroyAroundObjective;
import vekta.mission.objective.Objective;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.PirateShip;
import vekta.spawner.MissionGenerator;

import java.util.List;
import java.util.stream.Collectors;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class DestroyPirateObjectiveSpawner implements MissionGenerator.ObjectiveSpawner {
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
		// Find an inhabited planet with a sphere of influence
		List<SpaceObject> candidates = getWorld().findObjects(TerrestrialPlanet.class).stream()
				.filter(p -> p.impartsGravity() && p.getLandingSite().getTerrain().isInhabited())
				.collect(Collectors.toList());
		SpaceObject obj = !candidates.isEmpty() ? v.random(candidates) : null;
		
		return new DestroyAroundObjective("Fend off Pirates", PirateShip.class, obj);
	}
}
