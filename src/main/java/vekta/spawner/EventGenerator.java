package vekta.spawner;

import vekta.Player;
import vekta.Resources;

import static vekta.Vekta.v;

public class EventGenerator {
	private static final EventSpawner[] SPAWNERS = Resources.getSubclassInstances(EventSpawner.class);

	public static void spawnEvent(Player player) {
		v.random(SPAWNERS).spawn(player);
	}

	public interface EventSpawner extends Weighted {
		void spawn(Player player);
	}
}  
