package vekta.terrain.settlement;

import vekta.Faction;
import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.spawner.WorldGenerator;
import vekta.terrain.building.CapitalBuilding;
import vekta.terrain.building.EconomyBuilding;

import static vekta.Vekta.v;

public class ColonySettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	public ColonySettlement(Faction faction) {
		super(faction, "colony");

		add(new CapitalBuilding(this));
		add(WorldGenerator.createMarket(1));

		if(v.chance(.75F)) {
			add(new EconomyBuilding(this, (int)(v.random(2, 5))));
		}
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public String getGenericName() {
		return "Colony";
	}

	@Override
	public void onSetupEconomy(Economy economy) {
		getEconomy().setValue(v.random(1, 2));
		getEconomy().addModifier(new NoiseModifier(.5F));
	}

	@Override
	public void onSettlementMenu(Menu menu) {
	}
}
