package vekta.spawner.event;

import vekta.Faction;
import vekta.Player;
import vekta.economy.BubbleModifier;
import vekta.economy.ProductivityModifier;
import vekta.economy.TemporaryModifier;
import vekta.spawner.EventGenerator;
import vekta.spawner.FactionGenerator;
import vekta.spawner.ItemGenerator;

import static vekta.Vekta.v;

public class EconomyEventSpawner implements EventGenerator.EventSpawner {
	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public void spawn(Player player) {
		Faction faction = FactionGenerator.randomFaction();

		ProductivityModifier modifier;

		float r = v.random(1);
		if(r > .5F) {
			float amount = v.random(1, 5);
			modifier = new BubbleModifier(ItemGenerator.randomCategory(), amount, .2F / amount);
		}
		else {
			// TODO: randomized positive/negative effect names
			modifier = new TemporaryModifier("Unknown Factors", v.random(-1, 1), v.random(.1F));
		}

		faction.getEconomy().addModifier(modifier);
	}
}
