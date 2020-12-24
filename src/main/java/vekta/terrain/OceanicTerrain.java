package vekta.terrain;

import vekta.object.planet.TerrestrialPlanet;

public class OceanicTerrain extends Terrain {

	public OceanicTerrain(TerrestrialPlanet planet) {
		super(planet);
	}

	@Override
	public String getOverview() {
		if(getPlanet().getTemperatureCelsius() >= 100){
			return "You can't find anywhere to land; clouds of water vapor obscure this planet's surface.";
		}
		return "You can't find anywhere to land; a vast ocean covers this planet.";
	}

	@Override
	public boolean isHabitable() {
		return false;
	}
}
