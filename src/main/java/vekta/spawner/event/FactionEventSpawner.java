package vekta.spawner.event;

import vekta.economy.ProductivityModifier;
import vekta.economy.TemporaryModifier;
import vekta.faction.Faction;
import vekta.player.Player;
import vekta.spawner.EventGenerator;
import vekta.spawner.FactionGenerator;

import static vekta.Vekta.getWorld;
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

		String message;

		if(a.isAlly(b)) {
			a.setNeutral(b);
			message = a.getName() + " has withdrawn from their alliance with " + b.getName();
		}
		else if(a.isEnemy(b)) {
			a.setNeutral(b);
			message = a.getName() + " has negotiated peace with " + b.getName();
		}
		else if(v.chance(.7F)) {
			a.setAlly(b);
			ProductivityModifier mod = new TemporaryModifier("Trade Negotiations", 1, .05F);
			a.getEconomy().addModifier(mod);
			b.getEconomy().addModifier(mod);
			message = a.getName() + " has formed an alliance with " + b.getName();
		}
		else {
			a.setEnemy(b);
			ProductivityModifier mod = new TemporaryModifier("Wartime Spending", -1, .05F);
			a.getEconomy().addModifier(mod);
			b.getEconomy().addModifier(mod);
			message = a.getName() + " has declared war on " + b.getName();
		}

		for(Player p : getWorld().findObjects(Player.class)) {
			p.send(message);
		}
	}
}
