package vekta.spawner;

import com.google.common.collect.ImmutableList;
import processing.core.PVector;
import vekta.faction.Faction;
import vekta.world.RenderLevel;
import vekta.Resources;
import vekta.object.SpaceObject;
import vekta.spawner.item.*;
import vekta.terrain.*;
import vekta.terrain.building.MarketBuilding;
import vekta.terrain.settlement.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static processing.core.PApplet.max;
import static processing.core.PApplet.sqrt;
import static processing.core.PConstants.HALF_PI;
import static processing.core.PConstants.QUARTER_PI;
import static vekta.Vekta.*;

public class WorldGenerator {
	private static final WorldSpawner[][] SPAWNERS;

	private static final List<WeightedOption<Function<Faction, Settlement>>> SETTLEMENTS = ImmutableList.<WeightedOption<Function<Faction, Settlement>>>builder()
			.add(new WeightedOption<>(2, CitySettlement::new))
			.add(new WeightedOption<>(2, TownSettlement::new))
			.add(new WeightedOption<>(1, FortSettlement::new))
			.add(new WeightedOption<>(1, HideoutSettlement::new))
			.add(new WeightedOption<>(1, ColonySettlement::new))
			.add(new WeightedOption<>(1, OutpostSettlement::new))
			.add(new WeightedOption<>(1, EmptySettlement::new))
			.add(new WeightedOption<>(1, faction -> new AbandonedSettlement(faction, Resources.generateString("settlement"))))
			.add(new WeightedOption<>(.5F, ShipyardSettlement::new))
			.add(new WeightedOption<>(.5F, TribeSettlement::new))
			.build();

	static {
		// Load SPAWNERS from classpath
		WorldSpawner[] options = Resources.findSubclassInstances(WorldSpawner.class);
		// Create spawner array for each RenderLevel, indexed by ordinal()
		SPAWNERS = Arrays.stream(RenderLevel.values())
				.map(level -> Arrays.stream(options).filter(s -> s.getObjectLevel() == level).toArray(WorldSpawner[]::new))
				.toArray(WorldSpawner[][]::new);
	}

	public static float getRadius(RenderLevel level) {
		return 10000 * getDistanceUnit(level);
	}

	public static void spawnOccasional(RenderLevel level, SpaceObject center) {
		WorldSpawner[] array = SPAWNERS[level.ordinal()];
		if(array.length > 0) {
			WorldSpawner spawner = Weighted.random(array);
			spawner.spawn(center, randomSpawnPosition(spawner.getSpawnLevel(), center.getPosition()));
		}
	}

	public static Terrain createPlanetTerrain() {
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
			int featureCt = (int)v.random(2, 6);
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
		return Weighted.random(SETTLEMENTS).getValue().apply(faction);
	}

	public static void populateSettlement(Settlement settlement) {
		if(v.chance(.1F)) {
			DungeonGenerator.createDungeon(settlement);
		}
	}

	public static MarketBuilding createMarket(int shopTier) {
		return v.random(randomMarkets(shopTier, 1));
	}

	public static List<MarketBuilding> randomMarkets(int shopTier, float chance) {
		List<MarketBuilding> buildings = new ArrayList<>();
		if(v.chance(chance * 2)) {
			buildings.add(new MarketBuilding(shopTier, "Goods", null));
		}
		if(v.chance(chance * .1F)) {
			buildings.add(new MarketBuilding(max(1, shopTier - 1), "Trinkets", MissionItemSpawner.class));
		}
		if(v.chance(chance * .3F)) {
			buildings.add(new MarketBuilding(shopTier, "Modules", ModuleItemSpawner.class));
		}
		if(v.chance(chance * .3F)) {
			buildings.add(new MarketBuilding(shopTier, "Clothing", ClothingItemSpawner.class));
		}
		if(v.chance(chance * .1F)) {
			buildings.add(new MarketBuilding(shopTier, "Supplies", ColonyItemSpawner.class));
		}
		if(v.chance(chance * .1F)) {
			buildings.add(new MarketBuilding(shopTier, "Blueprints", BlueprintItemSpawner.class));
		}
		if(v.chance(chance * .02F)) {
			buildings.add(new MarketBuilding(shopTier, "Wildlife", SpeciesItemSpawner.class));
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
		return PVector.random2D().mult(v.random(radius / 4, radius)).add(center);
	}

	public static int randomPlanetColor() {
		return v.color(v.random(100, 255), v.random(100, 255), v.random(100, 255));
	}

	public interface WorldSpawner extends Weighted {
		RenderLevel getSpawnLevel();

		default RenderLevel getObjectLevel() {
			return getSpawnLevel();
		}

		void spawn(SpaceObject center, PVector pos);
	}
}
