package vekta.spawner.event;

import vekta.Faction;
import vekta.Player;
import vekta.spawner.EventGenerator;
import vekta.spawner.FactionGenerator;

import static vekta.Vekta.v;

public class FactionEventSpawner implements EventGenerator.EventSpawner {
	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public void spawn(Player player) {
		Faction a = FactionGenerator.randomFaction();
		Faction b = FactionGenerator.randomFaction();
		if(b == a) {
			b = FactionGenerator.createFaction();
		}

		if(a.isAlly(b)) {
			a.setNeutral(b);
			player.send(a.getName() + " has withdrawn from their alliance with " + b.getName());
		}
		else if(a.isEnemy(b)) {
			a.setNeutral(b);
			player.send(a.getName() + " has negotiated peace with " + b.getName());
		}
		else if(v.chance(.7F)) {
			a.setAlly(b);
			player.send(a.getName() + " has formed an alliance with " + b.getName());
		}
		else {
			a.setEnemy(b);
			player.send(a.getName() + " has declared war on " + b.getName());
		}
	}
}
