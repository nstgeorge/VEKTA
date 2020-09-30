package vekta.spawner;

import vekta.faction.Faction;
import vekta.Resources;
import vekta.faction.PlayerFaction;

import java.util.List;
import java.util.stream.Collectors;

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

	public static List<Faction> findNonPlayerFactions() {
		return getWorld().findObjects(Faction.class).stream().filter(f -> !(f instanceof PlayerFaction)).collect(Collectors.toList());
	}

	public static Faction randomFactionPossiblyNew() {
		List<Faction> factions = findNonPlayerFactions();
		return factions.isEmpty() || v.chance(NEW_FACTION_RATE / factions.size())
				? createFaction()
				: v.random(factions);
	}

	public static Faction randomFaction() {
		List<Faction> factions = findNonPlayerFactions();
		return factions.isEmpty() ? createFaction() : v.random(factions);
	}
}
