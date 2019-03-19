package vekta.spawner;

import vekta.Resources;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.item.ItemCategory;
import vekta.item.PatternItemCategory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static vekta.Vekta.round;
import static vekta.Vekta.v;

public class ItemGenerator {
	private static final ItemSpawner[] SPAWNERS = Resources.getSubclassInstances(ItemSpawner.class);
	private static final ItemCategory[] CATEGORIES;

	static {
		Map<String, List<String>> map = Resources.getStringMap("item_category", true);
		CATEGORIES = map.keySet().stream()
				.map(k -> new PatternItemCategory(k, map.get(k).toArray(new String[0])))
				.toArray(ItemCategory[]::new);
	}
	
	public static ItemSpawner getSpawner(Class<? extends ItemSpawner> type) {
		return Arrays.stream(SPAWNERS).filter(type::isInstance).findFirst().orElse(null);
	}

	public static Item randomItem() {
		return Weighted.random(SPAWNERS).create();
	}

	public static ItemCategory randomCategory() {
		if(v.chance(.2F)) {
			// Occasionally return a random adjective category
			String adj = Resources.generateString("item_adj_common");
			return new PatternItemCategory(adj + " Items", new String[] {adj});
		}
		return v.random(CATEGORIES);
	}

	public static void addLoot(Inventory inv, int lootTier) {
		addLoot(inv, lootTier, null);
	}

	public static void addLoot(Inventory inv, int lootTier, ItemSpawner spawner) {
		int itemCt = round(v.random(lootTier, lootTier * 3));
		for(int i = 0; i < itemCt; i++) {
			Item item = spawner != null ? spawner.create() : randomItem();
			inv.add(item);
		}
	}

	public interface ItemSpawner extends Weighted, Serializable {
		boolean isValid(Item item);

		Item create();
	}
}
