package vekta.spawner;

import processing.core.PVector;
import vekta.Player;
import vekta.RenderLevel;
import vekta.Resources;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.MissionMenuHandle;
import vekta.menu.option.BasicOption;
import vekta.menu.option.ItemTradeOption;
import vekta.menu.option.MissionOption;
import vekta.mission.Mission;
import vekta.mission.objective.*;
import vekta.mission.reward.AllianceReward;
import vekta.mission.reward.ItemReward;
import vekta.mission.reward.MoneyReward;
import vekta.mission.reward.SettlementReward;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.MessengerShip;
import vekta.person.Dialog;
import vekta.person.OpinionType;
import vekta.person.Person;
import vekta.spawner.item.MissionItemSpawner;
import vekta.spawner.world.AsteroidSpawner;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.*;

public class MissionGenerator {
	public static MessengerShip createMessenger(Player player, Dialog dialog) {
		PVector pos = WorldGenerator.randomSpawnPosition(RenderLevel.SHIP, player.getShip().getPosition());
		MessengerShip ship = new MessengerShip(
				player,
				dialog,
				dialog != null ? dialog.getPerson().getFullName() : "HERMES II",
				PVector.random2D(),
				pos,
				player.getShip().getVelocity(),
				dialog != null ? dialog.getPerson().getColor() : WorldGenerator.randomPlanetColor());
		addObject(ship);
		player.send(ship.getName() + " approaches with a message!")
				.withColor(ship.getColor())
				.withTime(2);
		return ship;
	}

	public static Mission createMission(Person person) {
		return createMission(person, (int)v.random(3) + 1);
	}

	public static Mission createMission(Person person, int lootTier) {
		Mission mission = new Mission(randomMissionName());
		mission.add(person);
		addRewards(person, mission, lootTier);
		addObjectives(person, mission, (int)v.random(lootTier + 1) + 1);
		return mission;
	}

	public static void addRewards(Person person, Mission mission, int lootTier) {
		float r = v.random(1);
		if(r > .4) {
			Inventory inv = new Inventory();
			inv.add((int)(10 * lootTier * v.random(1, lootTier) + 1));
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
		else if(r > .2) {
//			if(person.getFaction().isNeutral(player)) {}
//			else {}
			mission.add(new AllianceReward(person.getFaction()));
		}
		else {
			Settlement settlement = PersonGenerator.randomHome(person);
			if(person.getFaction().isAlly(settlement.getFaction())) {
				mission.add(new SettlementReward(settlement));
			}
		}
	}

	public static void addObjectives(Person person, Mission mission) {
		addObjectives(person, mission, 1);
	}

	public static void addObjectives(Person person, Mission mission, int steps) {
		float r = v.random(1);
		Objective objective;
		if(r > .7) {
			LandingSite site = randomLandingSite();
			mission.add(new LandAtObjective(site.getParent()));
			String task = Resources.generateString(site.getTerrain().isInhabited() ? "settlement_task" : "planet_task");
			objective = new TaskObjective(task, site.getParent());
		}
		else if(r > .4) {
			Person target = randomMissionPerson(person);
			objective = new DeliverItemObjective(ItemGenerator.randomItem(), target);
		}
		else if(r > .2) {
			Item item = ItemGenerator.randomItem();
			mission.add(new SearchForItemObjective(item, v.random(.1F, .5F)));
			objective = new DeliverItemObjective(item, v.chance(.8F) ? person : randomMissionPerson(person));
		}
		else {
			Person other = randomMissionPerson(person);
			mission.add(new LandAtObjective(other.findHomeObject()));
			objective = new DialogObjective("Confront", randomConfrontDialog(other, person, mission));
		}

		if(steps > 1) {
			objective.then(() -> addObjectives(person, mission, steps - 1));
		}
		mission.add(objective);
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

	public static Person randomMissionPerson(Person exclude) {
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
		Dialog dialog;
		float r = v.random(1);
		if(r > .4) {
			dialog = randomApproachDialog(player, person);
		}
		else {
			dialog = null;
		}
		Dialog greeting = person.createDialog("greeting");
		if(dialog != null) {
			greeting.addContinuation(dialog);
		}
		return greeting;
	}

	public static Dialog randomApproachDialog(Player player, Person person) {
		Dialog dialog;
		if(person.getOpinion(player) == OpinionType.GRATEFUL && v.chance(.4F)) {
			dialog = person.createDialog("offer");
			dialog.add(new ItemTradeOption(player.getInventory(), ItemGenerator.randomItem(), 0));
			person.setOpinion(player, OpinionType.FRIENDLY);
		}
		else if(v.chance(.3F)) {
			dialog = person.createDialog("offer");
			Mission mission = MissionGenerator.createMission(person);
			dialog.add(new ItemTradeOption(player.getInventory(), MissionItemSpawner.randomMissionItem(mission), 0));
		}
		else {
			dialog = person.createDialog("request");
			dialog.add(new BasicOption("Learn More", menu -> {
				Menu sub = new Menu(menu.getPlayer(), new MissionMenuHandle(menu.getDefault()));
				sub.add(new MissionOption(createMission(person)));
				sub.addDefault();
				setContext(sub);
			}));
		}
		//		Dialog greeting = person.createDialog("greeting");
		//		greeting.add(dialog);
		//		return greeting;
		return dialog;
	}

	public static Dialog randomConfrontDialog(Person person, Person sender, Mission mission) {
		Dialog dialog = person.createDialog("confronted");

		if(v.chance(.5F)) {
			dialog.addContinuation(person.createDialog("confession"));
		}

		if(v.chance(.75F)) {
			Dialog greeting = person.createDialog("greeting");
			greeting.add(sender.getShortName() + " sent me to talk to you.", dialog);
			dialog = greeting;
		}
		return dialog;
	}

	public interface MissionSpawner extends Weighted {
		void setup(Person person, Mission mission);
	}
}  
