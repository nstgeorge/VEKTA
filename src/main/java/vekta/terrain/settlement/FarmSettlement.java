package vekta.terrain.settlement;

import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.faction.Faction;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.spawner.item.SpeciesItemSpawner;
import vekta.terrain.location.Location;
import vekta.terrain.settlement.building.MarketBuilding;

import static vekta.Vekta.v;

public class FarmSettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	public FarmSettlement(Location location, Faction faction) {
		super(location, faction, "farm");

		add(new MarketBuilding(1, "Livestock", SpeciesItemSpawner.class));
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public String getGenericName() {
		return "Farm";
	}

	@Override
	public void initEconomy(Economy economy) {
		economy.setValue(v.random(.1F, .5F));
		economy.addModifier(new NoiseModifier(.02F));
	}

	@Override
	public void onSettlementMenu(Menu menu) {
	}
}
