package vekta.spawner;

import processing.core.PVector;
import vekta.Faction;
import vekta.RenderLevel;
import vekta.Resources;
import vekta.object.SpaceObject;
import vekta.spawner.item.BondItemSpawner;
import vekta.spawner.item.ColonyItemSpawner;
import vekta.spawner.item.MissionItemSpawner;
import vekta.spawner.item.ModuleItemSpawner;
import vekta.terrain.*;
import vekta.terrain.building.MarketBuilding;
import vekta.terrain.settlement.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static vekta.Vekta.*;

public class WorldGenerator {
	private static final WorldSpawner[][] SPAWNERS; // Externally defined weighted world spawners

	static {
		// Load SPAWNERS from classpath
		WorldSpawner[] options = Resources.getSubclassInstances(WorldSpawner.class);
		// Create spawner array for each RenderLevel, indexed by ordinal()
		SPAWNERS = Arrays.stream(RenderLevel.values())
				.map(level -> Arrays.stream(options).filter(s -> s.getRenderLevel() == level).toArray(WorldSpawner[]::new))
				.toArray(WorldSpawner[][]::new);
	}

	public static float getRadius(RenderLevel level) {
		return 10000 * getDistanceUnit(level);
	}

	public static void spawnOccasional(RenderLevel level, SpaceObject center) {
		PVector pos = randomSpawnPosition(level, center.getPosition());

		WorldSpawner[] array = SPAWNERS[level.ordinal()];
		if(array.length > 0) {
			Weighted.random(array).spawn(center, pos);
		}
	}

	public static Terrain randomTerrain() {
		Terrain terrain;
		boolean features = true;
		float r = v.random(1);
		if(r > .3) {
			terrain = new HabitableTerrain(createSettlement());
		}
		else if(r > .2) {
			terrain = new MiningTerrain();
		}
		else if(r > .1) {
			terrain = new OceanicTerrain();
			features = false;
		}
		else {
			terrain = new MoltenTerrain();
			features = false;
		}
		if(features) {
			int featureCt = (int)v.random(1, 4);
			for(int i = 0; i < featureCt; i++) {
				String feature = Resources.generateString("planet_feature");
				for(String s : feature.split(",")) {
					terrain.addFeature(s.trim());
				}
			}
		}
		return terrain;
	}

	public static Settlement createSettlement() {
		Faction faction = FactionGenerator.randomFaction();
		float r = v.random(1);
		if(r > .7) {
			return new CitySettlement(faction);
		}
		else if(r > .5) {
			return new TownSettlement(faction);
		}
		else if(r > .3) {
			return new ColonySettlement(faction);
		}
		else if(r > .2) {
			return new OutpostSettlement(faction);
		}
		else if(r > .1) {
			return new EmptySettlement(faction);
		}
		else {
			return new AbandonedSettlement(faction, Resources.generateString("settlement"));
		}
	}

	public static MarketBuilding randomMarket(int shopTier) {
		return v.random(randomMarkets(shopTier, 1));
	}

	public static List<MarketBuilding> randomMarkets(int shopTier, float chance) {
		List<MarketBuilding> buildings = new ArrayList<>();
		if(v.chance(chance * 2)) {
			buildings.add(new MarketBuilding(shopTier, "Goods", null));
		}
		if(v.chance(chance * .5F)) {
			buildings.add(new MarketBuilding(shopTier, "Trinkets", new MissionItemSpawner()));
		}
		if(v.chance(chance * .5F)) {
			buildings.add(new MarketBuilding(shopTier, "Modules", new ModuleItemSpawner()));
		}
		if(v.chance(chance * .5F)) {
			buildings.add(new MarketBuilding(shopTier, "Bonds", new BondItemSpawner()));
		}
		if(v.chance(chance * .5F)) {
			buildings.add(new MarketBuilding(shopTier, "Supplies", new ColonyItemSpawner()));
		}
		return buildings;
	}

	public static void orbit(SpaceObject parent, SpaceObject child, float variation) {
		PVector velocity = getOrbitVelocity(parent, child);
		if(variation > 0) {
			velocity.sub(parent.getVelocity())
					.rotate(v.random(-QUARTER_PI, QUARTER_PI) * variation)
					.add(parent.getVelocity());
		}
		child.setVelocity(velocity);
	}

	public static PVector getOrbitVelocity(SpaceObject parent, SpaceObject child) {
		PVector offset = parent.getPosition().sub(child.getPosition());
		float speed = sqrt(G * parent.getMass() / offset.mag());
		return offset.rotate(HALF_PI).setMag(speed).add(parent.getVelocity());
	}

	public static PVector randomSpawnPosition(RenderLevel level, PVector center) {
		float radius = getRadius(level);
		return PVector.random2D().mult(v.random(radius / 2, radius)).add(center);
	}

	public static int randomPlanetColor() {
		return v.color(v.random(100, 255), v.random(100, 255), v.random(100, 255));
	}

	public interface WorldSpawner extends Weighted {
		RenderLevel getRenderLevel();

		void spawn(SpaceObject center, PVector pos);
	}
}
