package vekta.terrain;

public class MiningTerrain extends Terrain {

	public MiningTerrain() {
		addFeature("Mineable");
	}

	@Override
	public String getOverview() {
		return "The surface is flecked with ore and precious metals.";
	}
}
