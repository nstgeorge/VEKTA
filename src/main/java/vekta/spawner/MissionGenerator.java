package vekta.spawner;

import processing.core.PVector;
import vekta.Player;
import vekta.RenderLevel;
import vekta.Resources;
import vekta.mission.Mission;
import vekta.mission.MissionIssuer;
import vekta.mission.objective.Objective;
import vekta.object.planet.TerrestrialPlanet;
import vekta.person.Person;
import vekta.spawner.world.AsteroidSpawner;
import vekta.terrain.LandingSite;

import java.util.Arrays;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class MissionGenerator {
	private static final ObjectiveSpawner[] OBJECTIVE_SPAWNERS = Resources.getSubclassInstances(ObjectiveSpawner.class);
	private static final RewardSpawner[] REWARD_SPAWNERS = Resources.getSubclassInstances(RewardSpawner.class);

	public static Mission createMission(Player player, MissionIssuer issuer) {
		return createMission(player, issuer, issuer.chooseMissionTier(player));
	}

	public static Mission createMission(Player player, MissionIssuer issuer, int tier) {
		Mission mission = new Mission(player, randomMissionName(), issuer, tier);
		mission.add(issuer);
		addObjectives(mission);
		addRewards(mission);
		return mission;
	}

	public static void addRewards(Mission mission) {
		RewardSpawner spawner = Weighted.random(Arrays.stream(REWARD_SPAWNERS)
				.filter(s -> s.isValid(mission))
				.toArray(RewardSpawner[]::new));
		spawner.setup(mission);
	}

	public static void addObjectives(Mission mission) {
		addObjectives(mission, (int)v.random(mission.getTier() + 1) + 1);
	}

	public static void addObjectives(Mission mission, int steps) {
		ObjectiveSpawner spawner = Weighted.random(Arrays.stream(OBJECTIVE_SPAWNERS)
				.filter(s -> s.isValid(mission))
				.toArray(ObjectiveSpawner[]::new));
		Objective main = spawner.getMainObjective(mission);
		if(steps > 1) {
			main.then(m -> addObjectives(m, steps - 1));
		}
		mission.add(main);
	}

	public static LandingSite randomLandingSite() {
		TerrestrialPlanet planet = getWorld().findRandomObject(TerrestrialPlanet.class);
		if(planet == null) {
			planet = AsteroidSpawner.createAsteroid(WorldGenerator.randomSpawnPosition(RenderLevel.PLANET, new PVector()));
		}
		return planet.getLandingSite();
	}

	public static Person randomMissionPerson() {
		return randomMissionPerson(null);
	}

	public static Person randomMissionPerson(MissionIssuer exclude) {
		Person person = getWorld().findRandomObject(Person.class);
		if(person == null || person == exclude || v.chance(.1F)) {
			person = PersonGenerator.createPerson();
		}
		return person;
	}

	public static String randomMissionName() {
		return Resources.generateString("mission");
	}

	public interface ObjectiveSpawner extends Weighted {
		boolean isValid(Mission mission);

		Objective getMainObjective(Mission mission);
	}

	public interface RewardSpawner extends Weighted {
		boolean isValid(Mission mission);

		void setup(Mission mission);
	}
}
