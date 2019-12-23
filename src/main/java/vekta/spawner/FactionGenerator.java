package vekta.spawner;

import vekta.Faction;
import vekta.Resources;

import java.util.List;

import static vekta.Vekta.*;

public class FactionGenerator {
	private static final float NEW_FACTION_RATE = .1F;

	public static Faction createFaction() {
		Faction faction = new Faction(
				Resources.generateString("faction"),
				v.random(10, 100), // Economic value
				v.random(.1F, .2F), // Economic risk
				WorldGenerator.randomPlanetColor());

		if(v.chance(.5F)) {
			faction.setAlly(randomFaction());
		}

		if(v.chance(.5F)) {
			faction.setEnemy(randomFaction());
		}

		return register(faction);
	}

	public static Faction randomFactionPossiblyNew() {
		List<Faction> factions = getWorld().findObjects(Faction.class);
		return factions.isEmpty() || v.chance(NEW_FACTION_RATE / factions.size())
				? createFaction()
				: v.random(factions);
	}

	public static Faction randomFaction() {
		List<Faction> factions = getWorld().findObjects(Faction.class);
		return factions.isEmpty() ? createFaction() : v.random(factions);
	}
}
