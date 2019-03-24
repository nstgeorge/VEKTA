package vekta.spawner;

import vekta.Player;
import vekta.Resources;
import vekta.situation.Situation;

import static vekta.Vekta.v;

public class EventGenerator {
	private static final EventSpawner[] EVENT_SPAWNERS = Resources.findSubclassInstances(EventSpawner.class);
	private static final Situation[] SITUATIONS = Resources.findSubclassInstances(Situation.class);

	public static void spawnEvent(Player player) {
		Weighted.random(EVENT_SPAWNERS).spawn(player);
	}

	public static void updateSituations(Player player) {
		Situation situation = v.random(SITUATIONS);
		Class attr = situation.getClass();
		boolean happening = situation.isHappening(player);
		boolean current = player.hasAttribute(attr);
		if(happening) {
			if(current) {
				situation.during(player);
			}
			else {
				player.addAttribute(attr);
				situation.start(player);
			}
		}
		else if(current) {
			player.removeAttribute(attr);
			situation.end(player);
		}
	}

	public interface EventSpawner extends Weighted {
		void spawn(Player player);
	}

}

