package vekta.spawner;

import vekta.Player;
import vekta.Resources;
import vekta.situation.Situation;

import static vekta.Vekta.v;

public class EventGenerator {
	private static final EventSpawner[] EVENT_SPAWNERS = Resources.getSubclassInstances(EventSpawner.class);
	private static final Situation[] SITUATIONS = Resources.getSubclassInstances(Situation.class);

	public static void spawnEvent(Player player) {
		Weighted.random(EVENT_SPAWNERS).spawn(player);
	}

	public static void updateSituations(Player player) {
		Situation situation = v.random(SITUATIONS);
		String attr = situation.getClass().getSimpleName();
		boolean has = player.hasAttribute(attr);
		if(!has && situation.isHappening(player)) {
			player.addAttribute(attr);
			situation.start(player);
		}
		else if(has && !situation.isHappening(player)) {
			player.removeAttribute(attr);
			situation.end(player);
		}
	}

	public interface EventSpawner extends Weighted {
		void spawn(Player player);
	}

}

