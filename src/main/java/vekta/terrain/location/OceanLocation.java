package vekta.terrain.location;

import vekta.module.ModuleType;
import vekta.object.planet.TerrestrialPlanet;
import vekta.player.Player;
import vekta.spawner.EcosystemGenerator;
import vekta.sync.Sync;
import vekta.terrain.Terrain;

import java.util.Set;

import static processing.core.PApplet.round;
import static vekta.Vekta.v;

public class OceanLocation extends Location {

	private final String name;

	private final boolean bioluminescent = v.chance(.05F);
	private boolean depleted = v.chance(.8F);

	public OceanLocation(TerrestrialPlanet planet, String name) {
		super(planet);
		this.name = name;

		if(bioluminescent) {
			//			EcosystemGenerator.populateEcosystem(getPlanet().getTerrain().getEcosystem(), round(v.random(3, 15)));
		}
	}

	@Override
	public String getName() {
		return name;
	}

	//	@Override
	//	public boolean isVisitable(Player player) {
	//		return terrain.getPlanet().getTemperatureCelsius() < 100;
	//	}

	@Override
	public String getOverview() {
		if(getPlanet().getTemperatureCelsius() <= 0) {
			return "You fly above the vast ice sheets covering this planet.";
		}
		return "You fly above the planet's ocean.";
	}

	@Override
	public void onSurveyTags(Set<String> tags) {

		float tempC = getPlanet().getTemperatureCelsius();
		tags.add(tempC < -50 ? "Frozen Oceans" : "Oceans");

		if(tempC < 10 && tempC > -50) {
			tags.add("Icebergs");
		}

		if(bioluminescent) {
			tags.add("Bioluminescence");
		}

		if(getPlanet().getAtmosphereDensity() > 2) {
			tags.add("Tsunamis");
		}

		//		if(shipwrecks) {
		//			tags.add("Shipwrecks");
		//		}
	}

	//	public boolean isDepleted() {
	//		return depleted;
	//	}

	//	public Inventory extract(float efficiency) {
	////	depleted = true;
	//
	//		Inventory loot = new Inventory();
	//
	//		int ct = (int)v.random(efficiency * 2) + 1;
	//		for(int i = 0; i < ct; i++) {
	//			Item item =
	//			loot.add(item);
	//		}
	//
	//		syncChanges();
	//		return loot;
	//	}

	public boolean isVisitable(Player player) {
		//		return !isDepleted() && player.getShip().getModules().stream()
		//				.anyMatch(m -> m.getType() == ModuleType.OCEAN);
		return false;
	}
}
