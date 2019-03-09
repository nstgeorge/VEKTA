package vekta.spawner;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.Resources;
import vekta.object.planet.TerrestrialPlanet;
import vekta.person.Person;
import vekta.spawner.world.AsteroidSpawner;
import vekta.terrain.HabitableTerrain;
import vekta.terrain.settlement.OutpostSettlement;
import vekta.terrain.settlement.Settlement;

import java.util.List;

import static vekta.Vekta.*;

public class PersonGenerator {
	public static Person createPerson() {
		return createPerson(randomHome());
	}

	public static Person createPerson(Settlement home) {
		Person person = new Person(randomPersonName(), FactionGenerator.randomFaction());
		person.setHome(home);
		if(v.random(1) < .5F) {
			person.setTitle(randomPersonTitle(person));
		}
		addObject(person);
		return person;
	}

	public static String randomPersonName() {
		return Resources.generateString("person");
	}

	public static String randomPersonTitle(Person person) {
		float r = v.random(1);
		if(r > .6 && person.hasHome()) {
			return "of " + person.findHomeObject().getName();
		}
		if(r > .4) {
			return "of " + person.getFaction().getName();
		}
		else {
			return Resources.generateString("person_title");
		}
	}

	public static void updateHome(Person person) {
		if(!person.hasHome()) {
			person.setHome(PersonGenerator.randomHome(person));
		}
	}

	public static Settlement randomHome(Person person) {
		if(person != null && person.hasHome() && v.chance(.2F)) {
			// Use person's home settlement
			return person.findHome();
		}
		return randomHome();
	}

	public static Settlement randomHome() {
		TerrestrialPlanet planet = getWorld().findRandomObject(TerrestrialPlanet.class);
		if(planet != null) {
			// Find a suitable existing settlement
			List<Settlement> settlements = planet.getLandingSite().getTerrain().getSettlements();
			if(!settlements.isEmpty()) {
				return v.random(settlements);
			}
		}

		// If no candidate was found, create an asteroid with a new settlement
		PVector pos = WorldGenerator.randomSpawnPosition(RenderLevel.PLANET, new PVector());
		Settlement settlement = new OutpostSettlement(FactionGenerator.randomFaction());
		AsteroidSpawner.createAsteroid(pos, new HabitableTerrain(settlement));
		return settlement;
	}
}
