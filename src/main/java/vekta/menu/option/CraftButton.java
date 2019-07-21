package vekta.menu.option;

import vekta.InfoGroup;
import vekta.KeyBinding;
import vekta.Resources;
import vekta.Settings;
import vekta.display.Layout;
import vekta.display.TextDisplay;
import vekta.item.BlueprintItem;
import vekta.item.Inventory;
import vekta.item.Item;
import vekta.item.ItemCategory;
import vekta.menu.Menu;

import java.util.List;

import static vekta.Vekta.DANGER_COLOR;
import static vekta.Vekta.UI_COLOR;

public class CraftButton implements ButtonOption, LayoutBuilder {
	private final Inventory inv;
	private final BlueprintItem blueprint;

	private List<Item> materials;

	public CraftButton(Inventory inv, BlueprintItem blueprint) {
		this.inv = inv;
		this.blueprint = blueprint;

		//		materials = blueprint.findMaterials(inv);
	}

	@Override
	public String getName() {
		return blueprint.getResultName();
	}

	@Override
	public boolean isEnabled() {
		return materials != null;
	}

	@Override
	public void onUpdate(Menu menu) {
		// TODO optimize
		materials = blueprint.findMaterials(inv);
	}

	@Override
	public String getSelectVerb() {
		return "craft";
	}

	@Override
	public void onSelect(Menu menu) {
		Item item = blueprint.createItem(menu.getPlayer(), materials);
		for(Item material : materials) {
			inv.remove(material);
		}
		inv.add(item);
		Resources.playSound("chirp"); // TODO: custom crafting sound
	}

	@Override
	public void onLayout(Layout layout) {
		layout.customize()
				.color(blueprint.getResultType().getColor());

		layout.add(new TextDisplay(blueprint.getResultName())).customize().fontSize(32);

		InfoGroup info = new InfoGroup();

		for(ItemCategory material : blueprint.getMaterials()) {
			layout.add(new TextDisplay("* " + material.getName())).customize()
					.color(inv.getItems().stream().anyMatch(material::isIncluded) ? UI_COLOR : DANGER_COLOR);
		}
		//		blueprint.onInfo(info);

		info.onLayout(layout);
		if(isEnabled()) {
			layout.add(new TextDisplay(Settings.getKeyText(KeyBinding.MENU_SELECT) + " to " + getSelectVerb()))
					.customize().color(100);
		}
	}
}
