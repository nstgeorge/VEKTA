package vekta;

import processing.core.PVector;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.Menu;
import vekta.menu.handle.MissionMenuHandle;
import vekta.menu.option.BasicOption;
import vekta.menu.option.ItemTradeOption;
import vekta.menu.option.MissionOption;
import vekta.mission.*;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.MessengerShip;
import vekta.person.Dialog;
import vekta.person.DialogType;
import vekta.person.OpinionType;
import vekta.person.Person;

import static vekta.Vekta.*;

public class MissionGenerator {
	public static MessengerShip createMessenger(Player player, Dialog dialog) {
		PVector pos = WorldGenerator.randomSpawnPosition(RenderLevel.AROUND_SHIP, player.getShip().getPosition());
		MessengerShip ship = new MessengerShip(
				player,
				dialog,
				dialog != null ? dialog.getPerson().getDisplayName() : "HERMES II",
				PVector.random2D(),
				pos,
				player.getShip().getVelocity(),
				dialog != null ? dialog.getPerson().getColor() : WorldGenerator.randomPlanetColor());
		addObject(ship);
		player.send(ship.getName() + " approaches with a message!")
				.withColor(ship.getColor());
		return ship;
	}

	public static Mission createMission(Person person) {
		return createMission(person, (int)v.random(3) + 1);
	}

	public static Mission createMission(Person person, int lootTier) {
		Mission mission = new Mission(randomMissionName());
		Inventory inv = new Inventory();
		inv.add((int)(10 * v.random(lootTier * lootTier) + 1));
		if(v.chance(.3F)) {
			inv.add(ItemGenerator.randomItem());
		}
		if(inv.getMoney() > 0) {
			mission.add(new MoneyReward(inv.getMoney()));
		}
		for(Item item : inv) {
			mission.add(new ItemReward(item));
		}
		mission.add(createObjective(person, mission, (int)v.random(lootTier + 1) + 1));
		return mission;
	}

	public static Objective createObjective(Person person, Mission mission) {
		return createObjective(person, mission, 1);
	}

	public static Objective createObjective(Person person, Mission mission, int steps) {
		float r = v.random(1);
		Objective objective;
		if(r > .5) {
			SpaceObject target;
			float sr = v.random(1);
			if(sr > .4) {
				target = person.getHomeObject();
			}
			else {
				target = getWorld().findRandomObject(TerrestrialPlanet.class);
			}
			objective = new LandAtObjective(target);
		}
		else {
			Person target = randomMissionPerson(person);
			objective = new DialogObjective(target.createDialog(DialogType.REQUEST));
		}

		if(steps > 1) {
			objective.then(() -> mission.add(createObjective(person, mission, steps - 1)));
		}
		return objective;
	}

	public static Person randomMissionPerson(Person exclude) {
		if(v.chance(.2F)) {
			Person person = getWorld().findRandomPerson();
			if(person != null && person != exclude) {
				return person;
			}
		}
		return PersonGenerator.createPerson();
	}

	public static String randomMissionName() {
		return Resources.generateString("mission_name");
	}

	public static Dialog randomVisitDialog(Player player, Person person) {
		Dialog dialog;
		float r = v.random(1);
		if(r > .4) {
			dialog = randomInitiateDialog(player, person);
		}
		else {
			dialog = null;
		}
		Dialog greeting = person.createDialog(DialogType.GREETING);
		if(dialog != null) {
			greeting.add(dialog);
		}
		return greeting;
	}

	public static Dialog randomInitiateDialog(Player player, Person person) {
		Dialog dialog;
		if(person.getOpinion(player) == OpinionType.GRATEFUL && v.chance(.4F)) {
			dialog = person.createDialog(DialogType.OFFER);
			dialog.add(new ItemTradeOption(player.getInventory(), ItemGenerator.randomItem(), 0));
			person.setOpinion(player, OpinionType.FRIENDLY);
		}
		else {
			dialog = person.createDialog(DialogType.REQUEST);
			dialog.add(new BasicOption("Learn More", menu -> {
				Menu sub = new Menu(menu.getPlayer(), new MissionMenuHandle(menu.getDefault()));
				sub.add(new MissionOption(createMission(person)));
				sub.addDefault();
				setContext(sub);
			}));
		}
		Dialog greeting = person.createDialog(DialogType.GREETING);
		greeting.add(dialog);
		return greeting;
	}
}  
