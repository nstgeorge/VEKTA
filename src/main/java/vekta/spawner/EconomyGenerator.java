package vekta.spawner;

import vekta.faction.Faction;
import vekta.Resources;
import vekta.economy.Economy;

public class EconomyGenerator {
	private static final EconomySpawner[] SPAWNERS = Resources.findSubclassInstances(EconomySpawner.class);

	public static void updateFaction(Faction faction) {
		EconomySpawner spawner = Weighted.random(SPAWNERS);
		spawner.spawn(faction.getEconomy());
	}

	public interface EconomySpawner extends Weighted {
		void spawn(Economy economy);
	}
}

