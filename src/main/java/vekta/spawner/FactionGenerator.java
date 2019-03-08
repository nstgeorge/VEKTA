package vekta.spawner;

import vekta.Faction;
import vekta.Resources;
import vekta.person.Person;

import static vekta.Vekta.*;

public class FactionGenerator {
	public static Faction createFaction() {
		Faction faction = new Faction(Resources.generateString("faction"), WorldGenerator.randomPlanetColor());
		addObject(faction);
		return faction;
	}

	public static Faction randomFaction() {
		return randomFaction(null);
	}

	public static Faction randomFaction(Person person) {
		if(person != null && v.chance(.2F)) {
			return person.getFaction();
		}
		Faction f = getWorld().findRandomObject(Faction.class);
		return f != null && v.chance(.9F) ? f : createFaction();
	}
}
