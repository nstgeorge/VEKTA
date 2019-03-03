package vekta;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.ModuleItem;
import vekta.object.module.*;

import static vekta.Vekta.getInstance;
import static vekta.Vekta.round;

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
			new RCSModule(1),
			new TargetingModule(),
			new TorpedoModule(1),
			new TractorBeamModule(1),
	};

	public static Item randomItem() {
		Vekta v = getInstance();
		float r = v.random(1);
		if(r > .4) {
			ItemType type = randomItemType();
			return new Item(generateItemName(type), type);
		}
		else if(r > .2) {
			return new ModuleItem(v.random(modules).getVariant());
		}
		else {
			return randomOre(WorldGenerator.generatePlanetName());
		}
	}

	public static Item randomOre(String planetName) {
		ItemType type = getInstance().random(1) > .2F ? ItemType.COMMON : ItemType.RARE;
		return new Item(generateOreName(planetName, type), type);
	}

	public static ItemType randomItemType() {
		float r = getInstance().random(1);
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
		int itemCt = round(getInstance().random(lootTier - 1, lootTier * 2));
		for(int i = 0; i < itemCt; i++) {
			// TODO: occasionally add ModuleItems
			Item item = randomItem();
			inv.add(item);
		}
	}

	public static String generateItemName(ItemType type) {
		Vekta v = getInstance();
		
		String name = v.random(Resources.getStrings("item_nouns"));
		if(v.random(1) < .5) {
			name += " " + v.random(Resources.getStrings("item_modifiers"));
		}

		if(type == ItemType.LEGENDARY) {
			name += " of " + WorldGenerator.generatePlanetName();
		}
		else {
			if(v.random(1) < .5) {
				String adj = v.random(Resources.getStrings(type == ItemType.COMMON ? "item_adj_common" : "item_adj_rare"));
				name = adj + " " + name;
			}
		}
		return name;
	}

	public static String generateOreName(String planetName, ItemType type) {
		Vekta v = getInstance();
		String key = "ore_" + (type == ItemType.COMMON ? "common" : "rare");
		String name = v.random(Resources.getStrings(key));
		if(type == ItemType.LEGENDARY) {
			name += " of " + planetName;
		}
		return name;
	}
}  
