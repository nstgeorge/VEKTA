package vekta.terrain;

import vekta.object.planet.TerrestrialPlanet;

public class MiningTerrain extends Terrain {
	public MiningTerrain(TerrestrialPlanet planet) {
		super(planet);
	}

	@Override
	public String getOverview() {
		return "The surface is flecked with ore and precious metals.";
	}

	@Override
	public boolean isHabitable() {
		return false;
	}
}
