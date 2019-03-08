package vekta.spawner;

import vekta.Faction;
import vekta.Resources;
import vekta.object.planet.TerrestrialPlanet;
import vekta.person.Person;
import vekta.terrain.LandingSite;
import vekta.terrain.building.HouseBuilding;
import vekta.terrain.settlement.Settlement;

import java.util.Collections;
import java.util.List;

import static vekta.Vekta.*;

public class PersonGenerator {
	public static Person createPerson() {
		TerrestrialPlanet home = getWorld().findRandomObject(TerrestrialPlanet.class);
		if(home != null) {
			Person person = createPerson(home.getLandingSite()); // TODO: set home to Settlement rather than LandingSite
			List<Settlement> settlements = home.getTerrain().getSettlements();
			if(!settlements.isEmpty()) {
				Settlement settlement = v.random(settlements);
				settlement.add(new HouseBuilding(person));
			}
			return person;
		}
		return createPerson(null);
	}

	public static Person createPerson(LandingSite home) {
		List<Settlement> settlements = home != null ? home.getTerrain().getSettlements() : Collections.emptyList();
		Faction faction = settlements.isEmpty()
				? FactionGenerator.randomFaction()
				: v.random(settlements).getFaction();

		Person person = new Person(randomPersonName(), faction);
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
