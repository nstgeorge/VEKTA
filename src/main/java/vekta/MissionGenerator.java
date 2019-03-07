package vekta;

import processing.core.PVector;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.menu.dialog.Dialog;
import vekta.mission.*;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.MessengerShip;
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
				WorldGenerator.randomPlanetColor());//TODO: use character/faction color
		addObject(ship);
		player.send(ship.getName() + " approaches with a message!")
				.withColor(ship.getColor());
		return ship;
	}

	public static Mission createMission(Person person) {
		Mission mission = new Mission(randomMissionName());
		Inventory inv = new Inventory();
		ItemGenerator.addLoot(inv, (int)v.random(3) + 1);
		for(Item item : inv) {
			mission.add(new ItemReward(item));
		}
		if(inv.getMoney() > 0) {
			mission.add(new MoneyReward(inv.getMoney()));
		}
		mission.add(createObjective(person, mission));
		return mission;
	}

	public static Objective createObjective(Person person, Mission mission) {
		float r = v.random(1);
		if(r > .5) {
			return new LandAtObjective(person.getHomeObject());
		}
		else {
			return new LandAtObjective(getWorld().findRandomObject(TerrestrialPlanet.class));
		}
	}

	public static String randomMissionName() {
		return Resources.generateString("mission_name");
	}
}  
