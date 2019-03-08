package vekta;

import vekta.item.*;
import vekta.mission.Mission;
import vekta.module.*;
import vekta.module.station.SensorModule;
import vekta.module.station.SolarArrayModule;
import vekta.module.station.StationCoreModule;
import vekta.module.station.StructuralModule;

import static vekta.Vekta.round;
import static vekta.Vekta.v;

public class ItemGenerator {

	// Register module types
	private static final Module[] modules = new Module[] {
			new AutopilotModule(),
			new BatteryModule(1),
			new CannonModule(),
			new DrillModule(1),
			new EngineModule(1),
			new GeneratorModule(1),
			new HyperdriveModule(1),
			new RadiatorModule(1),
			new RCSModule(1),
			new SolarArrayModule(1),
			new StationCoreModule(),
			new SensorModule(),
			new StructuralModule(1, 1),
			new TelescopeModule(1),
			new TargetingModule(),
			new TorpedoModule(1),
			new TractorBeamModule(1),
	};

	public static Item randomItem() {
		float r = v.random(1);
		if(r > .5) {
			ItemType type = randomItemType();
			return new Item(randomItemName(type), type);
		}
		else if(r > .3) {
			return new ModuleItem(v.random(modules).getVariant());
		}
		else if(r > .2) {
			return randomMissionItem(MissionGenerator.createMission(PersonGenerator.createPerson()));
		}
		else {
			return randomOre(WorldGenerator.randomPlanetName());
		}
	}

	public static Item randomOre(String planetName) {
		ItemType type = v.random(1) > .2F ? ItemType.COMMON : ItemType.RARE;
		return new Item(randomOreName(planetName, type), type);
	}

	public static Item randomMissionItem(Mission mission) {
		return new MissionItem(Resources.generateString("mission_item"), mission);
	}

	public static ItemType randomItemType() {
		float r = v.random(1);
		if(r > .3) {
			return ItemType.COMMON;
		}
		else if(r > .05) {
			return ItemType.RARE;
		}
		else {
			return ItemType.LEGENDARY;
		}
	}

	public static void addLoot(Inventory inv, int lootTier) {
		int itemCt = round(v.random(lootTier - 1, lootTier * 2));
		for(int i = 0; i < itemCt; i++) {
			// TODO: occasionally addFeature ModuleItems
			Item item = randomItem();
			inv.add(item);
		}
	}

	public static String randomItemName(ItemType type) {
		String name = Resources.generateString("item_noun");
		if(v.random(1) < .5) {
			name += " " + Resources.generateString("item_modifier");
		}

		if(type == ItemType.LEGENDARY) {
			name += " of " + WorldGenerator.randomPlanetName();
		}
		else {
			if(v.random(1) < .5) {
				String adj = Resources.generateString(type == ItemType.COMMON ? "item_adj_common" : "item_adj_rare");
				name = adj + " " + name;
			}
		}
		return name;
	}

	public static String randomOreName(String planetName, ItemType type) {
		String key = "ore_" + (type == ItemType.COMMON ? "common" : "rare");
		String name = Resources.generateString(key);
		if(type == ItemType.LEGENDARY) {
			name += " of " + planetName;
		}
		return name;
	}
}
