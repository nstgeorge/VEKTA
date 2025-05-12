package vekta.spawner;

import processing.core.PVector;
import vekta.Resources;
import vekta.economy.ProductivityModifier;
import vekta.faction.Faction;
import vekta.faction.PlayerFaction;
import vekta.object.planet.Asteroid;
import vekta.object.planet.TerrestrialPlanet;
import vekta.person.Person;
import vekta.person.personality.Personality;
import vekta.spawner.world.AsteroidSpawner;
import vekta.terrain.settlement.OutpostSettlement;
import vekta.terrain.settlement.Settlement;
import vekta.world.RenderLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static vekta.Vekta.*;

public class PersonGenerator {
	private static final Personality[] PERSONALITIES = Arrays.stream(Resources.findSubclassInstances(Personality.class)).filter(Personality::isRandomlyAssignable).toArray(Personality[]::new);
	private static final float PERSONALITY_CHANCE = .1F;
	private static final float MAX_INTERESTS = 2;

	public static Person createPerson() {
		return createPerson(randomHome());
	}

	public static Person createPerson(Settlement home) {
		Person person = register(new Person(randomPersonName(), home.getFaction()));
		person.setHome(home);
		if(v.random(1) < .5F) {
			person.setTitle(randomPersonTitle(person));
		}
		int interestCt = (int)v.random(MAX_INTERESTS) + 1;
		for(int i = 0; i < interestCt; i++) {
			person.addInterest(randomInterest());
		}
		if(v.chance(PERSONALITY_CHANCE)) {
			Personality personality = v.random(PERSONALITIES);
			person.setPersonality(personality);
		}
		return person;
	}

	public static String randomPersonName() {
		return Resources.generateString("person");
	}

	public static String randomBossName() {
		return Resources.generateString("boss");
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

	public static Settlement randomHome(Faction faction) {
		List<Settlement> homes = new ArrayList<>();
		// Choose from settlements affecting faction's economy
		for(ProductivityModifier mod : faction.getEconomy().getModifiers()) {
			if(mod instanceof Settlement) {
				homes.add((Settlement)mod);
			}
		}
		return !homes.isEmpty() ? v.random(homes) : randomHome();
	}

	public static Settlement randomHome() {
		// Find suitable existing settlements
		List<Settlement> settlements = getWorld().findObjects(TerrestrialPlanet.class).stream()
				.flatMap(planet -> planet.findInhabitedSettlements().stream())
				.filter(s -> !(s.getFaction() instanceof PlayerFaction) && s.isInhabited())
				.collect(Collectors.toList());

		if(!settlements.isEmpty()) {
			return v.random(settlements);
		}

		// If no candidate was found, create an asteroid with a new settlement
		PVector pos = WorldGenerator.randomSpawnPosition(RenderLevel.PLANET, new PVector());
		Asteroid asteroid = AsteroidSpawner.createAsteroid(pos);
		return new OutpostSettlement(asteroid, FactionGenerator.randomFaction());
	}

	public static String randomInterest() {
		return Resources.generateString("person_interest");
	}
}
