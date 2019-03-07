package vekta;

import vekta.object.planet.TerrestrialPlanet;
import vekta.person.Person;
import vekta.terrain.LandingSite;

import static vekta.Vekta.getWorld;
import static vekta.Vekta.v;

public class PersonGenerator {

	public static Person randomPerson() {
		TerrestrialPlanet home = getWorld().findRandomObject(TerrestrialPlanet.class);
		return randomPerson(home != null ? home.getLandingSite() : null);
	}

	public static Person randomPerson(LandingSite home) {
		Person person = new Person(randomPersonName(), home != null ? home.getParent().getColor() : WorldGenerator.randomPlanetColor());
		person.setHome(home);
		if(v.random(1) < .5F) {
			person.setTitle(randomPersonTitle(person));
		}
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
