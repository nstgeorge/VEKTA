package vekta.spawner;

import vekta.Faction;
import vekta.FactionType;
import vekta.Resources;

import java.util.List;

import static vekta.Vekta.*;

public class FactionGenerator {
	private static final float NEW_FACTION_RATE = .1F;

	public static Faction createFaction() {
		return register(new Faction(FactionType.EMPIRE,
				Resources.generateString("faction"),
				v.random(10, 100), // Economic value
				v.random(.1F, .2F), // Economic risk
				WorldGenerator.randomPlanetColor()));
	}

	public static Faction randomFaction() {
		List<Faction> factions = getWorld().findObjects(Faction.class);
		return factions.isEmpty() || v.chance(NEW_FACTION_RATE / factions.size())
				? createFaction()
				: v.random(factions);
	}
}
