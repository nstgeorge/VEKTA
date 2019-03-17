package vekta.spawner.objective;

import vekta.mission.Mission;
import vekta.mission.objective.DestroyAroundObjective;
import vekta.mission.objective.Objective;
import vekta.object.ship.PirateShip;
import vekta.spawner.MissionGenerator;
import vekta.spawner.PersonGenerator;
import vekta.terrain.LandingSite;

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
		LandingSite site = PersonGenerator.randomHome(mission.getIssuer().getFaction()).getSite();
		return new DestroyAroundObjective("Deter Pirates", PirateShip.class, site.getParent());
	}
}
