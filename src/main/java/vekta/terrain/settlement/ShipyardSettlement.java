package vekta.terrain.settlement;

import vekta.faction.Faction;
import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.spawner.item.ModuleItemSpawner;
import vekta.terrain.building.*;

import static vekta.Vekta.v;

public class ShipyardSettlement extends Settlement {

	public ShipyardSettlement(Faction faction) {
		super(faction, "shipyard");

		add(new CapitalBuilding("Overseer", this));

		add(new MarketBuilding(4, "Modules", ModuleItemSpawner.class));

		// ships/stations

		add(new JunkyardBuilding());

		if(v.chance(.75F)) {
			add(new RefineryBuilding());
			add(new WorkshopBuilding());
		}

		add(new ForumBuilding(this, (int)(v.random(10, 20))));
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
	public void onSetup() {
		//		getTerrain().addFeature("");

	}

	@Override
	public void setupEconomy(Economy economy) {
		economy.setValue(v.random(1, 5));
		economy.addModifier(new NoiseModifier(1));
	}
}
