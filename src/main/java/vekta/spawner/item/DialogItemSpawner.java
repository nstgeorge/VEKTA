package vekta.spawner.item;

import vekta.Resources;
import vekta.item.DialogItem;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.spawner.ItemGenerator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static vekta.Vekta.v;

public class DialogItemSpawner implements ItemGenerator.ItemSpawner {
	private static final Map<String, Set<String>> DIALOGS = Resources.getStringMap("item_dialog", false);
	private static final List<String> DIALOG_ITEMS = DIALOGS.keySet().stream()
			.map(s -> s.substring(0, s.lastIndexOf(" ")))
			.collect(Collectors.toList());

	@Override
	public float getWeight() {
		return .2F;
	}

	@Override
	public boolean isValid(Item item) {
		return item instanceof DialogItem;
	}

	@Override
	public Item create() {
		return new Item(v.random(DIALOG_ITEMS), ItemType.RARE);
	}

	public static String randomDialogResponse(String itemName, String dialogType) {
		Set<String> strings = DIALOGS.get(itemName + " " + dialogType);
		return strings.isEmpty() ? null : v.random(strings.toArray(new String[0]));
	}
}