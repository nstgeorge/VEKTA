package vekta.terrain.settlement;

import static vekta.Vekta.v;

import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.faction.Faction;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.ModuleItem;
import vekta.market.Market;
import vekta.module.BaseModule;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.item.ModuleItemSpawner;
import vekta.terrain.settlement.building.CapitalBuilding;
import vekta.terrain.settlement.building.ExchangeBuilding;
import vekta.terrain.settlement.building.ForumBuilding;
import vekta.terrain.settlement.building.JunkyardBuilding;
import vekta.terrain.settlement.building.MarketBuilding;
import vekta.terrain.settlement.building.RefineryBuilding;
import vekta.terrain.settlement.building.WorkshopBuilding;

public class ShipyardSettlement extends Settlement implements Market.Stock {

	public ShipyardSettlement(TerrestrialPlanet planet, Faction faction) {
		super(planet, faction, "shipyard");

		add(new CapitalBuilding("Overseer", this));

		add(new MarketBuilding(4, "Modules", ModuleItemSpawner.class, this));

		// ships/stations

		add(new JunkyardBuilding());

		if (v.chance(.75F)) {
			add(new RefineryBuilding());
			add(new WorkshopBuilding());
		}

		add(new ForumBuilding(this, (int) (v.random(10, 20))));
		add(new ExchangeBuilding());
	}

	@Override
	public String getGenericName() {
		return "Shipyard";
	}

	@Override
	public float getValueScale() {
		return 1.5F;
	}

	@Override
	public void initEconomy(Economy economy) {
		economy.setValue(v.random(1, 5));
		economy.addModifier(new NoiseModifier(1));
	}

	@Override
	public boolean isBuyable(Market market, Item item) {
		return item.getType() == ItemType.MODULE;
	}

	@Override
	public float onRestock(Market market) {
		for (BaseModule module : ModuleItemSpawner.getModulePrototypes()) {
			market.getInventory().add(new ModuleItem(module.createVariant()));
		}
		market.getInventory().add((int) v.random(200, 400));

		return Float.POSITIVE_INFINITY;
	}
}
