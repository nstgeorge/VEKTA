package vekta.spawner;

import vekta.Faction;
import vekta.FactionType;
import vekta.Resources;

import static vekta.Vekta.*;

public class FactionGenerator {
	public static Faction createFaction() {
		return register(new Faction(FactionType.EMPIRE,
				Resources.generateString("faction"),
				v.random(10, 100),
				WorldGenerator.randomPlanetColor()));
	}

	public static Faction randomFaction() {
		Faction f = getWorld().findRandomObject(Faction.class);
		return f != null && v.chance(.9F) ? f : createFaction();
	}
}
