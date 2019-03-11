package vekta.spawner.world;

import processing.core.PVector;
import vekta.RenderLevel;
import vekta.module.BatteryModule;
import vekta.module.RCSModule;
import vekta.module.station.SensorModule;
import vekta.module.station.SolarArrayModule;
import vekta.module.station.StationCoreModule;
import vekta.module.station.StructuralModule;
import vekta.object.SpaceObject;
import vekta.object.planet.TerrestrialPlanet;
import vekta.object.ship.SpaceStation;
import vekta.spawner.WorldGenerator;

import static vekta.Vekta.*;
import static vekta.spawner.ItemGenerator.addLoot;
import static vekta.spawner.WorldGenerator.orbit;
import static vekta.spawner.WorldGenerator.randomPlanetColor;

public class SpaceStationSpawner implements WorldGenerator.WorldSpawner {
	@Override
	public float getWeight() {
		return .5F;
	}

	@Override
	public RenderLevel getRenderLevel() {
		return RenderLevel.SHIP;
	}

	@Override
	public void spawn(SpaceObject center, PVector pos) {
		SpaceObject orbit = getWorld().findOrbitObject(center);
		if(orbit instanceof TerrestrialPlanet) {
			// Only spawn near terrestrial planets
			int color = v.random(1) < .6F ? orbit.getColor() : randomPlanetColor();
			SpaceStation s = register(new SpaceStation("OUTPOST I", new StationCoreModule(), PVector.random2D(), pos, new PVector(), color));

			// TODO: randomize
			SpaceStation.Component core = s.getCore();
			SpaceStation.Component rcs = core.attach(SpaceStation.Direction.UP, new RCSModule(1));
			SpaceStation.Component battery = core.attach(SpaceStation.Direction.RIGHT, new BatteryModule(1));
			SpaceStation.Component struct = core.attach(SpaceStation.Direction.LEFT, new StructuralModule(10, 1));
			SpaceStation.Component struct2 = core.attach(SpaceStation.Direction.DOWN, new StructuralModule(10, 1));
			SpaceStation.Component panel = struct.attach(SpaceStation.Direction.LEFT, new SolarArrayModule(1));
			SpaceStation.Component panel2 = struct.attach(SpaceStation.Direction.DOWN, new SolarArrayModule(1));
			SpaceStation.Component panel3 = struct2.attach(SpaceStation.Direction.RIGHT, new SolarArrayModule(1));
			SpaceStation.Component sensor = struct2.attach(SpaceStation.Direction.LEFT, new SensorModule());

			orbit(orbit, s, .5F);

			addLoot(s.getInventory(), 3);

		}
	}
}
