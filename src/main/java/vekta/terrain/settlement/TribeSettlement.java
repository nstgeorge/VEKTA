package vekta.terrain.settlement;

import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.faction.Faction;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.DeityGenerator;
import vekta.spawner.item.SpeciesItemSpawner;
import vekta.terrain.location.Location;
import vekta.terrain.settlement.building.CapitalBuilding;
import vekta.terrain.settlement.building.MarketBuilding;
import vekta.terrain.settlement.building.TempleBuilding;

import static vekta.Vekta.v;

public class TribeSettlement extends Settlement {

	public TribeSettlement(TerrestrialPlanet planet, Faction faction) {
		super(planet, faction, "tribe");

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
	public void initEconomy(Economy economy) {
		economy.setValue(v.random(.02F, .1F));
		economy.addModifier(new NoiseModifier(.001F));
	}
}
