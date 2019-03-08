package vekta.spawner;

import vekta.Resources;
import vekta.object.planet.TerrestrialPlanet;
import vekta.person.Person;
import vekta.terrain.HabitableTerrain;
import vekta.terrain.LandingSite;
import vekta.terrain.building.HouseBuilding;
import vekta.terrain.settlement.Settlement;

import static vekta.Vekta.*;

public class PersonGenerator {
	public static Person createPerson() {
		TerrestrialPlanet home = getWorld().findRandomObject(TerrestrialPlanet.class);
		if(home != null && home.getTerrain() instanceof HabitableTerrain) {
			Settlement settlement = ((HabitableTerrain)home.getTerrain()).getSettlement();
			Person person = createPerson(home.getLandingSite());
			settlement.add(new HouseBuilding(person));
			return person;
		}
		return createPerson(null);
	}

	public static Person createPerson(LandingSite home) {
		Person person = new Person(randomPersonName(), home != null ? home.getParent().getColor() : WorldGenerator.randomPlanetColor());
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
		if(r > .5 && person.getHome() != null) {
			return "of " + person.getHomeObject().getName();
		}
		else {
			return Resources.generateString("person_title");
		}
	}
}
