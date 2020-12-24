package vekta.terrain.location;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.module.ModuleType;
import vekta.object.planet.Planet;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;
import vekta.spawner.item.OreItemSpawner;
import vekta.sync.Sync;

import static vekta.Vekta.v;

public class MiningLocation extends Location {

	private String name;
	private boolean depleted;

	public MiningLocation(TerrestrialPlanet planet, String name) {
		super(planet);

		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getOverview() {
		// TODO: randomize
		return "You land among zigzagging veins of precious metals.";
	}

	public boolean isDepleted() {
		return depleted;
	}

	public Inventory extract(float efficiency) {
		Inventory loot = new Inventory();
		if(!depleted) {
			depleted = true;

			int ct = (int)v.random(efficiency * 2) + 1;
			for(int i = 0; i < ct; i++) {
				Item item = OreItemSpawner.randomOre(getPlanet().getName());
				loot.add(item);
			}
		}
		return loot;
	}

	//	public boolean isVisitable(Player player) {
	//		return !isDepleted() && player.getShip().getModules().stream()
	//				.anyMatch(m -> m.getType() == ModuleType.MINING);
	//	}
}
