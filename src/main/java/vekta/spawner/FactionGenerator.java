package vekta.spawner;

import vekta.Faction;
import vekta.Resources;

import static vekta.Vekta.*;

public class FactionGenerator {
	public static Faction createFaction() {
		return register(new Faction(Resources.generateString("faction"), WorldGenerator.randomPlanetColor()));
	}
	
	public static Faction randomFaction() {
		Faction f = getWorld().findRandomObject(Faction.class);
		return f != null && v.chance(.9F) ? f : createFaction();
	}
}
