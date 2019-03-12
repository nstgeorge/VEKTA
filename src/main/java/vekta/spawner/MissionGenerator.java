package vekta.spawner;

import processing.core.PVector;
import vekta.Player;
import vekta.RenderLevel;
import vekta.Resources;
import vekta.menu.Menu;
import vekta.menu.handle.MissionMenuHandle;
import vekta.menu.option.BasicOption;
import vekta.menu.option.ItemTradeOption;
import vekta.menu.option.MissionOption;
import vekta.mission.Mission;
import vekta.mission.MissionIssuer;
import vekta.mission.objective.Objective;
import vekta.object.planet.TerrestrialPlanet;
import vekta.person.Dialog;
import vekta.person.OpinionType;
import vekta.person.Person;
import vekta.spawner.item.MissionItemSpawner;
import vekta.spawner.world.AsteroidSpawner;
import vekta.terrain.LandingSite;

import java.util.Arrays;

import static vekta.Vekta.*;

public class MissionGenerator {
	private static final ObjectiveSpawner[] OBJECTIVE_SPAWNERS = Resources.getSubclassInstances(ObjectiveSpawner.class);
	private static final RewardSpawner[] REWARD_SPAWNERS = Resources.getSubclassInstances(RewardSpawner.class);

	public static Mission createMission(Player player, MissionIssuer issuer) {
		return createMission(player, issuer, (int)v.random(3) + 1);
	}

	public static Mission createMission(Player player, MissionIssuer issuer, int tier) {
		Mission mission = new Mission(player, randomMissionName(), issuer, tier);
		mission.add(issuer);
		addRewards(mission);
		addObjectives(mission);
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
		PersonGenerator.updateHome(person);
		return person;
	}

	public static String randomMissionName() {
		return Resources.generateString("mission");
	}

	public static Dialog randomVisitDialog(Player player, Person person) {
		Dialog dialog = null;
		if(person.isBusy()) {
			dialog = person.createDialog("busy");
		}
		else {
			float r = v.random(1);
			if(r > .4) {
				dialog = randomApproachDialog(player, person);
			}
		}
		Dialog greeting = person.createDialog("greeting");
		if(dialog != null) {
			greeting.then(dialog);
		}
		return greeting;
	}

	public static Dialog randomApproachDialog(Player player, Person person) {
		Dialog dialog;
		if(person.getOpinion(player.getFaction()) == OpinionType.GRATEFUL && v.chance(.4F)) {
			dialog = person.createDialog("offer");
			dialog.add(new ItemTradeOption(player.getInventory(), ItemGenerator.randomItem(), 0));
			person.setOpinion(player.getFaction(), OpinionType.FRIENDLY);
		}
		else if(v.chance(.3F)) {
			dialog = person.createDialog("offer");
			dialog.add(new ItemTradeOption(player.getInventory(), MissionItemSpawner.randomMissionItem(p -> MissionGenerator.createMission(p, person)), 0));
		}
		else {
			dialog = person.createDialog("request");
			dialog.add(new BasicOption("Learn More", menu -> {
				Menu sub = new Menu(menu.getPlayer(), new MissionMenuHandle(menu.getDefault()));
				sub.add(new MissionOption(createMission(player, person)));
				sub.addDefault();
				setContext(sub);
			}));
		}
		//		Dialog greeting = person.createDialog("greeting");
		//		greeting.add(dialog);
		//		return greeting;
		return dialog;
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
