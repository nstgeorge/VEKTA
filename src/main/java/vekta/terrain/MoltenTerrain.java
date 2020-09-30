package vekta.terrain;

public class MoltenTerrain extends Terrain {

	public MoltenTerrain() {
		addFeature("Molten Surface");
		addFeature("Treacherous");
	}

	@Override
	public String getOverview() {
		return "The entire planet is covered in molten lava.";
	}
}
