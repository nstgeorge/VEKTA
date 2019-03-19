package vekta.terrain.settlement;

import vekta.Faction;
import vekta.Player;
import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.economy.TemporaryModifier;
import vekta.spawner.FactionGenerator;
import vekta.spawner.ItemGenerator;
import vekta.spawner.item.ClothingItemSpawner;
import vekta.spawner.item.CoinItemSpawner;
import vekta.spawner.item.WeaponItemSpawner;
import vekta.terrain.building.MarketBuilding;

import static vekta.Vekta.v;

public class UnderworldSettlement extends Settlement {

	private final String genericName;

	public UnderworldSettlement(Faction faction) {
		super(faction, "underworld");

		String[] parts = getName().split(" ");
		genericName = parts[parts.length - 1];

		if(v.chance(.5F)) {
			add(new MarketBuilding(2, "Weapons", ItemGenerator.getSpawner(WeaponItemSpawner.class)));
		}
		else {
			MarketBuilding disguises = new MarketBuilding(2, "Disguises", ItemGenerator.getSpawner(CoinItemSpawner.class));
			add(disguises);
			disguises.getInventory().clearItems();
			int disguiseCt = (int)v.random(5, 10);
			for(int i = 0; i < disguiseCt; i++) {
				disguises.getInventory().add(ClothingItemSpawner.createDisguiseItem(FactionGenerator.randomFaction()));
			}
		}

		if(v.chance(.75F)) {
			add(new MarketBuilding(2, "Coins", ItemGenerator.getSpawner(CoinItemSpawner.class)));
		}
	}

	@Override
	public String getGenericName() {
		return genericName;
	}

	@Override
	public boolean hasSecurity(Player player) {
		return false;
	}

	@Override
	public void onSetupEconomy(Economy economy) {
		economy.setValue(v.random(.2F, 1));
		economy.addModifier(new NoiseModifier(.1F));
		economy.addModifier(new TemporaryModifier("Criminal Jurisdiction", -.01F, 0));
	}
}
