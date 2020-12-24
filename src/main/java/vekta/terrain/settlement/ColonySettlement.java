package vekta.terrain.settlement;

import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.faction.Faction;
import vekta.item.Inventory;
import vekta.menu.Menu;
import vekta.spawner.SettlementGenerator;
import vekta.terrain.location.Location;
import vekta.terrain.settlement.building.CapitalBuilding;
import vekta.terrain.settlement.building.ForumBuilding;

import static vekta.Vekta.v;

public class ColonySettlement extends Settlement {
	private final Inventory inventory = new Inventory();

	public ColonySettlement(Location location, Faction faction) {
		super(location, faction, "colony");

		add(new CapitalBuilding("Leader", this));
		add(SettlementGenerator.createMarket(1));

		if(v.chance(.75F)) {
			add(new ForumBuilding(this, (int)(v.random(2, 5))));
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
	public void initEconomy(Economy economy) {
		getEconomy().setValue(v.random(1, 2));
		getEconomy().addModifier(new NoiseModifier(.5F));
	}

	@Override
	public void onSettlementMenu(Menu menu) {
	}
}
