package vekta.terrain.settlement;

import vekta.faction.Faction;
import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.spawner.DeityGenerator;
import vekta.spawner.item.SpeciesItemSpawner;
import vekta.terrain.building.CapitalBuilding;
import vekta.terrain.building.MarketBuilding;
import vekta.terrain.building.TempleBuilding;

import static vekta.Vekta.v;

public class TribeSettlement extends Settlement {

	public TribeSettlement(Faction faction) {
		super(faction, "tribe");

		add(new CapitalBuilding("Chieftain", this));

		if(v.chance(.5F)) {
			add(new MarketBuilding(1, "Wildlife", SpeciesItemSpawner.class));
		}

		add(new TempleBuilding(DeityGenerator.randomDeity()));
	}

	@Override
	public String getGenericName() {
		return "Tribe";
	}

	@Override
	public float getValueScale() {
		return .5F;
	}

	@Override
	public void setupEconomy(Economy economy) {
		economy.setValue(v.random(.02F, .1F));
		economy.addModifier(new NoiseModifier(.001F));
	}
}
