package vekta.terrain.settlement;

import vekta.Faction;
import vekta.Resources;
import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.economy.TemporaryModifier;
import vekta.item.Inventory;
import vekta.spawner.ItemGenerator;
import vekta.spawner.item.CoinItemSpawner;
import vekta.terrain.building.MarketBuilding;

import static vekta.Vekta.v;

public class UnderworldSettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	private final String genericName;

	public UnderworldSettlement(Faction faction) {
		super(faction, faction.getName() + " Underworld", Resources.generateString("underworld"));

		genericName = Resources.generateString("underworld_type");
		
		add(new MarketBuilding(2, "Coins", ItemGenerator.getSpawner(CoinItemSpawner.class)));
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public String getGenericName() {
		return genericName;
	}

	@Override
	public void onSetupEconomy(Economy economy) {
		economy.setValue(v.random(.2F, 1));
		economy.addModifier(new NoiseModifier(.1F));
		economy.addModifier(new TemporaryModifier("Criminal Jurisdiction", -.01F, 0));
	}
}
