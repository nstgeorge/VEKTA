package vekta.terrain.settlement;

import vekta.faction.Faction;
import vekta.player.Player;
import vekta.economy.Economy;
import vekta.economy.NoiseModifier;
import vekta.terrain.building.CapitalBuilding;

import static vekta.Vekta.v;

public class FortSettlement extends Settlement {

	public FortSettlement(Faction faction) {
		super(faction, "fort");

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
	public void setupEconomy(Economy economy) {
		economy.setValue(v.random(.1F, .5F));
		economy.addModifier(new NoiseModifier(.01F));
	}
}
