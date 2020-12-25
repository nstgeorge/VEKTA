package vekta.spawner;

import com.google.common.collect.ImmutableList;
import vekta.Resources;
import vekta.faction.Faction;
import vekta.object.planet.TerrestrialPlanet;
import vekta.spawner.item.*;
import vekta.terrain.location.Location;
import vekta.terrain.settlement.*;
import vekta.terrain.settlement.building.MarketBuilding;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static processing.core.PApplet.max;
import static vekta.Vekta.v;

public class SettlementGenerator {

	private static final List<WeightedOption<BiFunction<TerrestrialPlanet, Faction, Settlement>>> SETTLEMENTS = ImmutableList.<WeightedOption<BiFunction<TerrestrialPlanet, Faction, Settlement>>>builder()
			.add(new WeightedOption<>(2, CitySettlement::new))
			.add(new WeightedOption<>(2, TownSettlement::new))
			.add(new WeightedOption<>(1, FarmSettlement::new))
			.add(new WeightedOption<>(1, FortSettlement::new))
			.add(new WeightedOption<>(1, HideoutSettlement::new))
			.add(new WeightedOption<>(1, ColonySettlement::new))
			.add(new WeightedOption<>(1, OutpostSettlement::new))
			.add(new WeightedOption<>(1, EmptySettlement::new))
			.add(new WeightedOption<>(1, (location, faction) -> new AbandonedSettlement(location, faction, Resources.generateString("settlement"))))
			.add(new WeightedOption<>(.5F, ShipyardSettlement::new))
			.add(new WeightedOption<>(.5F, TribeSettlement::new))
			.build();

	public static Settlement createSettlement(TerrestrialPlanet planet) {
		Faction faction = FactionGenerator.randomFaction();
		return Weighted.random(SETTLEMENTS).getValue().apply(planet, faction);
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
}
