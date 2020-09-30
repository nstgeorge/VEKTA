package vekta.item;

import vekta.item.category.ItemCategory;
import vekta.player.Player;
import vekta.util.InfoGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlueprintItem extends Item {
	private final String name;
	private final ItemType type;
	private final ItemProvider provider;
	private final ItemCategory[] materials;

	public BlueprintItem(String name, ItemType type, ItemProvider provider, ItemCategory[] materials) {
		this.name = name;
		this.type = type;
		this.provider = provider;
		this.materials = materials;
	}

	public String getResultName() {
		return name;
	}

	public ItemType getResultType() {
		return type;
	}

	public List<Item> findMaterials(Inventory inv) {
		List<ItemCategory> materials = new ArrayList<>(Arrays.asList(getMaterials()));
		List<Item> items = new ArrayList<>();
		for(Item item : inv) {
			for(int i = materials.size() - 1; i >= 0; i--) {
				ItemCategory material = materials.get(i);
				if(material.isIncluded(item)) {
					materials.remove(material);
					items.add(item);
					break;
				}
			}
			if(materials.isEmpty()) {
				break;
			}
		}
		return items.size() < getMaterials().length ? null : items;
	}

	public Item createItem(Player player, List<Item> items) {
		return provider.provide(player, items);
	}

	public ItemCategory[] getMaterials() {
		return materials;
	}

	@Override
	public ItemType getType() {
		return ItemType.RECIPE;
	}

	@Override
	public int getMass() {
		return 1;
	}

	@Override
	public String getName() {
		return "Blueprint (" + getResultName() + ")";
	}

	@Override
	public void onInfo(InfoGroup info) {
		//		info.addDescription("Materials: " + Arrays.stream(getMaterials())
		//				.map(ItemCategory::getName)
		//				.collect(Collectors.joining(", ")));

		for(ItemCategory material : getMaterials()) {
			info.addTrait(material.getName());
		}

		//		getPrototype().onInfo(info);
	}

	public interface ItemProvider extends Serializable {
		Item provide(Player player, List<Item> items);
	}
}
