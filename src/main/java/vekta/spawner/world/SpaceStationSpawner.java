package vekta.spawner.world;

import processing.core.PVector;
import vekta.Resources;
import vekta.module.BatteryModule;
import vekta.module.RCSModule;
import vekta.module.station.SensorModule;
import vekta.module.station.SolarArrayModule;
import vekta.module.station.StationCoreModule;
import vekta.module.station.StructuralModule;
import vekta.object.SpaceObject;
import vekta.object.ship.SpaceStation;
import vekta.terrain.LandingSite;

import static vekta.Vekta.register;
import static vekta.Vekta.v;
import static vekta.spawner.ItemGenerator.addLoot;
import static vekta.spawner.WorldGenerator.orbit;
import static vekta.spawner.WorldGenerator.randomPlanetColor;

public class SpaceStationSpawner extends NearPlanetSpawner {
	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos, LandingSite site) {
		if(site.getTerrain().isInhabited()) {
			int color = v.random(1) < .6F ? site.getParent().getColor() : randomPlanetColor();
			SpaceStation s = createStation(Resources.generateString("space_station"), pos, color);
			orbit(site.getParent(), s, .25F);
		}
	}

	public static SpaceStation createStation(String name, PVector pos, int color) {
		SpaceStation station = register(new SpaceStation(name, new StationCoreModule(), PVector.random2D(), pos, new PVector(), color));

		// TODO: randomize
		SpaceStation.Component core = station.getCore();
		SpaceStation.Component rcs = core.attach(SpaceStation.Direction.UP, new RCSModule(1));
		SpaceStation.Component battery = core.attach(SpaceStation.Direction.RIGHT, new BatteryModule());
		SpaceStation.Component struct = core.attach(SpaceStation.Direction.LEFT, new StructuralModule(10, 1));
		SpaceStation.Component struct2 = core.attach(SpaceStation.Direction.DOWN, new StructuralModule(10, 1));
		SpaceStation.Component panel = struct.attach(SpaceStation.Direction.LEFT, new SolarArrayModule(1));
		SpaceStation.Component panel2 = struct.attach(SpaceStation.Direction.DOWN, new SolarArrayModule(1));
		SpaceStation.Component panel3 = struct2.attach(SpaceStation.Direction.RIGHT, new SolarArrayModule(1));
		SpaceStation.Component sensor = struct2.attach(SpaceStation.Direction.LEFT, new SensorModule());

		addLoot(station.getInventory(), 2);

		return station;
	}
}
