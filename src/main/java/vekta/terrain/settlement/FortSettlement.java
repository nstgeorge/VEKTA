package vekta.terrain.settlement;

import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.faction.Faction;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;
import vekta.terrain.location.Location;
import vekta.terrain.settlement.building.CapitalBuilding;

import static vekta.Vekta.v;

public class FortSettlement extends Settlement {

	public FortSettlement(TerrestrialPlanet planet, Faction faction) {
		super(planet, faction, "fort");

		add(new CapitalBuilding("Officer", this));
	}

	@Override
	public String getGenericName() {
		return "Base";
	}

	@Override
	public float getValueScale() {
		return 2;
	}

	@Override
	public boolean hasSecurity(Player player) {
		return !player.getFaction().isAlly(getFaction());
	}

	@Override
	public void initEconomy(Economy economy) {
		economy.setValue(v.random(.1F, .5F));
		economy.addModifier(new NoiseModifier(.01F));
	}
}
